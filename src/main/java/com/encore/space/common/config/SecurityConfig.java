package com.encore.space.common.config;

import com.encore.space.common.jwt.JwtAuthFilter;
import com.encore.space.common.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    private final LoginService loginService;

    @Autowired
    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            LoginService loginService
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.loginService = loginService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(CorsConfig.corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/" ).permitAll()
                                .requestMatchers(SwaggerUrl).permitAll()
                                .requestMatchers(MemberApiUrl).permitAll()
                                .requestMatchers(LoginApiUrl).permitAll()
                                .requestMatchers(ManagerApiUrl).hasAnyRole("MANAGER")
                                .anyRequest()
                                    .authenticated()

                )

                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(loginService)
                        )
                )
                .build();
    }

    private static final String[] SwaggerUrl = {
            "/api/vi/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    private static final String[] MemberApiUrl = {
            "/api/member/create",
            "/api/member/emailAuthentication",
            "/api/member/emailCheck",
    };

    private static final String[] LoginApiUrl = {
            "/oauth2/**",
            "/login",
    };

    private static final String[] ManagerApiUrl = {
            "/manager/**",
    };

}
