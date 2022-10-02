package com.chernikov.registerme.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                            try {
                                auth
                                        .antMatchers("/api/v*/registration/**").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .formLogin();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }


}
