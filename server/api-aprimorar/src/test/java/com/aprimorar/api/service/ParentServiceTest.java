package com.aprimorar.api.service;

import com.aprimorar.api.dto.parent.ParentSummaryDTO;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.mapper.ParentMapper;
import com.aprimorar.api.repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        parent.setArchivedAt(null);

        parentSummaryDto = new ParentSummaryDTO(parent.getId(), parent.getName());
    }

    @Nested
    @DisplayName("listParents")
    class ListParents {

        @Test
        @DisplayName("returns active parents when includeArchived is false")
        void listActiveParentsSuccess() {
            Page<Parent> parents = new PageImpl<>(List.of(parent), DEFAULT_PAGEABLE, 1);
            when(parentRepo.findAllByArchivedAtIsNull(DEFAULT_PAGEABLE)).thenReturn(parents);
            when(parentMapper.toSummaryDto(parent)).thenReturn(parentSummaryDto);

            Page<ParentSummaryDTO> result = parentService.listParents(DEFAULT_PAGEABLE, false);

            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getContent().size());
            assertSame(parentSummaryDto, result.getContent().getFirst());
            verify(parentRepo).findAllByArchivedAtIsNull(DEFAULT_PAGEABLE);
            verify(parentMapper).toSummaryDto(parent);
            verifyNoMoreInteractions(parentRepo, parentMapper);
        }

        @Test
        @DisplayName("returns empty page when no active parents exist")
        void listActiveParentsEmpty() {
            Page<Parent> parents = new PageImpl<>(List.of(), DEFAULT_PAGEABLE, 0);
            when(parentRepo.findAllByArchivedAtIsNull(DEFAULT_PAGEABLE)).thenReturn(parents);

            Page<ParentSummaryDTO> result = parentService.listParents(DEFAULT_PAGEABLE, false);

            assertEquals(0, result.getTotalElements());
            assertTrue(result.getContent().isEmpty());
            verify(parentRepo).findAllByArchivedAtIsNull(DEFAULT_PAGEABLE);
            verifyNoMoreInteractions(parentRepo);
            verifyNoInteractions(parentMapper);
        }

        @Test
        @DisplayName("returns all parents when includeArchived is true")
        void listAllParentsIncludingArchived() {
            Parent archivedParent = new Parent();
            archivedParent.setId(UUID.randomUUID());
            archivedParent.setName("Archived Parent");
            archivedParent.setArchivedAt(java.time.Instant.parse("2026-03-08T10:00:00Z"));

            Page<Parent> parents = new PageImpl<>(List.of(parent, archivedParent), DEFAULT_PAGEABLE, 2);
            when(parentRepo.findAll(DEFAULT_PAGEABLE)).thenReturn(parents);
            when(parentMapper.toSummaryDto(parent)).thenReturn(parentSummaryDto);
            when(parentMapper.toSummaryDto(archivedParent)).thenReturn(new ParentSummaryDTO(archivedParent.getId(), archivedParent.getName()));

            Page<ParentSummaryDTO> result = parentService.listParents(DEFAULT_PAGEABLE, true);

            assertEquals(2, result.getTotalElements());
            verify(parentRepo).findAll(DEFAULT_PAGEABLE);
            verify(parentMapper).toSummaryDto(parent);
            verify(parentMapper).toSummaryDto(archivedParent);
            verifyNoMoreInteractions(parentRepo, parentMapper);
        }
    }
}
