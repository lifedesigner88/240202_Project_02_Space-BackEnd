package com.encore.space.common.domain;


import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.dto.resdto.MemberResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ChangeType {

    // 시큐리티에 있는 것을 가져다 쓰면 순환 참조가 걸림 조심.
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


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
                .profile(memberReqDto.getProfile())
                .password(memberReqDto.getPassword() == null ? null : passwordEncoder().encode(memberReqDto.getPassword()))
                .nickname(memberReqDto.getNickname())
                .loginType(memberReqDto.getLoginType())
                .build();
    }

}
