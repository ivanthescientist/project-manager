package com.ivanthescientist.projectmanager.application.command.handler;

import com.ivanthescientist.projectmanager.application.command.RegisterOrganizationOwnerCommand;
import com.ivanthescientist.projectmanager.application.command.RegisterUserCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserCommandHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerUser(RegisterUserCommand command) {

        Organization organization = organizationRepository.findOne(command.organizationId);
        User user = new User(command.email, passwordEncoder.encode(command.password), new String[] {"ROLE_USER"}, organization);

        user = userRepository.saveAndFlush(user);

        return  user;
    }

    public User registerOrganizationOwner(RegisterOrganizationOwnerCommand command) {
        User user = new User(command.email, passwordEncoder.encode(command.password),
                new String[] {"ROLE_USER", "ROLE_ORGANIZATION_ADMIN"}, null);

        user = userRepository.saveAndFlush(user);

        Organization organization = new Organization(user, user.getEmail() + "'s organization", "");

        organization = organizationRepository.saveAndFlush(organization);

        return organization.getOwner();
    }
}


