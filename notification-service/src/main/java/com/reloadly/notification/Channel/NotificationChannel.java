package com.reloadly.notification.Channel;

import com.reloadly.notification.model.ChannelType;
import com.reloadly.notification.model.Message;

public interface NotificationChannel {

    default void notify(Message message) {
        throw new RuntimeException("Notify method is not implemented yet.");
    }
    default boolean supports(ChannelType type) {
        throw new RuntimeException("Notify method is not implemented yet.");
    }
}
