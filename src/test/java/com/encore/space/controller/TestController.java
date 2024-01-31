package com.encore.space.controller;

import com.encore.space.common.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestController {

    @Autowired
    private EmailService emailService;

    @Test
    public void emailSendTest(){

    }
}
