package com.encore.space.domain.space.dto.resdto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembersBySpaceResDto {
    private final String role;
    private final String name;
    private final String nickName;
    private final String email;
}
