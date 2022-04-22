package com.reloadly.account.exception;

import lombok.Getter;
import lombok.Setter;

public class UserAlreadyExistsException extends Exception {

    @Getter
    @Setter
    private String message;

    public UserAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}
