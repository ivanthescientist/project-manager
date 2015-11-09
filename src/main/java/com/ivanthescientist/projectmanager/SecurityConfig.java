package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.infrastructure.security.SimpleAuthenticationFilter;
import com.ivanthescientist.projectmanager.infrastructure.security.SimpleAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SimpleAuthenticationProvider authenticationProvider;
    @Autowired
    private SimpleAuthenticationFilter simpleAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(simpleAuthenticationFilter, BasicAuthenticationFilter.class);
        http.servletApi().and().headers().cacheControl().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authenticationProvider).userDetailsService(userDetailsService).passwordEncoder(this.getPasswordEncoder());
    }

    @Bean
    protected PasswordEncoder getPasswordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder)
    {
        builder.authenticationProvider(authenticationProvider);
    }
}
