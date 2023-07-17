package com.example.shorty.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(path = "administration")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "register")
    public ResponseEntity<Object> registerNewAccount(@RequestBody Map<String, Object> requestMap) {
        return accountService.addNewAccount(requestMap);
    }

    @PostMapping(path = "login")
    public ResponseEntity<Object> loginAccount(@RequestBody Map<String, Object> requestMap) {
        return accountService.loginAccount(requestMap);
    }
}