package com.sb.file.compressor.core.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * @author Himal Rai on 2/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Service
public class MailSenderService {

    private final JavaMailSenderImpl javaMailSender;

    public MailSenderService(JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public  void sendSimpleMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }


}
