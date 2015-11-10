package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.application.command.CreateOrganizationCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class OrganizationControllerIntegrationTest extends BaseIntegrationTest {
    private static final String usernameUser = "user";
    private static final String passwordUser = "1111";
    private static final String usernameOrganizationManager = "user_manager";
    private static final String passwordOrganizationManager = "1111";
    private static final String usernameSolutionAdmin = "admin";
    private static final String passwordSolutionAdmin = "1111";
    private static final String organizationName = "Test organization";
    private static final String organizationDescription = "Test organization description";

    private User user;
    private User organizationManager;
    private User solutionAdmin;
    private Organization organization;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    WebApplicationContext context;

    @Autowired
    FilterChainProxy filterChain;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(filterChain)
                .build();

        user = new User(usernameUser, passwordUser, new String[]{"ROLE_USER"}, null);
        organizationManager = new User(usernameOrganizationManager,
                passwordOrganizationManager,
                new String[] {"ROLE_USER", "ROLE_ORGANIZATION_ADMIN"},
                null);

        organization = new Organization(organizationManager, organizationName, organizationDescription);

        solutionAdmin = new User(usernameSolutionAdmin,
                passwordSolutionAdmin,
                new String[] {"ROLE_USER", "ROLE_SOLUTION_ADMIN"},
                null);

        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(organizationManager);
        organizationRepository.saveAndFlush(organization);
        userRepository.saveAndFlush(solutionAdmin);
    }

    @Test
    public void testCreateOrganizationAsSolutionAdmin() throws Exception
    {
        CreateOrganizationCommand command = new CreateOrganizationCommand();
        command.name = organizationName;
        command.description = organizationDescription;

        mockMvc
                .perform(
                        authenticateAsUser(post("/organizations/"), solutionAdmin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(command))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(organizationName));
    }

    @Test
    public void testCreateOrganizationAsUser() throws Exception
    {
        CreateOrganizationCommand command = new CreateOrganizationCommand();
        command.name = organizationName;
        command.description = organizationDescription;

        mockMvc
                .perform(
                        authenticateAsUser(post("/organizations/"), user)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(command))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateOrganization() throws Exception
    {

    }

    @Test
    public void testAddMemberToOrganization() throws Exception
    {

    }

    @Test
    public void testRemoveMemberFromOrganization() throws Exception
    {

    }

    @Test
    public void testRemoveNonMemberFromOrganization() throws Exception
    {

    }
}
