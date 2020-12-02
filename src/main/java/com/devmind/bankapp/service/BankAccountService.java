package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Transaction;
import com.devmind.bankapp.model.AccountType;
import com.devmind.bankapp.model.TransactionStage;
import com.devmind.bankapp.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionService transactionService;

    public BankAccountService(BankAccountRepository bankAccountRepository, TransactionService transactionService) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionService = transactionService;
    }

    public Optional<Double> getBankAccountBalance(String iban) {
        return bankAccountRepository.getBankAccount(iban)
                .map(BankAccount::getBalance);
    }

    public Optional<BankAccount> getBankAccount(String iban) {
        return bankAccountRepository.getBankAccount(iban);
    }

    public double computeTransfer(BankAccount senderAccount, BankAccount receiverAccount, double amount, int transactionFee) {
        Transaction transaction;
        double senderNewBalance = calculateDifference(senderAccount, amount, transactionFee);

        if (senderNewBalance < 0 && senderAccount.getAccountType() != AccountType.CREDIT) {
            transaction = transactionService.saveTransaction(senderAccount.getIban(), receiverAccount.getIban(), amount, TransactionStage.DECLINED);
            System.out.println("Insufficient funds");
        } else {
            transaction = transactionService.saveTransaction(senderAccount.getIban(), receiverAccount.getIban(), amount, TransactionStage.PROCESSED);
            senderAccount.setBalance(senderNewBalance);
            receiverAccount.setBalance(Double.sum(amount, receiverAccount.getBalance()));
        }

        senderAccount.getTransactions().add(transaction);
        receiverAccount.getTransactions().add(transaction);

        return senderAccount.getBalance();
    }

    public List<Transaction> getTransactions(String iban) {
        List<Transaction> transactions = transactionService.getTransactions(iban);
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed().thenComparing(Transaction::getAmount));
        return transactions;
    }

    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    private double calculateDifference(BankAccount account, double amount, int transactionFee) {
        return account.getBalance() - amount - transactionFee;
    }
}
