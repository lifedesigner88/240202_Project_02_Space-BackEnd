package com.encore.space.domain.member.dto.resdto;

import com.encore.space.domain.member.domain.LoginType;
import com.encore.space.domain.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
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
