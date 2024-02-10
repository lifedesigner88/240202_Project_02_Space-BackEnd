package com.encore.space.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public EmailService(
            JavaMailSender javaMailSender,
            SpringTemplateEngine springTemplateEngine,
            RedisTemplate<String, String> redisTemplate
    ){
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.redisTemplate = redisTemplate;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    //타임리프를 이용한 context 설정
    public String setContext(String content) {
        Context context = new Context();
        context.setVariable("content", content);
        return springTemplateEngine.process("email", context);     // email.html
    }

    public void SendEmail(String email, String title, String content) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(message,true,"UTF-8");
        mailHelper.setFrom(fromEmail);
        mailHelper.setTo(email);
        mailHelper.setSubject(title);
        mailHelper.setText(setContext(content), true);
        javaMailSender.send(message);

    }
    public String makeRandomCode(String email ) throws NoSuchAlgorithmException {
        Random BooleanRandom = new Random();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<8;i++) {
            if(BooleanRandom.nextBoolean()){
                builder.append((char)((int)(random.nextInt(26))+97));
            }else{
                builder.append((random.nextInt(10)));
            }
        }
        redisTemplate.opsForValue().set(email, builder.toString(), 10, TimeUnit.MINUTES);
        return builder.toString();
    }

    public boolean VerificationCode(String email, String code) {
        String Code = redisTemplate.opsForValue().get(email);
        return Code != null && Code.equals(code);
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

}
