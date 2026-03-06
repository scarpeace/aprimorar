package com.aprimorar.api.controller;

import com.aprimorar.api.exception.handler.GlobalExceptionHandler;
import com.aprimorar.api.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        StudentController controller = new StudentController(studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should list students with includeArchived=false by default")
    void listStudentsShouldUseDefaultIncludeArchivedFalse() throws Exception {
        when(studentService.listStudents(any(Pageable.class), isNull(), eq(false)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

        mockMvc.perform(get("/v1/students"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(studentService).listStudents(pageableCaptor.capture(), isNull(), eq(false));

        Pageable pageable = pageableCaptor.getValue();
        Sort.Order sortOrder = pageable.getSort().getOrderFor("name");
        org.junit.jupiter.api.Assertions.assertEquals(0, pageable.getPageNumber());
        org.junit.jupiter.api.Assertions.assertEquals(20, pageable.getPageSize());
        org.junit.jupiter.api.Assertions.assertNotNull(sortOrder);
    }

    @Test
    @DisplayName("Should list students with includeArchived=true when requested")
    void listStudentsShouldUseIncludeArchivedTrueWhenRequested() throws Exception {
        when(studentService.listStudents(any(Pageable.class), eq("John"), eq(true)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 20), 0));

        mockMvc.perform(get("/v1/students")
                        .param("name", "John")
                        .param("includeArchived", "true"))
                .andExpect(status().isOk());

        verify(studentService).listStudents(any(Pageable.class), eq("John"), eq(true));
    }

    @Test
    @DisplayName("Should return 400 for invalid sortBy")
    void listStudentsShouldReturnBadRequestForInvalidSortBy() throws Exception {
        mockMvc.perform(get("/v1/students").param("sortBy", "archivedAt"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    @DisplayName("Should return 400 for negative page")
    void listStudentsShouldReturnBadRequestForNegativePage() throws Exception {
        mockMvc.perform(get("/v1/students").param("page", "-1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    @DisplayName("Should return 400 for size less than 1")
    void listStudentsShouldReturnBadRequestForInvalidSize() throws Exception {
        mockMvc.perform(get("/v1/students").param("size", "0"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(studentService);
    }

    @Test
    @DisplayName("Should return 204 and delegate DELETE to archive alias")
    void deleteStudentShouldDelegateToArchiveAlias() throws Exception {
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(delete("/v1/students/{studentId}", studentId))
                .andExpect(status().isNoContent());

        verify(studentService).archiveStudent(studentId);
    }

    @Test
    @DisplayName("Should return 204 when archiving and unarchiving student")
    void archiveAndUnarchiveStudentShouldReturnNoContent() throws Exception {
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(patch("/v1/students/{studentId}/archive", studentId))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/v1/students/{studentId}/unarchive", studentId))
                .andExpect(status().isNoContent());

        verify(studentService).archiveStudent(studentId);
        verify(studentService).unarchiveStudent(studentId);
    }

    @Test
    @DisplayName("Should return 204 on repeated archive and unarchive calls")
    void repeatedArchiveAndUnarchiveShouldReturnNoContent() throws Exception {
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(patch("/v1/students/{studentId}/archive", studentId))
                .andExpect(status().isNoContent());
        mockMvc.perform(patch("/v1/students/{studentId}/archive", studentId))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/v1/students/{studentId}/unarchive", studentId))
                .andExpect(status().isNoContent());
        mockMvc.perform(patch("/v1/students/{studentId}/unarchive", studentId))
                .andExpect(status().isNoContent());

        verify(studentService, times(2)).archiveStudent(studentId);
        verify(studentService, times(2)).unarchiveStudent(studentId);
    }
}
