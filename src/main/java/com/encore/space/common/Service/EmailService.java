package com.encore.space.common.Service;

import io.swagger.models.properties.EmailProperty;
import io.swagger.models.properties.StringProperty;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;


    @Value("${spring.mail.username}")
    private String fromEmail;

    //타임리프를 이용한 context 설정
    public String setContext(String content) {
        Context context = new Context();
        context.setVariable("content", content);
        return springTemplateEngine.process("email", context);     // email.html
    }

    public void SendEmail(String email, String title, String content) throws MessagingException  {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(message,true,"UTF-8");
        mailHelper.setFrom(fromEmail);
        mailHelper.setTo(email);
        mailHelper.setSubject(title);
        mailHelper.setText(setContext(content), true);
        javaMailSender.send(message);

    }

}
