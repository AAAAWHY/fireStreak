package com.firestreak.user_service;

import com.firestreak.user_service.dto.LoginRequest;
import com.firestreak.user_service.dto.RegisterRequest;
import com.firestreak.user_service.entity.UserEntity;
import com.firestreak.user_service.repository.UserRepository;
import com.firestreak.user_service.service.JWTService;
import com.firestreak.user_service.service.UserService;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.http.client.HttpResponseException;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
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
    void register_success() {
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

        RegisterRequest request = new RegisterRequest(login, email, password, username);
        String jwt = userService.register(request);

        assertThat(jwt).isNotNull();
    }

    @Test
    void register_duplicateLogin() {
        String login = "loginTest";
        String password = "passwordTest";
        String email = "tesst@abc.com";
        String username = "usernameTest";
        UserEntity savedUser = UserEntity.builder()
                .login(login)
                .build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(savedUser));

        RegisterRequest request = new RegisterRequest(login, email, password, username);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.register(request));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void register_duplicateEmail() {
        String login = "loginTest";
        String password = "passwordTest";
        String email = "tesst@abc.com";
        String username = "usernameTest";
        UserEntity savedUser = UserEntity.builder()
                .email(email)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(savedUser));

        RegisterRequest request = new RegisterRequest(login, email, password, username);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.register(request));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    void login_success_byLogin() {
        String login = "login";
        String password = "passwordTest";
        UserEntity savedUser = UserEntity.builder().passwordHash("mock").build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateJWT(any(), any())).thenReturn("jwtTest");
        LoginRequest request = new LoginRequest(login, password);

        assertThat(userService.login(request)).isNotNull();
    }

    @Test
    void login_success_byEmail() {
        String email = "email@test.com";
        String password = "passwordTest";
        UserEntity savedUser = UserEntity.builder().passwordHash("mock").build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateJWT(any(), any())).thenReturn("jwtTest");
        LoginRequest request = new LoginRequest(email, password);

        assertThat(userService.login(request)).isNotNull();
    }

    @Test
    void login_userNotFound_byEmail() {
        String email = "email@test.com";
        String password = "passwordTest";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest(email, password);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(request));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_userNotFound_byLogin() {
        String login = "login";
        String password = "passwordTest";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest(login, password);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(request));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void login_wrongPassword_byLogin() {
        String login = "login";
        String password = "password";
        UserEntity savedUser = UserEntity.builder().passwordHash("passwordHash").build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        LoginRequest loginRequest = new LoginRequest(login, password);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(loginRequest));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }


    @Test
    void login_wrongPassword_byEmail() {
        String email = "email@test.com";
        String password = "password";
        UserEntity savedUser = UserEntity.builder().passwordHash("passwordHash").build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        LoginRequest loginRequest = new LoginRequest(email, password);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(loginRequest));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void changePassword_success(){



    }
}