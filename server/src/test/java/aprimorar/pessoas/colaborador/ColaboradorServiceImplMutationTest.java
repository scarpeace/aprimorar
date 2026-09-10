package aprimorar.pessoas.colaborador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.enums.FuncoesColaborador;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.shared.endereco.domain.Endereco;
import aprimorar.pessoas.shared.endereco.web.dto.EnderecoRequestDTO;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceImplMutationTest {

    @Mock
    private ColaboradorRepository colaboradorRepo;

    private ColaboradorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ColaboradorServiceImpl(colaboradorRepo);
    }

    @Test
    void shouldCreateColaborador() {
        var dto = collaboratorRequest();
        var saved = dto.toEntity();
        setId(saved, UUID.randomUUID());

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao@example.com")).thenReturn(false);
        when(colaboradorRepo.save(any(ColaboradorEntity.class))).thenReturn(saved);

        var response = service.createColaborador(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("João Pereira", response.nome());

        ArgumentCaptor<ColaboradorEntity> captor = ArgumentCaptor.forClass(ColaboradorEntity.class);
        verify(colaboradorRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("joao@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.createColaborador(dto));

        assertEquals("Já existe um colaborador cadastrado com este CPF.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = collaboratorRequest();

        when(colaboradorRepo.existsByCpf("12345678900")).thenReturn(false);
        when(colaboradorRepo.existsByEmail("joao@example.com")).thenReturn(true);

        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.createColaborador(dto));

        assertEquals("Já existe um colaborador cadastrado com este e-mail.", ex.getMessage());
        verify(colaboradorRepo, never()).save(any());
    }

    @Test
    void shouldUpdateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        var dto = new ColaboradorRequestDTO(
            "Maria Souza",
            LocalDate.of(1992, 8, 10),
            "maria@example.com",
            "(61) 98888-7777",
            "123.456.789-00",
            "maria@example.com",
            FuncoesColaborador.ADMINISTRATIVO,
            new EnderecoRequestDTO("Rua B", "20", "Sala 2", "Centro", "Brasilia", "DF", "70000111")
        );

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(colaboradorRepo.existsByEmailAndIdNot("maria@example.com", id)).thenReturn(false);

        var response = service.updateColaborador(id, dto);

        assertEquals("Maria Souza", colaborador.getNome());
        assertEquals("maria@example.com", colaborador.getEmail());
        assertEquals("61988887777", colaborador.getTelefone());
        assertEquals(FuncoesColaborador.ADMINISTRATIVO, colaborador.getFuncao());
        assertEquals("Maria Souza", response.nome());
    }

    @Test
    void shouldThrowWhenUpdateAndEmailAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        when(colaboradorRepo.existsByEmailAndIdNot("joao@example.com", id)).thenReturn(true);

        var request = collaboratorRequest();
        var ex = assertThrows(ColaboradorDuplicadoException.class, () -> service.updateColaborador(id, request));

        assertEquals("Já existe um colaborador utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldDeactivateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));
        service.deactivateColaborador(id);

        assertFalse(colaborador.getActive());
    }

    @Test
    void shouldActivateColaborador() {
        UUID id = UUID.randomUUID();
        var colaborador = collaborator();
        setId(colaborador, id);
        colaborador.deactivate();

        when(colaboradorRepo.findById(id)).thenReturn(Optional.of(colaborador));

        service.activateColaborador(id);

        assertTrue(colaborador.getActive());
    }

    private static ColaboradorRequestDTO collaboratorRequest() {
        return new ColaboradorRequestDTO(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "(61) 99999-9999",
            "123.456.789-00",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new EnderecoRequestDTO("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000")
        );
    }

    private static ColaboradorEntity collaborator() {
        return new ColaboradorEntity(
            "João Pereira",
            LocalDate.of(1990, 5, 21),
            "joao@example.com",
            "61999999999",
            "12345678900",
            "joao@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }

    private static void setId(ColaboradorEntity colaborador, UUID id) {
        ReflectionTestUtils.setField(colaborador, "id", id);
    }
}
