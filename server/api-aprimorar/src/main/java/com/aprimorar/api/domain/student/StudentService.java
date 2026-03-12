package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.util.UUID;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.parent.command.ParentCommand;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.command.StudentCommand;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

@Service
public class StudentService {

    private final ParentRepository parentRepo;
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final AddressMapper addressMapper;
    private final Clock applicationClock;
    private final ParentMapper parentMapper;

    public StudentService(
            ParentRepository parentRepo,
            StudentRepository studentRepo,
            StudentMapper studentMapper, AddressMapper addressMapper,
            Clock applicationClock, ParentMapper parentMapper
    ) {
        this.parentRepo = parentRepo;
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
        this.addressMapper = addressMapper;
        this.applicationClock = applicationClock;
        this.parentMapper = parentMapper;
    }

    /*
      ------------------------ QUERY METHODS ------------------------
     */

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(Pageable pageable) {

        Page<Student> studentPage = studentRepo.findAll(pageable);
        return studentPage.map(studentMapper::convertToDto);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO findById(UUID studentId) {
        Student student = findStudentByIdOrThrow(studentId);
        return studentMapper.convertToDto(student);
    }

    /*
      ------------------------ COMMAND METHODS ------------------------
     */

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) {
        StudentCommand command = studentMapper.convertToCommand(studentRequestDto);
        Student newStudent = new Student();

        ensureStudentUniqueness(command);
        newStudent.create(command);

        Student savedStudent = studentRepo.save(newStudent);
        return studentMapper.convertToDto(savedStudent);
    }

    @Transactional
    public void archiveStudent(UUID studentId) {
        Student student = findStudentByIdOrThrow(studentId);
        student.archive(applicationClock.instant());
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) {
        Student student = findStudentByIdOrThrow(studentId);
        student.unarchive(applicationClock.instant());
    }

    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, StudentRequestDTO studentRequestDto) {
        ParentEntity parent = parentMapper.convertToCommand(studentRequestDto.parentRequestDTO());
        StudentCommand command = studentMapper.convertToCommand(studentRequestDto);

        //Doesn't have a lot of validation yet, just normalization
        Address address = addressMapper.convertToEntity(studentRequestDto.address());

        Student student = findStudentByIdOrThrow(studentId);

        ensureStudentUniqueness(command);
        student.update(command, parent, address);

        return studentMapper.convertToDto(student);
    }

    /*
      ------------------------ HELPER METHODS ------------------------
     */

    private Student findStudentByIdOrThrow(UUID studentId) {
        return studentRepo.findById(studentId).orElseThrow(StudentNotFoundException::new);
    }

    private Parent findParentCpfIdOrThrow(String cpf) {
                return parentRepo.findByCpf(cpf)
                .orElseThrow(() -> new ParentNotFoundException("Responsável com o CPF informado não existe no banco de dados"));
    }

    private void ensureStudentUniqueness(StudentCommand command) {
        if (studentRepo.existsByCpf(command.cpf())) {
            throw new StudentAlreadyExistException("Aluno com o CPF informado já existe no banco de dados");
        }

        if (studentRepo.existsByEmail(command.email())) {
            throw new StudentAlreadyExistException("Aluno com o Email informado já existe no banco de dados");
        }
    }

    }

    private void validate(){
        if()
    }
}
