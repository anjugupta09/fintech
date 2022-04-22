package com.reloadly.account.mapper;

import com.reloadly.account.request.RegistrationRequest;
import com.reloadly.model.entity.Account;
import com.reloadly.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    public User transformToUserEntity(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.setMobileNumber(registrationRequest.getMobileNumber());

        Account account = new Account();
        account.setBalance(registrationRequest.getAmount());
        account.setType(registrationRequest.getAccountType());
        account.setUser(user);
        user.getAccounts().add(account);
        return user;
    }

}
