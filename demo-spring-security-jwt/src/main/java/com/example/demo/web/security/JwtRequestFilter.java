package com.example.demo.web.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final String AUTH_HEADER = "Authorization";
    private final String AUTH_HEADER_START_WITH = "Bearer ";
    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailService jwtUserDetailService;

    public JwtRequestFilter(JwtTokenService jwtTokenService, JwtUserDetailService jwtUserDetailService) {
        this.jwtTokenService = jwtTokenService;
        this.jwtUserDetailService = jwtUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String tokenInHeader = httpServletRequest.getHeader(AUTH_HEADER);

        if (null != tokenInHeader && tokenInHeader.startsWith(AUTH_HEADER_START_WITH)) {
            final String token = tokenInHeader.substring(7);
            final String account = jwtTokenService.retrieveSubject(token);

            if (null != account && null == SecurityContextHolder.getContext().getAuthentication()) {
                final UserDetails userDetails = jwtUserDetailService.loadUserByUsername(account);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
