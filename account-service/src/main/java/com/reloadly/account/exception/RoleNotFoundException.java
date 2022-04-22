package com.reloadly.account.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleNotFoundException extends Exception {

    private String message;

    public RoleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
