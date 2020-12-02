package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionRepository {

    public void saveTransaction(Transaction transaction) {
        transaction.setId(getNextId());
        GeneralCache.transactions.add(transaction);
    }

    public List<Transaction> getTransactions(String iban) {
        return GeneralCache.transactions.stream()
                .filter(transaction -> transaction.getReceiverIban().equals(iban) || transaction.getSenderIban().equals(iban))
                .collect(Collectors.toList());
    }

    private int getNextId() {
        return GeneralCache.transactions.size() + 1;
    }
}
