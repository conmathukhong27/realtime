package org.example.process;

import org.apache.log4j.Logger;
import org.example.redis.KeyExpiredListener;
import org.example.redis.service.RedisService;

import org.example.utils.JedisStorage;
import org.example.utils.SmartProducer;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationExpireTime extends Thread{
    private static final Logger LOGGER = Logger.getLogger(NotificationExpireTime.class);
    private final SmartProducer producer;
//    private final RedisService jedisService;
    private final JedisStorage jedisStorage;
    protected final AtomicBoolean running;
    private int numRecords = 0;

    public NotificationExpireTime(SmartProducer smartProducer, JedisStorage jedisStorage){
        this.producer = smartProducer;
//        this.jedisService = jedisService;
        this.jedisStorage = jedisStorage;
        this.running = new AtomicBoolean(false);
    }

    public boolean process(){
        if (!this.running.get()){
            LOGGER.error("processor not working");
            return false;
        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException exception) {
//            LOGGER.error(exception.getMessage(), exception);
//        }

        try {
            this.jedisStorage.getJedis().psubscribe(new KeyExpiredListener(producer,jedisStorage), "__keyevent@" + this.jedisStorage.getDatabaseIndex() + "__:expired");
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage(), exception);
            return false;
        }
        return true;
    }
    public boolean open(){
        boolean status = producer.open();
        this.running.set(status);
        this.start();
        return status;
    }

    public void close(){
        producer.close();
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
                LOGGER.error("something wrong with event tracking processor");
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
