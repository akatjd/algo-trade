package com.kms.algotrade.security;

import com.kms.algotrade.entity.Account;
import com.kms.algotrade.repository.AccountRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

// 토큰 생성, 검증
@Component
@Slf4j
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secret_key;

    @Value("${jwt.access-token-validity-in-minutes}")
    private int expire_time;

    @Value("${jwt.refresh-token-validity-in-hours}")
    private int expire_hours;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AccountRepository accountRepository;

    // Access token 생성
    public String generateAccessToken(Authentication authentication) {
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

    // Refresh token 생성
    public String generateRefreshToken() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, expire_hours);
        Date expiresIn = cal.getTime();

        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        
        // refresh 토큰은 만료시간 외에 별다른 정보가 없음
        return Jwts.builder()
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
        System.out.println(Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token).getSignature());
        String username = Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Authentication getRefreshTokenAuthentication(String refreshToken) {
        Optional<Account> account = accountRepository.findByRefreshToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(account.get().getAccountId());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     * @param req
     * @return
     */
    public String resolveToken(HttpServletRequest req, String header) {
        String token = req.getHeader(header);
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
    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
//            e.printStackTrace();
            return JwtCode.EXPIRED;
        } catch (JwtException e) {
            log.info("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException{
        // refresh token을 DB와 매칭
        Authentication authentication = getRefreshTokenAuthentication(refreshToken);
        Account account = accountRepository.findByAccountId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("accountId : " + authentication.getName() + " was not found"));

        if(account.getRefreshToken().equals(refreshToken)){
            // 새로운거 access token 생성
            String newAccessToken = generateAccessToken(authentication);
            account.setAccessToken(newAccessToken);
            return newAccessToken;
        }else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }

    public enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
