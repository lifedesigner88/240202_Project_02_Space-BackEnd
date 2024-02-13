package com.encore.space.domain.login.domain;

import com.encore.space.domain.member.domain.LoginType;

import java.util.Map;

public class GoogleMember implements MemberInfo {
    private final Map<String, Object> attributes;
    private String token;

    public GoogleMember(Map<String, Object> attributes) {
        this.attributes = attributes;

    }
    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public LoginType getProvider() {
        return LoginType.GOOGLE;
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getPicture() {
        return String.valueOf(attributes.get("picture"));
    }
}
