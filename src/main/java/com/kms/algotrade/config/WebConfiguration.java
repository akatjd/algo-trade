package com.kms.algotrade.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("*") // 이걸로하니 cors 해결됨
                .allowedOrigins("http://127.0.0.1:8080") // localhost url은 cors 위배
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS")
                .maxAge(3000);
    }
}
