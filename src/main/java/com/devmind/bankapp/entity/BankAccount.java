package com.devmind.bankapp.entity;

import com.devmind.bankapp.model.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Data
@Builder
public class BankAccount {

    private int id;
    private AccountType accountType;
    private String iban;
    private Currency currency;
    private int clientId;
    private double balance;
    @JsonIgnore
    private List<Transaction> transactions;
    private boolean blocked;
    private LocalDateTime dateOpened;
    private LocalDateTime dateClosed;

}

