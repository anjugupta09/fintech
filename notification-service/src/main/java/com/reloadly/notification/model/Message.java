package com.reloadly.notification.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String email;
    private String phoneNumber;
    private String subject;
    private String body;
}
