package com.ivanthescientist.projectmanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanthescientist.projectmanager.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.Filter;

public class BaseIntegrationTest {
    @Autowired
    ObjectMapper objectMapper;

    public String toJson(Object object) throws Exception
    {
        return objectMapper.writeValueAsString(object);
    }

    public Object fromJson(String json, Class type) throws Exception
    {
        return objectMapper.readValue(json, type);
    }

    public void authenticatedUser(User user) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user,
                        user.getPassword(),
                        user.getAuthorities()));
    }
}
