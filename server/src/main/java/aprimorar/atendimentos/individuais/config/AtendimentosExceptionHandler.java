package aprimorar.atendimentos.individuais.config;

import aprimorar.atendimentos.individuais.domain.exception.AtendimentoConflitanteException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoDadosInvalidosException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoEdicaoExpiradaException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoEstadoInvalidoException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoNaoEncontradoException;
import aprimorar.common.models.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "aprimorar.atendimentos.individuais.web.controller")
public class AtendimentosExceptionHandler {

    @ExceptionHandler(AtendimentoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(AtendimentoNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Atendimento não encontrado", ex.getMessage());
    }

    @ExceptionHandler({
        AtendimentoConflitanteException.class,
        AtendimentoDadosInvalidosException.class,
        AtendimentoEdicaoExpiradaException.class,
        AtendimentoEstadoInvalidoException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Erro de regra de negócio", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String error, String message) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            error,
            List.of(message)
        );
        return ResponseEntity.status(status).body(body);
    }
}
