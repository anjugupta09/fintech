package com.reloadly.account.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidAccountNumber extends Exception {

    private String message;

    public InvalidAccountNumber(String message) {
        super(message);
        this.message = message;
    }
}
