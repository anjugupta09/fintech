package com.reloadly.account.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reloadly.account.exception.RoleNotFoundException;
import com.reloadly.account.exception.UserAlreadyExistsException;
import com.reloadly.account.mapper.UserMapper;
import com.reloadly.account.utils.KafkaUtils;
import com.reloadly.constant.Constants;
import com.reloadly.kafka.producer.KafkaProducer;
import com.reloadly.account.request.LoginRequest;
import com.reloadly.account.request.RegistrationRequest;
import com.reloadly.account.response.LoginResponse;
import com.reloadly.account.response.RegistrationResponse;
import com.reloadly.account.service.UserService;
import com.reloadly.kafka.EventPayload;
import com.reloadly.kafka.UserRegistrationPayload;
import com.reloadly.model.entity.Role;
import com.reloadly.model.RoleType;
import com.reloadly.model.entity.User;
import com.reloadly.repository.RoleRepository;
import com.reloadly.repository.UserRepository;
import com.reloadly.security.config.JwtTokenUtils;
import com.reloadly.security.service.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Value(value = "${kafka.topic.name}")
    private String topic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public RegistrationResponse registerNewUser(RegistrationRequest signUpRequest) throws Exception {
        String correlationId = ThreadContext.get(Constants.X_CORRELATION_ID);
        log.info("A new signupRequest for the user with email: {}, correlationId: {}", signUpRequest.getEmail(), correlationId);
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use!");
        }
        // Create new user's account
        User user = userMapper.transformToUserEntity(signUpRequest);
        user.setRoles(getRoles(signUpRequest.getRole()));

        userRepository.save(user);
        //publishing the message to kafka topic
        publishToKafkaTopic(correlationId, user);
        log.info("User registered having email: {}, correlationId: {}", signUpRequest.getEmail(), correlationId);
        return new RegistrationResponse("User registered successfully!");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new LoginResponse(jwtToken, Constants.BEARER,
                userDetails.getId(),
                userDetails.getEmail(),
                roles);
    }

    private Set<Role> getRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                            .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                            .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
                    roles.add(userRole);
                }
            }
        }
        return roles;
    }

    private void publishToKafkaTopic(String correlationId, User user) {
        CompletableFuture.runAsync(() -> {
            log.info("Pushing the message to Kafka topic: {}, correlationId: {} ", topic, correlationId);
            EventPayload<UserRegistrationPayload> payload = KafkaUtils.createUserRegistrationPayload(user, correlationId);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = null;
            try {
                json = objectMapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                log.error("Some exception occurred on Json parsing", e);
            }
            kafkaProducer.sendMessage(topic, json);
        });
    }
}
