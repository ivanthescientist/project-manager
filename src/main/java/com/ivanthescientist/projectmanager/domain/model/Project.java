package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.DomainException;
import com.ivanthescientist.projectmanager.domain.SecuredEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
public class Project implements SecuredEntity {

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

    @OneToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    protected List<User> participants = new ArrayList<>();

    @OneToMany(targetEntity = Task.class, mappedBy = "project")
    protected List<Task> tasks = new ArrayList<>();

    public Project()
    {
    }

    public Project(String name, String description, Organization organization)
    {
        this.name = name;
        this.description = description;
        this.organization = organization;
    }

    public void updateInfo(String name, String description)
    {
        this.name = name;
        this.description = description;
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

        if(!organization.isMember(user)) {
            throw new DomainException("User does not belong to project's organization");
        }

        participants.add(user);
    }

    public void removeParticipant(User user)
    {
        if(!this.participants.contains(user)) {
            throw new DomainException("Not a participant");
        }

        boolean hasTasks = !this.tasks.stream()
                .filter(task -> task.getAssignee().equals(user) && task.isInProgress())
                .collect(Collectors.toList())
                .isEmpty();

        if(hasTasks) {
            throw new DomainException("User has ongoing tasks");
        }

        participants.remove(user);
    }

    public Organization getOrganization() {
        return organization;
    }
}
