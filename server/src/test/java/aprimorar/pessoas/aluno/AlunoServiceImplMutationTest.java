package aprimorar.pessoas.aluno;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.pessoas.aluno.domain.AlunoEntity;
import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoEstadoInvalidoException;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.aluno.web.dto.AlunoRequestDTO;
import aprimorar.pessoas.endereco.domain.Endereco;
import aprimorar.pessoas.endereco.web.dto.EnderecoRequestDTO;
import aprimorar.pessoas.responsavel.domain.ResponsavelEntity;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelNaoEncontradoException;
import aprimorar.pessoas.responsavel.repository.ResponsavelRepository;
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

    private static final UUID GHOST_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");
    private static final UUID RESPONSAVEL_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private ResponsavelRepository responsavelRepo;

    private AlunoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlunoServiceImpl(alunoRepo, responsavelRepo, GHOST_ID.toString());
    }

    @Test
    void shouldCreateAluno() {
        var responsavel = responsavel();
        var dto = alunoRequest();
        var saved = dto.toEntity(responsavel);
        setId(saved, UUID.randomUUID());

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel));
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana@example.com")).thenReturn(false);
        when(alunoRepo.save(any(AlunoEntity.class))).thenReturn(saved);

        var response = service.createAluno(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals(RESPONSAVEL_ID, response.responsavelId());

        ArgumentCaptor<AlunoEntity> captor = ArgumentCaptor.forClass(AlunoEntity.class);
        verify(alunoRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("ana@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndResponsavelDoesNotExist() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.empty());

        var ex = assertThrows(ResponsavelNaoEncontradoException.class, () -> service.createAluno(dto));

        assertEquals("Responsável não encontrado no banco de dados", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.createAluno(dto));

        assertEquals("Já existe um aluno cadastrado com este CPF.", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
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
        var newResponsavel = responsavel("Maria Ramos", "maria@example.com");
        setId(aluno, id);

        var dto = new AlunoRequestDTO(
            "Maria Silva",
            LocalDate.of(2011, 2, 2),
            "123.456.789-00",
            "Escola Nova",
            "(61) 98888-7777",
            "maria.silva@example.com",
            enderecoRequest(),
            RESPONSAVEL_ID
        );

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(newResponsavel));
        when(alunoRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(alunoRepo.existsByEmailAndIdNot("maria.silva@example.com", id)).thenReturn(false);

        var response = service.updateAluno(id, dto);

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals("maria.silva@example.com", aluno.getEmail());
        assertEquals("61988887777", aluno.getTelefone());
        assertEquals("Escola Nova", aluno.getEscola());
        assertEquals(newResponsavel, aluno.getResponsavel());
        assertEquals("Maria Silva", response.nome());
    }

    @Test
    void shouldThrowWhenUpdateGhost() {
        var ghost = aluno(responsavel());
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));
        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));

        var request = alunoRequest();
        var ex = assertThrows(AlunoEstadoInvalidoException.class, () -> service.updateAluno(GHOST_ID, request));

        assertEquals("Não é possível modificar o registro de sistema 'Aluno Removido'.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdateAndCpfAlreadyUsed() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
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
        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
        when(alunoRepo.existsByCpfAndIdNot("12345678900", id)).thenReturn(false);
        when(alunoRepo.existsByEmailAndIdNot("ana@example.com", id)).thenReturn(true);

        var request = alunoRequest();
        var ex = assertThrows(AlunoDuplicadoException.class, () -> service.updateAluno(id, request));

        assertEquals("Já existe um aluno utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldArchiveAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        service.archiveAluno(id);

        assertFalse(aluno.getActive());
    }

    @Test
    void shouldThrowWhenArchiveGhost() {
        var ghost = aluno(responsavel());
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(AlunoEstadoInvalidoException.class, () -> service.archiveAluno(GHOST_ID));

        assertEquals("O registro não pode ser modificado.", ex.getMessage());
    }

    @Test
    void shouldUnarchiveAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);
        aluno.archive();

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        service.unarchiveAluno(id);

        assertTrue(aluno.getActive());
    }

    @Test
    void shouldThrowWhenDeleteGhost() {
        var ghost = aluno(responsavel());
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(AlunoEstadoInvalidoException.class, () -> service.deleteAluno(GHOST_ID));

        assertEquals("O registro não pode ser modificado.", ex.getMessage());
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
            RESPONSAVEL_ID
        );
    }

    private static EnderecoRequestDTO enderecoRequest() {
        return new EnderecoRequestDTO("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000");
    }

    private static AlunoEntity aluno(ResponsavelEntity responsavel) {
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

    private static ResponsavelEntity responsavel() {
        return responsavel("João Pereira", "joao@example.com");
    }

    private static ResponsavelEntity responsavel(String nome, String email) {
        var responsavel = new ResponsavelEntity(
            nome,
            LocalDate.of(1980, 5, 21),
            "61999999999",
            "98765432100",
            email
        );
        setId(responsavel, RESPONSAVEL_ID);
        return responsavel;
    }

    private static void setId(AlunoEntity aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

    private static void setId(ResponsavelEntity responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
