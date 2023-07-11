package com.example.shorty.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT s FROM Account s WHERE s.accountId = ?1")
    Optional<Account> findAccountByAccountId(String accountId);
}