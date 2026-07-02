package aprimorar.pessoas.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.aluno.AlunoResponseDTO;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlunoResponseDTOTest {

    @Test
    void shouldConvertAlunoEntityToDto() {
        var aluno = new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
        setField(aluno, "id", UUID.fromString("223e4567-e89b-12d3-a456-426614174000"));
        setField(aluno, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));
        setField(aluno, "updatedAt", LocalDateTime.of(2024, 1, 2, 10, 0));

        var dto = AlunoResponseDTO.toDto(aluno);

        assertEquals("Ana Silva", dto.nome());
        assertEquals("(61) 99999-9999", dto.telefone());
        assertEquals("ana.silva@example.com", dto.email());
        assertEquals("123.456.789-00", dto.cpf());
        assertEquals(LocalDate.of(2000, 1, 1), dto.dataNascimento());
        assertEquals("Colégio Aprimorar", dto.escola());
        assertEquals(Period.between(LocalDate.of(2000, 1, 1), LocalDate.now()).getYears(), dto.idade());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), dto.responsavelId());
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
