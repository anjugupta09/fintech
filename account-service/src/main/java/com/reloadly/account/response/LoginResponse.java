package com.reloadly.account.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private Long id;
    private String email;
    private List<String> roles;

}
