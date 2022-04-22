package com.reloadly.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reloadly.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @JsonProperty("accountNumber")
    private Long id;
    private AccountType type;
    private BigDecimal balance;
    private Long userId;
}
