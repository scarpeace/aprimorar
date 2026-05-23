package aprimorar.registration.student.api;

import aprimorar.registration.student.api.dto.StudentResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import aprimorar.shared.PageDTO;

public interface StudentService {

    StudentResponseDTO findById(UUID studentId);

    PageDTO<StudentResponseDTO> getStudents(Pageable pageable, String search, Boolean archived);
}
