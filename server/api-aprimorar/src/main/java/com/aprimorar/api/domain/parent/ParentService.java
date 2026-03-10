package com.aprimorar.api.domain.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;
import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParentService {

    private final ParentRepository parentRepo;
    private final ParentMapper parentMapper;

    public ParentService(ParentRepository parentRepo, ParentMapper parentMapper) {
        this.parentRepo = parentRepo;
        this.parentMapper = parentMapper;
    }

    @Transactional(readOnly = true)
    public Page<ParentResponseDTO> getParents(PageRequest pr) {
       Page<ParentResponseDTO> responseDto;
        log.info("ParentService:getParents execucao iniciada");
    
        try {
            Page<Parent> parentPage = parentRepo.findAll(pr);

            responseDto = parentPage.map(parentMapper::convertToDto);
            log.info("ParentService:getParents resumo da consulta: totalPaginas= {}, totalElementos= {}", responseDto.getTotalPages(), responseDto.getTotalElements());

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os responsáveis no banco de dados. Mensagem: {}", ex.getMessage(), ex);
            throw new EmployeeServiceBusinessException("Ocorreu um erro ao buscar os responsáveis no banco de dados");
        }

        log.info("ParentService:getParents execucao finalizada");
        return responseDto;
    }
}
