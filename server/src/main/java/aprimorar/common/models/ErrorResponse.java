package aprimorar.common.models;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        Integer statusCode,
        String erro,
        List<String> mensagens
) {
}
