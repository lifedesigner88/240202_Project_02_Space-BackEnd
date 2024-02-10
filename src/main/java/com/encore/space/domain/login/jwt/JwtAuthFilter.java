package com.encore.space.domain.login.jwt;

import com.encore.space.domain.login.domain.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.secretKey}")
    String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            Claims claims = validateToken(token);
            if (claims != null) {

                CustomUserDetails customUserDetails = CustomUserDetails.builder()
                        .userId( ((Integer) claims.get("userId")).longValue() )
                        .username(claims.getSubject())
                        .password("")
                        .build();

                customUserDetails.getAuthorities().add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
