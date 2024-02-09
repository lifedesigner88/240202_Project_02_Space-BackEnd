package com.encore.space.common.jwt;

import com.encore.space.common.response.CommonResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    String secretKey;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
            if(bearerToken != null){
                if(!bearerToken.startsWith("Bearer ")){
                    throw new AuthenticationServiceException("토큰 형식이 맞지 않습니다.");
                }
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                String token = bearerToken.substring(7);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(CommonResponse.responseMassage(HttpStatus.UNAUTHORIZED, e.getMessage()).toString());
        }
    }
}
