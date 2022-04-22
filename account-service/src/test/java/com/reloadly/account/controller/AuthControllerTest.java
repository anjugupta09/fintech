package com.reloadly.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.AccountType;
import com.reloadly.account.exception.UserAlreadyExistsException;
import com.reloadly.account.request.LoginRequest;
import com.reloadly.account.request.RegistrationRequest;
import com.reloadly.account.response.RegistrationResponse;
import com.reloadly.account.service.UserService;
import com.reloadly.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAuthenticateUser() throws Exception {
        UserDto userDto = new UserDto();
        LoginRequest loginRequest = new LoginRequest("abc@yopmail.com", "abc123#");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto, null, null);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        String json = mapper.writeValueAsString(loginRequest);

        ResultActions response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk());
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        RegistrationRequest signUpRequest = new RegistrationRequest("John", "", "John123#", "john@yopmail.com", "+34652738791", new BigDecimal(10000), AccountType.SAVINGS_ACCOUNT, new HashSet<>());
        Mockito.when(userService.registerNewUser(any(RegistrationRequest.class))).thenReturn(new RegistrationResponse("User registered successfully"));
        String json = mapper.writeValueAsString(signUpRequest);
        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.message").value("User registered successfully"));

        // Test when user provided email already exists in the database
        Mockito.when(userService.registerNewUser(any(RegistrationRequest.class))).thenThrow(new UserAlreadyExistsException("Email Address already in use!"));
        json = mapper.writeValueAsString(signUpRequest);
        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8").content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Email Address already in use!"));
    }
}
