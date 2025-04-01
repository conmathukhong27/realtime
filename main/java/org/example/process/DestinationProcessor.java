package org.example.process;


import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.example.model.Events;
import org.example.process.dictionary.EventTrackingObjectNameDestinationDict;
import org.example.process.dto.EventTrackingMessage;
import org.example.redis.service.RedisService;
import org.example.utils.CustomerTestUtils;
import org.example.utils.JedisStorage;
import org.example.utils.KafkaRecord;
import org.example.utils.SmartConsumer;

import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DestinationProcessor extends Thread {
    private static final Logger LOGGER = Logger.getLogger(DestinationProcessor.class);
    private final SmartConsumer consumer;
//    private final RedisService jedisService;
    private final JedisStorage jedisStorage;
    protected final AtomicBoolean running;
    private static final Gson gson = new Gson();
    private static final long TIMESTAMP_THRESHOLD = 115200000L;
    private int numRecords = 0;
    int countDestination;
    int countExpire;
    private final String SOURCE_LOG_PATH = "/u01/cskh_customer/bin/source_log.txt";

    public DestinationProcessor(SmartConsumer smartConsumer, JedisStorage jedisStorage){
        this.consumer = smartConsumer;
//        this.jedisService = jedisService;
        this.jedisStorage = jedisStorage;

        this.running = new AtomicBoolean(false);
        this.countDestination = 0;
        this.countExpire = 0;
    }
    public boolean process(){
        if (!this.running.get()){
            LOGGER.error("processor not working");
            return false;
        }
//        boolean flag = true;
//        if(flag){
//            jedisService.insert(new Events("0963193014","abc","Telecom_topup_view_transactionresult_app_view_info",60));
//            flag = false;
//        }

        KafkaRecord record = consumer.poll();
        if (record != null){
            try{
                EventTrackingMessage eventTrackingMessage = gson.fromJson(record.getContent(), EventTrackingMessage.class);
//                System.out.println(eventTrackingMessage.getMsisdn());
//                try{
//                    EventTrackingErrorValue errorValue = gson.fromJson(eventTrackingMessage.getRawEventValue(), EventTrackingErrorValue.class);
//                    eventTrackingMessage.setEventValue(errorValue);
//                } catch (Exception exception){
////                    LOGGER.debug("failed to parse error value: "+eventTrackingMessage.getRawEventValue());
//                }
                // process record
                long timestamp = Long.parseLong(eventTrackingMessage.getTimestamp());
                long delta = new Date().getTime() - timestamp;
                if (eventTrackingMessage.getMsisdn() != null &&
                        !eventTrackingMessage.getMsisdn().isEmpty() &&
                        eventTrackingMessage.getObjectName() != null &&
//                        eventTrackingMessage.getEventValue() != null &&
//                    eventTrackingMessage.getEventValue().getStatus() != null &&
//                    eventTrackingMessage.getEventValue().getStatus().equals("false") &&
//                        eventTrackingMessage.getEventValue().getDescription() != null &&
//                    eventTrackingMessage.getEventValue().getDescription().contains("server") &&
                        delta <= TIMESTAMP_THRESHOLD && delta >= 0
                ) {
                    List<String> productCodeDestination = EventTrackingObjectNameDestinationDict.search(eventTrackingMessage.getObjectName());
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // Chuyển đổi thời gian hiện tại sang định dạng chuỗi
                    String formattedTimestamp = currentDateTime.format(formatter);
                    String formattedDateTimestamp = currentDateTime.format(formatterDate);

                    String log = eventTrackingMessage.getMsisdn() + "," + eventTrackingMessage.getTimestamp() + "," + eventTrackingMessage.getObjectName() + "," + formattedTimestamp + "," + formattedDateTimestamp + "," + new Timestamp(record.getTimestamp());

                    for (String productCode : productCodeDestination) {
                        Events event = this.jedisStorage.getObject(eventTrackingMessage.getMsisdn() + "#" + productCode, Events.class);
                        if(event != null) {
//                          LOGGER.info("msisdn: " + event.getMsisdn() + " sourceEvent: " + event.getSourceEvent() + " destinationEvent: " + event.getDestinationEvent() + " expiryTimeInMillis: " + event.getExpiryTimeInMillis());
                            this.jedisStorage.delObject(eventTrackingMessage.getMsisdn() + "#" + productCode);
                            countDestination = countDestination + 1;
                            LOGGER.info("Thue bao destination " + countDestination + ": " + eventTrackingMessage.getMsisdn() + " with destination: " + productCode + "----" + formattedTimestamp + "---- timestamp kafka: " + new Timestamp(record.getTimestamp()));

                            FileWriter fileWriter = new FileWriter(SOURCE_LOG_PATH, true);
                            fileWriter.write(log + "\n");
                            fileWriter.close();
                        }
                        // event destination đến trước event source
                        else{
                            countExpire = countExpire + 1;
                            LOGGER.info("Thue bao early " + countExpire + ": " + eventTrackingMessage.getMsisdn() + " with early: " + eventTrackingMessage.getObjectName() + "----" + formattedTimestamp + "---- timestamp kafka: " + new Timestamp(record.getTimestamp()));

                            FileWriter fileWriter = new FileWriter(SOURCE_LOG_PATH, true);
                            fileWriter.write(log + "\n");
                            fileWriter.close();
                        }
                    }
                }

                consumer.applyAck(record.getOffset());
            } catch (Exception exception){
                LOGGER.error(exception.getMessage(), exception);
                return false;
            }
        }

        return true;

    }

    public boolean open(){
        boolean status = consumer.open();
        this.running.set(status);
        this.start();
        return status;
    }

    public void close(){
        consumer.close();
//        jedisService.close();
        running.set(false);
        try {
            this.join();
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("event tracking processor close");
    }

    @Override
    public void run(){
        int count = 0;
        Date pivot = new Date();
        // implement control TPS over event tracking kafka
        while(running.get()){
            boolean status = process();
            if (!status){
                LOGGER.error("something wrong with consumer destination processor");
            } else {
                count += 1;
            }

            // reset if reach time unit
            if (new Date().getTime() - pivot.getTime() > 1000 ){
//                LOGGER.warn("reset pivot ");
                pivot = new Date();
                count = numRecords;
            }
            // sleep until
            if (numRecords - count > 50){
                while((new Date().getTime()) - pivot.getTime() < 1000){
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
//                LOGGER.warn("reset pivot after sleep");
                pivot = new Date();
                count = numRecords;
            }
        }
    }
}
