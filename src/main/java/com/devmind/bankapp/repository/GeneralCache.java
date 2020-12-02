package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.Administrator;
import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.entity.Transaction;
import com.devmind.bankapp.model.AdminRight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.devmind.bankapp.model.AccountType.*;
import static com.devmind.bankapp.model.ClientType.*;
import static com.devmind.bankapp.model.TransactionStage.*;
import static java.util.stream.Collectors.toList;

@Component
public class GeneralCache {

    protected static Set<Client> clients;
    protected static Set<BankAccount> bankAccounts;
    static List<Transaction> transactions;
    protected static Set<Administrator> administrators;
    private static List<Currency> currencies;

    static {
        bankAccounts = new HashSet<>();
        clients = new HashSet<>();
        transactions = new ArrayList<>();
        administrators = new HashSet<>();
        currencies = getAllCurrencies();

        transactions.add(Transaction.builder()
                .id(1)
                .amount(50)
                .senderIban("RO1234")
                .receiverIban("RO7890")
                .transactionStage(PROCESSED)
                .date(LocalDateTime.now())
                .build());

        transactions.add(Transaction.builder()
                .id(2)
                .amount(550)
                .senderIban("RO1234")
                .receiverIban("RO9999")
                .transactionStage(PROCESSED)
                .date(LocalDateTime.now())
                .build());

        transactions.add(Transaction.builder()
                .id(3)
                .amount(100)
                .senderIban("RO56788")
                .receiverIban("RO567")
                .transactionStage(PENDING)
                .date(LocalDateTime.now())
                .build());

        bankAccounts.add(BankAccount.builder()
                .id(1)
                .accountType(CREDIT)
                .clientId(1)
                .iban("RO1234")
                .balance(15000)
                .currency(currencies.get(39))
                .transactions(transactions.stream().filter(transaction ->
                    transaction.getSenderIban().equals("RO1234") || transaction.getReceiverIban().equals("RO1234")).collect(toList()))
                .build());

        bankAccounts.add(BankAccount.builder()
                .id(2)
                .accountType(DEBIT)
                .clientId(1)
                .iban("RO567")
                .balance(3800)
                .currency(currencies.get(12))
                .transactions(transactions.stream().filter(transaction ->
                        transaction.getSenderIban().equals("RO567") || transaction.getReceiverIban().equals("RO567")).collect(toList()))
                .build());

        bankAccounts.add(BankAccount.builder()
                .id(3)
                .accountType(CREDIT)
                .clientId(2)
                .iban("RO56788")
                .balance(75600)
                .currency(currencies.get(12))
                .transactions(transactions.stream().filter(transaction ->
                        transaction.getSenderIban().equals("RO56788")  || transaction.getReceiverIban().equals("RO56788")).collect(toList()))
                .build());

        bankAccounts.add(BankAccount.builder()
                .id(4)
                .accountType(CREDIT)
                .clientId(3)
                .iban("RO7890")
                .balance(1000)
                .currency(currencies.get(12))
                .transactions(transactions.stream().filter(transaction ->
                        transaction.getSenderIban().equals("RO7890")  || transaction.getReceiverIban().equals("RO7890")).collect(toList()))
                .build());

        bankAccounts.add(BankAccount.builder()
                .id(5)
                .accountType(CREDIT)
                .clientId(5)
                .iban("RO99999")
                .balance(1000)
                .currency(currencies.get(12))
                .transactions(transactions.stream().filter(transaction ->
                        transaction.getSenderIban().equals("RO99999")  || transaction.getReceiverIban().equals("RO99999")).collect(toList()))
                .build());

        clients.add(Client.builder()
                .id(1)
                .username("firstClient")
                .password("PASSWORD")
                .type(ENTERPRISE)
                .fullName("SRL 1")
                .uniqueLegalId("CUI0459304")
                .bankAccounts(bankAccounts.stream().filter(bankAccount -> bankAccount.getClientId() ==  1).collect(toList()))
                .build());

        clients.add(Client.builder()
                .id(2)
                .username("secondClient")
                .password("PASSWORD")
                .type(INDIVIDUAL)
                .fullName("Grigore Grigoras")
                .uniqueLegalId("235097757657")
                .bankAccounts(bankAccounts.stream().filter(bankAccount -> bankAccount.getClientId() ==  2).collect(toList()))
                .build());

        clients.add(Client.builder()
                .id(3)
                .username("thirdClient")
                .password("PASSWORD")
                .type(INDIVIDUAL)
                .fullName("Danut Georgescu")
                .uniqueLegalId("12531755657")
                .bankAccounts(bankAccounts.stream().filter(bankAccount -> bankAccount.getClientId() ==  3).collect(toList()))
                .build());

        clients.add(Client.builder()
                .id(4)
                .username("fourthClient")
                .password("PASSWORD")
                .type(INDIVIDUAL)
                .fullName("Ionel Popescu")
                .uniqueLegalId("19083749827")
                .bankAccounts(bankAccounts.stream().filter(bankAccount -> bankAccount.getClientId() ==  4).collect(toList()))
                .build());

        clients.add(Client.builder()
                .id(5)
                .username("fifthClient")
                .password("PASSWORD")
                .type(ENTERPRISE)
                .fullName("SRL 2")
                .uniqueLegalId("CUI123596")
                .bankAccounts(bankAccounts.stream().filter(bankAccount -> bankAccount.getClientId() ==  5).collect(toList()))
                .build());

        administrators.add(Administrator.builder()
                .id(1)
                .username("firstAdmin")
                .password("PASSWORD")
                .adminRights(Arrays.asList(AdminRight.values()))
                .build());

        administrators.add(Administrator.builder()
                .id(1)
                .username("secondAdmin")
                .password("PASSWORD")
                .adminRights(Arrays.asList(AdminRight.values()))
                .build());

    }

    private static List<Currency> getAllCurrencies() {
        List<Currency> toret = new ArrayList<>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                Currency currency = Currency.getInstance(loc);
                if (currency != null) {
                    toret.add(currency);
                }
            } catch(Exception exc)
            {
            }
        }

        return toret;
    }
}
