package com.ivanthescientist.projectmanager.infrastructure.security;


import com.ivanthescientist.projectmanager.domain.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleAuthenticationUser extends org.springframework.security.core.userdetails.User{

    private User user;

    public SimpleAuthenticationUser(User user)
    {
        super(user.getEmail(), user.getPasswordHash(), SimpleAuthenticationUser.userRoleList(user));
        this.user = user;
    }

    public static List<SimpleGrantedAuthority> userRoleList(User user)
    {
        return user.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public User getUser()
    {
        return user;
    }
}
