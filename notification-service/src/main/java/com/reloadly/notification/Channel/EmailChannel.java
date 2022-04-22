package com.reloadly.notification.Channel;

import com.reloadly.notification.model.ChannelType;
import com.reloadly.notification.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailChannel implements NotificationChannel {

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void notify(Message message) {
        log.info("Sending email notification to user having email : {}", message.getEmail());
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(message.getEmail());
            mailMessage.setText(message.getBody());
            mailMessage.setSubject(message.getSubject());
            mailSender.send(mailMessage);
            log.info("Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message using email channel, exception: "+ e.getMessage());
            //throw new RuntimeException("Failed to send message using email channel, exception : "+e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(ChannelType channelType) {
        return ChannelType.EMAIL == channelType;
    }

}
