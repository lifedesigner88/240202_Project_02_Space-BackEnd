package com.encore.space.domain.space.dto.resdto;


import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.space.domain.SpaceMember;
import lombok.Getter;

@Getter
public class GetSpaceMemberResDto {
    private final String role;
    private final String name;
    private final String nickName;
    private final String email;

    public GetSpaceMemberResDto (SpaceMember spaceMember){
        Member member = spaceMember.getMember();
        this.role = spaceMember.getSpaceRole().name();
        this.name = member.getName();
        this.nickName = member.getNickname();
        this.email = member.getEmail();
    }
}
