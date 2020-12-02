package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.ClientType;
import com.devmind.bankapp.model.NewClientRequest;
import com.devmind.bankapp.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addClient(NewClientRequest newClientRequest) {
        Client client = Client.builder()
                .fullName(newClientRequest.getFullName())
                .type(newClientRequest.getClientType())
                .password(newClientRequest.getPassword())
                .username(newClientRequest.getUsername())
                .uniqueLegalId(newClientRequest.getUniqueLegalId())
                .country(newClientRequest.getCountry())
                .dateOfBirth(newClientRequest.getDateOfBirth())
                .dateJoined(LocalDateTime.now())
                .build();
        return clientRepository.addClient(client);
    }

    public List<BankAccount> getAllBankAccounts(String username) {
        return getClient(username)
                .map(Client::getBankAccounts)
                .orElse(Collections.emptyList());
    }

    public Set<Client> getClients(ClientType clientType) {
        return clientRepository.getClients(clientType);
    }

    public void addBankAccount(String username, BankAccount bankAccount) {
        getClient(username).ifPresent(client -> client.getBankAccounts().add(bankAccount));
    }

    public Optional<Client> getClient(String username) {
        return clientRepository.findClient(username);
    }

}
