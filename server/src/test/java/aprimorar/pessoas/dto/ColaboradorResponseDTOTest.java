package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.shared.FuncoesColaborador;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ColaboradorResponseDTOTest {

    @Test
    void shouldConvertColaboradorEntityToDto() {
        var colaborador = new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
        setField(colaborador, "id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        setField(colaborador, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));
        setField(colaborador, "updatedAt", LocalDateTime.of(2024, 1, 2, 10, 0));

        var dto = ColaboradorResponseDTO.toDto(colaborador);

        assertEquals("João Pereira", dto.nome());
        assertEquals(LocalDate.of(1990, 5, 21), dto.dataNascimento());
        assertEquals("joao.pereira@example.com", dto.pix());
        assertEquals("61999999999", dto.telefone());
        assertEquals("12345678900", dto.cpf());
        assertEquals("joao.pereira@example.com", dto.email());
        assertEquals(FuncoesColaborador.PROFESSOR, dto.funcao());
        assertEquals("Rua A", dto.endereco().rua());
        assertEquals(true, dto.active());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), dto.createdAt());
        assertEquals(LocalDateTime.of(2024, 1, 2, 10, 0), dto.updatedAt());
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
