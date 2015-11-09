package com.ivanthescientist.projectmanager.application.command.handler;


import com.ivanthescientist.projectmanager.application.command.AddProjectParticipantCommand;
import com.ivanthescientist.projectmanager.application.command.CreateProjectCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectCommandHandler {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    public Project createProject(CreateProjectCommand command)
    {
        Organization organization = organizationRepository.findOne(command.organizationId);
        Project project = new Project(command.name, command.description, organization);

        project = projectRepository.saveAndFlush(project);

        return project;
    }

    public List<User> addParticipant(AddProjectParticipantCommand command)
    {
        Project project = projectRepository.findOne(command.projectId);
        User participant = userRepository.findOne(command.userId);

        project.addParticipant(participant);

        project = projectRepository.saveAndFlush(project);

        return project.getParticipants();
    }

    public List<User> removeParticipant(AddProjectParticipantCommand command)
    {
        Project project = projectRepository.findOne(command.projectId);
        User participant = userRepository.findOne(command.userId);

        project.removeParticipant(participant);

        project = projectRepository.saveAndFlush(project);

        return project.getParticipants();
    }
}
