package com.devmind.bankapp.service;

import com.devmind.bankapp.model.LoginRequest;
import com.devmind.bankapp.model.UserRole;
import com.devmind.bankapp.repository.LoginRepository;
import com.devmind.bankapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginService {

    private static final int MIN_TO_EXPIRE = 60;

    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public String login(String token, LoginRequest user) {
        loginRepository.saveToken(token, user);
        return token;
    }

    public Optional<String> generateLoginTokenForClient(String username, String password) {

        return loginRepository.findClient(username, password)
                .map(user -> generateToken(username, UserRole.CLIENT.name()));
    }

    public Optional<String> generateLoginTokenForAdmin(String username, String password) {

        return loginRepository.findAdmin(username, password)
                .map(user -> generateToken(username, UserRole.ADMIN.name()));
    }

    public boolean isAuthenticatedAndInRole(String token, String username, UserRole expectedRole) {

        if (loginRepository.isAuthenticated(token, username) && hasCorrectRole(token, expectedRole)) {
            if (isExpired(token)) {
                System.out.println("Expired token");
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean isExpired(String token) {
        try {
            String[] decoded = token.split(":");
            LocalDateTime dateTimeOfLogin = DateUtils.toLocalDateTime(Long.parseLong(decoded[2]));
            return LocalDateTime.now().minusMinutes(MIN_TO_EXPIRE).isAfter(dateTimeOfLogin);

        } catch (Exception e) {
            System.out.println("Failed decoding time" + e);
            return false;
        }
    }

    private boolean hasCorrectRole(String token, UserRole expectedRole) {
        try {
            String[] decoded = token.split(":");
            return expectedRole == UserRole.valueOf(decoded[1]);
        } catch (Exception e) {
            System.out.println("Failed decoding role" + e);
            return false;
        }
    }

    private String generateToken(String username, String role) {
        return  username + ":" + role  + ":" + DateUtils.toMillis(LocalDateTime.now());
    }
}
