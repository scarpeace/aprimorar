package aprimorar.atendimentos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.atendimentos.dto.TransacaoRequestDTO;
import aprimorar.atendimentos.dto.TransacaoResponseDTO;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.service.TransacaoMutationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transacao", description = "Operacoes relacionadas a transacoes.")
@RestController
@RequestMapping("/transacoes")
public class TransacaoMutationController {

    private final TransacaoMutationService transacaoMutationService;

    public TransacaoMutationController(TransacaoMutationService transacaoMutationService) {
        this.transacaoMutationService = transacaoMutationService;
    }

    @PostMapping("/transacoes")
    @Operation(operationId = "createTransacao", description = "Cria uma transacao manual de saida para despesa.")
    @ApiResponse(responseCode = "201", description = "Transacao criada com sucesso.")
    public ResponseEntity<TransacaoResponseDTO> createTransacao(@RequestBody TransacaoRequestDTO dto) {
        var response = transacaoMutationService.criarSaidaDespesa(dto);
        return ResponseEntity.created(null).body(response);
    }

    @PostMapping("/transacoes/{transacaoId}/efetivar")
    @Operation(operationId = "efetivarTransacao", description = "Efetiva uma transacao pendente informando a forma de pagamento.")
    @ApiResponse(responseCode = "200", description = "Transacao efetivada com sucesso.")
    public ResponseEntity<Void> efetivarTransacao(@PathVariable Long transacaoId, @RequestBody FormaPagamento formaPagamento) {
        transacaoMutationService.efetivarTransacao(transacaoId, formaPagamento);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transacoes/{transacaoId}/cancelar")
    @Operation(operationId = "cancelarTransacao", description = "Cancela uma transacao pelo ID.")
    @ApiResponse(responseCode = "200", description = "Transacao cancelada com sucesso.")
    public ResponseEntity<Void> cancelarTransacao(@PathVariable Long transacaoId) {
        transacaoMutationService.cancelarTransacao(transacaoId);
        return ResponseEntity.ok().build();
    }
}
