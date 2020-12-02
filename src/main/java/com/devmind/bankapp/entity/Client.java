package com.devmind.bankapp.entity;

import com.devmind.bankapp.model.ClientType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Client extends User {

    private ClientType type;
    private String uniqueLegalId;
    private String country;
    private List<BankAccount> bankAccounts;
    private LocalDate dateOfBirth;
    private LocalDateTime dateJoined;
    @Setter
    private int transactionFee;

    Client(int id, String username, String password,String lastName, ClientType type, String uniqueLegalId, String country,
           List<BankAccount> bankAccounts, LocalDate dateOfBirth, LocalDateTime dateJoined, int transactionFee) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = lastName;
        this.type = type;
        this.uniqueLegalId = uniqueLegalId;
        this.country = country;
        this.bankAccounts = bankAccounts;
        this.dateOfBirth = dateOfBirth;
        this.dateJoined = dateJoined;
        this.transactionFee = transactionFee;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public static class ClientBuilder {
        private int id;
        private String username;
        private String password;
        private String fullName;
        private ClientType type;
        private String uniqueLegalId;
        private String country;
        private List<BankAccount> bankAccounts = new ArrayList<>();
        private LocalDate dateOfBirth;
        private LocalDateTime dateJoined;
        private int transactionFee;

        ClientBuilder() {
        }

        public ClientBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ClientBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ClientBuilder password(String password) {
            this.password = password;
            return this;
        }

        public ClientBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public ClientBuilder uniqueLegalId(String uniqueLegalId) {
            this.uniqueLegalId = uniqueLegalId;
            return this;
        }

        public ClientBuilder type(ClientType clientType) {
            this.type = clientType;
            return this;
        }

        public ClientBuilder country(String country) {
            this.country = country;
            return this;
        }

        public ClientBuilder bankAccounts(List<BankAccount> bankAccounts) {
            this.bankAccounts = bankAccounts;
            return this;
        }

        public ClientBuilder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public ClientBuilder dateJoined(LocalDateTime dateJoined) {
            this.dateJoined = dateJoined;
            return this;
        }

        public ClientBuilder transactionFee(int transactionFee) {
            this.transactionFee = transactionFee;
            return this;
        }

        public Client build() {
            return new Client(id, username, password, fullName, type, uniqueLegalId, country, bankAccounts, dateOfBirth, dateJoined, transactionFee);
        }

        public String toString() {
            return "Client.ClientBuilder(fullName=" + this.fullName + ", uniqueLegalId=" + this.uniqueLegalId +
                    ", country=" + this.country + ", bankAccounts=" + this.bankAccounts +
                    ", dateOfBirth=" + this.dateOfBirth + ", dateJoined=" + this.dateJoined + ")";
        }
    }
}
