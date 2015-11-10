package com.ivanthescientist.projectmanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanthescientist.projectmanager.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    public MockHttpServletRequestBuilder authenticateAsUser(MockHttpServletRequestBuilder builder, User user)
    {
        return builder
                .header("username", user.getEmail())
                .header("password", user.getPasswordHash());
    }

    public MockHttpServletRequestBuilder authenticateAsUser(MockHttpServletRequestBuilder builder,
                                                            String username,
                                                            String password)
    {
        return builder
                .header("username", username)
                .header("password", password);
    }
}
