package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.BankAccount;
import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.model.AccountType;
import com.devmind.bankapp.model.ClientType;
import com.devmind.bankapp.model.NewClientRequest;
import com.devmind.bankapp.repository.BankAccountRepository;
import com.devmind.bankapp.repository.ClientRepository;
import com.devmind.bankapp.repository.TransactionRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static com.devmind.bankapp.model.ClientType.ENTERPRISE;
import static com.devmind.bankapp.model.ClientType.INDIVIDUAL;

public class AdministratorServiceTest {

    private static final int DEFAULT_TRANSACTION_FEE = 0;

    private static AdministratorService administratorService;
    private static ClientRepository clientRepository;
    private static ClientService clientService;
    private static BankAccountService bankAccountService;

    @BeforeClass
    public static void setUp() {
        ClientRepository.cleanup();
        clientRepository = new ClientRepository();
        clientService = new ClientService(clientRepository);
        bankAccountService = new BankAccountService(new BankAccountRepository(), new TransactionService(new TransactionRepository()));
        administratorService = new AdministratorService(clientService, bankAccountService);

        Client clientSrl = Client.builder()
                .id(1)
                .username("somebody")
                .password("PASSWORD")
                .type(ENTERPRISE)
                .fullName("SRL 1")
                .uniqueLegalId("CUI0459304")
                .build();
        clientRepository.addClient(clientSrl);
    }

    @Test
    public void testAddClient_expectClientExists() {

        String fullName = "Marcel Ion";
        String cnp = "12348000934";
        String username = "usernameTest";
        String country = "RO";
        LocalDate dateOfBirth = LocalDate.of(1990, 10, 30);
        NewClientRequest newClient = new NewClientRequest(fullName, username, "1234", ClientType.INDIVIDUAL, cnp, country, dateOfBirth);

        administratorService.addClient(newClient);
        Client client = clientService.getClient("usernameTest").get();

        Assert.assertEquals(client.getDateJoined().toLocalDate(), LocalDate.now());
        Assert.assertEquals(client.getUniqueLegalId(), cnp);
        Assert.assertEquals(client.getCountry(), country);
        Assert.assertEquals(client.getFullName(), fullName);
        Assert.assertEquals(client.getTransactionFee(), DEFAULT_TRANSACTION_FEE);
    }

    @Test
    public void testAddBankAccount_expectClientHasBankAccountWithBalance() {

        String iban = administratorService.addBankAccount("somebody", AccountType.CREDIT, 50.0).get();
        Client client = clientService.getClient("somebody").get();
        BankAccount bankAccount = bankAccountService.getBankAccount(iban).get();

        Assert.assertNotNull(client.getBankAccounts());
        Assert.assertEquals(iban, client.getBankAccounts().get(0).getIban());
        Assert.assertEquals(50.0, bankAccount.getBalance(), 0);
        Assert.assertEquals(50.0, client.getBankAccounts().get(0).getBalance(), 0);
    }

    @Test
    public void testChangeTransactionFee_expectChangedValue() {

        administratorService.changeTransactionFee("somebody", 5);
        Client client = clientService.getClient("somebody").get();

        Assert.assertEquals(5 ,client.getTransactionFee());
    }

    @Test
    public void test_blockBankAccount() {

        BankAccount bankAccount = clientService.getClient("somebody").get().getBankAccounts().get(0);

        administratorService.blockBankAccount(bankAccount.getIban());
        Assert.assertTrue(bankAccount.isBlocked());

        administratorService.unblockBankAccount(bankAccount.getIban());
        Assert.assertFalse(bankAccount.isBlocked());
    }

    @Test
    public void test_getIndividualClients_expectOne() {
        Assert.assertEquals(1, administratorService.getClients(INDIVIDUAL).size());
    }

    @Test
    public void test_getEnterpriseClients_expectOne() {
        Assert.assertEquals(1, administratorService.getClients(ENTERPRISE).size());
    }

}
