package com.encore.space.domain.login.handler;

import com.encore.space.common.config.WebConfig;
import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.login.jwt.JwtProvider;
import com.encore.space.domain.login.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final WebConfig webConfig;

    @Autowired
    public LoginSuccessHandler(
            ObjectMapper objectMapper,
            JwtProvider jwtProvider,
            WebConfig webConfig
    ) {
        this.objectMapper = objectMapper;
        this.jwtProvider = jwtProvider;
        this.webConfig = webConfig;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        log.info(webConfig.ipCheck(request));
        objectMapper.writeValue(
                response.getWriter(),
                CommonResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("로그인 되었습니다.")
                        .result(
                                jwtProvider.exportToken(
                                        oauth2User.getAttribute("email"),
                                        Objects.requireNonNull(oauth2User.getAttribute("role")).toString(),
                                        webConfig.ipCheck(request)
                                )
                        )
                        .build()
        );
    }
}
