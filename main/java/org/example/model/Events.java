package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Events {
    private String msisdn;
    private String sourceEvent;
    private String destinationEvent;
    private int expiryTimeInMillis;


}
