package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.Administrator;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.AdminRight;
import com.devmind.bankapp.model.LoginRequest;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.repository.AdministratorRepository;
import com.devmind.bankapp.repository.ClientRepository;
import com.devmind.bankapp.repository.LoginRepository;
import com.devmind.bankapp.utils.DateUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.devmind.bankapp.model.ClientType.ENTERPRISE;
import static com.devmind.bankapp.model.ClientType.INDIVIDUAL;

public class LoginServiceTest {

    @Mock
    private static LoginRepository loginRepository;
    private static LoginService loginService;

    @BeforeClass
    public static void setUp() {
        ClientRepository.cleanup();
        loginRepository = new LoginRepository();
        loginService = new LoginService(loginRepository);

        ClientRepository clientRepository = new ClientRepository();
        Client clientSrl = Client.builder()
                .id(1)
                .username("someCompany")
                .password("PASSWORD")
                .type(ENTERPRISE)
                .fullName("SRL 312343")
                .uniqueLegalId("CUI0459304")
                .country("RO")
                .build();
        Client client = Client.builder()
                .id(1)
                .username("username")
                .password("1234")
                .type(INDIVIDUAL)
                .uniqueLegalId("24500997")
                .country("RO")
                .build();
        clientRepository.addClient(clientSrl);
        clientRepository.addClient(client);

        AdministratorRepository administratorRepository = new AdministratorRepository();
        Administrator admin = Administrator.builder()
                .username("superAdmin")
                .password("1234")
                .adminRights(Arrays.asList(AdminRight.values()))
                .build();
        administratorRepository.addAdmin(admin);
    }

    @Test
    public void testLoginAndSaveToken_expectUserAuthenticated() {

        LocalDateTime loginDate = LocalDateTime.now();
        LoginRequest loginRequest = new LoginRequest("username", "1234", loginDate);
        long millis = DateUtils.toMillis(loginDate);
        String token = "username:CLIENT:" + millis;

        loginService.login(token, loginRequest);

        Assert.assertTrue(loginRepository.isAuthenticated(token, "username"));
    }

    @Test
    public void testLoginClient_expectSuccess() {

        Optional<String> loginToken = loginService.generateLoginTokenForClient("someCompany", "PASSWORD");

        Assert.assertTrue(loginToken.isPresent());
        Assert.assertTrue(loginToken.get().contains("someCompany"));
        Assert.assertTrue(loginToken.get().contains("CLIENT"));
    }

    @Test
    public void testLoginAdmin_expectSuccess() {

        Optional<String> loginToken = loginService.generateLoginTokenForAdmin("superAdmin", "1234");

        Assert.assertTrue(loginToken.isPresent());
        Assert.assertTrue(loginToken.get().contains("superAdmin"));
        Assert.assertTrue(loginToken.get().contains("ADMIN"));
    }

    @Test
    public void testLoginWrongCredentials_expectFail() {

        Optional<String> loginToken = loginService.generateLoginTokenForAdmin("ADMIN", "gsertkj54869");
        Assert.assertFalse(loginToken.isPresent());
    }

    @Test
    public void testAuthenticatedAndInRole_expectTrue() {

        String loginToken = loginService.generateLoginTokenForClient("username", "1234").get();
        LoginRequest loginRequest = new LoginRequest("username", "1234", LocalDateTime.now());

        loginService.login(loginToken, loginRequest);

        boolean loginResult = loginService.isAuthenticatedAndInRole(loginToken, "username", UserRole.CLIENT);
        Assert.assertTrue(loginResult);
    }

}
