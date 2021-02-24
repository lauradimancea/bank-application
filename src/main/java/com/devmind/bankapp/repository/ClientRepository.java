package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.ClientType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientRepository {

    public Client addClient(Client client) {
        int id = GeneralCache.clients.size() + 1;
        client.setId(id);
        GeneralCache.clients.add(client);
        return client;
    }

    public void deleteClient(String username) {
        findClient(username).ifPresent(client -> GeneralCache.clients.remove(client));
    }

    public Optional<Client> findClient(String username) {
        return GeneralCache.clients.stream()
                .filter(client -> client.getUsername().equals(username))
                .findFirst();
    }

    public Set<Client> getClients(ClientType clientType) {
        return GeneralCache.clients.stream()
                .filter(client -> client.getType() == clientType)
                .collect(Collectors.toSet());
    }

    public static void cleanup() {
        GeneralCache.clients = new HashSet<>();
    }
}