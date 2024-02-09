package com.encore.space.domain.member.controller;

import com.encore.space.common.CommonResponse;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원관련 API")
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
    @Operation(summary = "회원 가입", description = "신규 회원 생성")
    @PostMapping("/create")
    private ResponseEntity<CommonResponse> memberCreate(@RequestBody @Valid MemberReqDto memberReqDto){
        return CommonResponse.responseMassage(
                HttpStatus.CREATED,
                "회원가입 성공",
                memberService.memberCreate(memberReqDto)
        );
    }

    @Operation(
            summary = "메일 인증 번호 발송",
            description = "이메일을 받아 해당 이메일로 인증번호 8자리 발송 "
    )
    @PostMapping("/emailAuthentication")
    public ResponseEntity<CommonResponse> emailAuthentication(@RequestBody HashMap<String, String> map) throws MessagingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "이메일 인증번호 발송",
                memberService.emailAuthentication(map.get("email"))
        );
    }
    @Operation(
            summary = "메일 인증 번호 확인",
            description = "인증 번호와 이메일을 입력받아 일치하는지 확인"
    )
    @PostMapping("/emailCheck")
    public ResponseEntity<CommonResponse> emailCheck(@RequestBody HashMap<String, String> map) {
        memberService.emailCheck(map.get("email"), map.get("code"));
        return CommonResponse.responseMassage(HttpStatus.OK, "이메일 인증완료");
    }
}
