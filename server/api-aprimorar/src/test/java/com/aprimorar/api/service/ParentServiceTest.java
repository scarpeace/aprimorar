package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    @Mock
    private ParentRepository parentRepo;

    @Mock
    private ParentMapper parentMapper;

    @InjectMocks
    private ParentService parentService;

    private Parent parent;
    private ParentSummaryDTO parentSummaryDto;

    @BeforeEach
    void setUp() {
        parent = new Parent();
        parent.setId(UUID.randomUUID());
        parent.setName("Jane Doe");
        parent.setEmail("jane.doe@email.com");
        parent.setContact("(61)99999-9999");
        parent.setCpf("12345678900");
        parent.setActive(true);

        parentSummaryDto = new ParentSummaryDTO(parent.getId(), parent.getName());
    }

    @Test
    @DisplayName("Should list active parents when success")
    void testListActiveParents() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Parent> parents = new PageImpl<>(List.of(parent), pageable, 1);
        when(parentRepo.findAllByActiveTrue(pageable)).thenReturn(parents);
        when(parentMapper.toSummaryDto(parent)).thenReturn(parentSummaryDto);

        Page<ParentSummaryDTO> result = parentService.listActiveParents(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(parentSummaryDto, result.getContent().getFirst());

        verify(parentRepo).findAllByActiveTrue(pageable);
        verify(parentMapper).toSummaryDto(parent);
        verifyNoMoreInteractions(parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should return empty page when there are no active parents")
    void testEmptyActiveParentList() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));

        Page<Parent> parents = new PageImpl<>(List.of(), pageable, 0);
        when(parentRepo.findAllByActiveTrue(pageable)).thenReturn(parents);

        Page<ParentSummaryDTO> result = parentService.listActiveParents(pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(parentRepo).findAllByActiveTrue(pageable);
        verifyNoMoreInteractions(parentRepo);
        verifyNoInteractions(parentMapper);
    }
}
