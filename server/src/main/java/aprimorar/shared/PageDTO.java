package aprimorar.shared;


import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Estrutura padrao de paginacao da API")
public record PageDTO<T>(

    @NotNull
    @Schema(description = "Número da página atual", example = "0", nullable = false)
    int page,

    @NotNull
    @Schema(description = "Tamanho da página", example = "10", nullable = false)
    int size,

    @NotNull
    @Schema(description = "Total de elementos", example = "100", nullable = false)
    long totalElements,

    @NotNull
    @Schema(description = "Total de páginas", example = "10", nullable = false)
    int totalPages,

    @NotNull
    @Schema(description = "Lista de elementos da pagina", example = "[]", nullable = false)
    List<T> content
) {
    public PageDTO(Page<T> page) {
        this(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getContent()
        );
    }
}
