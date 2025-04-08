package org.example;

import com.google.gson.Gson;

import org.example.process.DestinationProcessor;
import org.example.process.NotificationExpireTime;
import org.example.process.SourceDestinationProcessor;
import org.example.redis.service.RedisService;
import org.example.redis.service.RedisTemplateBuilder;
import org.example.utils.SmartConsumer;

import java.sql.Timestamp;
import java.util.*;

import org.example.utils.*;

import org.apache.log4j.Logger;
import java.lang.ref.WeakReference;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    public static final Gson gson = new Gson();
    private final SourceDestinationProcessor sourceDestinationProcessor;
    private final NotificationExpireTime notificationExpireTime;
    private final DestinationProcessor destinationProcessor;
//    private RedisTemplateBuilder redisTemplateBuilder;
    private JedisStorage jedisStorage;
    private static final long POLLING_INTERVAL = 10*60*1000; //10m
    private long pivotTimestamp;
    private static final int KAFKA_TPS = 512;
    private static final int REDIS_TIMEOUT = 30000;

    public Main(){
        SmartConsumer eventTrackingConsumer = new SmartConsumer(Resource.EVENT_RESOURCE.KAFKA_READABLE_TOPIC, Resource.EVENT_RESOURCE.KAFKA_CONSUMER_ID, KAFKA_TPS);
        SmartProducer destinationProducer = new SmartProducer(Resource.DES_RESOURCE.KAFKA_READABLE_TOPIC, Resource.DES_RESOURCE.KAFKA_CONSUMER_ID);
        SmartConsumer destinationConsumer = new SmartConsumer(Resource.DES_RESOURCE.KAFKA_READABLE_TOPIC, Resource.DES_RESOURCE.KAFKA_CONSUMER_ID, KAFKA_TPS);
        SmartProducer cepProducer = new SmartProducer(Resource.CEP_RESOURCE.KAFKA_READABLE_TOPIC, Resource.CEP_RESOURCE.KAFKA_CONSUMER_ID);

//        redisTemplateBuilder = new RedisTemplateBuilder(Resource.REDIS_RESOURCE.REDIS_HOST,Resource.REDIS_RESOURCE.REDIS_PORT,REDIS_TIMEOUT,Resource.REDIS_RESOURCE.REDIS_PASSWORD,Resource.REDIS_RESOURCE.REDIS_DB_INDEX);
        jedisStorage = JedisStorage.getInstance();

        //consumer topic kafka source,destination --- producer topic kafka destination
        sourceDestinationProcessor = new SourceDestinationProcessor(eventTrackingConsumer,destinationProducer,jedisStorage);
        //consumer topic kafka destination
        destinationProcessor = new DestinationProcessor(destinationConsumer,jedisStorage);
        //producer topic CEP
        notificationExpireTime = new NotificationExpireTime(cepProducer,jedisStorage);
        pivotTimestamp = new Date().getTime();
    }

    public static void main(String[] args){
        final Main cshkMain = new Main();

        try{
            boolean status = cshkMain.open();
            if (!status){
                LOGGER.error("unable to enable all processor");
            } else {
                while (!Thread.currentThread().isInterrupted()){
                    cshkMain.process();
                    Thread.sleep(250);
                }
            }
        } catch (InterruptedException ex){
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            cshkMain.close();
        }
    }

    public void process(){
        if (new Date().getTime() - pivotTimestamp > POLLING_INTERVAL){
            gc();
            pivotTimestamp = new Date().getTime();
        }
    }
    public boolean open(){
        boolean status = true;
        status &= sourceDestinationProcessor.open();
        status &= notificationExpireTime.open();
        status &= destinationProcessor.open();
        return status;
    }
    public void close(){
        sourceDestinationProcessor.close();
        notificationExpireTime.close();
        destinationProcessor.close();
        LOGGER.info("close error push process");
    }
    public static void gc() {
        Object obj = new Object();
        WeakReference ref = new WeakReference<Object>(obj);
        obj = null;
        while(ref.get() != null) {
            System.gc();
        }
    }
}