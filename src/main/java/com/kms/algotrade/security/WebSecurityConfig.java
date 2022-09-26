package com.kms.algotrade.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    public WebSecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/"); // default 로 이동할 url
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/script/**", "image/**", "/fonts/**", "lib/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
            Exception {
        http.authorizeRequests()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/**").permitAll()
//                .antMatchers("/board/**").authenticated() // 로그인시에만 접속 가능한 경로
                .antMatchers("/signup").permitAll()			// 회원가입
                .antMatchers("/signin/**").permitAll() 		// 로그인
                .antMatchers("/exception/**").permitAll() 	// 예외처리 포인트
                .anyRequest().hasRole("USER")				// 이외 나머지는 USER 권한필요
                .and()
                .formLogin()
                .usernameParameter("accountId") //화면단에서 받는 이메일과 패스워드 설정
                .passwordParameter("password")
                .loginPage("/account/login")
                .loginProcessingUrl("/account/login")
                .defaultSuccessUrl("/")
                .failureUrl("/account/login2")
                .and()
                .logout();

//        .antMatchers("/signup").permitAll()			// 회원가입
//        .antMatchers("/signin/**").permitAll() 		// 로그인
//        .antMatchers("/exception/**").permitAll() 	// 예외처리 포인트
//        .anyRequest().hasRole("USER")				// 이외 나머지는 USER 권한필요

        return http.build();
    }
}