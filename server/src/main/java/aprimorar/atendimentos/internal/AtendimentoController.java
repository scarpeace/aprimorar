package aprimorar.atendimentos.internal;

import java.time.Instant;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.atendimentos.api.dto.AlunoAtendimentosResponseDTO;
import aprimorar.atendimentos.api.dto.AtendimentoFinanceSummaryDTO;
import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.api.dto.ColaboradorAtendimentosResponseDTO;
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
    private final AtendimentoQueryService atendimentoQueryService;

    public AtendimentoController(AtendimentoMutationService atendimentoMutationService, AtendimentoQueryService atendimentoQueryService) {
        this.atendimentoMutationService = atendimentoMutationService;
        this.atendimentoQueryService = atendimentoQueryService;
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
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentos(pageable, search, startDate, endDate, hideCharged, hidePaid));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getAtendimentoById", description = "Consulta um atendimento especifico pelo ID.")
    @ApiResponse(responseCode = "200", description = "Atendimento encontrado e retornado.")
    public ResponseEntity<AtendimentoResponseDTO> getAtendimentoById(@PathVariable UUID id) {
        AtendimentoResponseDTO found = atendimentoQueryService.findAtendimentoById(id);
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
    public ResponseEntity<AlunoAtendimentosResponseDTO> getAtendimentosByStudentId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
        @RequestParam(required = false) Boolean charged
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentosByStudentId(pageable, id, startDate, endDate, charged));
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
    public ResponseEntity<ColaboradorAtendimentosResponseDTO> getAtendimentosByEmployeeId(
        @ParameterObject Pageable pageable,
        @PathVariable UUID id,
        @RequestParam(required = false) Boolean hidePaid,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getAtendimentosByEmployeeId(pageable, id, hidePaid, startDate, endDate));
    }

    @GetMapping("/finance/report")
    @Operation(
        operationId = "getFinanceReport",
        description = "Consolida o financeiro institucional no periodo informado"
    )
    @ApiResponse(responseCode = "200", description = "Relatorio financeiro institucional retornado com totais consolidados.")
    public ResponseEntity<AtendimentoFinanceSummaryDTO> getFinanceReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        return ResponseEntity.ok(atendimentoQueryService.getFinanceReport(startDate, endDate));
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAtendimento(@PathVariable UUID id) {
        atendimentoMutationService.deleteAtendimento(id);
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

    // @GetMapping("/finance/report/alunos")
    // @Operation(
    //     operationId = "obterRelatorioFinanceiroAtendimentosPorAluno",
    //     description = "Agrupa indicadores financeiros por aluno no periodo informado."
    // )
    // @ApiResponse(responseCode = "200", description = "Relatorio financeiro por aluno retornado.")
    // public ResponseEntity<AlunosFinanceSummaryResponseDTO> getStudentsFinanceReport(
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    // ) {
    //     return ResponseEntity.ok(atendimentoQueryService.getStudentsFinanceReport(startDate, endDate));
    // }

    // @GetMapping("/finance/report/colaboradores")
    // @Operation(
    //     operationId = "obterRelatorioFinanceiroAtendimentosPorColaborador",
    //     description = "Agrupa indicadores financeiros por colaborador no periodo informado."
    // )
    // @ApiResponse(responseCode = "200", description = "Relatorio financeiro por colaborador retornado.")
    // public ResponseEntity<ColaboradoresFinanceSummaryResponseDTO> getEmployeesFinanceReport(
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    // ) {
    //     return ResponseEntity.ok(atendimentoService.getEmployeesFinanceReport(startDate, endDate));
    // }

    // @GetMapping("/finance/colaboradores")
    // @Operation(
    //     operationId = "listarColaboradoresComFinanceiro",
    //     description = "Lista colaboradores paginados com indicadores financeiros do periodo informado."
    // )
    // @ApiResponse(responseCode = "200", description = "Colaboradores retornados com financeiro por linha e resumo consolidado.")
    // public ResponseEntity<ColaboradoresWithFinanceResponseDTO> getEmployeesWithFinance(
    //     @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
    //     @RequestParam(required = false) String search,
    //     @RequestParam(required = false, defaultValue = "false") Boolean archived,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    // ) {
    //     return ResponseEntity.ok(
    //         atendimentoService.getEmployeesWithFinance(pageable, search, archived, startDate, endDate)
    //     );
    // }

    // @GetMapping("/finance/alunos")
    // @Operation(
    //     operationId = "listarAlunosComFinanceiro",
    //     description = "Lista alunos paginados com indicadores financeiros do periodo informado."
    // )
    // @ApiResponse(responseCode = "200", description = "Alunos retornados com financeiro por linha e resumo consolidado.")
    // public ResponseEntity<AlunosWithFinanceResponseDTO> getStudentsWithFinance(
    //     @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
    //     @RequestParam(required = false) String search,
    //     @RequestParam(required = false, defaultValue = "false") Boolean archived,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
    //     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    // ) {
    //     return ResponseEntity.ok(
    //         atendimentoService.getStudentsWithFinance(pageable, search, archived, startDate, endDate)
    //     );
    // }
}
