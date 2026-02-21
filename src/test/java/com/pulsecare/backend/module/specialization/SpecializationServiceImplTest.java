package com.pulsecare.backend.module.specialization;

import com.pulsecare.backend.common.exception.ResourceAlreadyExistsException;
import com.pulsecare.backend.common.exception.ResourceNotFoundException;
import com.pulsecare.backend.module.specialization.model.Specialization;
import com.pulsecare.backend.module.specialization.repository.SpecializationRepository;
import com.pulsecare.backend.module.specialization.service.SpecializationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceImplTest {

    @Mock private SpecializationRepository repository;

    @InjectMocks private SpecializationServiceImpl service;

    @Test
    void findById_whenExists_returnsEntity() {
        Specialization s = new Specialization();
        s.setId(1);
        s.setName("Cardiology");

        when(repository.findById(1)).thenReturn(Optional.of(s));

        Specialization out = service.findById(1);

        assertSame(s, out);
        verify(repository).findById(1);
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(repository.findById(9)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(9));

        verify(repository).findById(9);
    }

    @Test
    void findAll_returnsList() {
        Specialization s1 = new Specialization();
        Specialization s2 = new Specialization();

        when(repository.findAll()).thenReturn(List.of(s1, s2));

        List<Specialization> out = service.findAll();

        assertEquals(2, out.size());
        assertSame(s1, out.get(0));
        assertSame(s2, out.get(1));
        verify(repository).findAll();
    }

    @Test
    void save_whenNameNotExists_saves() {
        Specialization s = new Specialization();
        s.setName("Neurology");

        when(repository.findByName("Neurology")).thenReturn(Optional.empty());
        when(repository.save(s)).thenReturn(s);

        Specialization out = service.save(s);

        assertSame(s, out);
        verify(repository).findByName("Neurology");
        verify(repository).save(s);
    }

    @Test
    void save_whenNameExists_throwsAlreadyExists() {
        Specialization s = new Specialization();
        s.setName("Neurology");

        when(repository.findByName("Neurology")).thenReturn(Optional.of(new Specialization()));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.save(s));

        verify(repository).findByName("Neurology");
        verify(repository, never()).save(any());
    }

    @Test
    void update_whenMissing_throwsNotFound() {
        Specialization incoming = new Specialization();
        incoming.setName("Updated");

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1, incoming));

        verify(repository).findById(1);
        verify(repository, never()).save(any());
    }

    @Test
    void update_whenExists_updatesName_andSaves() {
        Specialization existing = new Specialization();
        existing.setId(1);
        existing.setName("Old");

        Specialization incoming = new Specialization();
        incoming.setName("New");

        when(repository.findById(1)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Specialization out = service.update(1, incoming);

        assertSame(existing, out);
        assertEquals("New", existing.getName());
        verify(repository).findById(1);
        verify(repository).save(existing);
    }

    @Test
    void delete_whenMissing_throwsNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1));

        verify(repository).findById(1);
        verify(repository, never()).delete(any());
    }

    @Test
    void delete_whenExists_deletes() {
        Specialization existing = new Specialization();
        existing.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        service.delete(1);

        verify(repository).findById(1);
        verify(repository).delete(existing);
    }

    @Test
    void findAllById_whenAllFound_returnsSet() {
        Specialization s1 = new Specialization();
        s1.setId(1);
        Specialization s2 = new Specialization();
        s2.setId(2);

        when(repository.findAllById(Set.of(1, 2))).thenReturn(List.of(s1, s2));

        Set<Specialization> out = service.findAllById(Set.of(1, 2));

        assertEquals(2, out.size());
        assertTrue(out.contains(s1));
        assertTrue(out.contains(s2));
        verify(repository).findAllById(Set.of(1, 2));
    }

    @Test
    void findAllById_whenSomeMissing_throwsNotFound() {
        Specialization s1 = new Specialization();
        s1.setId(1);

        when(repository.findAllById(Set.of(1, 2))).thenReturn(List.of(s1));

        assertThrows(ResourceNotFoundException.class, () -> service.findAllById(Set.of(1, 2)));

        verify(repository).findAllById(Set.of(1, 2));
    }
}