package com.example.shorty.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("SELECT s FROM Account s WHERE s.accountId = ?1")
    Account findAccountById(String accountId);

    @Query("SELECT s FROM Account s WHERE s.accountId = ?1 AND s.password = ?2")
    Account findAccountByIdAndPassword(String accountId, String password);
}