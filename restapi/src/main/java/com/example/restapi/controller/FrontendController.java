package com.example.restapi.controller;

import com.example.core.ControllerPath;
import com.example.core.model.Account;
import com.example.core.model.ShortingRequest;
import com.example.core.model.ShortingResponse;
import com.example.core.model.UrlShortener;
import com.example.restapi.service.FrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class FrontendController {

    private final FrontendService frontendService;

    @Autowired
    public FrontendController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @GetMapping(ControllerPath.REGISTER)
    public String showRegisterPage(Account account, Model model) {
        return "register";
    }

    @GetMapping(ControllerPath.LOGIN)
    public String showLoginPage(Account account, Model model) {
        return "login";
    }

    @GetMapping(ControllerPath.SHORT)
    public String showShortPage(ShortingRequest shortingRequest, Model model) {
        return "short";
    }

    @GetMapping(ControllerPath.STATISTICS)
    public String showStatisticsPage(Account account, Model model) {
        return "statistics";
    }

    @GetMapping(ControllerPath.REDIRECT)
    public String showRedirectPage(UrlShortener urlShortener) {
        return "redirect";
    }

    @PostMapping(ControllerPath.REGISTER)
    public String registerUser(@ModelAttribute Account account, Model model) {
        final String accountId = account.getAccountId();

        if (accountId == null || accountId.isEmpty()) {
            model.addAttribute("errorMessage", "Account Id field is empty");
            return "register";
        }

        final Account accountRegistered = frontendService.sendRegisterRequest(accountId);

        if (accountRegistered == null) {
            model.addAttribute("errorMessage", "Account ID is already taken!");
        }
        else {
            model.addAttribute("successMessage", "Password: " + accountRegistered.getPassword());
        }

        return "register";
    }

    @PostMapping(ControllerPath.LOGIN)
    public String loginUser(@ModelAttribute Account account, Model model) {
        final String accountId = account.getAccountId();
        final String password = account.getPassword();

        if (accountId.isEmpty() || password.isEmpty()) {
            model.addAttribute("errorMessage", "User credentials are empty");
            return "login";
        }

        final boolean success = frontendService.sendLoginRequest(accountId, password);

        if (success) {
            model.addAttribute("successMessage", "Login successful");
        }
        else {
            model.addAttribute("errorMessage", "Login failed");
        }

        return "login";
    }

    @PostMapping(ControllerPath.SHORT)
    public String shortUrl(ShortingRequest shortingRequest, Model model) {
        final String accountId = shortingRequest.getAccountId();
        final String password = shortingRequest.getPassword();
        final String url = shortingRequest.getUrl();

        if (accountId.isEmpty() || password.isEmpty() || url.isEmpty()) {
            model.addAttribute("errorMessage", "Fields can't be empty");
            return "short";
        }

        int redirectType;
        final String redirectTypeString = shortingRequest.getRedirectType();
        if (redirectTypeString.isEmpty()) {
            redirectType = 302;
        }
        else {
            try {
                redirectType = Integer.parseInt(redirectTypeString);
            }
            catch (NumberFormatException nfe) {
                redirectType = 0;
            }
        }

        if (redirectType != 301 && redirectType != 302) {
            model.addAttribute("errorMessage", "Error: redirect type can be only be 301 (Moved Permanently) or 302 (Found)");
            return "short";
        }

        final ShortingResponse shortingResponse = frontendService.sendShortRequest(accountId, password, url, redirectType);

        final String description = shortingResponse.getDescription();
        if (description != null) {
            model.addAttribute("errorMessage", description);
            return "short";
        }

        final String shortUrl = shortingResponse.getShortUrl();
        model.addAttribute("successMessage", "short URL: " + shortUrl);

        return "short";
    }

    @PostMapping(ControllerPath.STATISTICS)
    public String getStatistics(Account account, Model model) {
        final String accountId = account.getAccountId();
        final String password = account.getPassword();

        if (accountId.isEmpty() || password.isEmpty()) {
            model.addAttribute("errorMessage", "User credentials are empty");
            return "statistics";
        }

        final List<UrlShortener> receivedURLs = frontendService.sendStatisticsRequest(accountId, password);

        if (receivedURLs.isEmpty()) {
            model.addAttribute("successMessage", "No registered URLs for this user");
        }

        model.addAttribute("URLs", receivedURLs);

        return "statistics";
    }

    @GetMapping(ControllerPath.GET_URL)
    public ResponseEntity<Void> redirect(@ModelAttribute UrlShortener urlShortener) {
        final String shortUrl = urlShortener.getShortUrl();

        if (shortUrl.isEmpty()) {
            return ResponseEntity.status(400).build();
        }

        return frontendService.sendRedirectRequest(shortUrl);
    }
}
