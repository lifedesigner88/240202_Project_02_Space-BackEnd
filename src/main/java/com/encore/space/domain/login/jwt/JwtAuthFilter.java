package com.encore.space.domain.login.jwt;

import com.encore.space.common.config.WebConfig;
import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.login.domain.CustomUserDetails;
import com.encore.space.domain.login.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final WebConfig webConfig;

    @Autowired
    public JwtAuthFilter(
            ObjectMapper objectMapper,
            JwtProvider jwtProvider,
            RedisTemplate<String, String> redisTemplate,
            WebConfig webConfig
    ) {
        this.objectMapper = objectMapper;
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
        this.webConfig = webConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtProvider.extractAccessToken(request);
            if (token != null) {
                Claims claims = jwtProvider.validateAccessToken(token);
                if (claims != null) {
                    CustomUserDetails customUserDetails = CustomUserDetails.builder()
                            .role(claims.get("role").toString())
                            .username(claims.getSubject())
                            .password("")
                            .build();

                    customUserDetails.getAuthorities().add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e ){
            String errorMessage = "";
            Object errorException = "";
            if(e instanceof MalformedJwtException){
                errorMessage = "올바르지 않은 토큰입니다.";
                errorException = e.getMessage();
            }
            if(e instanceof ExpiredJwtException){
                errorMessage = "다시 로그인해 주세요.";
                String token =  jwtProvider.extractAccessToken(request);
                String refreshToken = redisTemplate.opsForValue().get(token);
                if(refreshToken != null){
                    Claims claims = jwtProvider.validateRefreshToken(refreshToken);
                    if(claims.get("clientIP").toString().equals(webConfig.ipCheck(request))){
                        errorMessage = "토큰을 재발행 합니다.";
                        errorException = jwtProvider.reExportToken(
                                claims.getSubject(),
                                claims.get("role").toString(),
                                token,
                                refreshToken
                        );
                    }
                }else{
                    errorException = "로그인 만료";
                }

            }
            log.error(e.getClass().getName() + " : " + errorMessage);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(
                    response.getWriter(),
                    CommonResponse.builder()
                            .httpStatus(HttpStatus.UNAUTHORIZED)
                            .message(errorMessage)
                            .result(errorException)
                            .build()
            );
        }
    }
}
