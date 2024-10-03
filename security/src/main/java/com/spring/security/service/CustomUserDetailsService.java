package com.spring.security.service;

import com.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) 
}
