package com.chordncode.springfileserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
        .csrf(csrf -> csrf
            .disable()
        )
        .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/file")
            .usernameParameter("userId")
            .passwordParameter("userPw")
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
        )
        .sessionManagement(session -> session
            .maximumSessions(1)
        )
        .authorizeHttpRequests(authorize -> authorize
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated()
        )
        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
