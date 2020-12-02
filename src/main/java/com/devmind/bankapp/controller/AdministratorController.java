package com.devmind.bankapp.controller;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.AccountType;
import com.devmind.bankapp.model.ClientType;
import com.devmind.bankapp.model.NewClientRequest;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.service.AdministratorService;
import com.devmind.bankapp.service.LoginService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Set;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

    private final AdministratorService administratorService;
    private final LoginService loginService;

    public AdministratorController(AdministratorService administratorService, LoginService loginService) {
        this.administratorService = administratorService;
        this.loginService = loginService;
    }

    @GetMapping("/client")
    public HttpEntity<Set<Client>> getClients(@RequestParam("authToken") String authToken,
                                              @RequestParam("loginUsername") String loginUsername,
                                              @RequestParam("clientType") ClientType clientType) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return ResponseEntity.ok(administratorService.getClients(clientType));
    }

    @PostMapping("/client")
    public HttpEntity<Client> addClient(@RequestParam("authToken") String authToken,
                                        @RequestParam("loginUsername") String loginUsername,
                                        @RequestBody @Valid NewClientRequest newClientRequest) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return ResponseEntity.ok(administratorService.addClient(newClientRequest));

    }

    @PatchMapping("/client/transactionFee")
    public HttpEntity<Client> changeTransactionFee(@RequestParam("authToken") String authToken,
                                                   @RequestParam("loginUsername") String loginUsername,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("transactionFee") int transactionFee) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return administratorService.changeTransactionFee(username, transactionFee)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping("/bankAccount")
    public HttpEntity<String> addBankAccount(@RequestParam("authToken") String authToken,
                                             @RequestParam("loginUsername") String loginUsername,
                                             @RequestParam("username") String username,
                                             @RequestParam("accountType") AccountType accountType,
                                             @RequestParam(value = "balance", required = false, defaultValue = "0") int balance) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return administratorService.addBankAccount(username, accountType, balance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/bankAccount/block/{iban}")
    public HttpEntity<BankAccount> blockBankAccount(@RequestParam("authToken") String authToken,
                                                    @RequestParam("loginUsername") String loginUsername,
                                                    @PathParam("iban") String iban) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return administratorService.blockBankAccount(iban)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/bankAccount/unblock/{iban}")
    public HttpEntity<BankAccount> unblockBankAccount(@RequestParam("authToken") String authToken,
                                                      @RequestParam("loginUsername") String loginUsername,
                                                      @PathParam("iban") String iban) {

        if (!loginService.isAuthenticatedAndInRole(authToken, loginUsername, UserRole.ADMIN)) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
        return administratorService.unblockBankAccount(iban)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
