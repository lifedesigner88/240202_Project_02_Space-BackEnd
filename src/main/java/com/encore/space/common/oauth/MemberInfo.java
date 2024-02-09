package com.encore.space.common.oauth;

import com.encore.space.domain.member.domain.LoginType;

public interface MemberInfo {
    String getProviderId();
    LoginType getProvider();
    String getEmail();
    String getName();
    String getPicture();
}
