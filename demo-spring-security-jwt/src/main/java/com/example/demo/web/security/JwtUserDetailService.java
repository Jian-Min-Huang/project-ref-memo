package com.example.demo.web.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (userName.equals("Vincent")) {
            return new User("Vincent", "$2a$10$knHNo0tuBOXzO9cb0V8Tv.oDfYY.4fySgYL.W082n8AShQMFNfCye", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            throw new UsernameNotFoundException(userName);
        }
    }
}
