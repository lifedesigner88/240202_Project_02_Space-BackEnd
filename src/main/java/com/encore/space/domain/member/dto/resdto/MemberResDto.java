package com.encore.space.domain.member.dto.resdto;

import com.encore.space.domain.member.domain.LoginType;
import com.encore.space.domain.member.domain.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@Schema(description = "회원 반환을 위한 DTO")
@AllArgsConstructor
public class MemberResDto {
    @Schema(description = "아이디")
    private Long id;
    @Schema(description = "이름")
    private String name;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "권한")
    private Role role;
    @Schema(description = "로그인 타입")
    private LoginType loginType;
}
