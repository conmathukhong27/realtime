package org.example.process;

import org.apache.log4j.Logger;
import com.google.gson.Gson;
import org.example.model.EventMap;
import org.example.model.Events;
import org.example.process.dictionary.EventTrackingObjectNameDestinationDict;
import org.example.process.dictionary.EventTrackingObjectNameSourceDict;
import org.example.process.dto.EventTrackingMessage;
import org.example.redis.service.RedisService;
import org.example.utils.*;

import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SourceDestinationProcessor extends Thread{
    private static final Logger LOGGER = Logger.getLogger(SourceDestinationProcessor.class);
    private final SmartConsumer consumer;
    private final SmartProducer producer;
//    private final RedisService jedisService;
    protected final AtomicBoolean running;
    private static final Gson gson = new Gson();
    private static final long TIMESTAMP_THRESHOLD = 115200000L; // 32 hours
    private int numRecords = 0;
    private final JedisStorage jedisStorage;
    int countSource;
    int countDestination;
    int countExpire;
    private final String SOURCE_LOG_PATH = "/u01/cskh_customer/bin/source_log.txt";
    public SourceDestinationProcessor(SmartConsumer smartConsumer,SmartProducer smartProducer,JedisStorage jedisStorage){
        this.consumer = smartConsumer;
        this.producer = smartProducer;
        this.jedisStorage = jedisStorage;
        this.running = new AtomicBoolean(false);
        this.countSource = 0;
        this.countDestination = 0;
        this.countExpire = 0;
    }

    public boolean process(){
        if (!this.running.get()){
            LOGGER.error("processor not working");
            return false;
        }
        KafkaRecord record = consumer.poll();
        if (record != null){
            try{
                EventTrackingMessage eventTrackingMessage = gson.fromJson(record.getContent(), EventTrackingMessage.class);
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
                    EventMap productCodeSource = EventTrackingObjectNameSourceDict.search(eventTrackingMessage.getObjectName());
                    List<String> productCodeDestination = EventTrackingObjectNameDestinationDict.search(eventTrackingMessage.getObjectName());

                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // Chuyển đổi thời gian hiện tại sang định dạng chuỗi
                    String formattedTimestamp = currentDateTime.format(formatter);
                    String formattedDateTimestamp = currentDateTime.format(formatterDate);
                    // In ra timestamp
                    String log = eventTrackingMessage.getMsisdn() + "," + eventTrackingMessage.getTimestamp() + "," + eventTrackingMessage.getObjectName() + "," + formattedTimestamp + "," + formattedDateTimestamp + "," + new Timestamp(record.getTimestamp());

                    if(productCodeSource != null){
                        Events events = new Events(eventTrackingMessage.getMsisdn(), eventTrackingMessage.getObjectName(), productCodeSource.getDestination(), productCodeSource.getExpiryTimeInMillis());
                        this.jedisStorage.putObject(eventTrackingMessage.getMsisdn() + "#" + eventTrackingMessage.getObjectName(), events, events.getExpiryTimeInMillis());
                        countSource = countSource + 1;
                        LOGGER.info("Thue bao source " + countSource + ": " + eventTrackingMessage.getMsisdn() + " with source: " + eventTrackingMessage.getObjectName() + "----" + formattedTimestamp + "---- timestamp kafka: " + new Timestamp(record.getTimestamp()));
                        LOGGER.info("Successfully insert to redis event");

                        FileWriter fileWriter = new FileWriter(SOURCE_LOG_PATH, true);
                        fileWriter.write(log + "\n");
                        fileWriter.close();
                    }else if(productCodeDestination != null){
                        LOGGER.info("Thue bao start destination: " + eventTrackingMessage.getMsisdn() + " with destination: " + eventTrackingMessage.getObjectName() + "----" + formattedTimestamp + "---- timestamp kafka: " + new Timestamp(record.getTimestamp()));

                        // push kafka destination
                        producer.putMessage(record.getContent());
                    }
//                    else{
//                        System.out.println("not found event -- 3: "+record.getContent());
//                        System.out.println("3");
//                    }
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
        status &= producer.open();
        this.running.set(status);
        this.start();
        return status;
    }

    public void close(){
        consumer.close();
        producer.close();
//        jedisService.close();
        running.set(false);
        try {
            this.join();
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("consumer source and destination processor close");
    }

    @Override
    public void run(){
        int count = 0;
        Date pivot = new Date();
        // implement control TPS over event tracking kafka
        while(running.get()){
            boolean status = process();
            if (!status){
                LOGGER.error("something wrong with consumer source and destination processor");
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
