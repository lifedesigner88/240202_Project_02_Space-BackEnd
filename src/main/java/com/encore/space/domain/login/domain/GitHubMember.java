package com.encore.space.domain.login.domain;

import com.encore.space.domain.member.domain.LoginType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InternalException;
import lombok.Getter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class GitHubMember implements MemberInfo {
    private final Map<String, Object> attributes;
    private final String token;

    public GitHubMember(Map<String, Object> attributes, String token) {
        this.attributes = attributes;
        this.token = token;
    }
    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }
    @Override
    public LoginType getProvider() {
        return LoginType.GITHUB;
    }

    @Override
    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        String apiUrl = "https://api.github.com/user/emails";
        int responseCode ;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + this.token);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                List<GitHubEmail> emails = objectMapper.readValue(
                        connection.getInputStream(),
                        new TypeReference<>() {}
                );
                return emails.get(0).getEmail();

            } else {
                throw new InternalException("GitHub 에서 이메일을 받아오지 못했습니다.");
            }
        }  catch (IOException e) {
            throw new InternalException("GitHub 에서 이메일을 받아오지 못했습니다.");
        }
    }
    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getPicture() {
        return String.valueOf(attributes.get("avatar_url"));
    }

    @Getter
    private static class GitHubEmail {
        private String email;
        private boolean primary;
        private boolean verified;
        private String visibility;
    }

}
