package com.vector.server.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @description:
 * @Title: EmailTask
 * @Package com.vector.server.task
 * @Author 芝士汉堡
 * @Date 2022/12/10 21:03
 */
@Component
@Scope("prototype") // 保证每次调用都是新的对象
public class EmailTask implements Serializable {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${applet.email.system}")
    private String mailbox;

    @Async
    public void sendAsync(SimpleMailMessage message){
        message.setFrom(mailbox);
//        message.setCc(mailbox);
        javaMailSender.send(message);
    }
}
