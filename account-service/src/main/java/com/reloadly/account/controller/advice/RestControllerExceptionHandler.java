package com.reloadly.account.controller.advice;

import com.reloadly.account.exception.InvalidAccountNumber;
import com.reloadly.account.exception.RoleNotFoundException;
import com.reloadly.account.exception.UserAlreadyExistsException;
import com.reloadly.account.response.ErrorContent;
import com.reloadly.account.response.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleException(MethodArgumentNotValidException exception) {
        List<ErrorContent> errors = exception.getBindingResult().getAllErrors().stream().map(error ->
                ErrorContent.create(error.getDefaultMessage(),
                                error.getObjectName()).
                        withMetadata("fieldName", ((FieldError) error).getField())
                        .withMetadata("invalidValue", ((FieldError) error).getRejectedValue())
        ).collect(Collectors.toList());
        return ValidationErrorResponse.validationFailure("Validation Error", errors);
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class, InvalidAccountNumber.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleException(Exception exception) {
        return ValidationErrorResponse.validationFailure( exception.getMessage(), Collections.emptyList());
    }


    @ExceptionHandler(value = {RoleNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ValidationErrorResponse handleException(RoleNotFoundException exception) {
        return ValidationErrorResponse.validationFailure(exception.getMessage(), Collections.emptyList());
    }


}
