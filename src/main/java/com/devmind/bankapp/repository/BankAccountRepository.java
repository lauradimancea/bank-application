package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.BankAccount;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class BankAccountRepository {

    public Optional<BankAccount> getBankAccount(String iban) {
        return GeneralCache.bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getIban().equals(iban))
                .findFirst();
    }

    public BankAccount save(BankAccount bankAccount) {
        bankAccount.setId(getNextId());
        bankAccount.setTransactions(new ArrayList<>());
        GeneralCache.bankAccounts.add(bankAccount);
        return bankAccount;
    }

    private int getNextId() {
        return GeneralCache.bankAccounts.size() + 1;
    }
}
