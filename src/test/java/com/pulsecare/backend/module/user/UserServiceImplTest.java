package com.pulsecare.backend.module.user;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.user.dto.UserImageProjection;
import com.pulsecare.backend.module.user.dto.UserResponseDTO;
import com.pulsecare.backend.module.user.mapper.UserMapper;
import com.pulsecare.backend.module.user.model.Users;
import com.pulsecare.backend.module.user.repository.UserRepository;
import com.pulsecare.backend.module.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
        import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository repository;
    @Mock private UserMapper mapper;

    @InjectMocks private UserServiceImpl service;

    private UUID userId;
    private Users user;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        user = new Users();
        user.setId(userId);
        user.setUsername("aze");
    }

    @Test
    void findById_whenExists_returnsUser() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        Users result = service.findById(userId.toString());

        assertSame(user, result);
        verify(repository).findById(userId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById_whenNotExists_throwsNotFound() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(userId.toString()));
        verify(repository).findById(userId);
    }

    @Test
    void delete_whenExists_deletesEntity() {
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        service.delete(userId.toString());

        verify(repository).findById(userId);
        verify(repository).delete(user);
    }

    @Test
    void delete_whenNotExists_throwsNotFound() {
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(userId.toString()));
        verify(repository).findById(userId);
        verify(repository, never()).delete(any());
    }

    @Test
    void findByUsername_whenExists_mapsToDto() {
        UserResponseDTO dto = mock(UserResponseDTO.class);
        when(repository.findByUsername("aze")).thenReturn(Optional.of(user));
        when(mapper.toDTO(user)).thenReturn(dto);

        UserResponseDTO result = service.findByUsername("aze");

        assertSame(dto, result);
        verify(repository).findByUsername("aze");
        verify(mapper).toDTO(user);
    }

    @Test
    void findByUsername_whenMissing_throwsNotFound() {
        when(repository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByUsername("missing"));
        verify(repository).findByUsername("missing");
        verifyNoInteractions(mapper);
    }

    @Test
    void isUsernameExist_returnsTrue_whenPresent() {
        when(repository.findByUsername("aze")).thenReturn(Optional.of(user));

        assertTrue(service.isUsernameExist("aze"));
        verify(repository).findByUsername("aze");
    }

    @Test
    void validateUsernameUniqueness_whenOtherUserHasUsername_throwsAlreadyExists() {
        Users other = new Users();
        other.setId(UUID.randomUUID());
        other.setUsername("aze");

        when(repository.findByUsername("aze")).thenReturn(Optional.of(other));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateUsernameUniqueness("aze", userId));

        verify(repository).findByUsername("aze");
    }

    @Test
    void validateUsernameUniqueness_whenSameUserHasUsername_ok() {
        when(repository.findByUsername("aze")).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.validateUsernameUniqueness("aze", userId));
        verify(repository).findByUsername("aze");
    }

    @Test
    void validateUsernameDoesNotExist_whenExists_throwsAlreadyExists() {
        when(repository.findByUsername("aze")).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> service.validateUsernameDoesNotExist("aze"));

        verify(repository).findByUsername("aze");
    }

    @Test
    void validateUsernameDoesNotExist_whenMissing_ok() {
        when(repository.findByUsername("new")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.validateUsernameDoesNotExist("new"));
        verify(repository).findByUsername("new");
    }

    @Test
    void getUserProfileImage_whenValid_returnsProjection() {
        UserImageProjection proj = mock(UserImageProjection.class);
        when(repository.findUserImageById(userId)).thenReturn(Optional.of(proj));
        when(proj.getImageData()).thenReturn(new byte[]{1, 2});
        when(proj.getContentType()).thenReturn("image/png");

        UserImageProjection result = service.getUserProfileImage(userId);

        assertSame(proj, result);
        verify(repository).findUserImageById(userId);
    }

    @Test
    void getUserProfileImage_whenNoImageData_throwsNotFound() {
        UserImageProjection proj = mock(UserImageProjection.class);
        when(repository.findUserImageById(userId)).thenReturn(Optional.of(proj));
        when(proj.getImageData()).thenReturn(new byte[0]);

        assertThrows(ResourceNotFoundException.class, () -> service.getUserProfileImage(userId));

        verify(repository).findUserImageById(userId);
        verify(proj).getImageData();
        verify(proj, never()).getContentType();
    }

    @Test
    void getUserProfileImage_whenBlankContentType_throwsNotFound() {
        UserImageProjection proj = mock(UserImageProjection.class);
        when(repository.findUserImageById(userId)).thenReturn(Optional.of(proj));
        when(proj.getImageData()).thenReturn(new byte[]{1});
        when(proj.getContentType()).thenReturn("  ");

        assertThrows(ResourceNotFoundException.class, () -> service.getUserProfileImage(userId));
        verify(repository).findUserImageById(userId);
    }

    @Test
    void saveUserProfileImage_whenEmpty_throwsIllegalArgument() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.saveUserProfileImage(userId, file));

        verify(file).isEmpty();
        verifyNoInteractions(repository);
    }

    @Test
    void saveUserProfileImage_whenUserMissing_throwsNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.saveUserProfileImage(userId, file));

        verify(repository).findById(userId);
        verify(repository, never()).updateProfileImage(any(), any(), any(), any());
    }

    @Test
    void saveUserProfileImage_whenValid_updatesRepository() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        byte[] bytes = {9, 9, 9};

        when(file.isEmpty()).thenReturn(false);
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(file.getBytes()).thenReturn(bytes);
        when(file.getOriginalFilename()).thenReturn("p.png");
        when(file.getContentType()).thenReturn("image/png");

        service.saveUserProfileImage(userId, file);

        verify(repository).findById(userId);
        verify(repository).updateProfileImage(eq(userId), eq(bytes), eq("p.png"), eq("image/png"));
    }
}
