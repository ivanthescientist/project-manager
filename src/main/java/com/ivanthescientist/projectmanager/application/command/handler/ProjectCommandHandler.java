package com.ivanthescientist.projectmanager.application.command.handler;


import com.ivanthescientist.projectmanager.application.command.*;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@CommandHandler
@Component
public class ProjectCommandHandler {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @CommandHandler(CreateProjectCommand.class)
    public Project createProject(CreateProjectCommand command)
    {
        Organization organization = organizationRepository.findOne(command.organizationId);
        Project project = new Project(command.name, command.description, organization);

        project = projectRepository.saveAndFlush(project);

        return project;
    }

    @CommandHandler(AddProjectParticipantCommand.class)
    public List<User> addParticipant(AddProjectParticipantCommand command)
    {
        Project project = projectRepository.findOne(command.projectId);
        User participant = userRepository.findOne(command.userId);

        project.addParticipant(participant);

        project = projectRepository.saveAndFlush(project);

        return project.getParticipants();
    }

    @CommandHandler(RemoveProjectParticipantCommand.class)
    public List<User> removeParticipant(RemoveProjectParticipantCommand command)
    {
        Project project = projectRepository.findOne(command.projectId);
        User participant = userRepository.findOne(command.userId);

        project.removeParticipant(participant);

        project = projectRepository.saveAndFlush(project);

        return project.getParticipants();
    }

    @CommandHandler(UpdateProjectCommand.class)
    public Project updateProjectInfo(UpdateProjectCommand command)
    {
        Project project = projectRepository.findOne(command.projectId);
        project.updateInfo(command.name, command.description);

        project = projectRepository.saveAndFlush(project);

        return project;
    }
}
