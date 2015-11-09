package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.DomainException;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;

@Entity
@Table(name = "users")
@Inheritance
public class User {
    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String email;

    @Column
    private String passwordHash;

    @Column
    private HashSet<String> roles = new HashSet<>(0);

    @ManyToOne(targetEntity = Organization.class)
    protected Organization organization;

    public User() {}
    public User(String email, String passwordHash, String[] roles, Organization organization) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles.addAll(Arrays.asList(roles));
        this.organization = organization;
    }

    public void assignToOrganization(Organization organization) {
        if(this.organization != null) {
            throw new DomainException("User already assigned to organization");
        }

        this.organization = organization;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public HashSet<String> getRoles() {
        return roles;
    }
}
