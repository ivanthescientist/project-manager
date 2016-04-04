package com.ivanthescientist.projectmanager.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SimpleAuthenticationFilter extends GenericFilterBean {

    private AuthenticationManager manager;

    @Autowired
    public SimpleAuthenticationFilter(AuthenticationManager manager)
    {
        this.manager = manager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null || password == null) {
            username = request.getHeader("username");
            password = request.getHeader("password");
        }

        try {
            Assert.notNull(username);
            Assert.notNull(password);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            token.setDetails(new WebAuthenticationDetails(request));

            Authentication authentication = manager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ignored) {}


        filterChain.doFilter(servletRequest, servletResponse);
    }
}
