package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.application.command.*;
import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    @Autowired
    CommandHandlingContext commandHandlingContext;

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public List<Project> getProjectList() {
        return repository.findAll();
    }

    @RequestMapping(value = "/projects/{projectId}", method = RequestMethod.GET)
    public Project getProject(@PathVariable long projectId)
    {
        Project project = repository.findOne(projectId);

        return project;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public Project createProject(@RequestBody CreateProjectCommand command) throws Exception
    {
        return (Project) commandHandlingContext.handleCommand(command);
    }

    @RequestMapping(value = "/projects/{projectId}", method = RequestMethod.PUT)
    public Project updateProject(@RequestBody UpdateProjectCommand command,
                                 @PathVariable Long projectId) throws Exception
    {
        command.projectId = projectId;

        return (Project) commandHandlingContext.handleCommand(command);
    }

    @RequestMapping(value = "/projects/{projectId}/participants", method = RequestMethod.POST)
    public List<User> addParticipant(@RequestBody AddProjectParticipantCommand command,
                                     @PathVariable Long projectId) throws Exception
    {
        command.projectId = projectId;
        return (List<User>) commandHandlingContext.handleCommand(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}/participants/{participantId}", method = RequestMethod.DELETE)
    public List<User> removeParticipant(@PathVariable Long projectId,
                                        @PathVariable Long participantId) throws Exception
    {
        RemoveProjectParticipantCommand command = new RemoveProjectParticipantCommand();
        command.projectId = projectId;
        command.userId = participantId;

        return (List<User>) commandHandlingContext.handleCommand(command);
    }
}
