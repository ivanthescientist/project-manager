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
import org.springframework.security.web.FilterChainProxy;
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
public class UserControllerIntegrationTest extends BaseIntegrationTest {

    private static final String usernameUser = "user";
    private static final String passwordUser = "1111";
    private static final String usernameOrganizationManager = "user_manager";
    private static final String passwordOrganizationManager = "1111";

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    MockMvc mockMvc;


    @Before
    public void setUp() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testRegisterValidUser() throws Exception {
        RegisterUserCommand command = new RegisterUserCommand();
        command.email = usernameUser;
        command.password = passwordUser;
        command.organizationId = 1L;

        mockMvc.perform(
                post("/users")
                    .content(toJson(command))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertNotNull(userRepository.findOneByEmail(usernameUser));
    }

    @Test
    public void testRegisterExistingUser() throws Exception {
        User existingUser = new User(usernameUser, passwordUser, new String[] {"ROLE_USER"}, null);
        userRepository.saveAndFlush(existingUser);

        RegisterUserCommand command = new RegisterUserCommand();
        command.email = usernameUser;
        command.password = passwordUser;
        command.organizationId = 1L;

        mockMvc.perform(
                post("/users")
                        .content(toJson(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterOrganizationOwner() throws Exception {
        RegisterOrganizationOwnerCommand command = new RegisterOrganizationOwnerCommand();
        command.email = usernameOrganizationManager;
        command.password = passwordOrganizationManager;

        mockMvc.perform(
                post("/users/organization_owners")
                        .content(toJson(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        User user = userRepository.findOneByEmail(command.email);
        assertNotNull(user);
        assertFalse(organizationRepository.findAll().isEmpty());
    }

    @Test
    public void testLoginWithExistingUser() throws Exception
    {
        User user = new User(usernameUser, passwordUser, new String[] {"ROLE_USER"}, null);
        userRepository.saveAndFlush(user);

        mockMvc.perform(get("/users/me")
                .header("username", usernameUser)
                .header("password", passwordUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(usernameUser));
    }
}


