package com.chordncode.springfileserver.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.chordncode.springfileserver.data.entity.User;
import com.chordncode.springfileserver.file.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepo;
    public CustomUserDetailsService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username).orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 틀렸습니다."));
        return new CustomUserDetails(user);
    }

}
