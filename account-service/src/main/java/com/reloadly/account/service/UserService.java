package com.reloadly.account.service;

import com.reloadly.account.request.LoginRequest;
import com.reloadly.account.request.RegistrationRequest;
import com.reloadly.account.response.LoginResponse;
import com.reloadly.account.response.RegistrationResponse;

public interface UserService {

    RegistrationResponse registerNewUser(RegistrationRequest registrationRequest) throws Exception;

    LoginResponse login(LoginRequest loginRequest) ;

}
