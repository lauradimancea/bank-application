package com.devmind.bankapp.service;

import com.devmind.bankapp.entity.Client;
import com.devmind.bankapp.repository.ClientRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static com.devmind.bankapp.model.ClientType.ENTERPRISE;


public class ClientServiceTest {

    private static ClientService clientService;
    private static ClientRepository clientRepository;

    @BeforeClass
    public static void setUp() {
        ClientRepository.cleanup();
        clientRepository = new ClientRepository();
        clientService = new ClientService(clientRepository);
        Client clientSrl = Client.builder()
                .id(1)
                .username("somebody")
                .password("PASSWORD")
                .type(ENTERPRISE)
                .fullName("SRL 1")
                .uniqueLegalId("CUI0459304")
                .country("RO")
                .build();
        clientRepository.addClient(clientSrl);
    }

    @Test
    public void testGetBankAccounts_expectNone() {

        Assert.assertEquals(Collections.emptyList(), clientService.getAllBankAccounts("somebody"));
    }

    @Test
    public void test_getClient_expectClient() {

        Client client = clientService.getClient("somebody").get();

        Assert.assertEquals(ENTERPRISE, client.getType());
        Assert.assertEquals("CUI0459304", client.getUniqueLegalId());
        Assert.assertEquals("RO", client.getCountry());
        Assert.assertEquals("SRL 1", client.getFullName());
    }
}
