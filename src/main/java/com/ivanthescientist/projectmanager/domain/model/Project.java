package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.DomainException;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column
    protected String name;

    @Column
    protected String description;

    @ManyToOne(targetEntity = Organization.class)
    protected Organization organization;

    @OneToMany(targetEntity = User.class)
    protected List<User> participants;

    @OneToMany(targetEntity = Task.class, mappedBy = "project")
    protected List<Task> tasks;

    public Project()
    {
    }

    public Project(String name, String description, Organization organization)
    {
        this.name = name;
        this.description = description;
        this.organization = organization;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    public List<User> getParticipants()
    {
        return participants;
    }

    public boolean isParticipant(User user)
    {
        return this.participants.contains(user);
    }

    public void addParticipant(User user)
    {
        if(this.participants.contains(user)) {
            throw new DomainException("Already participant");
        }

        participants.add(user);
    }

    public void removeParticipant(User user)
    {
        if(this.participants.contains(user)) {
            throw new DomainException("Not a participant");
        }

        participants.remove(user);
    }

    public Organization getOrganization() {
        return organization;
    }
}
