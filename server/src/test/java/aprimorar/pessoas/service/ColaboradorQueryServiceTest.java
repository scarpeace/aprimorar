package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.repository.ColaboradorRepository;
import aprimorar.pessoas.shared.FuncoesColaborador;
import aprimorar.shared.exception.BusinessException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
class ColaboradorQueryServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepo;

    @InjectMocks
    private ColaboradorQueryService service;

    @Test
    void shouldReturnPaginatedColaboradores() {
        var pageable = PageRequest.of(0, 10);
        var filtro = new ColaboradorFiltroRequest("João", "joao@example.com", "123", true);
        var colaborador = validColaborador();

        when(colaboradorRepo.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(colaborador), pageable, 1));

        var result = service.getColaboradores(filtro, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("João Pereira", result.getContent().getFirst().nome());
        verify(colaboradorRepo).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void shouldReturnColaboradoresKpis() {
        when(colaboradorRepo.countByFuncaoNot(FuncoesColaborador.SISTEMA)).thenReturn(10L);
        when(colaboradorRepo.countByActiveTrueAndFuncaoNot(FuncoesColaborador.SISTEMA)).thenReturn(7L);

        var result = service.getColaboradoresKpis();

        assertEquals(10L, result.totalColaboradores());
        assertEquals(7L, result.totalColaboradoresAtivos());
    }

    @Test
    void shouldListColaboradores() {
        var colaborador = validColaborador();
        when(colaboradorRepo.findAllByFuncaoNotAndActiveTrueOrderByNomeAsc(FuncoesColaborador.SISTEMA))
            .thenReturn(List.of(colaborador));

        var result = service.listColaboradores();

        assertEquals(1, result.size());
        assertEquals("João Pereira", result.getFirst().name());
        verify(colaboradorRepo).findAllByFuncaoNotAndActiveTrueOrderByNomeAsc(FuncoesColaborador.SISTEMA);
    }

    @Test
    void shouldFindColaboradorById() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        var colaborador = validColaborador();
        setField(colaborador, "id", colaboradorId);
        setField(colaborador, "createdAt", LocalDateTime.of(2024, 1, 1, 10, 0));

        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.of(colaborador));

        var result = service.findColaboradorById(colaboradorId);

        assertEquals(colaboradorId, result.id());
        assertEquals("João Pereira", result.nome());
    }

    @Test
    void shouldThrowWhenColaboradorDoesNotExist() {
        var colaboradorId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(colaboradorRepo.findById(colaboradorId)).thenReturn(Optional.empty());

        var exception = assertThrows(BusinessException.class, () -> service.findColaboradorById(colaboradorId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Colaborador não encontrado no banco de dados", exception.getMessage());
    }

    private static Colaborador validColaborador() {
        return new Colaborador(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao.pereira@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "JOAO.PEREIRA@EXAMPLE.COM",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000-000", "Apto 1")
        );
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
