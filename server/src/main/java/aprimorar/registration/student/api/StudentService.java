package aprimorar.registration.student.api;

import aprimorar.registration.student.api.dto.StudentOptionsDTO;
import aprimorar.registration.student.api.dto.StudentRequestDTO;
import aprimorar.registration.student.api.dto.StudentResponseDTO;
import aprimorar.registration.student.api.dto.StudentCountSummaryDTO;
import aprimorar.shared.PageDTO;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    StudentResponseDTO findById(UUID studentId);

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);

    StudentCountSummaryDTO getSummary();

     List<StudentResponseDTO> getStudentsByParent(UUID parentId);

    List<StudentOptionsDTO> getStudentOptions();

    StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id);

    void archiveStudent(UUID studentId);

    void unarchiveStudent(UUID studentId);

    void deleteStudent(UUID studentId);
}
