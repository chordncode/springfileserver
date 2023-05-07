package com.chordncode.springfileserver.home.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chordncode.springfileserver.data.entity.User;
import com.chordncode.springfileserver.file.repository.UserRepository;
import com.chordncode.springfileserver.security.CustomUserDetails;
import com.chordncode.springfileserver.util.ResultStatus;

@Service
public class HomeServiceImpl implements IHomeService{

    private final UserRepository userRepo;
    public HomeServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ResultStatus changePw(String newPw) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findById(userId).orElse(null);
        if(user != null){
            user.setUserPw(new BCryptPasswordEncoder().encode(newPw));
            userRepo.saveAndFlush(user);
            ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setUser(user);
            return ResultStatus.SUCCESS;
        }
        return ResultStatus.FAILED;
    }

}
