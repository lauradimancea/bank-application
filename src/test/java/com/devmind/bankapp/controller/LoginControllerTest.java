package com.devmind.bankapp.controller;

import com.devmind.bankapp.model.LoginRequest;
import com.devmind.bankapp.repository.LoginRepository;
import com.devmind.bankapp.service.LoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;
    private LoginController loginController;

    @Before
    public void setUp() {
        loginController = new LoginController(loginService);
    }

    @Test
    public void testLoginClient_expectSuccess() {

        String username = "username";
        String password = "1234";
        String token = "TOKEN";

        when(loginService.generateLoginTokenForClient(eq(username), eq(password))).thenReturn(Optional.of(token));
        when(loginService.login(eq(token), any(LoginRequest.class))).thenReturn(token);

        HttpEntity<String> response = loginController.loginClient(username, password);

        Assert.assertEquals(ResponseEntity.ok(token), response);
    }

    @Test
    public void testLoginClient_expectForbidden() {

        String username = "none";
        String password = "0000";

        Assert.assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).body("FAILED AUTHENTICATION FOR CLIENT"),
                loginController.loginClient(username, password));
    }
}
