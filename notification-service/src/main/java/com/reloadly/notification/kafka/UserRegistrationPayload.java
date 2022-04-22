package com.reloadly.notification.kafka;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserRegistrationPayload implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private Long accountNumber;

    public UserRegistrationPayload() {

    }
}
