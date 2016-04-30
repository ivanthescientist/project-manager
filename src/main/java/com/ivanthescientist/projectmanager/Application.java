package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.application.command.CommandHandlingContext;
import com.ivanthescientist.projectmanager.application.command.RegisterOrganizationOwnerCommand;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@SpringBootApplication
@ComponentScan("com.ivanthescientist.projectmanager")
public class Application  {
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        UserRepository userRepository = context.getBean(UserRepository.class);
        CommandHandlingContext commandHandlingContext = context.getBean(CommandHandlingContext.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        User user = new User("ivan", passwordEncoder.encode("1111"), "ROLE_SOLUTION_ADMIN", "ROLE_USER");
        userRepository.saveAndFlush(user);

        RegisterOrganizationOwnerCommand command = new RegisterOrganizationOwnerCommand();
        command.username = "john";
        command.password = "1111";

        commandHandlingContext.handleCommand(command);
    }
}


