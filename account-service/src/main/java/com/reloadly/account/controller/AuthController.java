package com.reloadly.account.controller;

import com.reloadly.account.request.LoginRequest;
import com.reloadly.account.request.RegistrationRequest;
import com.reloadly.account.response.LoginResponse;
import com.reloadly.account.response.RegistrationResponse;
import com.reloadly.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest signUpRequest) throws Exception {
        RegistrationResponse registrationResponse = userService.registerNewUser(signUpRequest);
        return new ResponseEntity<>(registrationResponse, HttpStatus.OK);
    }

}
