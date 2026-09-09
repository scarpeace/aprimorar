package aprimorar.pessoas.responsavel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.responsavel.domain.ResponsavelEntity;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelNaoEncontradoException;
import aprimorar.pessoas.responsavel.repository.ResponsavelRepository;
import aprimorar.pessoas.responsavel.web.dto.ResponsavelFiltroRequest;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ResponsavelServiceImplQueryTest {

    @Mock
    private ResponsavelRepository responsavelRepo;

    @Mock
    private AlunoRepository alunoRepo;

    private ResponsavelServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ResponsavelServiceImpl(responsavelRepo, alunoRepo);
    }

    @Test
    void shouldGetResponsaveis() {
        var responsavel = responsavel("João Pereira");
        var pageable = PageRequest.of(0, 10);

        when(responsavelRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(responsavel), pageable, 1));

        var response = service.getResponsaveis(new ResponsavelFiltroRequest(null, null, null), pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(responsavel.getId(), response.getContent().getFirst().id());
        assertEquals("João Pereira", response.getContent().getFirst().nome());
    }

    @Test
    void shouldGetResponsaveisList() {
        var responsavel = responsavel("João Pereira");

        when(responsavelRepo.findAll()).thenReturn(List.of(responsavel));

        var response = service.getResponsaveisList();

        assertEquals(1, response.size());
        assertEquals(responsavel.getId(), response.getFirst().id());
        assertEquals("João Pereira", response.getFirst().nome());
    }

    @Test
    void shouldFindResponsavelById() {
        var id = UUID.randomUUID();
        var responsavel = responsavel("João Pereira");
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));

        var response = service.findResponsavelById(id);

        assertEquals(id, response.id());
        assertEquals("João Pereira", response.nome());
    }

    @Test
    void shouldExposeResponsavelContract() {
        var id = UUID.randomUUID();
        var responsavel = responsavel("João Pereira");
        setId(responsavel, id);

        when(responsavelRepo.findById(id)).thenReturn(Optional.of(responsavel));

        var response = service.buscarPorId(id);

        assertEquals(id, response.id());
        assertEquals("João Pereira", response.nome());
        assertEquals("joao@example.com", response.email());
        assertEquals("61999999999", response.telefone());
    }

    @Test
    void shouldThrowWhenFindResponsavelByIdDoesNotExist() {
        var id = UUID.randomUUID();

        when(responsavelRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(ResponsavelNaoEncontradoException.class, () -> service.findResponsavelById(id));

        assertEquals("Responsável não encontrado no banco de dados", ex.getMessage());
    }

    private static ResponsavelEntity responsavel(String nome) {
        var responsavel = new ResponsavelEntity(
            nome,
            LocalDate.of(1980, 5, 21),
            "61999999999",
            "12345678900",
            "joao@example.com"
        );
        setId(responsavel, UUID.randomUUID());
        return responsavel;
    }

    private static void setId(ResponsavelEntity responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
