package com.example.shorty.restapi;

import jakarta.persistence.*;
import lombok.*;
import java.beans.ConstructorProperties;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor_ = @ConstructorProperties({"accountId", "password"})) // maknuti ovo
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
    @NonNull
    private String accountId;
    @NonNull
    private String password;
}
