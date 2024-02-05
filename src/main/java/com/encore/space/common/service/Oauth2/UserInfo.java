package com.encore.space.common.service.Oauth2;

public interface UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
