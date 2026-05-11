package aprimorar.appointment.internal;

import aprimorar.appointment.api.AppointmentService;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/appointments")
@Tag(name = "Appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(operationId = "createAppointment", description = "Cadastra um novo appointment no sistema.")
    @ApiResponse(responseCode = "201", description = "Appointment cadastrado com sucesso.")
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@RequestBody @Valid AppointmentRequestDTO request) {
        AppointmentResponseDTO created = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(operationId = "getAppointments", description = "Retorna uma lista paginada de appointments.")
    @ApiResponse(responseCode = "200", description = "Lista de appointments retornada com sucesso.")
    public ResponseEntity<PageDTO<AppointmentResponseDTO>> getAppointments(
        @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.getAppointments(pageable));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAppointmentById", description = "Retorna um appointment por ID.")
    @ApiResponse(responseCode = "200", description = "Appointment retornado com sucesso.")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponseDTO found = appointmentService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/{id}/employee")
    @Operation(operationId = "getAppointmentsByEmployeeId", description = "Retorna appointments por ID do colaborador.")
    @ApiResponse(responseCode = "200", description = "Página de appointments do colaborador retornada com sucesso.")
    public ResponseEntity<PageDTO<AppointmentResponseDTO>> getAppointmentsByEmployeeId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean hidePaid
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByEmployeeId(pageable, id));
    }

    @GetMapping("/{id}/student")
    @Operation(operationId = "getAppointmentsByStudentId", description = "Retorna appointments por ID do aluno.")
    @ApiResponse(responseCode = "200", description = "Página de appointments do aluno retornada com sucesso.")
    public ResponseEntity<PageDTO<AppointmentResponseDTO>> getAppointmentsByStudentId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean hidePaid
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStudentId(pageable, id));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateAppointment", description = "Atualiza os dados de um appointment.")
    @ApiResponse(responseCode = "200", description = "Appointment atualizado com sucesso.")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable UUID id, @RequestBody @Valid AppointmentRequestDTO request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteAppointment", description = "Deleta um appointment do sistema.")
    @ApiResponse(responseCode = "204", description = "Appointment deletado com sucesso.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
    }

    @PatchMapping("/{id}/toggle-student-charge")
    @Operation(operationId = "toggleStudentAppointmentCharge", description = "Dá baixa na cobrança do aluno no appointment.")
    @ApiResponse(responseCode = "200", description = "Cobrança do aluno atualizada com sucesso.")
    public ResponseEntity<AppointmentResponseDTO> toggleStudentAppointmentCharge(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.toggleStudentCharge(id));
    }

    @PatchMapping("/{id}/toggle-employee-payment")
    @Operation(operationId = "toggleEmployeeAppointmentPayment", description = "Dá baixa no pagamento do colaborador no appointment.")
    @ApiResponse(responseCode = "200", description = "Pagamento do colaborador atualizado com sucesso.")
    public ResponseEntity<AppointmentResponseDTO> toggleEmployeeAppointmentPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.toggleEmployeePayment(id));
    }
}
