package com.chordncode.springfileserver.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chordncode.springfileserver.data.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    
    

}
