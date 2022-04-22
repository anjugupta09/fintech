package com.reloadly.account.controller;

import com.reloadly.account.response.AccountBalanceResponse;
import com.reloadly.account.service.AccountService;
import com.reloadly.model.dto.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/account")
@Transactional
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/details/{accountNumber}")
    public ResponseEntity<AccountDto> getBankAccount(@PathVariable Long accountNumber) throws Exception {
        AccountDto accountDto = accountService.getBankAccount(accountNumber);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> getBalance(@PathVariable Long accountNumber) throws Exception{
        return ResponseEntity.ok(accountService.getAccountBalance(accountNumber));
    }

}
