package com.encore.space.common.service.Oauth2;

import java.util.Map;

public class GoogleUserInfo implements UserInfo {
    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getProviderId() {
        return String.valueOf(attributes.get("sub"));
    }

    public String getProvider() {
        return "google";
    }

    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }
}
