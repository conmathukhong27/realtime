package org.example.process.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EventTrackingMessage {
    @SerializedName("identity")
    private String msisdn;
    @SerializedName("object_name")
    private String objectName;
//    @SerializedName("event_value")
//    private String rawEventValue;
//    private EventTrackingErrorValue eventValue;
    @SerializedName("time_stamp")
    private String timestamp;
    private String processCode;
}
