package com.reloadly.notification.Channel;

import com.reloadly.notification.model.ChannelType;
import com.reloadly.notification.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationChannelFactory {

    private final List<NotificationChannel> channelList;

    @Autowired
    public NotificationChannelFactory(List<NotificationChannel> channelList) {
        this.channelList = channelList;
    }

    public NotificationChannel get(ChannelType c) {
        return channelList
                .stream()
                .filter(service -> service.supports(c))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No channel found with type : " + c));
    }

    public void notifyAll(Message msg) {
        for(NotificationChannel channel : channelList) {
            channel.notify(msg);
        }
    }
    public List<NotificationChannel> getChannels() {
        return channelList;
    }
}
