package org.example.process.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EventTrackingErrorValue {
    @SerializedName("err_code")
    private String errorCode;
    @SerializedName("status")
    private String status;
    @SerializedName("description")
    private String description;
}
