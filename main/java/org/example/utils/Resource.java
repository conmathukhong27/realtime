package org.example.utils;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;

public class Resource {
    private static final String RESOURCE_FILE = "config";
    private static final Logger LOGGER = Logger.getLogger(Resource.class);
    private static final ResourceBundle CSKH_RESOURCE = Resource.getDmPResource(RESOURCE_FILE);

    private static ResourceBundle getDmPResource(String resourceFile) {
        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = ResourceBundle.getBundle(resourceFile);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return resourceBundle;
    }
    public static String getParamValue(String key) {
        return CSKH_RESOURCE.getString(key);
    }

    public static class EVENT_RESOURCE {
        public static final String KAFKA_BROKERS = Resource.getParamValue("event.kafka.brokers");
        public static final String KAFKA_READABLE_TOPIC = Resource.getParamValue("event.kafka.readable.topic");
        public static final String KAFKA_CONSUMER_ID = Resource.getParamValue("event.kafka.consumer.id");
    }

    public static class CEP_RESOURCE {
        public static final String KAFKA_BROKERS = Resource.getParamValue("cep.kafka.brokers");
        public static final String KAFKA_READABLE_TOPIC = Resource.getParamValue("cep.kafka.readable.topic");
        public static final String KAFKA_CONSUMER_ID = Resource.getParamValue("event.kafka.consumer.id");
    }

    public static class DES_RESOURCE {
        public static final String KAFKA_BROKERS = Resource.getParamValue("des.kafka.brokers");
        public static final String KAFKA_READABLE_TOPIC = Resource.getParamValue("des.kafka.readable.topic");
        public static final String KAFKA_CONSUMER_ID = Resource.getParamValue("event.kafka.consumer.id");
    }

    public static class REDIS_RESOURCE {
        public static final String REDIS_HOST = Resource.getParamValue("redis.host");
        public static final int REDIS_PORT = Integer.parseInt(Resource.getParamValue("redis.port"));
        public static final String REDIS_PASSWORD = Resource.getParamValue("redis.password");
        public static final int REDIS_DB_INDEX = Integer.parseInt(Resource.getParamValue("redis.database.index"));
    }
}
