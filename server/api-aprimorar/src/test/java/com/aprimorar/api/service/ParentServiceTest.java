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

    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 20, Sort.by("name"));

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
        Page<Parent> parents = new PageImpl<>(List.of(parent), DEFAULT_PAGEABLE, 1);
        when(parentRepo.findAllByActiveTrue(DEFAULT_PAGEABLE)).thenReturn(parents);
        when(parentMapper.toSummaryDto(parent)).thenReturn(parentSummaryDto);

        Page<ParentSummaryDTO> result = parentService.listActiveParents(DEFAULT_PAGEABLE);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertSame(parentSummaryDto, result.getContent().getFirst());

        verify(parentRepo).findAllByActiveTrue(DEFAULT_PAGEABLE);
        verify(parentMapper).toSummaryDto(parent);
        verifyNoMoreInteractions(parentRepo, parentMapper);
    }

    @Test
    @DisplayName("Should return empty page when there are no active parents")
    void testEmptyActiveParentList() {
        Page<Parent> parents = new PageImpl<>(List.of(), DEFAULT_PAGEABLE, 0);
        when(parentRepo.findAllByActiveTrue(DEFAULT_PAGEABLE)).thenReturn(parents);

        Page<ParentSummaryDTO> result = parentService.listActiveParents(DEFAULT_PAGEABLE);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(parentRepo).findAllByActiveTrue(DEFAULT_PAGEABLE);
        verifyNoMoreInteractions(parentRepo);
        verifyNoInteractions(parentMapper);
    }
}
