package com.ivanthescientist.projectmanager;

import com.ivanthescientist.projectmanager.domain.model.User;
import com.ivanthescientist.projectmanager.infrastructure.repository.OrganizationRepository;
import com.ivanthescientist.projectmanager.infrastructure.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    private User user;
    private User organizationManager;
    private User solutionAdmin;

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
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        user = new User(usernameUser, passwordUser, new String[]{"ROLE_USER"}, null);
        organizationManager = new User(usernameOrganizationManager,
                passwordOrganizationManager,
                new String[] {"ROLE_USER", "ROLE_ORGANIZATION_ADMIN"},
                null);

        solutionAdmin = new User(usernameSolutionAdmin,
                passwordSolutionAdmin,
                new String[] {"ROLE_USER", "ROLE_SOLUTION_ADMIN"},
                null);

        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(organizationManager);
        userRepository.saveAndFlush(solutionAdmin);
    }

    @Test
    public void testCreateOrganizationAsSolutionAdmin()
    {

    }

    @Test
    public void testCreateOrganizationAsUser()
    {

    }

    @Test
    public void testUpdateOrganization()
    {

    }

    @Test
    public void testAddMemberToOrganization()
    {

    }

    @Test
    public void testRemoveMemberFromOrganization()
    {

    }

    public void testRemoveNonMemberFromOrganization()
    {

    }
}
