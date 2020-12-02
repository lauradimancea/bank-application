package com.devmind.bankapp.entity;

import com.devmind.bankapp.model.AdminRight;
import lombok.Getter;

import java.util.List;

@Getter
public class Administrator extends User {

    private List<AdminRight> adminRights;

    Administrator(int id, String username, String password, String fullName, List<AdminRight> adminRights) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.adminRights = adminRights;
    }

    public static AdministratorBuilder builder() {
        return new AdministratorBuilder();
    }

    public static class AdministratorBuilder {
        private int id;
        private String username;
        private String password;
        private String fullName;
        private List<AdminRight> adminRights;

        AdministratorBuilder() {
        }

        public AdministratorBuilder id(int id) {
            this.id = id;
            return this;
        }

        public AdministratorBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AdministratorBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AdministratorBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public AdministratorBuilder adminRights(List<AdminRight> adminRights) {
            this.adminRights = adminRights;
            return this;
        }

        public Administrator build() {
            return new Administrator(id, username, password, fullName, adminRights);
        }

        public String toString() {
            return "Administrator.AdministratorBuilder(adminRights=" + this.adminRights + ")";
        }
    }
}
