package aprimorar.pessoas.aluno.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.aluno.domain.AlunoEntity;
import aprimorar.pessoas.aluno.domain.Responsavel;
import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.aluno.web.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.web.dto.ResponsavelRequestDTO;
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
class AlunoServiceImplMutationTest {

    @Mock
    private AlunoRepository alunoRepo;

    private AlunoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlunoServiceImpl(alunoRepo);
    }

    @Test
    void shouldCreateAluno() {
        var dto = alunoRequest();
        var saved = dto.toEntity();
        setId(saved, UUID.randomUUID());

        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana@example.com")).thenReturn(false);
        when(alunoRepo.save(any(AlunoEntity.class))).thenReturn(saved);

        var response = service.createAluno(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals("João Pereira", response.responsavel().nome());

        ArgumentCaptor<AlunoEntity> captor = ArgumentCaptor.forClass(AlunoEntity.class);
        verify(alunoRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("ana@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = alunoRequest();

        when(alunoRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.createAluno(dto));

        assertEquals("Já existe um aluno cadastrado com este CPF.", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = alunoRequest();

        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana@example.com")).thenReturn(true);

        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.createAluno(dto));

        assertEquals("Já existe um aluno cadastrado com este e-mail.", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldUpdateAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        var dto = new AlunoRequestDTO(
            "Maria Silva",
            LocalDate.of(2011, 2, 2),
            "123.456.789-00",
            "Escola Nova",
            "(61) 98888-7777",
            "maria.silva@example.com",
            enderecoRequest(),
            new ResponsavelRequestDTO("Maria Ramos", "maria@example.com", "(61) 98888-7777", "987.654.321-00")
        );

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(alunoRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(alunoRepo.existsByEmailAndIdNot("maria.silva@example.com", id)).thenReturn(false);

        var response = service.updateAluno(id, dto);

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals("maria.silva@example.com", aluno.getEmail());
        assertEquals("61988887777", aluno.getTelefone());
        assertEquals("Escola Nova", aluno.getEscola());
        assertEquals("Maria Ramos", aluno.getResponsavel().getNome());
        assertEquals("Maria Silva", response.nome());
    }

    @Test
    void shouldThrowWhenUpdateAndCpfAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(alunoRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(true);

        var request = alunoRequest();
        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.updateAluno(id, request));

        assertEquals("Já existe um aluno utilizando este CPF.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdateAndEmailAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(alunoRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(alunoRepo.existsByEmailAndIdNot("ana@example.com", id)).thenReturn(true);

        var request = alunoRequest();
        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.updateAluno(id, request));

        assertEquals("Já existe um aluno utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldDeactivateAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        service.deactivateAluno(id);

        assertFalse(aluno.getActive());
    }

    @Test
    void shouldActivateAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);
        aluno.deactivate();

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        service.activateAluno(id);

        assertTrue(aluno.getActive());
    }

    private static AlunoRequestDTO alunoRequest() {
        return new AlunoRequestDTO(
            "Ana Silva",
            LocalDate.of(2010, 1, 1),
            "123.456.789-00",
            "Colégio Aprimorar",
            "(61) 99999-9999",
            "ana@example.com",
            enderecoRequest(),
            responsavelRequest()
        );
    }

    private static EnderecoRequestDTO enderecoRequest() {
        return new EnderecoRequestDTO("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000");
    }

    private static ResponsavelRequestDTO responsavelRequest() {
        return new ResponsavelRequestDTO("João Pereira", "joao@example.com", "(61) 99999-9999", "987.654.321-00");
    }

    private static AlunoEntity aluno(Responsavel responsavel) {
        return new AlunoEntity(
            "Ana Silva",
            LocalDate.of(2010, 1, 1),
            "61999999999",
            "12345678900",
            "ana@example.com",
            "Colégio Aprimorar",
            responsavel,
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }

    private static Responsavel responsavel() {
        return responsavel("João Pereira", "joao@example.com");
    }

    private static Responsavel responsavel(String nome, String email) {
        return new Responsavel(
            nome,
            "61999999999",
            "98765432100",
            email
        );
    }

    private static void setId(AlunoEntity aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

}
