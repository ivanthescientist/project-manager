package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.SecuredEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Inheritance
public class User implements UserDetails, SecuredEntity {
    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String username;

    @Column
    private String passwordHash;

    @Column
    private HashSet<String> roles = new HashSet<>(0);

    public User() {}
    public User(String username, String passwordHash, String... roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles.addAll(Arrays.asList(roles));
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this) {
            return  true;
        }

        if(o == null || !(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        return this.getId() == user.getId();
    }

    @Override
    public int hashCode()
    {
        return (int) this.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
