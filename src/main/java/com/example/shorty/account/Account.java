package com.example.shorty.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
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
    @Getter
    private String accountId;
    @Getter
    private String password;

    public Account(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }
}
