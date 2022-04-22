package com.reloadly.transaction.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsufficientBalanceException extends Exception {

    private String message;

    public InsufficientBalanceException(String message) {
        super(message);
        this.message = message;
    }

}
