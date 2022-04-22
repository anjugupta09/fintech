package com.reloadly.account.response;

import com.reloadly.AccountType;
import com.reloadly.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountResponse {

    private Long id;
    private String number;
    private AccountType type;
    private BigDecimal balance;
    private User user;
}
