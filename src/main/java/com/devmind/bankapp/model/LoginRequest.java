package com.devmind.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
public class LoginRequest {

    @Getter
    private String username;
    private String password;
    private LocalDateTime loginDate;
}
