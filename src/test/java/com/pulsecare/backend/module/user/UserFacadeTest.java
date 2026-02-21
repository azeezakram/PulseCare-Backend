package com.pulsecare.backend.module.user;

import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.role.service.RoleService;
import com.pulsecare.backend.module.user.dto.UserRequestDTO;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.facade.UserFacade;
import com.pulsecare.backend.module.user.mapper.UserMapper;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock private UserService userService;
    @Mock private RoleService roleService;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserFacade facade;

    @Test
    void createNewUser_withRoleAndPassword_encodesAndSaves() {
        UserRequestDTO req = new UserRequestDTO(
                "Abdul", "Azeez", "aze", "a@x.com", "1234", "077", 1, true
        );

        Users entity = new Users();
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        Users saved = new Users();
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(userMapper.toEntity(req)).thenReturn(entity);
        when(roleService.findEntityById(1)).thenReturn(role);
        when(passwordEncoder.encode("1234")).thenReturn("ENC");
        when(userService.save(entity)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(dto);

        UserResponseDTO result = facade.createNewUser(req);

        assertSame(dto, result);

        verify(userService).validateUsernameDoesNotExist("aze");
        verify(userMapper).toEntity(req);
        verify(roleService).findEntityById(1);
        verify(passwordEncoder).encode("1234");

        // entity got role + encoded password set
        assertSame(role, entity.getRole());
        assertEquals("ENC", entity.getPassword());

        verify(userService).save(entity);
        verify(userMapper).toDTO(saved);
    }

    @Test
    void createNewUser_withoutRoleAndPassword_doesNotCallRoleOrEncoder() {
        UserRequestDTO req = new UserRequestDTO(
                "Abdul", "Azeez", "aze", "a@x.com", null, "077", null, true
        );

        Users entity = new Users();
        Users saved = new Users();
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(userMapper.toEntity(req)).thenReturn(entity);
        when(userService.save(entity)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(dto);

        UserResponseDTO result = facade.createNewUser(req);

        assertSame(dto, result);

        verify(userService).validateUsernameDoesNotExist("aze");
        verify(roleService, never()).findEntityById(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userService).save(entity);
    }

    @Test
    void updateUser_whenUsernameProvided_validatesUniqueness_thenUpdatesSaves() {
        UserRequestDTO req = new UserRequestDTO(
                "Abdul", "Azeez", "newUser", "a@x.com", "pw", "077", 2, true
        );

        Users existing = new Users();
        existing.setId(java.util.UUID.randomUUID());

        Role role = new Role();
        role.setId(2);

        Users saved = new Users();
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(userService.findById("id")).thenReturn(existing);
        when(roleService.findEntityById(2)).thenReturn(role);
        when(passwordEncoder.encode("pw")).thenReturn("ENC2");
        when(userService.save(existing)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(dto);

        UserResponseDTO result = facade.updateUser(req, "id");

        assertSame(dto, result);

        verify(userService).findById("id");
        verify(userService).validateUsernameUniqueness("newUser", existing.getId());
        verify(userMapper).updateEntity(req, existing);
        verify(roleService).findEntityById(2);
        verify(passwordEncoder).encode("pw");
        assertSame(role, existing.getRole());
        assertEquals("ENC2", existing.getPassword());
        verify(userService).save(existing);
        verify(userMapper).toDTO(saved);
    }

    @Test
    void updateUser_whenUsernameNull_skipsUniquenessValidation() {
        UserRequestDTO req = new UserRequestDTO(
                "Abdul", "Azeez", null, "a@x.com", null, "077", null, true
        );

        Users existing = new Users();
        existing.setId(java.util.UUID.randomUUID());

        Users saved = new Users();
        UserResponseDTO dto = mock(UserResponseDTO.class);

        when(userService.findById("id")).thenReturn(existing);
        when(userService.save(existing)).thenReturn(saved);
        when(userMapper.toDTO(saved)).thenReturn(dto);

        UserResponseDTO result = facade.updateUser(req, "id");

        assertSame(dto, result);

        verify(userService, never()).validateUsernameUniqueness(anyString(), any());
        verify(userMapper).updateEntity(req, existing);
        verify(passwordEncoder, never()).encode(anyString());
        verify(roleService, never()).findEntityById(anyInt());
    }
}
