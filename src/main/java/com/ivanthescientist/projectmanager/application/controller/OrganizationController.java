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

@RequestMapping("/api")
@RestController
public class OrganizationController {

    @Autowired
    CommandHandlingContext commandHandlingContext;

    @Autowired
    OrganizationRepository repository;

    @RequestMapping(value = "/organizations", method = RequestMethod.GET)
    public List<Organization> getOrganizations() {
        return repository.findAll();
    }

    @RequestMapping(value = "/organizations", method = RequestMethod.POST)
    public Organization createOrganization(@RequestBody CreateOrganizationCommand command) throws Exception {

        return (Organization) commandHandlingContext.handleCommand(command);
    }

    @RequestMapping(value = "/organizations/{id}")
    public Organization getOrganization(@PathVariable Long id)
    {
        Organization organization = repository.findOne(id);

        return organization;
    }

    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.GET)
    public List<User> getMembers(@PathVariable Long organizationId)
    {
        Organization organization = repository.findOne(organizationId);

        return organization.getMembers();
    }

    @RequestMapping(value = "/organizations/{organizationId}/members", method = RequestMethod.POST)
    public List<User> addMember(@RequestBody AddMemberCommand command,
                                @PathVariable Long organizationId) throws Exception {
        Organization organization = repository.findOne(organizationId);

        command.organizationId = organizationId;
        return (List<User>) commandHandlingContext.handleCommand(command);
    }

    @RequestMapping(value = "/organizations/{organizationId}/members/{memberId}", method = RequestMethod.DELETE)
    public List<User> removeMember(@PathVariable Long organizationId,
                                   @PathVariable Long memberId) throws Exception {
        RemoveMemberCommand command = new RemoveMemberCommand();
        command.organizationId = organizationId;
        command.userId = memberId;
        return (List<User>) commandHandlingContext.handleCommand(command);
    }

    @RequestMapping(value = "/organizations/{organizationId}", method = RequestMethod.PUT)
    public Organization updateInfo(@PathVariable Long organizationId,
                                   @RequestBody UpdateOrganizationCommand command) throws Exception {
        command.organizationId = organizationId;

        return (Organization) commandHandlingContext.handleCommand(command);
    }

    @ExceptionHandler(value = DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void domainExceptionHandler(DomainException exception)
    {
    }
}
