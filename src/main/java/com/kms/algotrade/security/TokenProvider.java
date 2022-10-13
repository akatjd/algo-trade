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

    /**
     * 적절한 설정을 통해 토큰을 생성하여 반환
     * @param authentication
     * @return
     */
    public String generateAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
//        cal.add(Calendar.MINUTE, (int)expire_time);
        cal.add(Calendar.SECOND, expire_time);
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

    // jwt refresh 토큰 생성
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
        log.info("getAuthentication token :: {}", token);
        System.out.println(Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token).getSignature());
        String username = Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(token).getBody().getSubject();
        log.info("username :: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Authentication getrefreshTokenAuthentication(String refreshToken) {
        log.info("refreshToken :: {}", refreshToken);
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
            // MalformedJwtException | ExpiredJwtException | IllegalArgumentException
            log.info("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException{
        // refresh token을 디비의 그것과 비교해보기

//        Authentication authentication = getAuthentication(refreshToken);
        Authentication authentication = getrefreshTokenAuthentication(refreshToken);
        log.info("authentication.getName() :: {}", authentication.getName());
        Account account = accountRepository.findByAccountId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("accountId : " + authentication.getName() + " was not found"));

        if(account.getAccessToken().equals(refreshToken)){
            // 새로운거 생성
            String newRefreshToken = createRefreshToken(authentication);
            log.info("newRefreshToken :: {}", newRefreshToken);
            account.setAccessToken(newRefreshToken);
            return newRefreshToken;
        }else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }

    @Transactional
    public String issueRefreshToken(Authentication authentication){
        String newRefreshToken = createRefreshToken(authentication);

        // 기존것이 있다면 바꿔주고, 없다면 만들어줌
        accountRepository.findByAccountId(authentication.getName())
                .ifPresentOrElse(
                        r-> {r.setAccessToken(newRefreshToken);
                            log.info("issueRefreshToken method | change token ");
                        },
                        () -> {
                            log.info(" {} doesn't exist", authentication.getName());
                        });

        return newRefreshToken;
    }

    private String createRefreshToken(Authentication authentication){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
//        cal.add(Calendar.MINUTE, (int)expire_time);
        cal.add(Calendar.SECOND, expire_time);
        Date expiresIn = cal.getTime();

        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiresIn)
                .compact();
    }

    public enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
