package com.encore.space.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

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
    public String makeRandomNumber(String email ) throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

}
