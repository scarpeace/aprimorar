package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.util.UUID;

import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

@Service
public class StudentService {


    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    private final Clock applicationClock;

    public StudentService(StudentRepository studentRepo, StudentMapper studentMapper, Clock applicationClock) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.applicationClock = applicationClock;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        Page<StudentEntity> studentPage = studentRepo.findAll(pageable);
        return studentPage.map(studentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {

        StudentEntity student = findStudentOrThrow(studentId);
        return studentMapper.convertToDto(student);
    }

    /*
      ------------------------ INSERT METHODS ------------------------
     */

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) {

        if(studentRepo.existsByCpf(studentRequestDto.cpf())){
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if(studentRepo.existsByEmail(studentRequestDto.email())){
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }

        StudentEntity student = studentMapper.convertToEntity(studentRequestDto);
        StudentEntity savedStudent = studentRepo.save(student);

        return studentMapper.convertToDto(savedStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {

        StudentEntity student = findStudentOrThrow(studentId);
        student.archive();
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {

        StudentEntity student = findStudentOrThrow(studentId);
        student.unarchive();
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) {

        StudentEntity foundStudent = findStudentOrThrow(studentId);
        StudentEntity newStudent = studentMapper.updateToEntity(updateStudentDto);

        foundStudent.setName(newStudent.getName());
        foundStudent.setContact(newStudent.getContact());
        foundStudent.setEmail(newStudent.getEmail());
        foundStudent.setCpf(newStudent.getCpf());
        foundStudent.setBirthdate(newStudent.getBirthdate());
        foundStudent.setSchool(newStudent.getSchool());
        foundStudent.updateAddress(newStudent.getAddressEntity());
        foundStudent.setParentEntity(newStudent.getParentEntity());

        return studentMapper.convertToDto(foundStudent);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private StudentEntity findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId).orElseThrow(StudentNotFoundException::new);
    }
}
