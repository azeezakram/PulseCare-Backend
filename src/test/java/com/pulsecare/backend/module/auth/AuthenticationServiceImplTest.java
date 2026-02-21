package com.pulsecare.backend.module.auth;

import com.pulsecare.backend.module.authentication.dto.LoginRequestDTO;
import com.pulsecare.backend.module.authentication.dto.LoginResponseDTO;
import com.pulsecare.backend.module.authentication.service.AuthenticationServiceImpl;
import com.pulsecare.backend.module.authentication.utils.JwtUtil;
import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.user.exception.UserInvalidCredentialException;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AuthenticationServiceImpl service;

    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private Users activeUser(String username, String roleName) {
        Users u = new Users();
        u.setId(UUID.randomUUID());
        u.setUsername(username);
        u.setPassword("hashed");
        u.setIsActive(true);

        Role role = new Role();
        role.setName(roleName);
        u.setRole(role);

        u.setLastLoginAt(null);
        return u;
    }

    @Test
    void login_success_authenticates_updatesLastLogin_saves_generatesToken_returnsResponse() {
        LoginRequestDTO req = new LoginRequestDTO("aze", "pw");
        Users user = activeUser("aze", "ADMIN");

        when(userRepository.findByUsername("aze")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("TOKEN");

        LoginResponseDTO res = service.login(req);

        assertEquals("TOKEN", res.token());
        assertEquals("aze", res.username());
        assertEquals("ADMIN", res.role());

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertEquals("aze", captor.getValue().getPrincipal());
        assertEquals("pw", captor.getValue().getCredentials());

        verify(userRepository).findByUsername("aze");
        verify(userRepository).save(user);
        verify(jwtUtil).generateToken(user);

        assertNotNull(user.getLastLoginAt(), "lastLoginAt should be set");
    }

    @Test
    void login_whenAuthFails_throwsInvalidCredential_andDoesNotHitRepository() {
        LoginRequestDTO req = new LoginRequestDTO("aze", "wrong");

        doThrow(new BadCredentialsException("bad"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.login(req));

        assertEquals("Invalid username or password", ex.getMessage());
        verifyNoInteractions(userRepository, jwtUtil);
    }

    @Test
    void login_whenUserNotFound_throwsInvalidCredential() {
        LoginRequestDTO req = new LoginRequestDTO("aze", "pw");

        when(userRepository.findByUsername("aze")).thenReturn(Optional.empty());

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.login(req));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findByUsername("aze");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void login_whenUserInactive_throwsInvalidCredential_andDoesNotSaveOrGenerateToken() {
        LoginRequestDTO req = new LoginRequestDTO("aze", "pw");
        Users user = activeUser("aze", "ADMIN");
        user.setIsActive(false);

        when(userRepository.findByUsername("aze")).thenReturn(Optional.of(user));

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.login(req));

        assertEquals("User is not active", ex.getMessage());

        verify(userRepository).findByUsername("aze");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void verifyByPassword_whenNoAuth_throwsUnauthorized() {
        SecurityContextHolder.clearContext();

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.verifyByPassword(req));

        assertEquals("Unauthorized", ex.getMessage());
        verifyNoInteractions(authenticationManager, userRepository, jwtUtil);
    }

    @Test
    void verifyByPassword_whenAnonymous_throwsUnauthorized() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.getContext().setAuthentication(auth);

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.verifyByPassword(req));

        assertEquals("Unauthorized", ex.getMessage());
        verifyNoInteractions(authenticationManager, userRepository, jwtUtil);
    }

    @Test
    void verifyByPassword_whenNotAuthenticated_throwsUnauthorized() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(auth);

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.verifyByPassword(req));

        assertEquals("Unauthorized", ex.getMessage());
        verifyNoInteractions(authenticationManager, userRepository, jwtUtil);
    }

    @Test
    void verifyByPassword_whenUserNotFound_throwsInvalidCredential() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("aze");
        when(auth.getName()).thenReturn("aze");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByUsername("aze")).thenReturn(Optional.empty());

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.verifyByPassword(req));

        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findByUsername("aze");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(authenticationManager, jwtUtil);
    }

    @Test
    void verifyByPassword_whenUserInactive_throwsInvalidCredential() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("aze");
        when(auth.getName()).thenReturn("aze");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Users user = activeUser("aze", "ADMIN");
        user.setIsActive(false);

        when(userRepository.findByUsername("aze")).thenReturn(Optional.of(user));

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        UserInvalidCredentialException ex =
                assertThrows(UserInvalidCredentialException.class, () -> service.verifyByPassword(req));

        assertEquals("User is not active", ex.getMessage());
        verify(userRepository).findByUsername("aze");
        verifyNoInteractions(authenticationManager, jwtUtil);
    }

    @Test
    void verifyByPassword_whenPasswordCorrect_returnsTrue() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("aze");
        when(auth.getName()).thenReturn("aze");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Users user = activeUser("aze", "ADMIN");
        when(userRepository.findByUsername("aze")).thenReturn(Optional.of(user));

        // authenticate succeeds
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        assertTrue(service.verifyByPassword(req));

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());
        assertEquals("aze", captor.getValue().getPrincipal());
        assertEquals("pw", captor.getValue().getCredentials());
    }

    @Test
    void verifyByPassword_whenPasswordWrong_returnsFalse() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("aze");
        when(auth.getName()).thenReturn("aze");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Users user = activeUser("aze", "ADMIN");
        when(userRepository.findByUsername("aze")).thenReturn(Optional.of(user));

        doThrow(new BadCredentialsException("bad"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        LoginRequestDTO req = new LoginRequestDTO("ignored", "pw");

        assertFalse(service.verifyByPassword(req));
    }
}
