package com.encore.space.common.handler;

import com.encore.space.common.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    CustomAuthenticationEntryPointHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                response.getWriter(),
                CommonResponse.builder()
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .message("인증에 실패하였습니다")
                        .build()
        );
    }
}
