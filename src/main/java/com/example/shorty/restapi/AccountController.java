package com.example.shorty.restapi;

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
    public ResponseEntity<Object> registerNewAccount(@RequestBody Map<String, Object> requestMap) {
        Account newAccount = accountService.addNewAccount(requestMap);

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
    public ResponseEntity<Object> loginAccount(@RequestBody Map<String, Object> requestMap) {
        boolean success = accountService.loginAccount(requestMap);

        Map<String, Object> data = new HashMap<>();
        data.put("success", success);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}