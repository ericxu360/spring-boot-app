package com.disgustingcat.springbootapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.okta.spring.boot.oauth.Okta;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        /* 
        http.authorizeHttpRequests((requests) -> 
            requests.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        */
        http.cors(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.disable());

      //  http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

      //  Okta.configureResourceServer401ResponseBody(http);

     //   return http.build();
        
        http.authorizeHttpRequests((req) -> req.anyRequest().permitAll());
        return http.build();
    } 
}
