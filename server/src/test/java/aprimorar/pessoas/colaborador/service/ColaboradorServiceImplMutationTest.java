package aprimorar.pessoas.colaborador.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.enums.FuncoesColaborador;
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
