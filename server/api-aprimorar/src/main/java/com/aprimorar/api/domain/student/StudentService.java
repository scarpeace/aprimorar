package com.aprimorar.api.domain.student;

import java.time.Clock;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.address.AddressEntity;
import com.aprimorar.api.domain.address.AddressMapper;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.parent.ParentMapper;
import com.aprimorar.api.domain.parent.ParentRepository;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentServiceBusinessException;
import com.aprimorar.api.shared.MapperUtils;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;
    private final StudentMapper studentMapper;
    private final AddressMapper addressMapper;
    private final ParentMapper parentMapper;
    private final MapperUtils mapperUtils;
    private final Clock applicationClock;

    public StudentService(StudentRepository studentRepo, ParentRepository parentRepo, StudentMapper studentMapper, ParentMapper parentMapper, Clock applicationClock) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.studentMapper = studentMapper;
        this.addressMapper = new AddressMapper();
        this.parentMapper = parentMapper;
        this.mapperUtils = null;
        this.applicationClock = applicationClock;
    }

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDto) throws StudentServiceBusinessException {
        log.info("StudentService:createStudent execucao iniciada");
        StudentResponseDTO responseDto;

        try {
            log.debug("StudentService:createStudent dados recebidos {}", mapperUtils.jsonAsString(studentRequestDto));
            StudentEntity student = studentMapper.convertToEntity(studentRequestDto);

            //TODO: Investigar melhor essa necessidade das anotações @NotNull
            StudentEntity savedStudent = studentRepo.save(student);

            responseDto = studentMapper.convertToDto(savedStudent);
            log.debug("StudentService:createStudent estudante criado com sucesso {}", mapperUtils.jsonAsString(responseDto));

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao salvar o estudante no banco de dados. Mensagem: {}", ex.getMessage());
            throw new StudentServiceBusinessException("Ocorreu um erro ao criar o estudante");
        }

        log.info("StudentService:createStudent execucao finalizada");
        return responseDto;

    }

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> getStudents(PageRequest pr) {
        Page<StudentResponseDTO> responseDto;
        log.info("StudentService:getStudents execucao iniciada");

        try {
            Page<StudentEntity> studentPage = studentRepo.findAll(pr);

            responseDto = studentPage.map(studentMapper::convertToDto);
            log.info("StudentService:getStudents resumo da consulta: totalPaginas={}, totalElementos={}", responseDto.getTotalPages(), responseDto.getTotalElements());

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os estudantes no banco de dados. Mensagem: {}", ex.getMessage());
            throw new StudentServiceBusinessException("Ocorreu um erro ao buscar os estudantes no banco de dados: " + ex.getMessage());
        }

        log.info("StudentService:getStudents execucao finalizada");
        return responseDto;
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getById(UUID studentId) throws StudentServiceBusinessException {
        StudentResponseDTO responseDto;
        log.info("StudentService:getStudentById execucao iniciada");

        try {
            StudentEntity student = findStudentOrThrow(studentId);
            responseDto = studentMapper.convertToDto(student);
            log.debug("StudentService:getStudentById estudante encontrado para o id {} {}", studentId, mapperUtils.jsonAsString(responseDto));

            log.info("StudentService:getStudentById execucao finalizada");
            return responseDto;

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar o estudante {} no banco de dados. Mensagem: {}", studentId, ex.getMessage());
            throw new StudentServiceBusinessException("Ocorreu um erro ao buscar o estudante no banco de dados: " + ex.getMessage());
        }
    }

    @Transactional
    public void archiveStudent(UUID studentId) throws StudentServiceBusinessException {
        log.info("StudentService:archiveStudent execucao iniciada");

        try {
            log.debug("StudentService:archiveStudent buscando estudante no banco de dados pelo id {}", studentId);
            StudentEntity student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException(studentId));

            student.setArchivedAt(applicationClock.instant());
            log.info("StudentService:archiveStudent estudante arquivado com sucesso. id={}", student.getId());

        } catch (StudentNotFoundException ex) {
            log.info("StudentService:archiveStudent estudante nao encontrado. id={}", studentId);
            throw ex;
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao arquivar o estudante {} no banco de dados. Mensagem: {}", studentId, ex.getMessage(), ex);
            throw new StudentServiceBusinessException("Ocorreu um erro ao arquivar o estudante no banco de dados");
        }
    }

    @Transactional
    public void unarchiveStudent(UUID studentId) throws StudentServiceBusinessException {
        log.info("StudentService:unarchiveStudent execucao iniciada");
        try {
            log.debug("StudentService:unarchiveStudent buscando estudante no banco de dados pelo id {}", studentId);
            StudentEntity student = findStudentOrThrow(studentId);
            student.setArchivedAt(null);
            log.info("StudentService:unarchiveStudent estudante desarquivado com sucesso. id={}", student.getId());

        } catch (StudentNotFoundException ex) {
            log.info("StudentService:unarchiveStudent estudante nao encontrado. id={}", studentId);
            throw ex;
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao desarquivar o estudante {} no banco de dados. Mensagem: {}", studentId, ex.getMessage(), ex);
            throw new StudentServiceBusinessException("Ocorreu um erro ao desarquivar o estudante no banco de dados");

        }
    }

    //TODO: Precisa testar isso aqui pra ver se tá funcionando.
    @Transactional
    public StudentResponseDTO updateStudent(UUID studentId, UpdateStudentDTO updateStudentDto) throws StudentServiceBusinessException {
        StudentResponseDTO responseDto;
        log.info("StudentService:updateStudent execucao iniciada");

        try {
            StudentEntity foundStudent = findStudentOrThrow(studentId);

            log.info("StudentService:updateStudent atualizando estudando com responsável recebido: {}", updateStudentDto.parent());
            Parent newParent = parentMapper.convertToEntity(updateStudentDto.parent());
            foundStudent.setParent(newParent);
            log.info("StudentService:updateStudent responsável mapeado e associado ao aluno: {}", newParent.getId());

            //TODO mudar todas as ocorrências de AdressEntity para Address
            AddressEntity newAddress = addressMapper.convertToEntity(updateStudentDto.address());

            foundStudent.setName(updateStudentDto.name());
            foundStudent.setContact(updateStudentDto.contact());
            foundStudent.setEmail(updateStudentDto.email());
            foundStudent.setCpf(updateStudentDto.cpf());
            foundStudent.setBirthdate(updateStudentDto.birthdate());
            foundStudent.setSchool(updateStudentDto.school());
            foundStudent.setAddress(newAddress);

            responseDto = studentMapper.convertToDto(foundStudent);
            log.debug("StudentService:updateStudent estudante atualizado. id={}, dados={}", studentId, mapperUtils.jsonAsString(responseDto));

            log.info("StudentService:updateStudent execucao finalizada");

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao atualizar o estudante {} no banco de dados. Mensagem: {}", studentId, ex.getMessage(), ex);
            throw new StudentServiceBusinessException("Ocorreu um erro ao atualizar o estudante no banco de dados");
        }
        return responseDto;
    }

    private StudentEntity findStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }
}
