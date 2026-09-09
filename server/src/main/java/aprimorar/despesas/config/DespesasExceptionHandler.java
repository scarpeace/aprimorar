package aprimorar.despesas.config;

import aprimorar.common.models.ErrorResponse;
import aprimorar.despesas.domain.exception.DespesaNaoEncontradaException;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DespesasExceptionHandler {

    @ExceptionHandler(DespesaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(DespesaNaoEncontradaException ex) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Despesa não encontrada",
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
