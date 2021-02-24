package com.devmind.bankapp.controller;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.BankTransferDto;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.service.BankAccountService;
import com.devmind.bankapp.service.ClientService;
import com.devmind.bankapp.service.LoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private LoginService loginService;

    private ClientController clientController;

    private String authenticatedUsername;
    private String iban;
    private String authToken;
    private List<BankAccount> bankAccounts;

    @Before
    public void setUp() {
        clientController = new ClientController(clientService, bankAccountService, loginService);
        authenticatedUsername = "okUsername";
        iban = "iban123";
        authToken = "token";
        BankAccount bankAccount = BankAccount.builder().iban(iban).balance(10.0).build();
        bankAccounts = List.of(bankAccount);

        when(loginService.isAuthenticatedAndInRole(eq(authToken), eq(authenticatedUsername), eq(UserRole.CLIENT))).thenReturn(true);
    }

    @Test
    public void testGetAllBankAccounts_expectOK() {

        when(clientService.getAllBankAccounts(authenticatedUsername)).thenReturn(bankAccounts);
        Assert.assertEquals(ResponseEntity.ok(bankAccounts), clientController.getBankAccounts(authToken, authenticatedUsername));
    }

    @Test
    public void testGetBankAccountBalance_expect10() {

        when(bankAccountService.getBankAccountBalance(eq(iban))).thenReturn(Optional.of(10.0));
        Assert.assertEquals(ResponseEntity.ok(10.0),
                clientController.getBankAccountBalance(authToken, authenticatedUsername, iban));
    }

    @Test
    public void testGetBankAccountTransactions_expectNone() {

        Assert.assertEquals(ResponseEntity.ok(Collections.emptyList()),
                clientController.getBankAccountTransactions(authToken, authenticatedUsername, iban));
    }

    @Test
    public void testTransfer_expectTransactionSuccess() {

        String receiverIban = "RO129485ij00";
        BankTransferDto bankTransferDto = new BankTransferDto(iban, receiverIban, 5);

        when(clientService.getClient(eq(iban))).thenReturn(Optional.of(Client.builder().username(authenticatedUsername).transactionFee(2).build()));

        when(bankAccountService.getBankAccount(eq(iban))).thenReturn(Optional.of(BankAccount.builder().balance(100).build()));
        when(bankAccountService.getBankAccount(eq(receiverIban))).thenReturn(Optional.of(BankAccount.builder().balance(10).build()));
        when(bankAccountService.computeTransfer(any(BankAccount.class), any(BankAccount.class), anyDouble(), anyInt())).thenReturn((double) (100-5-2));

        Assert.assertEquals(ResponseEntity.ok(93.0), clientController.transfer(authToken, authenticatedUsername, bankTransferDto));
    }

}
