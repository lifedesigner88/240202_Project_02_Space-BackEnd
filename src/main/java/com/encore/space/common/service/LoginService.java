package com.encore.space.common.service;

import com.encore.space.common.oauth.GitHubMember;
import com.encore.space.common.oauth.GoogleMember;
import com.encore.space.common.oauth.MemberInfo;
import com.encore.space.domain.member.dto.reqdto.MemberReqDto;
import com.encore.space.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class LoginService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String gitClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String gitClientSecret;


    private final MemberService memberService;

    @Autowired
    public LoginService(MemberService memberService) {
        this.memberService = memberService;
    }

    /*
    http://localhost:8080/login/oauth2/code/google
    http://localhost:8080/login/oauth2/code/github
    http://localhost:8080/oauth2/authorization/google
    http://localhost:8080/oauth2/authorization/github
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        MemberInfo info = null;

        // google
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            info = new GoogleMember(oAuth2User.getAttributes());
        }

        // github
        if(userRequest.getClientRegistration().getRegistrationId().equals("github")){
            info = new GitHubMember(oAuth2User.getAttributes(), userRequest.getAccessToken().getTokenValue());
        }

        if(!memberService.DuplicatedEmail(Objects.requireNonNull(info).getEmail())){
            memberService.memberCreate(
                    MemberReqDto.builder()
                            .name(info.getName())
                            .email(info.getEmail())
                            .nickname(info.getName())
                            .profile(info.getPicture())
                            .loginType(info.getProvider())
                            .build()
            );
        }
        return oAuth2User;
    }
}
