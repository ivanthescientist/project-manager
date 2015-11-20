package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.DomainException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @Column
    @GeneratedValue
    protected long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    protected User owner;

    @Column
    protected String name;

    @Column
    protected String description;

    @OneToMany(targetEntity = Project.class, mappedBy="organization")
    protected List<Project> projects = new ArrayList<>();

    @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    protected List<User> members = new ArrayList<>();

    public Organization() {}

    public Organization(User owner, String name, String description)
    {
        this.owner = owner;
        this.name = name;
        this.description = description;
    }

    public Long getId()
    {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getMembers()
    {
        return members;
    }

    public boolean isMember(User user)
    {
        return this.members.contains(user);
    }

    public boolean isOwner(User user)
    {
        // Too lazy to implement equals
        return this.owner.getId() == user.getId();
    }

    public void addMember(User user)
    {
        if(isMember(user)) {
            throw new DomainException("Already a member");
        }

        members.add(user);
    }

    public void removeMember(User user)
    {
        if(!isMember(user)) {
            throw  new DomainException("Not a member");
        }

        if(!projects.stream()
                .filter(project -> project.isParticipant(user))
                .collect(Collectors.toList())
                .isEmpty()) {
            throw new DomainException("Member has ongoing projects");
        }

        this.members.remove(user);
    }

    public void updateInfo(String name, String description)
    {
        this.name = name;
        this.description = description;
    }
}
