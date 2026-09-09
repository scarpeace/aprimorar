package aprimorar.common.openapi;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "Não autenticado.",
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/problem+json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = org.springframework.http.ProblemDetail.class)
        )
    ),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema.",
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/problem+json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = org.springframework.http.ProblemDetail.class)
        )
    )
})
public @interface CommonProblemResponses {
}
