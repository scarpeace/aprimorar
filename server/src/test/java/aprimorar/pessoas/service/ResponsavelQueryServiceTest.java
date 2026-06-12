package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.ResponsavelFiltroRequest;
import aprimorar.pessoas.repository.ResponsavelRepository;
import aprimorar.shared.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ResponsavelQueryServiceTest {

    @Mock
    private ResponsavelRepository responsavelRepo;

    @InjectMocks
    private ResponsavelQueryService service;

    @Test
    void shouldReturnPaginatedResponsaveis() {
        var pageable = PageRequest.of(0, 10);
        var filtro = new ResponsavelFiltroRequest("João", "joao@example.com", "123");

        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );
        setField(responsavel, "id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        setField(responsavel, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));

        when(responsavelRepo.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(responsavel), pageable, 1));

        var result = service.getResponsaveis(filtro, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("João Silva", result.getContent().getFirst().nome());
        assertEquals("12345678901", result.getContent().getFirst().cpf());
        verify(responsavelRepo).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldListResponsaveis() {
        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );
        setField(responsavel, "id", UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(responsavelRepo.findAll()).thenReturn(List.of(responsavel));

        var result = service.listResponsaveis();

        assertEquals(1, result.size());
        assertEquals("João Silva", result.getFirst().name());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), result.getFirst().id());
        verify(responsavelRepo).findAll();
    }

    @Test
    void shouldFindResponsavelById() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        var responsavel = new Responsavel(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "(11) 99999-9999",
            "123.456.789-01",
            "joao.silva@example.com"
        );
        setField(responsavel, "id", responsavelId);
        setField(responsavel, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.of(responsavel));

        var result = service.findResponsavelById(responsavelId);

        assertEquals(responsavelId, result.id());
        assertEquals("João Silva", result.nome());
        verify(responsavelRepo).findById(responsavelId);
    }

    @Test
    void shouldThrowWhenResponsavelByIdDoesNotExist() {
        var responsavelId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        when(responsavelRepo.findById(responsavelId)).thenReturn(java.util.Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.findResponsavelById(responsavelId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Responsável não encontrado no banco de dados", exception.getMessage());
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
