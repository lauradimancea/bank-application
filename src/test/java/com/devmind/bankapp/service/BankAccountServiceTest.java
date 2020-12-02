package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.model.TransactionStage;
import com.devmind.bankapp.repository.BankAccountRepository;
import com.devmind.bankapp.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.devmind.bankapp.model.AccountType.CREDIT;
import static com.devmind.bankapp.model.AccountType.DEBIT;

public class BankAccountServiceTest {

    private static BankAccountService bankAccountService;
    private static BankAccountRepository bankAccountRepository;
    private static TransactionService transactionService;
    private static TransactionRepository transactionRepository;

    private static BankAccount bankAccount;
    private static BankAccount otherBankAccount;

    @BeforeClass
    public static void setUp() {
        bankAccountRepository = new BankAccountRepository();
        transactionRepository = new TransactionRepository();
        transactionService = new TransactionService(transactionRepository);
        bankAccountService = new BankAccountService(bankAccountRepository, transactionService);

        bankAccount = BankAccount.builder()
                .id(1)
                .accountType(DEBIT)
                .clientId(1)
                .iban("RO1230000BJ09843")
                .balance(100)
                .dateOpened(LocalDateTime.of(2019, 01, 01, 12, 00))
                .build();
        bankAccountRepository.save(bankAccount);

        otherBankAccount = BankAccount.builder()
                .id(3)
                .accountType(CREDIT)
                .clientId(2)
                .iban("RO5678800012")
                .balance(0)
                .dateOpened(LocalDateTime.of(2020, 06, 01, 12, 00))
                .build();
        bankAccountRepository.save(otherBankAccount);
    }

    @Test
    public void testGetBankAccount_expectBankAccount() {

        BankAccount bankAccount = bankAccountService.getBankAccount("RO1230000BJ09843").get();
        Assert.assertEquals(DEBIT, bankAccount.getAccountType());
        Assert.assertEquals(100, bankAccount.getBalance(), 0);
        Assert.assertFalse(bankAccount.isBlocked());
        Assert.assertEquals(LocalDate.of(2019, 01, 01), bankAccount.getDateOpened().toLocalDate());
    }

    @Test
    public void testBankTransfer_expectProcessedTransfer() {

        bankAccountService.computeTransfer(bankAccount, otherBankAccount, 50, 5);

        Assert.assertEquals(45, bankAccount.getBalance(), 0);
        Assert.assertEquals(50, otherBankAccount.getBalance(), 0);
        Assert.assertEquals(1, bankAccount.getTransactions().size());
        Assert.assertEquals(1, otherBankAccount.getTransactions().size());
        Assert.assertEquals(TransactionStage.PROCESSED, bankAccount.getTransactions().get(0).getTransactionStage());
        Assert.assertEquals(50, bankAccount.getTransactions().get(0).getAmount(), 0);

    }

    @Test
    public void testBankTransfer_expectDeclinedTransfer() {

        bankAccountService.computeTransfer(bankAccount, otherBankAccount, 150, 5);

        Assert.assertEquals(45, bankAccount.getBalance(), 0);
        Assert.assertEquals(50, otherBankAccount.getBalance(), 0);
        Assert.assertEquals(2, bankAccount.getTransactions().size());
        Assert.assertEquals(2, otherBankAccount.getTransactions().size());
        Assert.assertEquals(TransactionStage.DECLINED, bankAccount.getTransactions().get(1).getTransactionStage());
        Assert.assertEquals(150, bankAccount.getTransactions().get(1).getAmount(), 0);

    }
}
