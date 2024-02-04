package com.encore.space.common.config;

import com.encore.space.common.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)   //  6.* 부터는 csrf() 이 사라진다.
                .cors(cors -> cors.configurationSource(CorsConfig.corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                // 접속 URL 설정
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                // 플어줄 것
                                .requestMatchers(
                                        "/",
                                        "/api/member/create",
                                        "/api/member/emailAuthentication",
                                        "/api/member/emailCheck",

                                        "/api/doLogin",
                                        "/api/login",
                                        "/login"
                                        )
                                .permitAll()

                                // 메니저 권한
                                .requestMatchers(
                                        "/swagger-resources/**",
                                        "/swagger-ui.html",
                                        "/v2/api-docs",
                                        "/webjars/**"
                                )
                                .hasAnyRole("MANAGER")

                                .anyRequest()
                                    .authenticated()

                )

                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

}
