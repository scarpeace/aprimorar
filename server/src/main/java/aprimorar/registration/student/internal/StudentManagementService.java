package aprimorar.registration.student.internal;

import aprimorar.registration.student.api.dto.StudentCountSummaryDTO;
import aprimorar.registration.student.api.dto.StudentOptionsDTO;
import aprimorar.registration.student.api.dto.StudentRequestDTO;
import aprimorar.registration.student.api.dto.StudentResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface StudentManagementService {

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);

    StudentCountSummaryDTO getSummary();

    List<StudentOptionsDTO> getStudentOptions();

    List<StudentResponseDTO> getStudentsByParent(UUID parentId);

    StudentResponseDTO findById(UUID studentId);

    StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id);

    void archiveStudent(UUID studentId);

    void unarchiveStudent(UUID studentId);

    void deleteStudent(UUID studentId);
}
