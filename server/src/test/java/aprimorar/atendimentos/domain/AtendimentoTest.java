package aprimorar.atendimentos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.domain.Endereco;
import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.shared.FuncoesColaborador;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AtendimentoTest {

    @Test
    void shouldCreateAtendimento() {
        var aluno = validAluno();
        var colaborador = validColaborador();
        var inicio = LocalDateTime.now().plusDays(1);
        var fim = inicio.plusHours(1);

        var atendimento = new Atendimento(
            inicio,
            fim,
            TipoAtendimento.MENTORIA,
            aluno,
            colaborador,
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(100)
        );

        assertEquals(inicio, atendimento.getDataHoraInicio());
        assertEquals(fim, atendimento.getDataHoraFim());
        assertEquals(TipoAtendimento.MENTORIA, atendimento.getTipo());
        assertEquals(StatusAtendimento.AGENDADO, atendimento.getStatus());
        assertSame(aluno, atendimento.getAluno());
        assertSame(colaborador, atendimento.getColaborador());
        assertEquals(BigDecimal.valueOf(150), atendimento.getPagamentoAluno());
        assertEquals(BigDecimal.valueOf(100), atendimento.getRepasseColaborador());
    }

    @Test
    void shouldUpdateAtendimento() {
        var atendimento = validAtendimento();
        var novoAluno = new Aluno(
            "Maria Silva",
            LocalDate.of(2001, 2, 2),
            "(21) 98888-7777",
            "987.654.321-00",
            "maria.silva@example.com",
            "Escola Nova",
            validResponsavel("Maria Ramos", "987.654.321-01", "maria.ramos@example.com"),
            new Endereco("Rua B", "20", "Centro", "Rio", "RJ", "20000000", null)
        );
        var novoColaborador = new Colaborador(
            "Pedro Souza",
            LocalDate.of(1992, 3, 3),
            "pedro.pix@example.com",
            "(21) 97777-6666",
            "111.222.333-44",
            "pedro.souza@example.com",
            FuncoesColaborador.MENTOR,
            new Endereco("Rua C", "30", "Centro", "Rio", "RJ", "21000000", null)
        );
        var novoInicio = LocalDateTime.now().plusDays(2);
        var novoFim = novoInicio.plusHours(2);

        var updated = atendimento.update(
            novoInicio,
            novoFim,
            TipoAtendimento.TERAPIA,
            novoAluno,
            novoColaborador,
            BigDecimal.valueOf(220),
            BigDecimal.valueOf(140)
        );

        assertSame(atendimento, updated);
        assertEquals(novoInicio, atendimento.getDataHoraInicio());
        assertEquals(novoFim, atendimento.getDataHoraFim());
        assertEquals(TipoAtendimento.TERAPIA, atendimento.getTipo());
        assertSame(novoAluno, atendimento.getAluno());
        assertSame(novoColaborador, atendimento.getColaborador());
        assertEquals(BigDecimal.valueOf(220), atendimento.getPagamentoAluno());
        assertEquals(BigDecimal.valueOf(140), atendimento.getRepasseColaborador());
    }

    @Test
    void shouldCancelAtendimento() {
        var atendimento = validAtendimento();

        atendimento.cancelar();

        assertEquals(StatusAtendimento.CANCELADO, atendimento.getStatus());
    }

    @Test
    void shouldNotCancelAtendimentoWhenAlreadyConcluido() {
        var atendimento = validAtendimento();
        atendimento.concluir();

        var exception = assertThrows(BusinessException.class, atendimento::cancelar);

        assertEquals("Não é possível cancelar um evento concluído", exception.getMessage());
    }

    @Test
    void shouldConcludeAtendimento() {
        var atendimento = validAtendimento();

        atendimento.concluir();

        assertEquals(StatusAtendimento.CONCLUIDO, atendimento.getStatus());
    }

    @Test
    void shouldNotConcludeAtendimentoWhenCancelled() {
        var atendimento = validAtendimento();
        atendimento.cancelar();

        var exception = assertThrows(BusinessException.class, atendimento::concluir);

        assertEquals("Não é possível concluir um evento cancelado", exception.getMessage());
    }

    @Test
    void shouldRescheduleAtendimento() {
        var atendimento = validAtendimento();
        var novoInicio = LocalDateTime.now().plusDays(3);
        var novoFim = novoInicio.plusHours(1);

        atendimento.reagendar(novoInicio, novoFim);

        assertEquals(novoInicio, atendimento.getDataHoraInicio());
        assertEquals(novoFim, atendimento.getDataHoraFim());
    }

    @Test
    void shouldNotRescheduleCancelledAtendimento() {
        var atendimento = validAtendimento();
        atendimento.cancelar();

        var exception = assertThrows(
            BusinessException.class,
            () -> atendimento.reagendar(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1))
        );

        assertEquals("Não é possível reagendar um evento cancelado", exception.getMessage());
    }

    @Test
    void shouldSetStudentPaymentDate() {
        var atendimento = validAtendimento();

        atendimento.togglePagamentoAluno();

        assertNotNull(atendimento.getDataPagamentoAluno());
    }

    @Test
    void shouldClearStudentPaymentDateWhenToggledAgain() {
        var atendimento = validAtendimento();
        atendimento.togglePagamentoAluno();

        atendimento.togglePagamentoAluno();

        assertNull(atendimento.getDataPagamentoAluno());
    }

    @Test
    void shouldSetCollaboratorRepasseDate() {
        var atendimento = validAtendimento();

        atendimento.toggleRepasseColaborador();

        assertNotNull(atendimento.getDataRepasseColaborador());
    }

    @Test
    void shouldClearCollaboratorRepasseDateWhenToggledAgain() {
        var atendimento = validAtendimento();
        atendimento.toggleRepasseColaborador();

        atendimento.toggleRepasseColaborador();

        assertNull(atendimento.getDataRepasseColaborador());
    }

    @Test
    void shouldNotTogglePaymentsWhenCancelled() {
        var atendimento = validAtendimento();
        atendimento.cancelar();

        var exception = assertThrows(BusinessException.class, atendimento::togglePagamentoAluno);

        assertEquals("Não é possível alterar pagamentos de um atendimento cancelado", exception.getMessage());
    }

    @Test
    void shouldValidateJanelaEdicao() throws ReflectiveOperationException {
        var atendimento = validAtendimento();

        setField(atendimento, "dataHoraFim", LocalDateTime.now().minusDays(21));

        var exception = assertThrows(BusinessException.class, atendimento::validarJanelaEdicao);

        assertEquals("A janela de 20 dias para editar as informações do atendimento encerrou", exception.getMessage());
    }

    @Test
    void shouldNotCreateAtendimentoWhenEndIsBeforeStart() {
        var inicio = LocalDateTime.now().plusDays(1);
        var fim = inicio.minusHours(1);

        var exception = assertThrows(
            IllegalStateException.class,
            () -> new Atendimento(
                inicio,
                fim,
                TipoAtendimento.AULA,
                validAluno(),
                validColaborador(),
                BigDecimal.valueOf(150),
                BigDecimal.valueOf(100)
            )
        );

        assertEquals("Data de fim do atendimento nao pode ser anterior a data de inicio", exception.getMessage());
    }

    @Test
    void shouldNotCreateAtendimentoWhenPagamentoIsLowerThanRepasse() {
        var inicio = LocalDateTime.now().plusDays(1);
        var fim = inicio.plusHours(1);

        var exception = assertThrows(
            IllegalStateException.class,
            () -> new Atendimento(
                inicio,
                fim,
                TipoAtendimento.AULA,
                validAluno(),
                validColaborador(),
                BigDecimal.valueOf(90),
                BigDecimal.valueOf(100)
            )
        );

        assertEquals("O valor do atendimento nao pode ser menor que o pagamento", exception.getMessage());
    }

    @Test
    void shouldNotCreateAtendimentoWhenPagamentoIsLowerThanMinimum() {
        var inicio = LocalDateTime.now().plusDays(1);
        var fim = inicio.plusHours(1);

        var exception = assertThrows(
            IllegalStateException.class,
            () -> new Atendimento(
                inicio,
                fim,
                TipoAtendimento.AULA,
                validAluno(),
                validColaborador(),
                BigDecimal.valueOf(49),
                BigDecimal.valueOf(30)
            )
        );

        assertEquals("O valor do atendimento nao pode ser menor que R$50,00", exception.getMessage());
    }

    @Test
    void shouldSetCreatedAtAndUpdatedAt() {
        var atendimento = validAtendimento();

        atendimento.prePersist();
        atendimento.preUpdate();

        assertNotNull(atendimento.getCreatedAt());
        assertNotNull(atendimento.getUpdatedAt());
    }

    private static Atendimento validAtendimento() {
        var inicio = LocalDateTime.now().plusDays(1);
        return new Atendimento(
            inicio,
            inicio.plusHours(1),
            TipoAtendimento.AULA,
            validAluno(),
            validColaborador(),
            BigDecimal.valueOf(150),
            BigDecimal.valueOf(100)
        );
    }

    private static Aluno validAluno() {
        return new Aluno(
            "Ana Silva",
            LocalDate.of(2000, 1, 1),
            "(61) 99999-9999",
            "123.456.789-00",
            "ana.silva@example.com",
            "Colégio Aprimorar",
            validResponsavel("João Silva", "123.456.789-01", "joao.silva@example.com"),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
    }

    private static Colaborador validColaborador() {
        return new Colaborador(
            "Lucas Almeida",
            LocalDate.of(1990, 5, 21),
            "lucas.pix@example.com",
            "(61) 99999-8888",
            "123.456.789-99",
            "lucas.almeida@example.com",
            FuncoesColaborador.PROFESSOR,
            new Endereco("Rua B", "20", "Centro", "Brasilia", "DF", "71000000", null)
        );
    }

    private static Responsavel validResponsavel(String nome, String cpf, String email) {
        return new Responsavel(
            nome,
            LocalDate.of(1980, 1, 1),
            "(11) 99999-9999",
            cpf,
            email
        );
    }

    private static void setField(Object target, String fieldName, Object value) throws ReflectiveOperationException {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
