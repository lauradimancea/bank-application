package com.devmind.bankapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Setter
    protected int id;
    protected String username;
    protected String password;
    protected String fullName;
}
