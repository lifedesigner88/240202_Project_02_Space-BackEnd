package com.encore.space.common.config;

import com.encore.space.common.handler.CustomAccessDeniedHandler;
import com.encore.space.common.handler.CustomAuthenticationEntryPointHandler;
import com.encore.space.domain.login.jwt.JwtAuthFilter;
import com.encore.space.domain.login.handler.LoginFailureHandler;
import com.encore.space.domain.login.handler.LoginSuccessHandler;
import com.encore.space.domain.login.service.LoginService;
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
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;



    @Autowired
    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            LoginService loginService,
            LoginSuccessHandler loginSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.loginService = loginService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.customAuthenticationEntryPointHandler = customAuthenticationEntryPointHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
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
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/" ).permitAll()
                                .requestMatchers(SwaggerUrl).permitAll()
                                .requestMatchers(MemberApiUrl).permitAll()
                                .requestMatchers(LoginApiUrl).permitAll()
                                .requestMatchers(PostApiUrl).permitAll()
                                .requestMatchers(FileResource).permitAll()
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
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )

                .exceptionHandling( (exceptionHandling) -> {
                    exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPointHandler);
                    exceptionHandling.accessDeniedHandler(customAccessDeniedHandler);
                })

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
            "/api/member/login",
            "/api/member/create",
            "/api/member/emailAuthentication",
            "/api/member/emailCheck",
            "/ws/**"
    };

    private static final String[] LoginApiUrl = {

            "/oauth2/**",
            "/login",
    };

    private static final String[] PostApiUrl = {
            "/api/post/list",
    };

    private static final String[] FileResource = {
            "/static/**",
    };

    private static final String[] ManagerApiUrl = {
            "/manager/**",
    };


}
