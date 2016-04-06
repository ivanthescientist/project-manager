package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.application.command.*;
import com.ivanthescientist.projectmanager.domain.DomainException;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.security.DomainSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasRole('ROLE_USER')")
@RestController
public class OrganizationController {

    @Autowired
    CommandHandlingContext commandHandlingContext;

    @Autowired
    OrganizationRepository repository;

    @DomainSecurity
    @PreAuthorize("hasRole('ROLE_SOLUTION_ADMIN')")
    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public List<Organization> getOrganizations() {
        return repository.findAll();
    }

    @DomainSecurity
    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    public Organization createOrganization(@RequestBody CreateOrganizationCommand command,
                                           @AuthenticationPrincipal User user) throws Exception {
        command.ownerId = user.getId();

        return (Organization) commandHandlingContext.handleCommand(command);
    }

    @DomainSecurity
    @RequestMapping(value = "/organizations/{id}")
    public Organization getOrganization(@PathVariable Long id,
                                        @AuthenticationPrincipal User user)
    {
        Organization organization = repository.findOne(id);

        if(!organization.isMember(user) && !organization.isOwner(user)) {
            throw new AccessDeniedException("Access Violation");
        }

        return organization;
    }

    @DomainSecurity
    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.GET)
    public List<User> getMembers(@PathVariable Long organizationId,
                                 @AuthenticationPrincipal User user)
    {
        Organization organization = repository.findOne(organizationId);

        if(!organization.isMember(user) && !organization.isOwner(user)) {
            throw new AccessDeniedException("Access Violation");
        }

        return organization.getMembers();
    }

    @DomainSecurity
    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.POST)
    public List<User> addMember(@RequestBody AddMemberCommand command,
                                @PathVariable Long organizationId,
                                @AuthenticationPrincipal User user) throws Exception {
        Organization organization = repository.findOne(organizationId);

        if(!organization.isOwner(user)) {
            throw new AccessDeniedException("Access Violation");
        }

        command.organizationId = organizationId;
        return (List<User>) commandHandlingContext.handleCommand(command);
    }

    @DomainSecurity
    @RequestMapping(value = "/organizations/{organizationId}/members/{memberId}", method = RequestMethod.DELETE)
    public List<User> removeMember(@PathVariable Long organizationId,
                                   @PathVariable Long memberId,
                                   @AuthenticationPrincipal User user) throws Exception {
        Organization organization = repository.findOne(organizationId);

        if(!organization.isOwner(user)) {
            throw new AccessDeniedException("Access Violation");
        }

        RemoveMemberCommand command = new RemoveMemberCommand();
        command.organizationId = organizationId;
        command.userId = memberId;
        return (List<User>) commandHandlingContext.handleCommand(command);
    }

    @DomainSecurity
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_ADMIN')")
    @RequestMapping(value = "/organizations/{organizationId}", method = RequestMethod.PUT)
    public Organization updateInfo(@PathVariable Long organizationId,
                                   @RequestBody UpdateOrganizationCommand command,
                                   @AuthenticationPrincipal User user) throws Exception {

        Organization organization = repository.findOne(organizationId);

        if(!organization.isOwner(user)) {
            throw new AccessDeniedException("Access Violation");
        }

        command.organizationId = organizationId;

        return (Organization) commandHandlingContext.handleCommand(command);
    }

    @ExceptionHandler(value = DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void domainExceptionHandler(DomainException exception)
    {
    }
}
