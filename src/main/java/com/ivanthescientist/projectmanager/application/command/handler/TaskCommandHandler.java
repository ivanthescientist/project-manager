package com.ivanthescientist.projectmanager.application.command.handler;

import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.TaskRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskCommandHandler {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

}
