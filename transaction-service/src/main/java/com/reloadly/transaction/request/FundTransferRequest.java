package com.reloadly.transaction.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class FundTransferRequest {

    @NotNull
    private Long fromAccount;
    @NotNull
    private Long toAccount;
    @NotNull
    private BigDecimal amount;
}
