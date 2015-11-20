package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.application.command.AddProjectParticipantCommand;
import com.ivanthescientist.projectmanager.application.command.CreateProjectCommand;
import com.ivanthescientist.projectmanager.application.command.UpdateProjectCommand;
import com.ivanthescientist.projectmanager.domain.model.Organization;
import com.ivanthescientist.projectmanager.domain.model.Project;
import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.ProjectRepository;
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
public class ProjectControllerIntegrationTest extends BaseIntegrationTest {
    private static final String usernameUser = "user";
    private static final String passwordUser = "1111";
    private static final String usernameOrganizationManager = "user_manager";
    private static final String passwordOrganizationManager = "1111";
    private static final String organizationName = "Test organization";
    private static final String organizationDescription = "Test organization description";
    private static final String projectName = "Test project name";
    private static final String projectDescription = "Test project description";
    private static final String existingProjectName = "Existing Test project name";
    private static final String existingProjectDescription = "Existing Test project description";
    private User user;
    private User organizationManager;
    private Organization organization;
    private Project project;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    WebApplicationContext context;

    @Autowired
    FilterChainProxy filterChain;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        projectRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .addFilter(filterChain)
                .build();

        user = new User(usernameUser, passwordUser, new String[]{"ROLE_USER"});

        organizationManager = new User(usernameOrganizationManager,
                passwordOrganizationManager,
                new String[]{"ROLE_USER", "ROLE_ORGANIZATION_ADMIN"});

        organization = new Organization(organizationManager, organizationName, organizationDescription);
        organization.addMember(user);

        project = new Project(existingProjectName, existingProjectDescription, organization);

        user = userRepository.saveAndFlush(user);
        organizationManager = userRepository.saveAndFlush(organizationManager);
        organization = organizationRepository.saveAndFlush(organization);
        project = projectRepository.saveAndFlush(project);
    }

    @Test
    public void testCreateProject() throws Exception
    {
        CreateProjectCommand command = new CreateProjectCommand();
        command.name = projectName;
        command.description = projectDescription;
        command.organizationId = organization.getId();

        mockMvc.perform(authenticateAsUser(post("/projects"), organizationManager)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(command))
        ).andExpect(status().isOk());

        assertNotNull(projectRepository.findOneByName(projectName));
    }

    @Test
    public void testUpdateProjectInfo() throws Exception
    {
        String expectedName = "New Test project name";
        String expectedDescription = "New Test project description";

        UpdateProjectCommand command = new UpdateProjectCommand();
        command.name = expectedName;
        command.description = expectedDescription;
    }

    @Test
    public void testAddParticipant() throws Exception
    {
        AddProjectParticipantCommand command = new AddProjectParticipantCommand();
        command.projectId = project.getId();
        command.userId = user.getId();

        mockMvc.perform(
                authenticateAsUser(post("/projects/" + project.getId() + "/participants"), organizationManager)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(command))
        ).andExpect(status().isOk());

        project = projectRepository.findOne(project.getId());
        assertTrue(project.isParticipant(user));
    }

    @Test
    public void testRemoveParticipant() throws Exception
    {
        project.addParticipant(user);
        project = projectRepository.saveAndFlush(project);

        mockMvc.perform(
                authenticateAsUser(delete("/projects/" + project.getId() + "/participants/" + user.getId()), organizationManager)
        ).andExpect(status().isOk());

        project = projectRepository.findOne(project.getId());
        assertFalse(project.isParticipant(user));
    }
}
