package com.example.shorty.account;

import com.example.shorty.generator.StringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Object> addNewAccount(Map<String, Object> requestMap) {
        Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            return createDescriptionResponse("Failed - no 'accountId' field in request body");
        }

        String accountId = accountIdObject.toString();

        Account account = accountRepository.findAccountById(accountId);
        if (account != null) {
            return createRegisterResponse(false, "Account ID already exists!");
        }

        String password = StringGenerator.generatePassword();
        Account newAccount = new Account(accountId, password);
        accountRepository.save(newAccount);

        return createRegisterResponse(true, password);
    }

    public ResponseEntity<Object> loginAccount(Map<String, Object> requestMap) {
        Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            return createDescriptionResponse("Failed - no 'accountId' field in request body");
        }

        Object passwordObject = requestMap.get("password");
        if (passwordObject == null) {
            return createDescriptionResponse("Failed - no 'password' field in request body");
        }

        String accountId = accountIdObject.toString();
        String password = passwordObject.toString();

        Account account = accountRepository.findAccountByIdAndPassword(accountId, password);
        if (account == null) {
            return createSuccessStatusResponse(false);
        }

        return createSuccessStatusResponse(true);
    }

    public ResponseEntity<Object> createRegisterResponse(boolean success, String helper) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", success);

        if (success) {
            data.put("password", helper);
        }
        else {
            data.put("description", helper);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> createSuccessStatusResponse(boolean isSuccessful) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", isSuccessful);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> createDescriptionResponse(String description) {
        Map<String, String> data = new HashMap<>();
        data.put("description", description);
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
