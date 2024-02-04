package com.encore.space.common;


import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import org.springframework.stereotype.Component;

@Component
public class ChangeType {

    public MemberResDto memberTOmemberResDto(Member member){
        return MemberResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .loginType(member.getLoginType())
                .build();
    }

    public Member memberReqDtoTOmember(MemberReqDto memberReqDto){
        return Member.builder()
                .name(memberReqDto.getName())
                .email(memberReqDto.getEmail())
                .password(memberReqDto.getPassword())
                .nickname(memberReqDto.getNickname())
                .loginType(memberReqDto.getLoginType())
                .build();
    }

}
