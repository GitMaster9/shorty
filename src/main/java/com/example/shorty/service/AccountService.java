package com.example.shorty.service;

import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.utils.StringGenerator;
import com.example.shorty.utils.StringGeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addNewAccount(String accountId) {
        final Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            return null;
        }

        final String password = StringGenerator.generateRandomString(StringGeneratorType.PASSWORD);

        final Account newAccount = new Account();
        newAccount.setAccountId(accountId);
        newAccount.setPassword(password);

        accountRepository.save(newAccount);

        return newAccount;
    }

    public Account loginAccount(String accountId, String password) {
        return accountRepository.findByAccountIdAndPassword(accountId, password);
    }
}
