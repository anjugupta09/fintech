package com.reloadly.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
public class ErrorContent {
    private String message;
    private String objectName;

    @Singular("metadata")
    private Map<String, ?> metadata;

    public ErrorContent withMessage(String message) {
        return this.toBuilder().message(message).build();
    }

    public ErrorContent withMetadata(String key, Object value) {
        return this.toBuilder().metadata(key, value).build();
    }

    public ErrorContent withMetadata(Map<String, ?> metadata) {
        return this.toBuilder().metadata(metadata).build();
    }

    public static ErrorContent create(String message, String objectName) {
        return ErrorContent.builder().message(message).objectName(objectName).build();
    }
}
