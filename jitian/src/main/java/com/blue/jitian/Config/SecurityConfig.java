package com.blue.jitian.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Security配置类
 * 暂时禁用所有安全认证，方便开发测试
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // 禁用CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 允许所有请求
            );
        
        return http.build();
    }
}
