package com.encore.space.common.service;

import com.encore.space.common.service.Oauth2.GoogleUserInfo;
import com.encore.space.common.service.Oauth2.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    String gitClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    String gitClientSecret;

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    @Autowired
    public Oauth2UserService(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    /*
    http://localhost:8080/login/oauth2/code/google
    http://localhost:8080/login/oauth2/code/github
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        UserInfo info = null;

        // 구글이라면
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            info = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(userRequest.getClientRegistration().getRegistrationId().equals("github")){
            String accessToken = null;
            try {
                accessToken = getAccessToken(userRequest.getAccessToken().getTokenValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        log.info("oAuth2User : " + oAuth2User );
        log.info("oAuth2User info : " + info );
        log.info("userRequest : " + userRequest.getAccessToken().getTokenValue());

        return oAuth2User;
    }


    public String getAccessToken(String code) throws IOException {

        String accessToken = code;
        String apiUrl = "https://api.github.com/user/emails";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("GitHub API Response: " + response.toString());
        } else {
            System.out.println("Failed to fetch emails. Response Code: " + responseCode);
        }

        return accessToken;
    }

    private String extractAccessToken(String accessTokenResponse) {
        // 실제로는 JSON 파싱을 사용하거나 정규식 등을 활용하여 엑세스 토큰을 추출해야 합니다.
        // 여기에서는 간단히 문자열에서 엑세스 토큰을 추출하는 예시를 제시합니다.
        return accessTokenResponse.split("&")[0].split("=")[1];
    }
}
