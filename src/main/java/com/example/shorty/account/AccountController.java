package com.example.shorty.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "administration")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /*
    @GetMapping(path = "get")
    public List<Account> getAccount() {
        return accountService.getRegisterAccountResponse();
    }*/

    @GetMapping(path = "get")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PostMapping(path = "register")
    public ResponseEntity<Object> registerNewAccount(@RequestBody Account account) {
        return accountService.addNewAccount(account);
    }

    @PostMapping(path = "login")
    public ResponseEntity<Object> loginAccount(@RequestBody Account account) {
        return accountService.loginAccount(account);
    }
}