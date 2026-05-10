package aprimorar.registration.student.api;

import aprimorar.registration.student.api.dto.StudentOptionsDTO;
import aprimorar.registration.student.api.dto.StudentRequestDTO;
import aprimorar.registration.student.api.dto.StudentResponseDTO;
import aprimorar.shared.PageDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    // StudentSummaryDTO getSummary(UUID studentId, Instant startDate, Instant endDate);

    StudentResponseDTO findById(UUID studentId);

//    Map<UUID, StudentResponseDTO> findByIds(Collection<UUID> studentIds);

//    boolean existsById(UUID id);

//    List<UUID> findIdsByNameContaining(String name);

//    boolean hasActiveLinkedStudents(UUID parentId);

    StudentResponseDTO createStudent(StudentRequestDTO dto);

    PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);
//
//    PageDTO<StudentResponseDTO> getStudentsByParent(UUID parentId, Pageable pageable);

    List<StudentOptionsDTO> getStudentOptions();

    StudentResponseDTO updateStudent(StudentRequestDTO dto, UUID id);

    void archiveStudent(UUID studentId);

    void unarchiveStudent(UUID studentId);

    void deleteStudent(UUID studentId);
}
