package com.pulsecare.backend.module.user.service;

import com.pulsecare.backend.module.user.model.PrincipleUserDetails;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PrincipleUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public PrincipleUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .collect(Collectors.toSet());

        return new PrincipleUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getIsActive(),
                authorities
        );
    }
}
