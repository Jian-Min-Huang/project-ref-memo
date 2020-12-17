package com.example.demo.config;

import com.example.demo.web.security.JwtRequestFilter;
import com.example.demo.web.security.JwtUserDetailService;
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

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUserDetailService jwtUserDetailService;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, JwtUserDetailService jwtUserDetailService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUserDetailService = jwtUserDetailService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers("/demo").access("hasAuthority('ROLE_USER')")
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
