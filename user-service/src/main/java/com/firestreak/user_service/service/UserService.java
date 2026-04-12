package com.firestreak.user_service.service;


import com.firestreak.user_service.dto.*;
import com.firestreak.user_service.enums.Role;
import com.firestreak.user_service.entity.UserEntity;
import com.firestreak.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;


    public RegisterResponse register(RegisterRequest request){
        String login = request.login();
        String password = request.password();;
        String email = request.email();
        String username = request.username();

        if(userRepository.findByLogin(login).isPresent())
            return  RegisterResponse.builder().
                    success(false)
                    .error("User with this login already exists")
                    .build();

        if(userRepository.findByEmail(email).isPresent())
            return  RegisterResponse.builder().
                    success(false)
                    .error("User with this email already exists")
                    .build();

        String passwordHash = passwordEncoder.encode(password);
        UserEntity user = UserEntity.builder()
                .email(email)
                .login(login)
                .passwordHash(passwordHash)
                .username(username)
                .role(Role.USER)
                .build();

        user = userRepository.save(user);
        return RegisterResponse.builder()
                .success(true)
                .jwt(jwtService.generateJWT(user.getId(), user.getRole()))
                .error("")
                .build();
    }


    public LoginResponse login(LoginRequest request){
        String identifier = request.identifier();
        String password = request.password();
        UserEntity user;
        if(identifier.contains("@")) {
            user = userRepository.findByEmail(identifier).orElse(null);
            if (user == null)
                return LoginResponse.builder()
                    .success(false)
                    .error("User with this login/email not found")
                    .build();
        }
        else {
            user = userRepository.findByLogin(identifier).orElse(null);
            if (user == null)
                return LoginResponse.builder()
                        .success(false)
                        .error("User with this login/email not found")
                        .build();
        }
        if(passwordEncoder.matches(password,user.getPasswordHash()))
            return LoginResponse.builder()
                    .success(true)
                    .jwt(jwtService.generateJWT(user.getId(), user.getRole()))
                    .build();
        else
            return LoginResponse.builder()
                    .success(false)
                    .error("Wrong password")
                    .build();
    }



    public ChangePasswordResponse changePassword(ChangePasswordRequest request){
        String curPassword = request.curPassword();
        String newPassword = request.newPassword();

        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity user = userRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found to change password"));

        if(passwordEncoder.matches(curPassword, user.getPasswordHash())) {
            String newPasswordHash = passwordEncoder.encode(newPassword);
            user.setPasswordHash(newPasswordHash);
            userRepository.save(user);
            return ChangePasswordResponse.builder()
                    .success(true)
                    .build();
        }
        else
            return ChangePasswordResponse.builder()
                    .success(false)
                    .error("Current password is wrong")
                    .build();
    }
}
