package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.application.command.AddMemberCommand;
import com.ivanthescientist.projectmanager.application.command.CreateOrganizationCommand;
import com.ivanthescientist.projectmanager.application.command.UpdateOrganizationCommand;
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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    AuthenticationProvider authenticationProvider;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        organizationRepository.deleteAll();
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        user = new User(usernameUser, passwordUser, "ROLE_USER");

        organizationManager = new User(usernameOrganizationManager,
                passwordOrganizationManager,
                "ROLE_USER", "ROLE_ORGANIZATION_ADMIN");

        organization = new Organization(organizationManager, organizationName, organizationDescription);

        solutionAdmin = new User(usernameSolutionAdmin,
                passwordSolutionAdmin,
                "ROLE_USER", "ROLE_SOLUTION_ADMIN");

        user = userRepository.saveAndFlush(user);
        organizationManager = userRepository.saveAndFlush(organizationManager);
        organization = organizationRepository.saveAndFlush(organization);
        solutionAdmin = userRepository.saveAndFlush(solutionAdmin);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(solutionAdmin, solutionAdmin.getPassword(), solutionAdmin.getAuthorities()));
    }

    @Test
    public void testCreateOrganizationAsSolutionAdmin() throws Exception
    {
        CreateOrganizationCommand command = new CreateOrganizationCommand();
        command.name = organizationName;
        command.description = organizationDescription;
        command.ownerId = organizationManager.getId();

        mockMvc
                .perform(
                        post("/api/organizations/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(command))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(organizationName));
    }

    @Test
    public void testUpdateOrganization() throws Exception
    {
        authenticatedUser(organizationManager);

        String expectedName = "some new name";
        String expectedDescription = "some new description";
        UpdateOrganizationCommand command = new UpdateOrganizationCommand();
        command.name = expectedName;
        command.description = expectedDescription;

        mockMvc
                .perform(put("/api/organizations/" + organization.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(command))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void testAddMemberToOrganization() throws Exception
    {
        authenticatedUser(organizationManager);

        AddMemberCommand command = new AddMemberCommand();
        command.organizationId = organization.getId();
        command.userId = user.getId();

        mockMvc
                .perform(post("/api/organizations/" + organization.getId() + "/members",
                        organizationManager)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(command))
                ).andExpect(status().isOk());

        organization = organizationRepository.findOne(organization.getId());
        assertTrue(organization.isMember(user));
    }

    @Test
    public void testRemoveMemberFromOrganization() throws Exception
    {
        authenticatedUser(organizationManager);

        organization.addMember(user);
        organizationRepository.saveAndFlush(organization);

        mockMvc
                .perform(delete("/api/organizations/" + organization.getId()
                        + "/members/" + user.getId())
                ).andExpect(status().isOk());

        organization = organizationRepository.findOne(organization.getId());
        assertFalse(organization.isMember(user));
    }

    @Test
    public void testRemoveNonMemberFromOrganization() throws Exception
    {
        authenticatedUser(organizationManager);

        mockMvc
                .perform(delete("/api/organizations/" + organization.getId()
                        + "/members/" + user.getId())
                ).andExpect(status().isBadRequest());
    }
}
