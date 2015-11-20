package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.application.command.AddProjectParticipantCommand;
import com.ivanthescientist.projectmanager.application.command.CreateProjectCommand;
import com.ivanthescientist.projectmanager.application.command.RemoveProjectParticipantCommand;
import com.ivanthescientist.projectmanager.application.command.UpdateProjectCommand;
import com.ivanthescientist.projectmanager.application.command.handler.ProjectCommandHandler;
import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    @Autowired
    ProjectCommandHandler commandHandler;

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
    public Project getProject(@PathVariable long id)
    {
        return repository.findOne(id);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public Project createProject(@RequestBody CreateProjectCommand command)
    {
        return commandHandler.createProject(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{id}", method = RequestMethod.PUT)
    public Project updateProject(@RequestBody UpdateProjectCommand command)
    {
        return commandHandler.updateProjectInfo(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}/participants", method = RequestMethod.POST)
    public List<User> addParticipant(@RequestBody AddProjectParticipantCommand command, @PathVariable Long projectId)
    {
        command.projectId = projectId;
        return commandHandler.addParticipant(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}/participants/{participantId}", method = RequestMethod.DELETE)
    public List<User> removeParticipant(@PathVariable Long projectId, @PathVariable Long participantId)
    {
        RemoveProjectParticipantCommand command = new RemoveProjectParticipantCommand();
        command.projectId = projectId;
        command.userId = participantId;

        return commandHandler.removeParticipant(command);
    }
}
