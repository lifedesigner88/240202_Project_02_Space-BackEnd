package com.encore.space.domain.member.dto.reqdto;

import com.encore.space.domain.member.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberReqDto {

    private String name;
    private String email;
    private String password;
    private String nickname;
    private LoginType loginType;

}
