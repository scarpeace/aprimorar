package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.util.UUID;

import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    public StudentService(
            ParentRepository parentRepo,
            StudentRepository studentRepo,
            StudentMapper studentMapper
    ) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        Page<Student> page = studentRepo.findAll(pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(studentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {

        Student student = findStudentOrThrow(studentId);
        log.info("Aluno {} consultado com sucesso.", student.getName().toUpperCase());
        return studentMapper.convertToDto(student);
    }

    /*
      ------------------------ INSERT METHODS ------------------------
     */

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) {

        Student student = studentMapper.convertToEntity(studentRequestDto);

        StudentRules.validate(student);
        ensureStudentUniqueness(student);
        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO request) {

        Student student = studentMapper.convertToEntity(request);

        findStudentOrThrow(id);
        ensureParentExists(request.parent());
        Student updatedStudent = studentRepo.save(student);

        log.info("Aluno {} atualizado com sucesso.", updatedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(updatedStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(Instant.now());
        log.info("Aluno {} arquivado com sucesso.", student.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        student.setArchivedAt(null);
        log.info("Aluno {} desarquivado com sucesso.", student.getName().toUpperCase());
    }


    @Transactional
    public void deleteStudent(UUID studentId) {
        Student student = findStudentOrThrow(studentId);
        studentRepo.delete(student);
        log.info("Aluno {} deletado com sucesso.", student.getName().toUpperCase());
    }
    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(()-> new StudentNotFoundException("Aluno não encontrado no banco de dados"));
    }

    private void ensureParentExists(Parent parent) {
            parentRepo.findByCpf(parent.getCpf())
                .orElseThrow(() -> new ParentNotFoundException("Responsável com o CPF informado não existe no banco de dados"));
    }

    private void ensureStudentUniqueness(Student student) {
        if (studentRepo.existsByCpf(student.getCpf())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmail(student.getEmail())) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

}
