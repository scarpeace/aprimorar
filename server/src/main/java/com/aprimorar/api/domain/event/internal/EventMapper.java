package com.aprimorar.api.domain.event.internal;

import com.aprimorar.api.domain.employee.api.EmployeeService;
import com.aprimorar.api.domain.employee.api.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.event.api.dto.EventResponseDTO;
import com.aprimorar.api.domain.student.api.StudentService;
import com.aprimorar.api.domain.student.api.dto.StudentResponseDTO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ObjectProvider<StudentService> studentService;
    private final ObjectProvider<EmployeeService> employeeService;

    public EventMapper(ObjectProvider<StudentService> studentService, ObjectProvider<EmployeeService> employeeService) {
        this.studentService = studentService;
        this.employeeService = employeeService;
    }

    public EventResponseDTO convertToDto(Event event) {
        StudentResponseDTO student = studentService.getObject().findById(event.getStudentId());
        EmployeeResponseDTO employee = employeeService.getObject().getReferenceById(event.getEmployeeId());
        return convertToDto(event, student, employee);
    }

    public EventResponseDTO convertToDto(Event event, StudentResponseDTO student, EmployeeResponseDTO employee) {
        return new EventResponseDTO(
            event.getId(),
            event.getDescription(),
            event.getContent(),
            event.getStartDate(),
            event.getEndDateTime(),
            event.getDuration(),
            event.getPrice(),
            event.getPayment(),
            event.getProfit(),
            student.id(),
            student.name(),
            employee.id(),
            employee.name(),
            event.getEmployeePaymentDate(),
            event.getStudentChargeDate(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
