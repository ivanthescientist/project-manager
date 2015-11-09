package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.application.command.AddMemberCommand;
import com.ivanthescientist.projectmanager.application.command.CreateOrganizationCommand;
import com.ivanthescientist.projectmanager.application.command.RemoveMemberCommand;
import com.ivanthescientist.projectmanager.application.command.UpdateOrganizationCommand;
import com.ivanthescientist.projectmanager.application.command.handler.OrganizationCommandHandler;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.security.SimpleAuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
public class OrganizationController {

    @Autowired
    OrganizationCommandHandler commandHandler;

    @Autowired
    OrganizationRepository repository;

    @PreAuthorize("hasRole('ROLE_SOLUTION_ADMIN')")
    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    public Organization createOrganization(@RequestBody CreateOrganizationCommand command,
                                           @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        command.ownerId = authenticationUser.getUser().getId();

        return commandHandler.createOrganization(command);
    }

    @RequestMapping(value = "/organizations/{id}")
    public Organization getOrganization(@PathVariable Long id,
                                        @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        Organization organization = repository.findOne(id);
        User currentUser = authenticationUser.getUser();

        if(!organization.isMember(currentUser) && !organization.isOwner(currentUser)) {
            throw new AccessDeniedException("Access Violation");
        }

        return organization;
    }

    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.GET)
    public List<User> getMembers(@PathVariable Long organizationId,
                                 @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        Organization organization = repository.findOne(organizationId);
        User currentUser = authenticationUser.getUser();

        if(!organization.isMember(currentUser) && !organization.isOwner(currentUser)) {
            throw new AccessDeniedException("Access Violation");
        }

        return organization.getMembers();
    }

    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.POST)
    public List<User> addMember(@RequestBody AddMemberCommand command,
                                @PathVariable Long organizationId,
                                @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        Organization organization = repository.findOne(organizationId);
        User currentUser = authenticationUser.getUser();

        if(!organization.isOwner(currentUser)) {
            throw new AccessDeniedException("Access Violation");
        }

        command.organizationId = organizationId;
        return commandHandler.addMember(command);
    }

    @RequestMapping(value = "/organizations/{organizationId}/members/{memberId}", method = RequestMethod.DELETE)
    public List<User> removeMember(@PathVariable Long organizationId,
                                   @PathVariable Long memberId,
                                   @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        Organization organization = repository.findOne(organizationId);
        User currentUser = authenticationUser.getUser();

        if(!organization.isOwner(currentUser)) {
            throw new AccessDeniedException("Access Violation");
        }

        RemoveMemberCommand command = new RemoveMemberCommand();
        command.organizationId = organizationId;
        command.userId = memberId;
        return commandHandler.removeMember(command);
    }

    @PreAuthorize("hasRole('ROLE_ORGANIZATION_ADMIN')")
    @RequestMapping(value = "/organizations/{organizationId}", method = RequestMethod.PUT)
    public Organization updateInfo(@PathVariable Long organizationId,
                                   @RequestBody UpdateOrganizationCommand command,
                                   @AuthenticationPrincipal SimpleAuthenticationUser authenticationUser)
    {
        User currentUser = authenticationUser.getUser();
        Organization organization = repository.findOne(organizationId);

        if(!organization.isOwner(currentUser)) {
            throw new AccessDeniedException("Access Violation");
        }

        command.organizationId = organizationId;

        return commandHandler.updateOrganization(command);
    }
}
