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
    private Long id;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private Role role;
    private LoginType loginType;
}
