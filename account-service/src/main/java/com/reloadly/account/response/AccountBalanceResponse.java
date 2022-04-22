package com.reloadly.account.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AccountBalanceResponse {

    private Long accountNumber;
    private BigDecimal amount;
}
