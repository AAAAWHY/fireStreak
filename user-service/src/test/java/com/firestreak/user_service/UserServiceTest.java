package com.firestreak.user_service;

import com.firestreak.user_service.dto.RegisterRequest;
import com.firestreak.user_service.entity.UserEntity;
import com.firestreak.user_service.repository.UserRepository;
import com.firestreak.user_service.service.JWTService;
import com.firestreak.user_service.service.UserService;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JWTService jwtService;
    @InjectMocks
    UserService userService;

    @Test
    void register_success(){
        String login = "loginTest";
        String password = "passwordTest";
        String email = "tesst@abc.com";
        String username = "usernameTest";
        UserEntity savedUser = UserEntity.builder().id(1L).build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(jwtService.generateJWT(any(), any())).thenReturn("jwt-mock");
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword-mock");
        when(userRepository.save(any())).thenReturn(savedUser);

        RegisterRequest request = new RegisterRequest(login,email,password,username);
        String jwt = userService.register(request);

        assertThat(jwt).isNotNull();
    }

    @Test
    void register_duplicateLogin(){
        String login = "loginTest";
        String password = "passwordTest";
        String email = "tesst@abc.com";
        String username = "usernameTest";
        UserEntity savedUser = UserEntity.builder()
                .login(login)
                .build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(savedUser));

        RegisterRequest request = new RegisterRequest(login,email,password,username);

        assertThrows(ResponseStatusException.class, () -> userService.register(request));
    }

    @Test
    void register_duplicateEmail(){
        String login = "loginTest";
        String password = "passwordTest";
        String email = "tesst@abc.com";
        String username = "usernameTest";
        UserEntity savedUser = UserEntity.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        RegisterRequest request = new RegisterRequest(login,email,password,username);

        assertThrows(ResponseStatusException.class, () -> userService.register(request));
    }
}
