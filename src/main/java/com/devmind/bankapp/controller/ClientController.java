package com.devmind.bankapp.controller;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.entity.Transaction;
import com.devmind.bankapp.model.BankTransferDto;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.service.BankAccountService;
import com.devmind.bankapp.service.ClientService;
import com.devmind.bankapp.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final BankAccountService bankAccountService;
    private final LoginService loginService;

    @Autowired
    public ClientController(ClientService clientService, BankAccountService bankAccountService, LoginService loginService) {
        this.clientService = clientService;
        this.bankAccountService = bankAccountService;
        this.loginService = loginService;
    }

    @GetMapping(path = "/account/{username}")
    public HttpEntity<List<BankAccount>> getBankAccounts(@RequestParam("authToken") String authToken,
                                                         @PathVariable(value = "username") String username) {

        if (!loginService.isAuthenticatedAndInRole(authToken, username, UserRole.CLIENT)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return ResponseEntity.ok(clientService.getAllBankAccounts(username));
    }

    @GetMapping(path = "/account/balance/{iban}")
    public HttpEntity<Double> getBankAccountBalance(@RequestParam("authToken") String authToken,
                                                    @RequestParam("loginUsername") String loginUsername,
                                                    @PathVariable(value = "iban") String iban) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.CLIENT)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return bankAccountService.getBankAccountBalance(iban)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/account/transaction/{iban}")
    public HttpEntity<List<Transaction>> getBankAccountTransactions(@RequestParam("authToken") String authToken,
                                                                    @RequestParam("loginUsername") String loginUsername,
                                                                    @PathVariable(value = "iban") String iban) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.CLIENT)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return ResponseEntity.ok(bankAccountService.getTransactions(iban));
    }

    @PostMapping(path = "/transfer")
    public HttpEntity<Double> transfer(@RequestParam("authToken") String authToken,
                                       @RequestParam("loginUsername") String loginUsername,
                                       @RequestBody BankTransferDto bankTransferDto) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.CLIENT)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }

        Optional<Client> clientOptional = clientService.getClient(bankTransferDto.getSenderIban());
        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(FORBIDDEN).build();
        }

        if (!clientOptional.get().getUsername().equals(loginUsername)) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }

        int transactionFee = clientOptional
                .map(Client::getTransactionFee)
                .orElse(0);

        return bankAccountService.getBankAccount(bankTransferDto.getSenderIban())
                .map(sender -> bankAccountService.getBankAccount(bankTransferDto.getReceiverIban())
                            .map(receiver -> ResponseEntity.ok(bankAccountService.computeTransfer(sender, receiver, bankTransferDto.getAmount(), transactionFee)))
                            .orElse(ResponseEntity.badRequest().build()))
                .orElse(ResponseEntity.badRequest().build());

    }
}
