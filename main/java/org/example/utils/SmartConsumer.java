package org.example.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

public class SmartConsumer extends Thread {

    private static final Logger LOGGER = Logger.getLogger(SmartConsumer.class);
    private static final int MAX_QUEUED_RECORDS = 5120;
    private static final int MAX_POLL_INTERVAL_MILLIS = 300000;
    private static final String OFFSET_RESET_LATEST = "latest";
    private static final String SASL_JAAS_CONFIG = "com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true keyTab=\"/etc/security/keytabs/10211079/dev.dmp.keytab\" storeKey=true useTicketCache=false serviceName=\"kafka\" principal=\"dmp_dev@VDS.VT\";";
    protected final AtomicBoolean running;
    protected final String topic;
    protected final String groupId;
    protected final int tps;
    protected long lastPollTime;

    protected final BlockingQueue<KafkaRecord> queuedRecords;
    protected final BlockingQueue<PartitionOffset> unappliedAcks;
    protected final List<TopicPartition> assignedPartitions;
    protected KafkaConsumer<String, String> kafkaConsumer = null;

    public SmartConsumer(String topic, String groupId, int tps) {
        this.running = new AtomicBoolean(false);
        this.topic = topic;
        this.groupId = groupId;
        this.tps = tps;

        this.queuedRecords = new ArrayBlockingQueue<>(MAX_QUEUED_RECORDS);
        this.unappliedAcks = new LinkedBlockingQueue<>();
        this.assignedPartitions = new ArrayList<>();
    }
    public boolean open() {
        boolean status = false;
        try {
            Properties kafkaConsumerParams = new Properties();
            kafkaConsumerParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Resource.EVENT_RESOURCE.KAFKA_BROKERS);
            kafkaConsumerParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            kafkaConsumerParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            kafkaConsumerParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            kafkaConsumerParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
            kafkaConsumerParams.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MILLIS);
            kafkaConsumerParams.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Math.max(tps / 4, 256));
            kafkaConsumerParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET_LATEST);
            kafkaConsumerParams.put(SaslConfigs.SASL_JAAS_CONFIG, SASL_JAAS_CONFIG);
            kafkaConsumerParams.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");

            kafkaConsumer = new KafkaConsumer<>(kafkaConsumerParams);
            kafkaConsumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
                @Override
                //lam gi do truoc khi consumer bi thu hoi
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    if (!partitions.isEmpty()) {
                        LOGGER.info("Kafka consumer previous assignment revoked: " + StringUtils.join(partitions, "|"));
                    }
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    // Update cached partitions
                    assignedPartitions.clear();
                    assignedPartitions.addAll(partitions);
                    partitions.forEach(partition -> {
                        LOGGER.info("Kafka consumer new assignment: " + partition);
                    });
                }
            });

            running.set(true);
            super.start();
            status = true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return status;
    }

    public void close() {
        if (kafkaConsumer != null) {
            try {
                running.set(false);
                kafkaConsumer.wakeup();
                try {
                    this.join();
                } catch (InterruptedException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
                while (!queuedRecords.isEmpty()) {
                    handleAcks();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        LOGGER.error(ex.getMessage(), ex);
                    }
                    keepConnctionAlive();
                }
                kafkaConsumer.close();
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            kafkaConsumer = null;
        }
    }
    public boolean isRunning() {
        return running.get();
    }

    public KafkaRecord poll() {
        return queuedRecords.poll();
    }

    public KafkaRecord poll(long duration) {
        try {
            return queuedRecords.poll(duration, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.info("consumer poll kafka failed");
            return null;
        }
    }
    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }
    public void applyAck(PartitionOffset offset) {
        unappliedAcks.add(offset);
    }

    public void applyAck(List<PartitionOffset> offsets) {
        unappliedAcks.addAll(offsets);
    }

    protected void handleAcks() {
        int size = unappliedAcks.size();
        if (size > 0) {
            List<PartitionOffset> offsets = new ArrayList<>(size);
            unappliedAcks.drainTo(offsets, size);
            Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
            for (PartitionOffset offset : offsets) {
                offsetsToCommit.put(new TopicPartition(offset.getTopic(), offset.getPartition()), new OffsetAndMetadata(offset.getOffset() + 1, null));
            }
            if (!offsetsToCommit.isEmpty()) {
                kafkaConsumer.commitSync(offsetsToCommit);
            }
        }
    }

    protected void keepConnctionAlive() {
        if ((!assignedPartitions.isEmpty())
                && (System.currentTimeMillis() - lastPollTime > (long) (0.7 * MAX_POLL_INTERVAL_MILLIS))) {
            boolean rebalanceHappened = true;
            while (rebalanceHappened) {
                List<TopicPartition> copyOfAssignedPartitions = new ArrayList<>(assignedPartitions);
                try {
                    kafkaConsumer.pause(copyOfAssignedPartitions);
                    rebalanceHappened = false;
                    lastPollTime = System.currentTimeMillis();
                    kafkaConsumer.poll(Duration.ofMillis(0));
                } catch (IllegalStateException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                } finally {
                    kafkaConsumer.resume(copyOfAssignedPartitions);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                try {
                    lastPollTime = System.currentTimeMillis();
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(250));
                    if (!records.isEmpty()) {
                        for (TopicPartition partition : records.partitions()) {
                            List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                            for (ConsumerRecord<String, String> record : partitionRecords) {
//                                System.out.println("topic: " + partition.topic() + " partition: " + partition.partition() + " offset: " + record.offset() + " value: " + record.value() + " timestamp: " + record.timestamp());
//                                if (queuedRecords.offer(new KafkaRecord(new PartitionOffset(partition.topic(), partition.partition(), record.offset()), record.value(),record.timestamp()))){
//                                    System.out.println(1);
//                                    KafkaRecord record123 = queuedRecords.poll();
//                                    System.out.println("topic: " + record123.getOffset().getTopic() + " partition: " + record123.getOffset().getPartition() + " offset: " + record123.getOffset().getOffset() + " value: " + record123.getContent() + " timestamp: " + record123.getTimestamp());
//                                }else {
//                                    System.out.println(2);
//                                }
//                                KafkaRecord kafkaRecord = new KafkaRecord(new PartitionOffset(partition.topic(), partition.partition(), record.offset()), record.value(),record.timestamp());

                                while (!queuedRecords.offer(new KafkaRecord(new PartitionOffset(partition.topic(), partition.partition(), record.offset()), record.value(),record.timestamp()))) {
                                    handleAcks();
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException ex) {
                                        LOGGER.error(ex.getMessage(), ex);
                                    }
                                    keepConnctionAlive();
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("consumer finished");
    }
}
