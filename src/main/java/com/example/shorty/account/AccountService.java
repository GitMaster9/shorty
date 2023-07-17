package com.example.shorty.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            return createRequestFailResponse("Failed - no 'accountId' field in request body");
        }

        String accountId = accountIdObject.toString();

        Optional<Account> accountOptional = accountRepository.findAccountByAccountId(accountId);
        if (accountOptional.isPresent()) {
            return createRegisterFailResponse();
        }
        else {
            // TODO: Generate a random password for new user
            String password = "dummypwd";
            Account account = new Account(accountId, password);
            accountRepository.save(account);

            return createRegisterSuccessResponse(password);
        }
    }

    public ResponseEntity<Object> createRegisterFailResponse() {
        Map<String, Object> data = new HashMap<>();
        data.put("success", false);
        data.put("description", "Account ID already exists!");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> createRegisterSuccessResponse(String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("password", password);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> loginAccount(Account account) {
        String accountId = account.getAccountId();
        String password = account.getPassword();

        Optional<Account> accountOptional = accountRepository.findAccountByAccountId(accountId);
        if (accountOptional.isPresent()) {
            if (password.equals(accountOptional.get().getPassword())) {
                return createSuccessStatusResponse(true);
            }
        }

        return createSuccessStatusResponse(false);
    }

    public ResponseEntity<Object> createSuccessStatusResponse(boolean isSuccessful) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", isSuccessful);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public ResponseEntity<Object> createRequestFailResponse(String description) {
        Map<String, String> data = new HashMap<>();
        data.put("description", description);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
