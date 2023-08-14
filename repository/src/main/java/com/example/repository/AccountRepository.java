package com.example.repository;

import com.example.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByAccountId(String accountId);

    Account findByAccountIdAndPassword(String accountId, String password);
}