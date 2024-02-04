package com.encore.space.domain.member.controller;

import com.encore.space.common.CommonResponse;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

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
    private ResponseEntity<CommonResponse> memberCreate(@RequestBody @Valid MemberReqDto memberReqDto){
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "회원가입 성공",
                memberService.memberCreate(memberReqDto)
        );
    }

    @PostMapping("/emailAuthentication")
    public ResponseEntity<CommonResponse> emailAuthentication(@RequestBody HashMap<String, String> map) throws MessagingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "이메일 인증번호 발송",
                memberService.emailAuthentication(map.get("email"))
        );
    }
    @PostMapping("/emailCheck")
    public ResponseEntity<CommonResponse> emailCheck(@RequestBody HashMap<String, String> map) {
        memberService.emailCheck(map.get("email"), map.get("code"));
        return CommonResponse.responseMassage(HttpStatus.OK, "이메일 인증완료");
    }
}
