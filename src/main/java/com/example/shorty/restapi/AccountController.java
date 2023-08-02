package com.example.shorty.restapi;

import com.example.shorty.exception.ApiBadRequestException;
import com.example.shorty.exception.ExceptionMessages;
import com.example.shorty.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = ControllerPath.ADMINISTRATION)
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = ControllerPath.REGISTER)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> requestMap) {
        final Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            throw new ApiBadRequestException(ExceptionMessages.MISSING_ACCOUNT_ID);
        }

        final String accountId = accountIdObject.toString();

        final Account newAccount = accountService.addNewAccount(accountId);

        Map<String, Object> data = new HashMap<>();
        boolean success;

        if (newAccount != null) {
            success = true;
            data.put("password", newAccount.getPassword());
        }
        else {
            success = false;
            data.put("description", "Account ID already exists!");
        }

        data.put("success", success);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = ControllerPath.LOGIN)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> requestMap) {
        final Object accountIdObject = requestMap.get("accountId");
        if (accountIdObject == null) {
            throw new ApiBadRequestException(ExceptionMessages.MISSING_ACCOUNT_ID);
        }

        final Object passwordObject = requestMap.get("password");
        if (passwordObject == null) {
            throw new ApiBadRequestException(ExceptionMessages.MISSING_PASSWORD);
        }

        final String accountId = accountIdObject.toString();
        final String password = passwordObject.toString();

        final Account account = accountService.loginAccount(accountId, password);

        final boolean success = account != null;
        Map<String, Object> data = new HashMap<>();
        data.put("success", success);

        return ResponseEntity.ok(data);
    }
}