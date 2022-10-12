package com.kms.algotrade.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LoginFailHandler loginFailHandler;

    @Autowired
    TokenProvider tokenProvider;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/trade/**").permitAll()
//                .antMatchers("/upbit/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/upbit/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider))

                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("http://127.0.0.1:8080/login")
                .failureHandler(null);

//        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    //PasswordEncoder
    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoderPwd());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
