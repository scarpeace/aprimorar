package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.domain.student.Student;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventRules {

    public static void validate(Event event){
        validateRequiredFields(event, event.getStudent(), event.getEmployee());
        validatePriceAndPayment(event.getPrice(), event.getPayment());
        validateParticipants(event.getStudent(), event.getEmployee());
        validateDates(event.getStartDateTime(), event.getEndDateTime());
    }

    private static void validateRequiredFields(Event event, Student student, Employee employee) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new InvalidEventException("Título do evento é obrigatório");
        }
        if (event.getStartDateTime() == null) {
            throw new InvalidEventException("Data de início do evento é obrigatório");
        }
        if (event.getEndDateTime() == null) {
            throw new InvalidEventException("Data de término do evento é obrigatório");
        }
        if (event.getPrice() == null) {
            throw new InvalidEventException("Valor do evento é obrigatório");
        }
        if (event.getPayment() == null) {
            throw new InvalidEventException("Pagamento do evento é obrigatório");
        }
        if (event.getContent() == null) {
            throw new InvalidEventException("O conteúdo do evento é obrigatório");
        }
        if (student == null) {
            throw new InvalidEventException("Um evento não pode existir sem um estudante");
        }
        if (employee == null) {
            throw new InvalidEventException("Um evento não pode existir sem um colaborador");
        }
    }

    private static void validatePriceAndPayment(BigDecimal price, BigDecimal payment) {
        if (price.compareTo(payment) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que R$50,00");
        }
    }

    private static void validateParticipants(Student student, Employee employee) {
        if (student.getArchivedAt() != null) {
            throw new InvalidEventException("Evento não pode ter estudantes arquivados");
        }
        if (employee.getArchivedAt() != null) {
            throw new InvalidEventException("Evento não pode ter colaboradores arquivados");
        }
    }

    private static void validateDates(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidEventException("Data de fim do evento nao pode estar no passado");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new InvalidEventException("Data de fim do evento nao pode ser anterior a data de inicio");
        }
    }

    public static void validateEditWindow(Event event){
        if(event.getEndDateTime().isAfter(event.getEndDateTime().plusWeeks(2))){
            throw new NotAllowedToUpdateEventException("A janela para a editar as informações do evento encerrou");
        }
    }
}
