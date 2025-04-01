package org.example.redis.service;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.example.model.Events;
import redis.clients.jedis.Jedis;

public class RedisService {
    private static final Logger LOGGER = Logger.getLogger(RedisService.class);

    private static final Gson gson= new Gson();
    private final Jedis jedis;
    public RedisService(Jedis jedis) {
        this.jedis = jedis;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void close(){
        this.jedis.close();
        LOGGER.info("jedis processor close");
    }
    public void insert(Events event){
        try{
            String jsonObject = gson.toJson(event);
//            System.out.println(jedis.ping());
//            System.out.println(jsonObject);
//
//            System.out.println("msisdn: " + event.getMsisdn() + " source: " + event.getSourceEvent() + " destination: " + event.getDestinationEvent() + " expiry: " + event.getExpiryTimeInMillis());
            jedis.setex(event.getMsisdn() + "#" + event.getSourceEvent(), event.getExpiryTimeInMillis(), jsonObject);
//            try{
//                String value = jedis.get(event.getMsisdn() + "_" + event.getSourceEvent());
//                if(value != null){
//                    return value;
//                }
//            }catch(Exception ex){
//                LOGGER.error("Fail to insert redis", ex);
//            }
        } catch (Exception ex){
            LOGGER.error("Fail to insert redis", ex);
        }
//        return null;
    }

    public String getKey(String key){
        try{
            return jedis.get(key);
        } catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    public void deleteKey(String key){
        try{
            jedis.del(key);
        } catch (Exception ex){
            System.out.println("khong ton tai");
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public int getDatabaseIndex(){
        return jedis.getDB();
    }
}
