package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.Transaction;
import com.devmind.bankapp.model.TransactionStage;
import com.devmind.bankapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(String senderIban, String receiverIban, double amount, TransactionStage stage) {
        Transaction transaction = Transaction.builder()
                .receiverIban(receiverIban)
                .senderIban(senderIban)
                .amount(amount)
                .transactionStage(stage)
                .date(LocalDateTime.now())
                .build();
        transactionRepository.saveTransaction(transaction);
        return transaction;
    }

    public List<Transaction> getTransactions(String iban) {
        return transactionRepository.getTransactions(iban);
    }
}
