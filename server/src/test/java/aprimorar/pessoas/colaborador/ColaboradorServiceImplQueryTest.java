package aprimorar.pessoas.colaborador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.enums.FuncoesColaborador;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.pessoas.colaborador.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.shared.endereco.domain.Endereco;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceImplQueryTest {


    @Mock
    private ColaboradorRepository colaboradorRepo;

    private ColaboradorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ColaboradorServiceImpl(colaboradorRepo);
    }

    @Test
    void shouldGetColaboradores() {
        var colaborador = colaborador("João Pereira");
        var pageable = PageRequest.of(0, 10);

        when(colaboradorRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(colaborador), pageable, 1));

        var response = service.getColaboradores(new ColaboradorFiltroRequest(null, null, null, true), pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(colaborador.getId(), response.getContent().getFirst().id());
        assertEquals("João Pereira", response.getContent().getFirst().nome());
    }

    @Test
    void shouldFindById() {
        var id = UUID.randomUUID();
        var colaborador = colaborador("João Pereira");
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        var response = service.findById(id);

        assertEquals(id, response.id());
        assertEquals("João Pereira", response.nome());
    }

    @Test
    void shouldExposeColaboradorContract() {
        var id = UUID.randomUUID();
        var colaborador = colaborador("João Pereira");
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        var response = service.buscarPorId(id);

        assertEquals(id, response.id());
        assertEquals("João Pereira", response.nome());
        assertEquals(true, response.ativo());
    }

    @Test
    void shouldThrowWhenFindByIdDoesNotExist() {
        var id = UUID.randomUUID();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(ColaboradorNaoEncontradoException.class, () -> service.findById(id));

        assertEquals("Colaborador não encontrado no banco de dados", ex.getMessage());
    }

    @Test
    void shouldGetColaboradoresOptions() {
        var colaborador = colaborador("João Pereira");

        when(colaboradorRepo.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(colaborador));

        var response = service.getColaboradoresOptions();

        assertEquals(1, response.size());
        assertEquals(colaborador.getId(), response.getFirst().id());
        assertEquals("João Pereira", response.getFirst().nome());
    }

    private static ColaboradorEntity colaborador(String nome) {
        var colaborador = new ColaboradorEntity(
            nome,
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "61999999999",
            "12345678900",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
        setId(colaborador, UUID.randomUUID());
        return colaborador;
    }

    private static void setId(ColaboradorEntity colaborador, UUID id) {
        ReflectionTestUtils.setField(colaborador, "id", id);
    }
}
