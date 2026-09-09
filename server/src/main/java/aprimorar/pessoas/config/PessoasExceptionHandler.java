package aprimorar.pessoas.config;

import aprimorar.common.models.ErrorResponse;
import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoEstadoInvalidoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorEstadoInvalidoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelDuplicadoException;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelEstadoInvalidoException;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PessoasExceptionHandler {

    @ExceptionHandler({
        AlunoNaoEncontradoException.class,
        ColaboradorNaoEncontradoException.class,
        ResponsavelNaoEncontradoException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Pessoa não encontrada", ex.getMessage());
    }

    @ExceptionHandler({
        AlunoDuplicadoException.class,
        ColaboradorDuplicadoException.class,
        ResponsavelDuplicadoException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Pessoa duplicada", ex.getMessage());
    }

    @ExceptionHandler({
        AlunoEstadoInvalidoException.class,
        ColaboradorEstadoInvalidoException.class,
        ResponsavelEstadoInvalidoException.class
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
