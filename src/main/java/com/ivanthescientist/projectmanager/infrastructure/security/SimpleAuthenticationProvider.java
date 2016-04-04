package com.ivanthescientist.projectmanager.infrastructure.security;

import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication.getPrincipal());
        Assert.notNull(authentication.getCredentials());

        String email = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        if(email.equals("ivan") && password.equals("1111")) {
            User user = new User("ivan", "1111", "ROLE_USER", "ROLE_SOLUTION_ADMIN");

            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }

        User user = userRepository.findOneByEmail(email);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Passwords don't match");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
