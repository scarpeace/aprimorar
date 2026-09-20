package aprimorar.agendamento.alunos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import aprimorar.financeiro.api.cobrancas_particular.CobrancaParticularApi;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.domain.Responsavel;
import aprimorar.agendamento.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import aprimorar.agendamento.alunos.repository.AlunoRepository;

import aprimorar.agendamento.alunos.web.dto.aluno.AlunoRequest;

import aprimorar.agendamento.common.domain.Endereco;
import aprimorar.agendamento.common.web.dto.endereco.EnderecoRequest;

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
class AlunoServiceMutationTest {

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private CobrancaParticularApi cobrancaApi;

    private AlunoService service;

    @BeforeEach
    void setUp() {
        service = new AlunoService(alunoRepo, cobrancaApi);
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

    private static AlunoRequest alunoRequest() {
        return new AlunoRequest(
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

    private static EnderecoRequest enderecoRequest() {
        return new EnderecoRequest("Rua A", "10", "Apto 1", "Centro", "Brasilia", "DF", "70000000");
    }

    private static AlunoRequest.ResponsavelRequest responsavelRequest() {
        return new AlunoRequest.ResponsavelRequest("João Pereira", "joao@example.com", "(61) 99999-9999", "987.654.321-00");
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
        return new Responsavel(
            nome,
            "61999999999",
            "98765432100",
            email
        );
    }

    private static void setId(Aluno aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

}
