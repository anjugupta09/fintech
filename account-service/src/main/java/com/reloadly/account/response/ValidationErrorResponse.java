package com.reloadly.account.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse {

    private String message;
    List<ErrorContent> errors;

    public static ValidationErrorResponse validationFailure(String errorMessage, List<ErrorContent> errorContentList) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setMessage(errorMessage);
        response.setErrors(errorContentList);
        return response;
    }

}

