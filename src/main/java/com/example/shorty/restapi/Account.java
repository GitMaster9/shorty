package com.example.shorty.restapi;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @SequenceGenerator(
            name = TableConstant.AccountSequence,
            sequenceName = TableConstant.AccountSequence,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = TableConstant.AccountSequence
    )
    private Long id;
    private String accountId;
    private String password;
}
