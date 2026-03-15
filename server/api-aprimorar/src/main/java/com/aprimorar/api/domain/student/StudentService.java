package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.parent.ParentService;
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

/**
 * Centraliza as regras de negócio do aluno.
 *
 * <p>
 * Aqui ficam a criação, atualização, consultas e ações de arquivar/desarquivar.
 * Também é esse service que garante que CPF e email não se repitam e que o
 * aluno
 * sempre fique associado a um responsável válido.
 *
 * <p>
 * Quando o responsável vem no request, o service decide se ele já existe e deve
 * ser reaproveitado ou se precisa ser persistido antes de associar ao aluno.
 *
 * @author scarpellini
 * @version 1.0
 * @since 2026-03-14
 */
@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final ParentService parentService;

    public StudentService(
            ParentRepository parentRepo,
            StudentRepository studentRepo,
            StudentMapper studentMapper,
            ParentService parentService) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.parentService = null;
    }

    /* ----- Query Methods ----- */

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        Page<Student> page = studentRepo.findAll(pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", page.getTotalElements());
        return page.map(studentMapper::convertToDto);
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
        StudentRules.validate(student);
        student.setParent(resolveParentAssociation(student.getParent()));
        ensureStudentUniqueness(student);
        Student savedStudent = studentRepo.save(student);

        log.info("Aluno {} cadastrado com sucesso.", savedStudent.getName().toUpperCase());
        return studentMapper.convertToDto(savedStudent);
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO request) {

        Student updatedData = studentMapper.convertToEntity(request);
        Student existingStudent = findStudentOrThrow(id);

        StudentRules.validate(updatedData);
        ensureStudentUniquenessForUpdate(updatedData, id);

        existingStudent.setName(updatedData.getName());
        existingStudent.setBirthdate(updatedData.getBirthdate());
        existingStudent.setCpf(updatedData.getCpf());
        existingStudent.setSchool(updatedData.getSchool());
        existingStudent.setContact(updatedData.getContact());
        existingStudent.setEmail(updatedData.getEmail());
        existingStudent.setAddress(updatedData.getAddress());
        existingStudent.setParent(resolveParentAssociation(updatedData.getParent()));

        log.info("Aluno {} atualizado com sucesso.", existingStudent.getName().toUpperCase());
        return studentMapper.convertToDto(existingStudent);
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

    private Parent resolveParentAssociation(Parent parent) {
        if (parent.getId() != null) {
            return parentRepo.findById(parent.getId())
                    .orElseThrow(() -> new ParentNotFoundException("Responsável não encontrado no banco de dados"));
        }

        return parentRepo.findByCpf(parent.getCpf())
                .orElseGet(() -> {
                    if (parentRepo.existsByEmail(parent.getEmail())) {
                        throw new ParentAlreadyExistsException(
                                "Responsável com o Email informado já existe no banco de dados");
                    }

                    return parentRepo.save(parent);
                });
    }

}
