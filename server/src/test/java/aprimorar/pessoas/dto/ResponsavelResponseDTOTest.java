package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.domain.Responsavel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class ResponsavelResponseDTOTest {

    @Test
    void shouldConvertResponsavelEntityToDto() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );
        setField(responsavel, "id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        setField(responsavel, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));
        setField(responsavel, "updatedAt", LocalDateTime.of(2024, 1, 2, 10, 0));

        var dto = ResponsavelResponseDTO.toDto(responsavel);

        assertEquals("João Silva", dto.nome());
        assertEquals(LocalDate.of(1990, 1, 1), dto.dataNascimento());
        assertEquals("12345678901", dto.cpf());
        assertEquals("11999999999", dto.telefone());
        assertEquals("joao.silva@example.com", dto.email());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.createdAt());
        assertEquals(LocalDateTime.of(2024, 1, 2, 10, 0), dto.updatedAt());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), dto.id());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
