package com.chordncode.springfileserver.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chordncode.springfileserver.data.entity.User;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getUserAuth())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getUserPw();
    }

    @Override
    public String getUsername() {
        return this.user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
