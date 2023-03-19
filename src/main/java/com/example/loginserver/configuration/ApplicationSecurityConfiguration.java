package com.example.loginserver.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration implements WebSecurityConfigurer<WebSecurity> {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void init(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers("/h2-console/**"); // allow access to h2-console
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers("/user")
                .requestMatchers(HttpMethod.POST, "/user")
                .requestMatchers(HttpMethod.PATCH, "/user/update")
                .requestMatchers(HttpMethod.DELETE, "/user")
                .requestMatchers(HttpMethod.POST, "/login")
                .requestMatchers(HttpMethod.POST, "/login/forgot")
                .anyRequest();
    }

}





