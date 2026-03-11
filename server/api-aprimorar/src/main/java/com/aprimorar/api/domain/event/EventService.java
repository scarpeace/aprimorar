package com.aprimorar.api.domain.event;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.exception.EventServiceBusinessException;
import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.domain.student.StudentRepository;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.shared.MapperUtils;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmployeeRepository employeeRepo;
    private final EventMapper eventMapper;
    private final MapperUtils mapperUtils;

    public EventService(EventRepository eventRepo,
            StudentRepository studentRepo,
            EmployeeRepository employeeRepo,
            EventMapper eventMapper,
            MapperUtils mapperUtils) {
        this.eventRepo = eventRepo;
        this.studentRepo = studentRepo;
        this.employeeRepo = employeeRepo;
        this.eventMapper = eventMapper;
        this.mapperUtils = mapperUtils;
    }

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        log.info("EventService:createEvent execucao iniciada");
        EventResponseDTO responseDto;
        try {
            log.debug("EventService:createEvent dados recebidos {}", mapperUtils.jsonAsString(eventRequestDTO));
            Event event = eventMapper.convertToEntity(eventRequestDTO);
            StudentEntity student = findActiveStudentOrThrow(eventRequestDTO.studentId());
            Employee employee = findActiveEmployeeOrThrow(eventRequestDTO.employeeId());

            //TODO Atualizar todos as referencias de StudentEntity para Student
            event.setStudentEntity(student);
            event.setEmployee(employee);
            Event savedEvent = eventRepo.save(event);

            responseDto = eventMapper.convertToDto(savedEvent);
            log.debug("EventService:createEvent evento criado com sucesso {}", mapperUtils.jsonAsString(responseDto));

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao salvar o evento no banco de dados. Mensagem: {}", ex.getMessage());
            throw new EventServiceBusinessException("Ocorreu um erro ao criar o evento");
        }

        log.info("EventService:createEvent execucao finalizada");
        return responseDto;
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEvents(PageRequest pr) throws EmployeeServiceBusinessException {
        Page<EventResponseDTO> responseDto;
        log.info("EventService:getEvents execucao iniciada");

        try {
            Page<Event> eventPage = eventRepo.findAll(pr);

            responseDto = eventPage.map(eventMapper::convertToDto);
            log.info("EventService:getEvents resumo da consulta: totalPaginas={}, totalElementos={}", responseDto.getTotalPages(), responseDto.getTotalElements());

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os eventos no banco de dados. Mensagem: {}", ex.getMessage());
            throw new EventServiceBusinessException("Ocorreu um erro ao buscar os eventos no banco de dados: " + ex.getMessage());
        }

        log.info("EventService:getEvents execucao finalizada");
        return responseDto;
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long eventId) {
        EventResponseDTO responseDto;
        log.info("EventService:getEventById execucao iniciada");

        try {
            Event event = eventRepo.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException(eventId));

            responseDto = eventMapper.convertToDto(event);
            log.debug("EventService:getEventById evento encontrado para o id {} {}", eventId, mapperUtils.jsonAsString(responseDto));

            log.info("EventService:getEventById execucao finalizada");
            return responseDto;

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar o evento {} no banco de dados. Mensagem: {}", eventId, ex.getMessage());
            throw new EventServiceBusinessException("Ocorreu um erro ao buscar o evento no banco de dados: " + ex.getMessage());
        }
    }

    @Transactional
    public EventResponseDTO updateEvent(Long eventId, UpdateEventDTO updateEventDto) {
        EventResponseDTO responseDto;
        log.info("EventService:updateEvent execucao iniciada");

        try {
            Event foundEvent = eventRepo.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException(eventId));

            foundEvent.setTitle(updateEventDto.title());
            foundEvent.setDescription(updateEventDto.description());
            foundEvent.setStartDateTime(updateEventDto.startDateTime());
            foundEvent.setEndDateTime(updateEventDto.endDateTime());
            foundEvent.setPrice(updateEventDto.price());
            foundEvent.setPayment(updateEventDto.payment());
            foundEvent.setContent(updateEventDto.content());

            StudentEntity newStudent = findActiveStudentOrThrow(updateEventDto.studentId());
            Employee newEmployee = findActiveEmployeeOrThrow(updateEventDto.employeeId());

            foundEvent.setStudentEntity(newStudent);
            foundEvent.setEmployee(newEmployee);

            responseDto = eventMapper.convertToDto(foundEvent);
            log.debug("EventService:updateEvent evento atualizado. id={}, dados={}", foundEvent.getId(), mapperUtils.jsonAsString(responseDto));

            log.info("EventService:updateEvent execucao finalizada");

        } catch (Exception ex) {
            log.error("Ocorreu um erro ao atualizar o evento {} no banco de dados. Mensagem: {}", eventId, ex.getMessage(), ex);
            throw new EventServiceBusinessException("Ocorreu um erro ao atualizar o evento no banco de dados");
        }
            return responseDto;

    }

    @Transactional
    public void deleteEvent(Long eventId) {
        log.info("EventService:deleteEvent execucao iniciada");

        Event foundEvent = findAnyEventOrThrow(eventId);
        eventRepo.delete(foundEvent);
        log.info("EventService:deleteEvent evento deletado. id={}", foundEvent.getId());
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> getEventsByEmployeeId(UUID employeeId, PageRequest pr) {
        Page<EventResponseDTO> responseDto;
        log.info("EventService:getEventsByEmployeeId execucao iniciada");

        try {
            Employee employee = findActiveEmployeeOrThrow(employeeId);
            Page<Event> eventPage = eventRepo.findAllByEmployeeId(employee.getId(), pr);
            
            responseDto = eventPage.map(eventMapper::convertToDto);
            log.debug("EventService:getEventsByEmployeeId evento encontrado para o id {} {}", employee.getId(), mapperUtils.jsonAsString(responseDto));

            log.info("EventService:getEventsByEmployeeId execucao finalizada");
           
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os eventos {} no banco de dados. Mensagem: {}", employeeId, ex.getMessage());
            throw new EventServiceBusinessException("Ocorreu um erro ao buscar os eventos no banco de dados: " + ex.getMessage());
        }
         return responseDto;
    }

     public Page<EventResponseDTO> getEventsByStudentId(UUID studentId, PageRequest pr) {
        Page<EventResponseDTO> responseDto;
        log.info("EventService:getEventsByStudentId execucao iniciada");

        try {
            StudentEntity student = findActiveStudentOrThrow(studentId);
            Page<Event> eventPage = eventRepo.findAllByStudentEntityId(student.getId(), pr);
            
            responseDto = eventPage.map(eventMapper::convertToDto);
            log.debug("EventService:getEventsByStudentId evento encontrado para o id {} {}", student.getId(), mapperUtils.jsonAsString(responseDto));

            log.info("EventService:getEventsByStudentId execucao finalizada");
           
        } catch (Exception ex) {
            log.error("Ocorreu um erro ao buscar os eventos {} no banco de dados. Mensagem: {}", studentId, ex.getMessage());
            throw new EventServiceBusinessException("Ocorreu um erro ao buscar os eventos no banco de dados: " + ex.getMessage());
        }
         return responseDto;
    }

    private Event findAnyEventOrThrow(Long eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private StudentEntity findActiveStudentOrThrow(UUID studentId) {
        return studentRepo.findByIdAndArchivedAtIsNull(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    private Employee findActiveEmployeeOrThrow(UUID employeeId) {
        return employeeRepo.findByIdAndArchivedAtIsNull(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

}
