package com.reloadly.notification.Channel;

import com.reloadly.notification.model.ChannelType;
import com.reloadly.notification.model.Message;
import org.springframework.stereotype.Component;

@Component
public class SmsChannel implements NotificationChannel {

    @Override
    public void notify(Message message) {
        //can add the sms notification functionality by using some third party APIs.
    }

    @Override
    public boolean supports(ChannelType channelType) {
        return ChannelType.EMAIL == channelType;
    }
}
