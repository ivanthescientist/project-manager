package com.ivanthescientist.projectmanager.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class SimpleAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication.getPrincipal());
        Assert.notNull(authentication.getCredentials());

        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        UserDetails details = userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, details.getPassword())) {
            throw new BadCredentialsException("SimpleAuthenticationProvider.");
        }

        return new UsernamePasswordAuthenticationToken(details, password, details.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
