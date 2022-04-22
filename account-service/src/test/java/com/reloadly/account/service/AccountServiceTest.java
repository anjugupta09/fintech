package com.reloadly.account.service;

import com.reloadly.account.exception.InvalidAccountNumber;
import com.reloadly.account.mapper.AccountMapper;
import com.reloadly.account.service.impl.AccountServiceImpl;
import com.reloadly.model.dto.AccountDto;
import com.reloadly.model.entity.Account;
import com.reloadly.repository.AccountRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void testGetAccountDetails_success() throws Exception {
        Account account = mockAccount();
        when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));
        when(accountMapper.convertToDto(account)).thenReturn(new AccountDto());
        Assert.notNull(accountService.getBankAccount(101L));
    }

    @Test
    public void testGetAccountDetails_exception() throws Exception {
        when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(InvalidAccountNumber.class, () ->accountService.getBankAccount(101L));
    }

    private Account mockAccount(){
        Account account = new Account();
        account.setBalance(new BigDecimal(100));
        account.setId(101L);
        return account;
    }
}
