package com.pulsecare.backend.module.role;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.role.dto.RoleReqDto;
import com.pulsecare.backend.module.role.dto.RoleResDto;
import com.pulsecare.backend.module.role.mapper.RoleMapper;
import com.pulsecare.backend.module.role.model.Role;
import com.pulsecare.backend.module.role.repository.RoleRepository;
import com.pulsecare.backend.module.role.service.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock private RoleRepository repository;
    @Mock private RoleMapper mapper;

    @InjectMocks private RoleServiceImpl service;

    @Test
    void findById_whenExists_returnsDto() {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        RoleResDto dto = mock(RoleResDto.class);

        when(repository.findById(1)).thenReturn(Optional.of(role));
        when(mapper.toDTO(role)).thenReturn(dto);

        RoleResDto out = service.findById(1);

        assertSame(dto, out);
        verify(repository).findById(1);
        verify(mapper).toDTO(role);
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(9)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(9));

        verify(repository).findById(9);
        verifyNoInteractions(mapper);
    }

    @Test
    void findEntityById_whenExists_returnsEntity() {
        Role role = new Role();
        role.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(role));

        Role out = service.findEntityById(1);

        assertSame(role, out);
        verify(repository).findById(1);
        verifyNoInteractions(mapper);
    }

    @Test
    void findEntityById_whenMissing_throwsNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findEntityById(1));

        verify(repository).findById(1);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_mapsToDtoList() {
        Role r1 = new Role();
        r1.setId(1);
        Role r2 = new Role();
        r2.setId(2);

        RoleResDto d1 = mock(RoleResDto.class);
        RoleResDto d2 = mock(RoleResDto.class);

        when(repository.findAll()).thenReturn(List.of(r1, r2));
        when(mapper.toDTO(r1)).thenReturn(d1);
        when(mapper.toDTO(r2)).thenReturn(d2);

        List<RoleResDto> out = service.findAll();

        assertEquals(2, out.size());
        assertSame(d1, out.get(0));
        assertSame(d2, out.get(1));
        verify(repository).findAll();
        verify(mapper).toDTO(r1);
        verify(mapper).toDTO(r2);
    }

    @Test
    void save_whenNameNotExists_savesAndReturnsDto() {
        RoleReqDto req = new RoleReqDto("DOCTOR");

        Role entity = new Role();
        entity.setName("DOCTOR");

        Role saved = new Role();
        saved.setId(5);
        saved.setName("DOCTOR");

        RoleResDto outDto = mock(RoleResDto.class);

        when(repository.findByName("DOCTOR")).thenReturn(Optional.empty());
        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(outDto);

        RoleResDto out = service.save(req);

        assertSame(outDto, out);
        verify(repository).findByName("DOCTOR");
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toDTO(saved);
    }

    @Test
    void save_whenNameExists_throwsAlreadyExists() {
        RoleReqDto req = new RoleReqDto("DOCTOR");

        when(repository.findByName("DOCTOR")).thenReturn(Optional.of(new Role()));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(req));

        verify(repository).findByName("DOCTOR");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    void update_whenMissing_throwsNotFound() {
        RoleReqDto req = new RoleReqDto("NEW");

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1, req));

        verify(repository).findById(1);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void update_whenExists_updatesName_saves_returnsDto() {
        Role existing = new Role();
        existing.setId(1);
        existing.setName("OLD");

        Role updated = existing;

        RoleResDto dto = mock(RoleResDto.class);

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(dto);

        RoleResDto out = service.update(1, new RoleReqDto("NEW"));

        assertSame(dto, out);
        assertEquals("NEW", existing.getName());
        verify(repository).findById(1);
        verify(repository).save(existing);
        verify(mapper).toDTO(updated);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1));

        verify(repository).findById(1);
        verify(repository, never()).delete(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void delete_whenExists_deletes() {
        Role existing = new Role();
        existing.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        service.delete(1);

        verify(repository).findById(1);
        verify(repository).delete(existing);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllById_whenAllFound_returnsSet() {
        Role r1 = new Role();
        r1.setId(1);
        Role r2 = new Role();
        r2.setId(2);

        when(repository.findAllById(Set.of(1, 2))).thenReturn(List.of(r1, r2));

        var out = service.findAllById(Set.of(1, 2));

        assertEquals(2, out.size());
        assertTrue(out.contains(r1));
        assertTrue(out.contains(r2));
        verify(repository).findAllById(Set.of(1, 2));
    }

    @Test
    void findAllById_whenSomeMissing_throwsNotFound() {
        Role r1 = new Role();
        r1.setId(1);

        when(repository.findAllById(Set.of(1, 2))).thenReturn(List.of(r1));

        assertThrows(ResourceNotFoundException.class, () -> service.findAllById(Set.of(1, 2)));

        verify(repository).findAllById(Set.of(1, 2));
    }
}