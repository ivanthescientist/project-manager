package com.ivanthescientist.projectmanager.application.controller;

import com.ivanthescientist.projectmanager.application.command.CommandHandlingContext;
import com.ivanthescientist.projectmanager.application.command.RegisterOrganizationOwnerCommand;
import com.ivanthescientist.projectmanager.application.command.RegisterUserCommand;
import com.ivanthescientist.projectmanager.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private CommandHandlingContext commandHandlingContext;

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public User register(@RequestBody RegisterUserCommand command) throws Exception
    {
        return (User) commandHandlingContext.handleCommand(command);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/users/me", method = RequestMethod.GET)
    public User currentUser(@AuthenticationPrincipal User user)
    {
        return user;
    }

    @RequestMapping(path = "/users/organization_owners", method = RequestMethod.POST)
    public User registerOrganizationOwner(@RequestBody RegisterOrganizationOwnerCommand command) throws Exception
    {
        return (User) commandHandlingContext.handleCommand(command);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void databaseConflictHandler()
    {
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void notFoundExceptionHandler()
    {
    }
}
