 package aprimorar.appointment.internal;

import aprimorar.appointment.api.dto.AppointmentFinanceSummaryDTO;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.appointment.api.dto.EmployeeAppointmentsResponseDTO;
import aprimorar.appointment.api.dto.EmployeesFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.EmployeesWithFinanceResponseDTO;
import aprimorar.appointment.api.dto.StudentAppointmentsResponseDTO;
import aprimorar.appointment.api.dto.StudentsFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.StudentsWithFinanceResponseDTO;
import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/appointments")
@Tag(name = "Appointment")
public class AppointmentController {

    private final AppointmentServiceImpl appointmentService;

    public AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(
        operationId = "createAppointment",
        description = "Cria um atendimento vinculando aluno e colaborador."
    )
    @ApiResponse(responseCode = "201", description = "Atendimento criado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody @Valid AppointmentRequestDTO request) {
        AppointmentResponseDTO created = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(
        operationId = "getAppointments",
        description = "Lista atendimentos com paginacao, ordenacao e filtros opcionais por texto, periodo, cobranca do aluno e pagamento do colaborador."
    )
    @ApiResponse(responseCode = "200", description = "Pagina de atendimentos retornada conforme os filtros informados.")
    public ResponseEntity<PageDTO<AppointmentResponseDTO>> getAppointments(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
        @RequestParam(required = false) Boolean hideCharged,
        @RequestParam(required = false) Boolean hidePaid
    ) {
        return ResponseEntity.ok(
            appointmentService.getAppointments(pageable, search, startDate, endDate, hideCharged, hidePaid)
        );
    }

    @GetMapping("/{id}")
    @Operation(
        operationId = "getAppointmentById",
        description = "Consulta um atendimento especifico pelo ID."
    )
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponseDTO found = appointmentService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/{id}/student")
    @Operation(
        operationId = "getAppointmentsByStudentId",
        description = "Retorna a agenda paginada de um aluno junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(responseCode = "200", description = "Agenda e resumo financeiro do aluno retornados conforme periodo e filtros informados.")
    public ResponseEntity<StudentAppointmentsResponseDTO> getAppointmentsByStudentId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
        @RequestParam(required = false) Boolean charged
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStudentId(pageable, id, startDate, endDate, charged));
    }

    @GetMapping("/{id}/employee")
    @Operation(
        operationId = "getAppointmentsByEmployeeId",
        description = "Retorna a agenda paginada de um colaborador junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(responseCode = "200", description = "Agenda e resumo financeiro do colaborador retornados conforme periodo e filtros informados.")
    public ResponseEntity<EmployeeAppointmentsResponseDTO> getAppointmentsByEmployeeId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean hidePaid,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByEmployeeId(pageable, id, hidePaid, startDate, endDate));
    }

    @GetMapping("/finance/report")
    @Operation(
        operationId = "getAppointmentFinanceReport",
        description = "Consolida o financeiro institucional no periodo informado"
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro institucional retornado com totais consolidados.")
    public ResponseEntity<AppointmentFinanceSummaryDTO> getFinanceReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(appointmentService.getFinanceReport(startDate, endDate));
    }

    @GetMapping("/finance/report/students")
    @Operation(
        operationId = "getStudentsAppointmentsFinanceReport",
        description = "Agrupa indicadores financeiros por aluno no periodo informado."
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro por aluno retornado.")
    public ResponseEntity<StudentsFinanceSummaryResponseDTO> getStudentsFinanceReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(appointmentService.getStudentsFinanceReport(startDate, endDate));
    }

    @GetMapping("/finance/report/employees")
    @Operation(
        operationId = "getEmployeesAppointmentsFinanceReport",
        description = "Agrupa indicadores financeiros por colaborador no periodo informado."
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro por colaborador retornado.")
    public ResponseEntity<EmployeesFinanceSummaryResponseDTO> getEmployeesFinanceReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(appointmentService.getEmployeesFinanceReport(startDate, endDate));
    }

    @GetMapping("/finance/employees")
    @Operation(
        operationId = "getEmployeesWithFinance",
        description = "Lista colaboradores paginados com indicadores financeiros do periodo informado."
    )
    @ApiResponse(responseCode = "200", description = "Colaboradores retornados com financeiro por linha e resumo consolidado.")
    public ResponseEntity<EmployeesWithFinanceResponseDTO> getEmployeesWithFinance(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false, defaultValue = "false") Boolean archived,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            appointmentService.getEmployeesWithFinance(pageable, search, archived, startDate, endDate)
        );
    }

    @GetMapping("/finance/students")
    @Operation(
        operationId = "getStudentsWithFinance",
        description = "Lista alunos paginados com indicadores financeiros do periodo informado."
    )
    @ApiResponse(responseCode = "200", description = "Alunos retornados com financeiro por linha e resumo consolidado.")
    public ResponseEntity<StudentsWithFinanceResponseDTO> getStudentsWithFinance(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false, defaultValue = "false") Boolean archived,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            appointmentService.getStudentsWithFinance(pageable, search, archived, startDate, endDate)
        );
    }

    @PutMapping("/{id}")
    @Operation(
        operationId = "updateAppointment",
        description = "Atualiza dados cadastrais do atendimento."
    )
    @ApiResponse(responseCode = "200", description = "Atendimento atualizado e retornado com os dados atuais.")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable UUID id, @RequestBody @Valid AppointmentRequestDTO request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
        operationId = "deleteAppointment",
        description = "Remove definitivamente um atendimento."
    )
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }

    @PatchMapping("/{id}/toggle-student-charge")
    @Operation(
        operationId = "toggleStudentAppointmentCharge",
        description = "Alterna a baixa financeira do aluno para o atendimento."
    )
    @ApiResponse(responseCode = "200", description = "Status de cobranca do aluno atualizado e atendimento retornado.")
    public ResponseEntity<AppointmentResponseDTO> toggleStudentAppointmentCharge(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.toggleStudentCharge(id));
    }

    @PatchMapping("/{id}/toggle-employee-payment")
    @Operation(
        operationId = "toggleEmployeeAppointmentPayment",
        description = "Alterna a baixa financeira do colaborador para o atendimento."
    )
    @ApiResponse(responseCode = "200", description = "Status de pagamento do colaborador atualizado e atendimento retornado.")
    public ResponseEntity<AppointmentResponseDTO> toggleEmployeeAppointmentPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.toggleEmployeePayment(id));
    }
}
