package aprimorar.atendimentos.internal.web;

import aprimorar.atendimentos.api.dto.AlunoFinanceiroResumoDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.atendimentos.api.AtendimentosQueryApi;
import aprimorar.atendimentos.api.dto.AtendimentosAlunoResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentosColaboradorResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorFinanceiroResumoDTO;
import aprimorar.atendimentos.internal.application.AtendimentoMutationService;
import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/atendimentos")
@Tag(name = "Atendimento")
public class AtendimentoController {

    private final AtendimentoMutationService atendimentoMutationService;
    private final AtendimentosQueryApi atendimentoQueryApi;

    public AtendimentoController(
        AtendimentoMutationService atendimentoMutationService,
        AtendimentosQueryApi atendimentoQueryApi
    ) {
        this.atendimentoMutationService = atendimentoMutationService;
        this.atendimentoQueryApi = atendimentoQueryApi;
    }

    @PostMapping
    @Operation(operationId = "createAtendimento", description = "Cria um atendimento vinculando aluno e colaborador.")
    @ApiResponse(responseCode = "201", description = "Atendimento criado e retornado com os dados consolidados de aluno e colaborador.")
    public ResponseEntity<AtendimentoResponseDTO> createAtendimento(@RequestBody @Valid AtendimentoRequestDTO request) {
        AtendimentoResponseDTO created = atendimentoMutationService.createAtendimento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(
        operationId = "getAtendimentos",
        description = "Lista atendimentos com paginacao, ordenacao e filtros opcionais por texto, periodo, cobranca do aluno e pagamento do colaborador."
    )
    @ApiResponse(responseCode = "200", description = "Pagina de atendimentos retornada conforme os filtros informados.")
    public ResponseEntity<PageDTO<AtendimentoResponseDTO>> getAtendimentos(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
        @RequestParam(required = false) Boolean hideCharged,
        @RequestParam(required = false) Boolean hidePaid
    ) {
        return ResponseEntity.ok(atendimentoQueryApi.getAtendimentos(pageable, search, startDate, endDate, hideCharged, hidePaid));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAtendimentoById", description = "Consulta um atendimento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
    public ResponseEntity<AtendimentoResponseDTO> getAtendimentoById(@PathVariable UUID id) {
        AtendimentoResponseDTO found = atendimentoQueryApi.findAtendimentoById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/{id}/aluno")
    @Operation(
        operationId = "getAtendimentosByStudentId",
        description = "Retorna a agenda paginada de um aluno junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Agenda e resumo financeiro do aluno retornados conforme periodo e filtros informados."
    )
    public ResponseEntity<AtendimentosAlunoResponseDTO> getAtendimentosByStudentId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
        @RequestParam(required = false) Boolean charged
    ) {
        return ResponseEntity.ok(atendimentoQueryApi.getAtendimentosByStudentId(pageable, id, startDate, endDate, charged));
    }

    @GetMapping("/{id}/colaborador")
    @Operation(
        operationId = "getAtendimentosByEmployeeId",
        description = "Retorna a agenda paginada de um colaborador junto com os indicadores do mesmo periodo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Agenda e resumo financeiro do colaborador retornados conforme periodo e filtros informados."
    )
    public ResponseEntity<AtendimentosColaboradorResponseDTO> getAtendimentosByEmployeeId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean hidePaid,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoQueryApi.getAtendimentosByEmployeeId(pageable, id, hidePaid, startDate, endDate));
    }

    @GetMapping("/finance/report")
    @Operation(
        operationId = "getIndicadoresAtendimentos",
        description = "Consolida o financeiro institucional no periodo informado"
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro institucional retornado com totais consolidados.")
    public ResponseEntity<AtendimentoFinanceSummaryDTO> getFinanceReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoQueryApi.getIndicadoresAtendimentos(startDate, endDate));
    }

    @GetMapping("/finance/colaboradores")
    @Operation(
        operationId = "getOverviewFinanceiroColaboradores",
        description = "Lista colaboradores paginados com indicadores de pago e pendente por colaborador."
    )
    @ApiResponse(responseCode = "200", description = "Colaboradores retornados com indicadores de financeiro por linha e resumo consolidado.")
    public ResponseEntity<PageDTO<ColaboradorFinanceiroResumoDTO>> getOverviewFinanceiroColaboradores(
        @ParameterObject @PageableDefault(sort = "employeeName") Pageable pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            atendimentoQueryApi.getOverviewFinanceiroColaboradores(pageable, startDate, endDate)
        );
    }

    @GetMapping("/finance/alunos")
    @Operation(
        operationId = "getOverviewFinanceiroAlunos",
        description = "Lista alunos paginados com indicadores de cobrado e pendente por aluno."
    )
    @ApiResponse(responseCode = "200", description = "Alunos retornados com indicadores de financeiro por linha e resumo consolidado.")
    public ResponseEntity<PageDTO<AlunoFinanceiroResumoDTO>> getOverviewFinanceiroAlunos(
        @ParameterObject @PageableDefault(sort = "studentName") Pageable pageable,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(
            atendimentoQueryApi.getOverviewFinanceiroAlunos(pageable, startDate, endDate)
        );
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateAtendimento", description = "Atualiza dados cadastrais do atendimento.")
    @ApiResponse(responseCode = "200", description = "Atendimento atualizado e retornado com os dados atuais.")
    public ResponseEntity<AtendimentoResponseDTO> updateAtendimento(
        @PathVariable UUID id,
        @RequestBody @Valid AtendimentoRequestDTO request
    ) {
        return ResponseEntity.ok(atendimentoMutationService.updateAtendimento(id, request));
    }



    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteAtendimento", description = "Remove definitivamente um atendimento.")
    @ApiResponse(responseCode = "204", description = "Atendimento removido sem corpo de resposta.")
    public ResponseEntity<Void> deleteAtendimento(@PathVariable UUID id) {
        atendimentoMutationService.deleteAtendimento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-student-charge")
    @Operation(operationId = "toggleStudentAtendimentoCharge", description = "Alterna a baixa financeira do aluno para o atendimento.")
    @ApiResponse(responseCode = "200", description = "Status de cobranca do aluno atualizado e atendimento retornado.")
    public ResponseEntity<AtendimentoResponseDTO> toggleStudentAtendimentoCharge(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoMutationService.toggleStudentCharge(id));
    }

    @PatchMapping("/{id}/toggle-employee-payment")
    @Operation(
        operationId = "toggleEmployeeAtendimentoPayment",
        description = "Alterna a baixa financeira do colaborador para o atendimento."
    )
    @ApiResponse(responseCode = "200", description = "Status de pagamento do colaborador atualizado e atendimento retornado.")
    public ResponseEntity<AtendimentoResponseDTO> toggleEmployeeAtendimentoPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(atendimentoMutationService.toggleEmployeePayment(id));
    }

}
