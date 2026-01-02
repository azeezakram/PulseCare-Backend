package com.pulsecare.backend.module.authentication.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PrincipleUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean isActive;
    private final GrantedAuthority authority;

    public PrincipleUserDetails(String username, String password, boolean isActive, GrantedAuthority authority) {
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.authority = authority;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isActive);
    }
}
