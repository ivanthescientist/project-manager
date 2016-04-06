package com.ivanthescientist.projectmanager.application.command.handler;

import com.ivanthescientist.projectmanager.application.command.CommandHandler;
import com.ivanthescientist.projectmanager.application.command.RegisterOrganizationOwnerCommand;
import com.ivanthescientist.projectmanager.application.command.RegisterUserCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@CommandHandler
@Component
public class UserCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CommandHandler(RegisterUserCommand.class)
    public User registerUser(RegisterUserCommand command) {

        Organization organization = organizationRepository.findOne(command.organizationId);
        User user = new User(command.username, passwordEncoder.encode(command.password), "ROLE_USER");

        user = userRepository.save(user);
        organization.addMember(user);
        organizationRepository.saveAndFlush(organization);

        return  user;
    }

    @CommandHandler(RegisterOrganizationOwnerCommand.class)
    public User registerOrganizationOwner(RegisterOrganizationOwnerCommand command) {
        User user = new User(command.username, passwordEncoder.encode(command.password),
                "ROLE_USER", "ROLE_ORGANIZATION_ADMIN");

        user = userRepository.saveAndFlush(user);

        Organization organization = new Organization(user, user.getUsername() + "'s organization", "");

        organization = organizationRepository.saveAndFlush(organization);

        return organization.getOwner();
    }
}


