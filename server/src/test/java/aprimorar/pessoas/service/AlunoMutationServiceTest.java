package aprimorar.pessoas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.EnderecoRequestDTO;
import aprimorar.pessoas.dto.aluno.AlunoRequestDTO;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ResponsavelRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AlunoMutationServiceTest {

    private static final UUID GHOST_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");
    private static final UUID RESPONSAVEL_ID = UUID.fromString("11111111-1111-4111-8111-111111111111");

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private ResponsavelRepository responsavelRepo;

    @Mock
    private AtendimentoRepository atendimentoRepo;

    private AlunoMutationService service;

    @BeforeEach
    void setUp() {
        service = new AlunoMutationService(alunoRepo, responsavelRepo, atendimentoRepo, GHOST_ID.toString());
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
        when(alunoRepo.save(any(Aluno.class))).thenReturn(saved);

        var response = service.createAluno(dto);

        assertEquals(saved.getId(), response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals(RESPONSAVEL_ID, response.responsavelId());

        ArgumentCaptor<Aluno> captor = ArgumentCaptor.forClass(Aluno.class);
        verify(alunoRepo).save(captor.capture());
        assertEquals("12345678900", captor.getValue().getCpf());
        assertEquals("ana@example.com", captor.getValue().getEmail());
        assertEquals("61999999999", captor.getValue().getTelefone());
    }

    @Test
    void shouldThrowWhenCreateAndResponsavelDoesNotExist() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.createAluno(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Responsável não encontrado no banco de dados", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndCpfAlreadyExists() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createAluno(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um aluno cadastrado com este CPF.", ex.getMessage());
        verify(alunoRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateAndEmailAlreadyExists() {
        var dto = alunoRequest();

        when(responsavelRepo.findById(RESPONSAVEL_ID)).thenReturn(Optional.of(responsavel()));
        when(alunoRepo.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepo.existsByEmail("ana@example.com")).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.createAluno(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
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
        var ex = assertThrows(BusinessException.class, () -> service.updateAluno(GHOST_ID, request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
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
        var ex = assertThrows(BusinessException.class, () -> service.updateAluno(id, request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
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
        var ex = assertThrows(BusinessException.class, () -> service.updateAluno(id, request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Já existe um aluno utilizando este e-mail.", ex.getMessage());
    }

    @Test
    void shouldArchiveAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.alunoPossuiPagamentoAlunoPendente(id)).thenReturn(false);

        service.archiveAluno(id);

        assertFalse(aluno.getActive());
    }

    @Test
    void shouldThrowWhenArchiveGhost() {
        var ghost = aluno(responsavel());
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(BusinessException.class, () -> service.archiveAluno(GHOST_ID));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("O registro não pode ser modificado.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenArchiveAndHasScheduledAtendimento() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.archiveAluno(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível arquivar um aluno com atendimentos agendados.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenArchiveAndHasPendingPayment() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.alunoPossuiPagamentoAlunoPendente(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.archiveAluno(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível arquivar um aluno com pagamento pendente.", ex.getMessage());
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
    void shouldDeleteAluno() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        var ghost = aluno(responsavel());

        setId(aluno, id);
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.alunoPossuiPagamentoAlunoPendente(id)).thenReturn(false);
        when(alunoRepo.getReferenceById(GHOST_ID)).thenReturn(ghost);

        service.deleteAluno(id);

        verify(atendimentoRepo).reassignAtendimentosAlunoToGhost(id, ghost);
        verify(alunoRepo).delete(aluno);
    }

    @Test
    void shouldThrowWhenDeleteGhost() {
        var ghost = aluno(responsavel());
        setId(ghost, GHOST_ID);

        when(alunoRepo.findById(GHOST_ID)).thenReturn(Optional.of(ghost));

        var ex = assertThrows(BusinessException.class, () -> service.deleteAluno(GHOST_ID));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("O registro não pode ser modificado.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDeleteAndHasScheduledAtendimento() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.deleteAluno(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível excluir um aluno com atendimentos agendados.", ex.getMessage());
        verify(alunoRepo, never()).delete(any(Aluno.class));
    }

    @Test
    void shouldThrowWhenDeleteAndHasPendingPayment() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(atendimentoRepo.alunoPossuiAtendimentoAgendado(id)).thenReturn(false);
        when(atendimentoRepo.alunoPossuiPagamentoAlunoPendente(id)).thenReturn(true);

        var ex = assertThrows(BusinessException.class, () -> service.deleteAluno(id));

        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals("Não é possível excluir um aluno com pagamento pendente.", ex.getMessage());
        verify(alunoRepo, never()).delete(any(Aluno.class));
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

    private static Aluno aluno(Responsavel responsavel) {
        return new Aluno(
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
        var responsavel = new Responsavel(
            nome,
            LocalDate.of(1980, 5, 21),
            "61999999999",
            "98765432100",
            email
        );
        setId(responsavel, RESPONSAVEL_ID);
        return responsavel;
    }

    private static void setId(Aluno aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

    private static void setId(Responsavel responsavel, UUID id) {
        ReflectionTestUtils.setField(responsavel, "id", id);
    }
}
