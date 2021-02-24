package com.devmind.bankapp.entity;

import com.devmind.bankapp.model.TransactionStage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {

    @JsonIgnore
    private int id;
    private String senderIban;
    private String receiverIban;
    private LocalDateTime date;
    private double amount;
    private String currency;
    private TransactionStage transactionStage;
}
