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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    @Autowired
    ProjectCommandHandler commandHandler;

    @RequestMapping(value = "/projects/{projectId}", method = RequestMethod.GET)
    public Project getProject(@PathVariable long projectId,
                              @AuthenticationPrincipal User user)
    {
        Project project = repository.findOne(projectId);

        if(!project.getOrganization().isOwner(user) && !project.isParticipant(user)) {
            throw new AccessDeniedException("User doesn't own project");
        }

        return project;
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public Project createProject(@RequestBody CreateProjectCommand command)
    {
        return commandHandler.createProject(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}", method = RequestMethod.PUT)
    public Project updateProject(@RequestBody UpdateProjectCommand command,
                                 @PathVariable Long projectId,
                                 @AuthenticationPrincipal User user)
    {
        Project project = repository.findOne(projectId);
        command.projectId = projectId;

        if(!project.getOrganization().isOwner(user)) {
            throw new AccessDeniedException("User doesn't own project");
        }

        return commandHandler.updateProjectInfo(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}/participants", method = RequestMethod.POST)
    public List<User> addParticipant(@RequestBody AddProjectParticipantCommand command,
                                     @PathVariable Long projectId,
                                     @AuthenticationPrincipal User user)
    {
        Project project = repository.findOne(command.projectId);

        if(!project.getOrganization().isOwner(user)) {
            throw new AccessDeniedException("User doesn't own project");
        }

        command.projectId = projectId;
        return commandHandler.addParticipant(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_MANAGER')")
    @RequestMapping(value = "/projects/{projectId}/participants/{participantId}", method = RequestMethod.DELETE)
    public List<User> removeParticipant(@PathVariable Long projectId,
                                        @PathVariable Long participantId,
                                        @AuthenticationPrincipal User user)
    {
        Project project = repository.findOne(projectId);

        if(!project.getOrganization().isOwner(user)) {
            throw new AccessDeniedException("User doesn't own project");
        }

        RemoveProjectParticipantCommand command = new RemoveProjectParticipantCommand();
        command.projectId = projectId;
        command.userId = participantId;

        return commandHandler.removeParticipant(command);
    }
}
