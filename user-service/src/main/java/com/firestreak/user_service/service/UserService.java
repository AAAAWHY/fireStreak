package com.firestreak.user_service.service;


import com.firestreak.user_service.entity.UserEntity;
import com.firestreak.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //пока не добавил валидацию на то что пароль больше 4 символо и что имейл это действительно имейл думаю эт длеать в дто
    public void register(String email, String login, String password, String username){
        if(!userRepository.findByLogin(login).isEmpty())
            return; //change with dto

        String passwordHash = passwordEncoder.encode(password);
        UserEntity user = UserEntity.builder()
                .email(email)
                .login(login)
                .passwordHash(passwordHash)
                .username(username)
                .build();

        userRepository.save(user);
    }


    public void login(String identifier, String password){
        UserEntity user;
        if(identifier.contains("@")) {
            user = userRepository.findByEmail(identifier).orElse(null);
            if (user == null) return; //аналогично потом возвращать дто с ошибкой
        }
        else {
            user = userRepository.findByLogin(identifier).orElse(null);
            if (user == null) return; //аналогично потом возвращать дто с ошибкой
        }
        if(passwordEncoder.matches(password,user.getPasswordHash()))
            return; //dto with jwt
        else
            return; //dto with error
    }



    public void changePassword(Long id, String CurPassword, String newPassword){
        UserEntity user = userRepository.findById(id).orElse(null);
        if(user==null) return; //fail dto

        if(passwordEncoder.matches(CurPassword, user.getPasswordHash())) {
            String newPasswordHash = passwordEncoder.encode(newPassword);
            user.setPasswordHash(newPasswordHash);
            userRepository.save(user);
            return; //success dto
        }
        else
            return; //fail dto
    }
}
