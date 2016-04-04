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
public class ProjectControllerIntegrationTest extends BaseIntegrationTest {
    private static final String USERNAME_USER = "user";
    private static final String PASSWORD_USER = "1111";
    private static final String USERNAME_ORGANIZATION_MANAGER = "user_manager";
    private static final String PASSWORD_ORGANIZATION_MANAGER = "1111";
    private static final String ORGANIZATION_NAME = "Test organization";
    private static final String ORGANIZATION_DESCRIPTION = "Test organization description";
    private static final String PROJECT_NAME = "Test project name";
    private static final String PROJECT_DESCRIPTION = "Test project description";
    private static final String EXISTING_PROJECT_NAME = "Existing Test project name";
    private static final String EXISTING_PROJECT_DESCRIPTION = "Existing Test project description";

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

    MockMvc mockMvc;

    @Before
    public void setUp() {
        projectRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        user = new User(USERNAME_USER, PASSWORD_USER, "ROLE_USER");

        organizationManager = new User(USERNAME_ORGANIZATION_MANAGER,
                PASSWORD_ORGANIZATION_MANAGER,
                "ROLE_USER", "ROLE_ORGANIZATION_ADMIN");

        organization = new Organization(organizationManager, ORGANIZATION_NAME, ORGANIZATION_DESCRIPTION);
        organization.addMember(user);

        project = new Project(EXISTING_PROJECT_NAME, EXISTING_PROJECT_DESCRIPTION, organization);

        user = userRepository.saveAndFlush(user);
        organizationManager = userRepository.saveAndFlush(organizationManager);
        organization = organizationRepository.saveAndFlush(organization);
        project = projectRepository.saveAndFlush(project);
    }

    @Test
    public void testCreateProject() throws Exception
    {
        CreateProjectCommand command = new CreateProjectCommand();
        command.name = PROJECT_NAME;
        command.description = PROJECT_DESCRIPTION;
        command.organizationId = organization.getId();

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(command))
        ).andExpect(status().isOk());

        assertNotNull(projectRepository.findOneByName(PROJECT_NAME));
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
        authenticatedUser(organizationManager);
        AddProjectParticipantCommand command = new AddProjectParticipantCommand();
        command.projectId = project.getId();
        command.userId = user.getId();

        mockMvc.perform(
                post("/projects/" + project.getId() + "/participants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(command))
        ).andExpect(status().isOk());

        project = projectRepository.findOne(project.getId());
        assertTrue(project.isParticipant(user));
    }

    @Test
    public void testRemoveParticipant() throws Exception
    {
        authenticatedUser(organizationManager);
        project.addParticipant(user);
        project = projectRepository.saveAndFlush(project);

        mockMvc.perform(
                delete("/projects/" + project.getId() + "/participants/" + user.getId())
        ).andExpect(status().isOk());

        project = projectRepository.findOne(project.getId());
        assertFalse(project.isParticipant(user));
    }
}
