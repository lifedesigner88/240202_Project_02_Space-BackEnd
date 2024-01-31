package com.encore.space.common.controller;

import com.encore.space.common.Service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }


    @GetMapping("/email")
    @ResponseBody
    public String emailSend()  {
        // String email, String title, String content
        try {
            emailService.SendEmail(
                    "jo_gn97@naver.com" ,
                    "엔코아 스페이스에서 보내는 메일입니다.",
                    "진짜루?"
            );
        } catch (Exception e){
            e.printStackTrace();
        }
        log.info("매일");
        return "ok";
    }

}
