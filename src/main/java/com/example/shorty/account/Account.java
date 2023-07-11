package com.example.shorty.account;

import jakarta.persistence.*;

@Entity
@Table
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;
    private String accountId;
    private String password;

    public Account() {
    }

    public Account(Long id, String accountId, String password) {
        this.id = id;
        this.accountId = accountId;
        this.password = password;
    }

    public Account(String accountId) {
        this.accountId = accountId;
    }

    public Account(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
