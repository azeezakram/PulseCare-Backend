package com.pulsecare.backend.module.authentication.service;

import com.pulsecare.backend.module.authentication.model.PrincipleUserDetails;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipleUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public PrincipleUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        GrantedAuthority role =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());

        return new PrincipleUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive(),
                role
        );
    }
}
