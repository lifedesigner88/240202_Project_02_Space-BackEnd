package com.encore.space.domain.member.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.email.dto.EmailCodeReqDto;
import com.encore.space.domain.email.dto.EmailReqDto;
import com.encore.space.domain.login.domain.CustomUserDetails;
import com.encore.space.domain.login.dto.LoginReqDto;
import com.encore.space.domain.login.service.LoginService;
import com.encore.space.domain.member.domain.LoginType;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final LoginService loginService;

    @Autowired
    public MemberController(
            MemberService memberService,
            LoginService loginService
    ) {
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @Operation(
            summary = "이메일 회원 가입",
            description = "신규 회원 생성"
    )
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> memberCreate(@RequestBody @Valid MemberReqDto memberReqDto){
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
    public ResponseEntity<CommonResponse> emailAuthentication(@RequestBody @Valid EmailReqDto emailReqDto) throws MessagingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return CommonResponse.responseMassage(
                HttpStatus.OK,
                "이메일 인증번호 발송",
                memberService.emailAuthentication(emailReqDto)
        );
    }

    @Operation(
            summary = "메일 인증 번호 확인",
            description = "인증 번호와 이메일을 입력받아 일치하는지 확인"
    )
    @PostMapping("/emailCheck")
    public ResponseEntity<CommonResponse> emailCheck(@RequestBody @Valid EmailCodeReqDto emailCodeReqDto) {
        memberService.emailCheck(emailCodeReqDto);
        return CommonResponse.responseMassage(HttpStatus.OK, "이메일 인증완료");
    }

    @Operation(
            summary = "일반 로그인",
            description = "이메일과 패스워드를 받아 로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> emailLogin(@RequestBody @Valid LoginReqDto loginReqDto) {
        return CommonResponse.responseMassage(HttpStatus.OK, "로그인 되었습니다.", loginService.login(loginReqDto, LoginType.EMAIL));
    }

    @Operation(
            summary = "로그아웃",
            description = "AccessToken 을 입력받아 로그아웃"
    )
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {
        loginService.logout(request);
        return CommonResponse.responseMassage(HttpStatus.OK, "로그아웃 되었습니다.");
    }


    @Operation(
            summary = "일반 로그인 test",
            description = "이메일과 패스워드를 받아 로그인 test"
    )
    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/qwe")
    public String qwe(@AuthenticationPrincipal CustomUserDetails userDetails){
        return "ok" + userDetails.getUsername() ;
    }
}
