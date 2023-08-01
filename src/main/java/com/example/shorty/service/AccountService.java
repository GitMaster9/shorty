package com.example.shorty.service;

import com.example.shorty.repository.AccountRepository;
import com.example.shorty.restapi.Account;
import com.example.shorty.utils.StringGenerator;
import com.example.shorty.utils.StringGeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addNewAccount(Map<String, Object> requestMap) {
        Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            return null;
        }

        String accountId = accountIdObject.toString();

        Account account =  accountRepository.findByAccountId(accountId);
        if (account != null) {
            return null;
        }

        String password = StringGenerator.generateRandomString(StringGeneratorType.PASSWORD);
        Account newAccount = new Account(accountId, password);
        accountRepository.save(newAccount);

        return newAccount;
    }

    public boolean loginAccount(Map<String, Object> requestMap) {
        Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            return false;
        }

        Object passwordObject = requestMap.get("password");
        if (passwordObject == null) {
            return false;
        }

        String accountId = accountIdObject.toString();
        String password = passwordObject.toString();

        Account account = accountRepository.findByAccountIdAndPassword(accountId, password);

        return account != null;
    }
}
