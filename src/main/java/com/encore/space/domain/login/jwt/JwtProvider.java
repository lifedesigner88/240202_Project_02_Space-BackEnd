package com.encore.space.domain.login.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.accessTokenSecretKey}")
    String accessTokenSecretKey;

    @Value("${jwt.refreshTokenSecretKey}")
    String refreshTokenSecretKey;

    @Value("${jwt.accessTokenTime}")
    int accessTokenTime;

    @Value("${jwt.refreshTokenTime}")
    int refreshTokenTime;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    private final RedisTemplate<String,String> redisTemplate;

    public JwtProvider(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createAccessToken(String email,String role){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role",role);
        Date now = new Date();

        Key key = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime * 60 * 1000L))
                .signWith( key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken (String email,String role, String clientIP){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role",role);
        claims.put("clientIP", clientIP);
        Date now = new Date();

        Key key = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime * 24 * 60 * 60 * 1000L))
                .signWith( key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims validateAccessToken(String token) {
        Key key = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder().
                setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims validateRefreshToken(String token) {
        Key key = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> exportToken(String email, String role, String clientIP){
        String accessToken = this.createAccessToken(
                email, role
        );

        String refreshToken = this.createRefreshToken(
                email, role, clientIP
        );
        log.info("refreshToken : "+refreshToken);
        redisTemplate.opsForValue().set(accessToken, refreshToken, refreshTokenTime, TimeUnit.DAYS);

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        return  map;
    }

    public Map<String, Object> reExportToken(String email, String role, String accessToken, String refreshToken){
        String newAccessToken = this.createAccessToken(
                email, role
        );
        Jedis jedis = new Jedis(redisHost, 6379);
        long ttl = jedis.ttl(accessToken);
        redisTemplate.delete(accessToken);
        redisTemplate.opsForValue().set(newAccessToken, refreshToken, ttl, TimeUnit.SECONDS);

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", newAccessToken);
        return  map;
    }
}
