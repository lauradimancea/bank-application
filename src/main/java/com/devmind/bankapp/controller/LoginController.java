package com.devmind.bankapp.controller;

import com.devmind.bankapp.model.LoginRequest;
import com.devmind.bankapp.service.LoginService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/client")
    public HttpEntity<String> loginClient(@RequestParam("username") String username,
                                          @RequestParam("password") String password) {

        Optional<String> loginToken = loginService.generateLoginTokenForClient(username, password)
                .map(token -> loginService.login(token, new LoginRequest(username, password, LocalDateTime.now())));

        return loginToken.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).body("FAILED AUTHENTICATION FOR CLIENT"));
    }

    @PostMapping("/admin")
    public HttpEntity<String> loginAdmin(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {

        Optional<String> tokenOptional = loginService.generateLoginTokenForAdmin(username, password)
                .map(token -> loginService.login(token, new LoginRequest(username, password, LocalDateTime.now())));

        return tokenOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).body("FAILED AUTHENTICATION FOR ADMIN"));
    }
}
