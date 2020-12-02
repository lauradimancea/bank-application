package com.devmind.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class BankTransferDto {

    @NonNull
    private String senderIban;
    private String receiverIban;
    private double amount;
}
