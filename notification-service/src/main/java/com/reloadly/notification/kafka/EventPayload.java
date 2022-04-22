package com.reloadly.notification.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EventPayload<T> implements Serializable {

    private String correlationId;
    private String timestamp;
    private String eventType;
    private T payload;
}
