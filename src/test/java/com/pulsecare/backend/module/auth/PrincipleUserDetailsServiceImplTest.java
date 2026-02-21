package com.pulsecare.backend.module.auth;

import com.pulsecare.backend.module.authentication.model.PrincipleUserDetails;
import com.pulsecare.backend.module.authentication.service.PrincipleUserDetailsServiceImpl;
import com.pulsecare.backend.module.user.dto.UserLoginView;
import com.pulsecare.backend.module.user.repository.UserRepository;
import com.pulsecare.backend.module.role.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrincipleUserDetailsServiceImplTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private PrincipleUserDetailsServiceImpl service;

    @Test
    void loadUserByUsername_whenMissing_throwsUsernameNotFound() {
        when(userRepository.findLoginUser("aze")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("aze"));
        verify(userRepository).findLoginUser("aze");
    }

    @Test
    void loadUserByUsername_whenFound_returnsPrincipleUserDetails_withRoleAuthority() {
        Role role = new Role();
        role.setName("ADMIN");

        UserLoginView view = mock(UserLoginView.class);
        when(view.username()).thenReturn("aze");
        when(view.password()).thenReturn("HASH");
        when(view.isActive()).thenReturn(true);
        when(view.role()).thenReturn(role);

        when(userRepository.findLoginUser("aze")).thenReturn(Optional.of(view));

        UserDetails details = service.loadUserByUsername("aze");

        assertTrue(details instanceof PrincipleUserDetails);
        assertEquals("aze", details.getUsername());
        assertEquals("HASH", details.getPassword());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
