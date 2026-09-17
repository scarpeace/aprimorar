package aprimorar.instituicao.alunos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.cobrancas.CobrancaApi;
import aprimorar.instituicao.alunos.domain.AlunoEntity;
import aprimorar.instituicao.alunos.domain.Responsavel;
import aprimorar.instituicao.alunos.domain.exception.AlunoDuplicadoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import aprimorar.instituicao.alunos.repository.AlunoRepository;

import aprimorar.instituicao.alunos.web.dto.aluno.AlunoRequestDTO;
import aprimorar.instituicao.alunos.web.dto.aluno.ResponsavelRequestDTO;
import aprimorar.instituicao.common.domain.Endereco;
import aprimorar.instituicao.common.web.dto.endereco.EnderecoRequestDTO;

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
class AlunoServiceImplMutationTest {

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private CobrancaApi cobrancaApi;

    private AlunoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlunoServiceImpl(alunoRepo, cobrancaApi);
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
    void shouldNotDeactivateAlunoWithPendingCharge() {
        UUID id = UUID.randomUUID();
        var aluno = aluno(responsavel());
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));
        when(cobrancaApi.possuiPendenciaPorAlunoId(id)).thenReturn(true);

        var exception = assertThrows(AlunoPossuiPendenciaFinanceiraException.class, () -> service.deactivateAluno(id));

        assertEquals("Não é possível desativar um aluno com cobrança pendente", exception.getMessage());
        assertTrue(aluno.getActive());
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
