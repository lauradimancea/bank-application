package com.devmind.bankapp.controller;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.AccountType;
import com.devmind.bankapp.model.ClientType;
import com.devmind.bankapp.model.NewClientRequest;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.service.AdministratorService;
import com.devmind.bankapp.service.LoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdministratorControllerTest {

    @Mock
    private AdministratorService administratorService;
    @Mock
    private LoginService loginService;

    private AdministratorController administratorController;
    private String authenticatedUsername;
    private String authToken;

    private Set<Client> clients;

    @Before
    public void setUp() {
        administratorController = new AdministratorController(administratorService, loginService);
        authenticatedUsername = "superGoodAdmin";
        authToken = "token";
        clients = Set.of(Client.builder().username("client").type(ClientType.INDIVIDUAL).build());

        when(loginService.isAuthenticatedAndInRole(eq(authToken), eq(authenticatedUsername), eq(UserRole.ADMIN))).thenReturn(true);
    }

    @Test
    public void testGetClients_expectOne() {

        when(administratorService.getClients(ClientType.INDIVIDUAL)).thenReturn(clients);
        Assert.assertEquals(ResponseEntity.ok(clients), administratorController.getClients(authToken, authenticatedUsername, ClientType.INDIVIDUAL));
    }

    @Test
    public void testAddClient_expectAdded() {

        NewClientRequest clientRequest = new NewClientRequest("Ionel", "ionel", "1234",
                ClientType.INDIVIDUAL, "233423", "RO", LocalDate.of(1900, 01, 01));
        Client client = Client.builder().build();
        when(administratorService.addClient(eq(clientRequest))).thenReturn(client);

        Assert.assertEquals(ResponseEntity.ok(client), administratorController.addClient(authToken, authenticatedUsername, clientRequest));

    }

    @Test
    public void testChangeTransactionFee_expectChange() {

        Client client = Client.builder().username("username").transactionFee(2).build();
        when(administratorService.changeTransactionFee(anyString(), anyInt())).thenReturn(Optional.of(client));

        Assert.assertEquals(ResponseEntity.ok(client), administratorController.changeTransactionFee(authToken, authenticatedUsername, "username", 2));

    }

    @Test
    public void testAddBankAccount() {

        when(administratorService.addBankAccount(eq("username"), eq(AccountType.DEBIT), anyDouble())).thenReturn(Optional.of("iban"));

        Assert.assertEquals(ResponseEntity.ok("iban"), administratorController.addBankAccount(authToken, authenticatedUsername, "username", AccountType.DEBIT, 10));
    }
}
