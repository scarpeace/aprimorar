package com.aprimorar.api.domain.employee;

import java.time.Clock;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.dto.UpdateEmployeeDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;
import com.aprimorar.api.shared.MapperUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final Clock applicationClock;
    private final EmployeeMapper employeeMapper;
    private final MapperUtils mapperUtils;

    public EmployeeService(
            EmployeeRepository employeeRepo,
            Clock applicationClock,
            EmployeeMapper employeeMapper,
            MapperUtils mapperUtils
    ) {
        this.employeeRepo = employeeRepo;
        this.applicationClock = applicationClock;
        this.employeeMapper = employeeMapper;
        this.mapperUtils = mapperUtils;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDto) throws EmployeeServiceBusinessException {
        log.info("EmployeeService:createEmployee execucao iniciada");
        EmployeeResponseDTO responseDto;

        try {
            log.debug("EmployeeService:createEmployee dados recebidos {}", mapperUtils.jsonAsString(employeeRequestDto));
            Employee employee = employeeMapper.convertToEntity(employeeRequestDto);

            Employee savedEmployee = employeeRepo.save(employee);

            responseDto = employeeMapper.convertToDto(savedEmployee);
            log.debug("EmployeeService:createEmployee colaborador criado com sucesso {}", mapperUtils.jsonAsString(responseDto));

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao salvar o colaborador no banco de dados. Mensagem: {}", ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao criar o colaborador");
        }

        log.info("EmployeeService:createEmployee execucao finalizada");
        return responseDto;

    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(PageRequest pr) throws EmployeeServiceBusinessException {
        Page<EmployeeResponseDTO> responseDto;
        log.info("EmployeeService:getEmployees execucao iniciada");

        try {
            Page<Employee> employeePage = employeeRepo.findAll(pr);

            responseDto = employeePage.map(employeeMapper::convertToDto);
            log.info("EmployeeService:getEmployees resumo da consulta: totalPaginas={}, totalElementos={}", responseDto.getTotalPages(), responseDto.getTotalElements());

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os colaboradores no banco de dados. Mensagem: {}", ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar os colaboradores no banco de dados: " + ex.getMessage());
        }

        log.info("EmployeeService:getEmployees execucao finalizada");
        return responseDto;
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getById(UUID employeeId) throws EmployeeServiceBusinessException {
        EmployeeResponseDTO responseDto;
        log.info("EmployeeService:getEmployeeById execucao iniciada");

        try {
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

            responseDto = employeeMapper.convertToDto(employee);
            log.debug("EmployeeService:getEmployeeById colaborador encontrado para o id {} {}", employeeId, mapperUtils.jsonAsString(responseDto));

            log.info("EmployeeService:getEmployeeById execucao finalizada");
            return responseDto;

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar o colaborador {} no banco de dados. Mensagem: {}", employeeId, ex.getMessage());
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar o colaborador no banco de dados: " + ex.getMessage());
        }

    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO updateEmployeeDto) throws EmployeeServiceBusinessException {
        EmployeeResponseDTO responseDto;
        log.info("EmployeeService:updateEmployee execucao iniciada");

        try {
            Employee foundEmployee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

            foundEmployee.setName(updateEmployeeDto.name());
            foundEmployee.setEmail(updateEmployeeDto.email());
            foundEmployee.setBirthdate(updateEmployeeDto.birthdate());
            foundEmployee.setPix(updateEmployeeDto.pix());
            foundEmployee.setContact(updateEmployeeDto.contact());
            foundEmployee.setCpf(updateEmployeeDto.cpf());

            responseDto = employeeMapper.convertToDto(foundEmployee);
            log.debug("EmployeeService:updateEmployee colaborador atualizado. id={}, dados={}", employeeId, mapperUtils.jsonAsString(responseDto));

            log.info("EmployeeService:updateEmployee execucao finalizada");
            return employeeMapper.convertToDto(foundEmployee);

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao atualizar o colaborador {} no banco de dados. Mensagem: {}", employeeId, ex.getMessage(), ex);
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao atualizar o colaborador no banco de dados");
        }

    }

    @Transactional
    public void archiveEmployee(UUID employeeId) throws EmployeeServiceBusinessException {
        log.info("EmployeeService:archiveEmployee execucao iniciada");

        try {
            log.debug("EmployeeService:archiveEmployee buscando colaborador no banco de dados pelo id {}", employeeId);
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

            employee.setArchivedAt(applicationClock.instant());
            log.info("EmployeeService:archiveEmployee colaborador arquivado com sucesso. id={}", employee.getId());

        } catch (EmployeeNotFoundException ex) {
            log.info("EmployeeService:archiveEmployee colaborador nao encontrado. id={}", employeeId);
            throw ex;
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao arquivar o colaborador {} no banco de dados. Mensagem: {}", employeeId, ex.getMessage(), ex);
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao arquivar o colaborador no banco de dados");

        }
    }

    @Transactional
    public void unarchiveEmployee(UUID employeeId) throws EmployeeServiceBusinessException {
        log.info("EmployeeService:unarchiveEmployee execucao iniciada");
        try {

            log.debug("EmployeeService:unarchiveEmployee buscando colaborador no banco de dados pelo id {}", employeeId);
            Employee employee = employeeRepo.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

            employee.setArchivedAt(null);
            log.info("EmployeeService:unarchiveEmployee colaborador desarquivado com sucesso. id={}", employee.getId());

        } catch (EmployeeNotFoundException ex) {
            log.info("EmployeeService:unarchiveEmployee colaborador nao encontrado. id={}", employeeId);
            throw ex;
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao desarquivar o colaborador {} no banco de dados. Mensagem: {}", employeeId, ex.getMessage(), ex);
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao desarquivar o colaborador no banco de dados");

        }
    }
}
