package com.patrick.sneakerkillerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

    /**
     * 官方邮件
     */
    @Value("${spring.mail.username}")
    private String from;

    private JavaMailSender mailSender;

    @Autowired
    public SendMailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendMail(String toMail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toMail);
        message.setSubject("抢购成功通知");
        message.setText("抢购成功，请在10分钟内前往 '我的订单' 完成付款");
        mailSender.send(message);
    }
}
