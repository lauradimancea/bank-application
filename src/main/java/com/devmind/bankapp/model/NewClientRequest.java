package com.devmind.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class NewClientRequest {

    @NotNull
    private String fullName;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private ClientType clientType;
    @NotNull
    private String uniqueLegalId;
    private String country;
    private LocalDate dateOfBirth;

}
