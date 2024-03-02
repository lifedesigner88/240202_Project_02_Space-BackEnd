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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<CommonResponse> memberCreate(@RequestBody @Valid MemberReqDto memberReqDto) {
        return CommonResponse.responseMessage(
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
        return CommonResponse.responseMessage(
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
        return CommonResponse.responseMessage(HttpStatus.OK, "이메일 인증완료");
    }

    @Operation(
            summary = "일반 로그인",
            description = "이메일과 패스워드를 받아 로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> emailLogin(@RequestBody @Valid LoginReqDto loginReqDto) {
        return CommonResponse.responseMessage(HttpStatus.OK, "로그인 되었습니다.", loginService.login(loginReqDto, LoginType.EMAIL));
    }

    @Operation(
            summary = "로그아웃",
            description = "AccessToken 을 입력받아 로그아웃"
    )
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {
        loginService.logout(request);
        return CommonResponse.responseMessage(HttpStatus.OK, "로그아웃 되었습니다.");
    }


    @Operation(
            summary = "일반 로그인 test",
            description = "이메일과 패스워드를 받아 로그인 test"
    )

//    @PreAuthorize("hasAnyRole('MANAGER')")

    @PostMapping("/qwe")
    public String qwe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return "ok" + userDetails.getUsername();
    }

    @Operation(
            summary = "가입된 모든 회원 조회",
            description = "모든 회원 조회"
    )
    @GetMapping("/members")
    public ResponseEntity<CommonResponse> findAllMembers() {
        return CommonResponse.responseMessage(HttpStatus.OK, "모든 회원 조회", memberService.findAllMembers());
    }
    @Operation(
            summary = "로그인된 개인정보 조회",
            description = "개인정보조회"
    )
    @GetMapping("/mypage")
    public ResponseEntity<CommonResponse> getMyInfo() {
        return CommonResponse.responseMessage(HttpStatus.OK, "MyPage 정보", memberService.getMyInfo());
    }


    @Operation(
            summary = "DelYn으로 회원 탈퇴 기능",
            description = "탈퇴후 반복 재 가입을 위헤 이메일에 "+"id*_Deleted_"+" 를 추가"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse> deleteMemberUseingDelYn() {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "회원 탈퇴 성공",
                memberService.deleteMemberUseingDelYn());
    }

    @Operation(
            summary = "회원 이름과 닉네임 변경",
            description = "이름과 닉네임을 수정"
    )
    @PatchMapping("/patch")
    public ResponseEntity<CommonResponse> patchMemberInfo(@RequestBody MemberReqDto memberReqDto) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "회원 정보 수정 완료",
                memberService.patchMemberInfo(memberReqDto));
    }

    @Operation(
            summary = "프로필 사진을 저장함",
            description = "프로필 사진 저장"
    )
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestParam MultipartFile profile) {
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "회원 프로필 사진 수정 완료",
                memberService.updateProfile(profile));
    }

    @Operation(
            summary = "프로필 사진 출력",
            description = "프로필 사진을 출력함"
    )
    @GetMapping("/profile/image/{email}")
    public ResponseEntity<Resource> getProfileImage (@PathVariable String email){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(memberService.getProfileImage(email));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "비밀번호를 업데이트함"
    )
    @PatchMapping("/password")
    public ResponseEntity<CommonResponse> updatePassword (@RequestBody MemberReqDto memberReqDto){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "패스워드 변경 완료",
                memberService.updatePassword(memberReqDto.getPassword()));
    }

}
