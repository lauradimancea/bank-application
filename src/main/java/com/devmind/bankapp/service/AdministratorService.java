package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.AccountType;
import com.devmind.bankapp.model.ClientType;
import com.devmind.bankapp.model.NewClientRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Service
public class AdministratorService {

    private final ClientService clientService;
    private final BankAccountService bankAccountService;

    public AdministratorService(ClientService clientService,
                                BankAccountService bankAccountService) {
        this.clientService = clientService;
        this.bankAccountService = bankAccountService;
    }

    public Set<Client> getClients(ClientType clientType) {
        return clientService.getClients(clientType);
    }

    public Client addClient(NewClientRequest newClientRequest) {
        return clientService.addClient(newClientRequest);
    }

    public Optional<Client> changeTransactionFee(String username, int transactionFee) {
        return clientService.getClient(username)
                .map(client -> {
                    client.setTransactionFee(transactionFee);
                    return client;
                });
    }

    public Optional<String> addBankAccount(String username, AccountType accountType, double balance) {
        return clientService.getClient(username)
                .map(client -> generateAndProcessBankAccount(client, accountType, balance));
    }

    public Optional<BankAccount> unblockBankAccount(String iban) {
        return setBlockStateBankAccount(iban, false);
    }

    public Optional<BankAccount> blockBankAccount(String iban) {
        return setBlockStateBankAccount(iban, true);
    }

    private Optional<BankAccount> setBlockStateBankAccount(String iban, boolean blocked) {
        return bankAccountService.getBankAccount(iban)
                .map(bankAccount -> {
                    bankAccount.setBlocked(blocked);
                    return bankAccount;
                });
    }

    private String generateAndProcessBankAccount(Client client, AccountType accountType, double balance) {
        BankAccount newBankAccount = BankAccount.builder()
                .iban(generateIban())
                .dateOpened(LocalDateTime.now())
                .accountType(accountType)
                .clientId(client.getId())
                .balance(balance)
                .build();
        bankAccountService.save(newBankAccount);
        clientService.addBankAccount(client.getUsername(), newBankAccount);
        return newBankAccount.getIban();
    }

    private String generateIban() {
        return generateIban("RO");
    }

    private String generateIban(String countryPrefix) {
        Random random = new Random();
        StringBuilder code = new StringBuilder(countryPrefix);
        for (int i = 0; i <= 9 ; i++) {
            code.append(random.nextInt(9));
        }
        return code.toString();
    }
}
