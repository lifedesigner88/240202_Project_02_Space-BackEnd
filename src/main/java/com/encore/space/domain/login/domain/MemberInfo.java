package com.encore.space.domain.login.domain;

import com.encore.space.domain.member.domain.LoginType;

public interface MemberInfo {
    String getProviderId();
    LoginType getProvider();
    String getEmail();
    String getName();
    String getPicture();
}
