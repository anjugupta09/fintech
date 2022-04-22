package com.reloadly.account.service;

import com.reloadly.account.response.AccountBalanceResponse;
import com.reloadly.model.dto.AccountDto;

public interface AccountService {

    AccountDto getBankAccount(Long accountNumber) throws Exception;

    AccountBalanceResponse getAccountBalance(Long accountNumber) throws Exception;
}
