package com.devmind.bankapp.repository;

import com.devmind.bankapp.entity.Administrator;
import org.springframework.stereotype.Component;

@Component
public class AdministratorRepository {

    public Administrator addAdmin(Administrator administrator) {
        int id = GeneralCache.administrators.size() + 1;
        administrator.setId(id);
        GeneralCache.administrators.add(administrator);
        return administrator;
    }
}
