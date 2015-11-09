package com.ivanthescientist.projectmanager.application.command.handler;

import com.ivanthescientist.projectmanager.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeRecordCommandHandler {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TimeRecordRepository timeRecordRepository;

}
