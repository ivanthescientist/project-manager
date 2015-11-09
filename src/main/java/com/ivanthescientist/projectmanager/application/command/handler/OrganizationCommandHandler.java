package com.ivanthescientist.projectmanager.application.command.handler;

import com.ivanthescientist.projectmanager.application.command.AddMemberCommand;
import com.ivanthescientist.projectmanager.application.command.CreateOrganizationCommand;
import com.ivanthescientist.projectmanager.application.command.RemoveMemberCommand;
import com.ivanthescientist.projectmanager.application.command.UpdateOrganizationCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.TaskRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganizationCommandHandler {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    public Organization createOrganization(CreateOrganizationCommand command)
    {
        User owner = userRepository.findOne(command.ownerId);

        Organization organization = new Organization(owner, command.name, command.description);

        organization = organizationRepository.saveAndFlush(organization);

        return organization;
    }

    public List<User> addMember(AddMemberCommand command)
    {
        User user = userRepository.findOne(command.userId);
        Organization organization = organizationRepository.findOne(command.organizationId);

        organization.addMember(user);

        organization = organizationRepository.saveAndFlush(organization);

        return organization.getMembers();
    }

    public List<User> removeMember(RemoveMemberCommand command) {
        User user = userRepository.findOne(command.userId);
        Organization organization = organizationRepository.findOne(command.organizationId);

        organization.removeMember(user);

        organization = organizationRepository.saveAndFlush(organization);

        return organization.getMembers();
    }

    public Organization updateOrganization(UpdateOrganizationCommand command)
    {
        Organization organization = organizationRepository.findOne(command.organizationId);
        organization.updateInfo(command.name, command.description);

        organization = organizationRepository.saveAndFlush(organization);

        return organization;
    }
}