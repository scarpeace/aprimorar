package com.aprimorar.api.domain.registration.student.api;

import com.aprimorar.api.domain.registration.student.api.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentRequestDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentResponseDTO;
import com.aprimorar.api.domain.registration.student.api.dto.StudentSummaryDTO;
import com.aprimorar.api.shared.PageDTO;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentSummaryDTO getSummary(UUID studentId, Instant startDate, Instant endDate);

    StudentResponseDTO findById(UUID studentId);

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);

    List<StudentOptionsDTO> getStudentOptions();

//    PageDTO<StudentResponseDTO> getStudentsByParent(UUID parentId, Pageable pageable);

    StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id);

    void archiveStudent(UUID studentId);

    void unarchiveStudent(UUID studentId);

    void deleteStudent(UUID studentId);
}
