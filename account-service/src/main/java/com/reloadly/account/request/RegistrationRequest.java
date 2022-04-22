package com.reloadly.account.request;

import com.reloadly.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "(\\+34|0)[0-9]{9}")
    private String mobileNumber;

    @NotNull
    private BigDecimal amount;
    private AccountType accountType;
    private Set<String> role;
}
