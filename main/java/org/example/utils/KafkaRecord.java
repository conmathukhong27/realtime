package org.example.utils;

import java.io.Serializable;

public class KafkaRecord implements Serializable {
    private PartitionOffset offset;
    private String content;
    private long timestamp;
    public KafkaRecord() {
    }

    public KafkaRecord(PartitionOffset offset, String content, long timestamp) {
        this.offset = offset;
        this.content = content;
        this.timestamp = timestamp;
    }

    public PartitionOffset getOffset() {
        return offset;
    }

    public void setOffset(PartitionOffset offset) {
        this.offset = offset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
