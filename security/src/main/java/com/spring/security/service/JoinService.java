package com.spring.security.service;

import com.spring.security.dto.JoinDto;
import com.spring.security.entity.User;
import com.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto) {

        String email = joinDto.getEmail();
        String username = joinDto.getPassword();
        String password = joinDto.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {

            return;
        }

        User user = User.builder()
                .email(email)
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_ADMIN") // Spring은 앞단에 있는 접두사를 통해 뒤에 원하는 권한을 설정할 수 있음.
                .build();

        userRepository.save(user);
    }
}
