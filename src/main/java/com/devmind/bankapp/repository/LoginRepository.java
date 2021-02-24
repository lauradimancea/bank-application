package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.User;
import com.devmind.bankapp.model.LoginRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class LoginRepository {

    private static final Map<String, LoginRequest> logedUsers = new HashMap<>();

    public Optional<User> findClient(String username, String password) {

        return GeneralCache.clients.stream()
                .filter(client -> client.getPassword().equals(password) && client.getUsername().equals(username))
                .map(client -> (User)client)
                .findFirst();
    }

    public Optional<User> findAdmin(String username, String password) {

        return GeneralCache.administrators.stream()
                .filter(admin -> admin.getPassword().equals(password) && admin.getUsername().equals(username))
                .map(admin -> (User)admin)
                .findFirst();
    }

    public void saveToken(String token, LoginRequest user) {
        logedUsers.put(token, user);
    }

    public boolean isAuthenticated(String token, String username) {
        return logedUsers.containsKey(token) && logedUsers.get(token).getUsername().equals(username);
    }
}
