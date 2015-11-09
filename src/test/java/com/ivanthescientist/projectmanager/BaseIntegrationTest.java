package com.ivanthescientist.projectmanager;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

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
}
