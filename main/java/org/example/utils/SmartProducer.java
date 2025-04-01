package org.example.utils;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import java.util.Properties;

public class SmartProducer {
    private static final Logger LOGGER = Logger.getLogger(SmartProducer.class);
    private Producer<String, String> kafkaProducer;
    private static final String SASL_JAAS_CONFIG = "com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true keyTab=\"/etc/security/keytabs/10211079/dev.dmp.keytab\" storeKey=true useTicketCache=false serviceName=\"kafka\" principal=\"dmp_dev@VDS.VT\";";
    private final String clientId;
    private final String topic;

    public SmartProducer(String topic, String clientId) {
        this.clientId = clientId;
        this.topic = topic;
        this.kafkaProducer = null;
    }
    public boolean putMessage(String message) {
        boolean status = false;
        if (kafkaProducer != null) {
            try {
                kafkaProducer.send(new ProducerRecord<String,String>(topic, message));
                status = true;
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return status;
    }

    /**
     *
     * @return
     */
    public boolean open() {
        boolean status = false;
        try {
            close();
            Properties kafkaParams = new Properties();
            kafkaParams.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Resource.DES_RESOURCE.KAFKA_BROKERS);
            kafkaParams.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
            kafkaParams.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            kafkaParams.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            kafkaParams.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
            kafkaParams.put(SaslConfigs.SASL_JAAS_CONFIG, SASL_JAAS_CONFIG);
            this.kafkaProducer = new KafkaProducer<>(kafkaParams);
            status = true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return status;
    }

    /**
     *
     */
    public void close() {
        if (kafkaProducer != null) {
            kafkaProducer.close();
            kafkaProducer = null;
        }
    }

//    public static void ProducerKafka() {
//        try {
//            Properties kafkaParams = new Properties();
//            kafkaParams.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
//            kafkaParams.put(ProducerConfig.CLIENT_ID_CONFIG, "karafka");
//            kafkaParams.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//            kafkaParams.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
////            kafkaParams.put("request.required.acks", "1");
////            kafkaParams.put("sasl.kerberos.service.name", "kafka");
////            kafkaParams.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SECURITY_PROTOCOL);
////            if ("SASL_PLAINTEXT".equals(protocol)) {
////                kafkaParams.put(SaslConfigs.SASL_JAAS_CONFIG, "com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true debug=true keyTab=\"" + keyTab + "\" storeKey=true useTicketCache=false serviceName=\"kafka\" principal=\"" + principal + "\";");
////            }
//            KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaParams);
//            while (true) {
//                producer.send(new ProducerRecord<>("quickstart-events", "test", "Hello Kafka"));
//                Thread.sleep(1000);
//            }
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//    }
}
