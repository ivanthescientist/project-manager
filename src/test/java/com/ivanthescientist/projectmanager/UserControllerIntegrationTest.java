package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.application.command.RegisterOrganizationOwnerCommand;
import com.ivanthescientist.projectmanager.application.command.RegisterUserCommand;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIntegrationTest extends BaseIntegrationTest {

    private static final String USERNAME_USER = "user";
    private static final String PASSWORD_USER = "1111";
    private static final String USERNAME_ORGANIZATION_MANAGER = "user_manager";
    private static final String PASSWORD_ORGANIZATION_MANAGER = "1111";

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;


    @Before
    public void setUp() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testRegisterValidUser() throws Exception {
        RegisterUserCommand command = new RegisterUserCommand();
        command.username = USERNAME_USER;
        command.password = PASSWORD_USER;
        command.organizationId = 1L;

        mockMvc.perform(
                post("/api/users")
                    .content(toJson(command))
                    .contentType(MediaType.APPLICATION_JSON));

        assertNotNull(userRepository.findOneByUsername(USERNAME_USER));
    }

    @Test
    public void testRegisterExistingUser() throws Exception {
        User existingUser = new User(USERNAME_USER, PASSWORD_USER, "ROLE_USER");
        userRepository.saveAndFlush(existingUser);

        RegisterUserCommand command = new RegisterUserCommand();
        command.username = USERNAME_USER;
        command.password = PASSWORD_USER;
        command.organizationId = 1L;

        mockMvc.perform(
                post("/api/users")
                        .content(toJson(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterOrganizationOwner() throws Exception {
        RegisterOrganizationOwnerCommand command = new RegisterOrganizationOwnerCommand();
        command.username = USERNAME_ORGANIZATION_MANAGER;
        command.password = PASSWORD_ORGANIZATION_MANAGER;

        mockMvc.perform(
                post("/api/users/organization-owners")
                        .content(toJson(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User user = userRepository.findOneByUsername(command.username);
        assertNotNull(user);
        assertFalse(organizationRepository.findAll().isEmpty());
    }
}


