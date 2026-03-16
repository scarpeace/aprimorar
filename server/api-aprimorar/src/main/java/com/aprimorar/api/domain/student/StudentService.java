package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.event.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.dto.StudentOptionDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;


@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final EventRepository eventRepo;

    public StudentService(
            ParentRepository parentRepo,
            StudentRepository studentRepo,
            StudentMapper studentMapper,
            EventRepository eventRepo) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.eventRepo = eventRepo;
    }

    /* ----- Query Methods ----- */

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        Specification<Student> spec = Specification.allOf(  
                StudentSpecifications.isNotGhost(),
                StudentSpecifications.notArchived()
        );

        Page<Student> page = studentRepo.findAll(spec, pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(studentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<StudentOptionDTO> getStudentOptions() {
        Specification<Student> spec = Specification.allOf(
                StudentSpecifications.isNotGhost(),
                StudentSpecifications.notArchived()
        );
        
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return studentRepo.findAll(spec, sort).stream()
                .map(s -> new StudentOptionDTO(s.getId(), s.getName()))
                .toList();
    }

    public List<StudentResponseDTO> getStudentsByParent(UUID parentId) {
        List<Student> studentParentsList = studentRepo.findAllByParentId(parentId);
        return studentParentsList.stream().map(studentMapper::convertToDto).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {

        Student student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return studentMapper.convertToDto(student);
    }

    /* ----- Command Methods ----- */

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) {

        Student student = studentMapper.convertToEntity(studentRequestDto);
        student.setParent(resolveParentOrThrow(studentRequestDto.parentId()));
        StudentRules.validate(student);
        ensureStudentUniqueness(student);
        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO request) {

        if (GHOST_STUDENT_ID.equals(id)) {
            throw new IllegalArgumentException("Não é possível modificar o registro de sistema 'ALUNO ARQUIVADO'.");
        }

        Student updatedData = studentMapper.convertToEntity(request);
        Student existingStudent = findStudentOrThrow(id);

        ensureStudentUniquenessForUpdate(updatedData, id);

        existingStudent.setName(updatedData.getName());
        existingStudent.setBirthdate(updatedData.getBirthdate());
        existingStudent.setCpf(updatedData.getCpf());
        existingStudent.setSchool(updatedData.getSchool());
        existingStudent.setContact(updatedData.getContact());
        existingStudent.setEmail(updatedData.getEmail());
        existingStudent.setAddress(updatedData.getAddress());
        existingStudent.setParent(resolveParentOrThrow(request.parentId()));

        StudentRules.validate(existingStudent);

        log.info("Aluno {} atualizado com sucesso.", existingStudent.getName().toUpperCase());
        return studentMapper.convertToDto(existingStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível arquivar o registro de sistema 'ALUNO ARQUIVADO'.");
        }
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(Instant.now());
        log.info("Aluno {} arquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("O registro 'ALUNO ARQUIVADO' não pode ser desarquivado.");
        }
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(null);
        log.info("Aluno {} desarquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        if (GHOST_STUDENT_ID.equals(studentId)) {
            throw new IllegalArgumentException("Não é possível deletar o registro de sistema 'ALUNO ARQUIVADO'.");
        }

        Student student = findStudentOrThrow(studentId);
        
        // Reatribui eventos para o aluno fantasma antes de deletar
        log.info("Reatribuindo eventos do aluno {} para o Ghost Student.", student.getName().toUpperCase());
        eventRepo.reassignEventsToGhost(studentId, GHOST_STUDENT_ID);

        studentRepo.delete(student);
        log.info("Aluno {} deletado com sucesso.", student.getName().toUpperCase());
    }

    /* ----- Helper Methods ----- */

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado no banco de dados"));
    }

    private void ensureStudentUniqueness(Student student) {
        if (studentRepo.existsByCpf(student.getCpf())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmail(student.getEmail())) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

    private void ensureStudentUniquenessForUpdate(Student student, UUID studentId) {
        if (studentRepo.existsByCpfAndIdNot(student.getCpf(), studentId)) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmailAndIdNot(student.getEmail(), studentId)) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

    private Parent resolveParentOrThrow(UUID parentId) {
        return parentRepo.findById(parentId)
                .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
    }

}
