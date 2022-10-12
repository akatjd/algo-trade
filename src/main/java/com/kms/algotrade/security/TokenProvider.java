package com.kms.algotrade.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

// 토큰 생성, 검증
@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.access-token-validity-in-minutes}")
    private long expire_time;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 적절한 설정을 통해 토큰을 생성하여 반환
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, (int)expire_time);
        Date expiresIn = cal.getTime();

        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        String username = Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     * @param req
     * @return
     */
    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token != null) {
            return token;
        }
        return null;
    }

    /**
     * 토큰을 검증
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // MalformedJwtException | ExpiredJwtException | IllegalArgumentException
            e.printStackTrace();
        }
        return false;
    }
}
