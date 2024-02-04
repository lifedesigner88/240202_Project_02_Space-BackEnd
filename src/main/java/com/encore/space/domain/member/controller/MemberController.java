package com.encore.space.domain.member.controller;

import com.encore.space.common.CommonResponse;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(
            MemberService memberService
    ) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    private ResponseEntity<CommonResponse> memberCreate(MemberReqDto memberReqDto){
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "회원가입 성공",
                memberService.memberCreate(memberReqDto)
        );
    }

    @PostMapping("/emailAuthentication")
    public ResponseEntity<CommonResponse> emailAuthentication(@RequestParam String email) throws MessagingException, NoSuchAlgorithmException {
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "이메일 인증번호 발송",
                memberService.emailAuthentication(email)
        );
    }
}
