package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
    public Project getProject(@PathVariable long id)
    {
        return repository.findOne(id);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public Project createProject(@RequestBody Project project)
    {
        project = repository.saveAndFlush(project);
        return project;
    }

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.PUT)
    public Project updateProject(@RequestBody Project project)
    {
        project = repository.saveAndFlush(project);
        return project;
    }
}
