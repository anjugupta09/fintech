package com.reloadly.kafka;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegistrationPayload {

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private Long accountNumber;
}
