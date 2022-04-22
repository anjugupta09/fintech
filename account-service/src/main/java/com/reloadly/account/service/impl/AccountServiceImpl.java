package com.reloadly.account.service.impl;

import com.reloadly.account.exception.InvalidAccountNumber;
import com.reloadly.account.mapper.AccountMapper;
import com.reloadly.account.response.AccountBalanceResponse;
import com.reloadly.account.service.AccountService;
import com.reloadly.model.dto.AccountDto;
import com.reloadly.model.entity.Account;
import com.reloadly.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountDto getBankAccount(Long accountNumber) throws Exception {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new InvalidAccountNumber("Error! Invalid Account Number"));
        return accountMapper.convertToDto(account);
    }

    @Override
    public AccountBalanceResponse getAccountBalance(Long accountNumber) throws Exception {
        log.info("Get Balance for account : {}", accountNumber);
        Account bankAccount = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new InvalidAccountNumber("Error! Invalid Account Number"));

        return AccountBalanceResponse.builder()
                .accountNumber(accountNumber)
                .amount(bankAccount.getBalance())
                .build();
    }
}
