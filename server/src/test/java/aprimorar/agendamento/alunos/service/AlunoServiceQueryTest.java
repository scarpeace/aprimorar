package aprimorar.agendamento.alunos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.mockito.Mockito.when;

import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularAPI;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.domain.Responsavel;
import aprimorar.agendamento.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.agendamento.alunos.repository.AlunoRepository;


import aprimorar.agendamento.common.domain.Endereco;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AlunoServiceQueryTest {

    @Mock
    private AlunoRepository alunoRepo;

    @Mock
    private CobrancaParticularAPI cobrancaApi;

    private AlunoService service;

    @BeforeEach
    void setUp() {
        service = new AlunoService(alunoRepo, cobrancaApi);
    }

    @Test
    void shouldListAlunos() {
        var aluno = aluno("Ana Silva");

        when(alunoRepo.findAll(
                    ArgumentMatchers.<Specification<Aluno>>any(),
                    ArgumentMatchers.any(Sort.class)
                )).thenReturn(List.of(aluno));

        var response = service.listAlunosOptions();

        assertEquals(1, response.size());
        assertEquals(aluno.getId(), response.getFirst().id());
        assertEquals("Ana Silva", response.getFirst().nome());
    }

    @Test
    void shouldFindAlunoById() {
        var id = UUID.randomUUID();
        var aluno = aluno("Ana Silva");
        setId(aluno, id);

        when(alunoRepo.findById(id)).thenReturn(Optional.of(aluno));

        var response = service.findAlunoById(id);

        assertEquals(id, response.id());
        assertEquals("Ana Silva", response.nome());
        assertEquals("João Pereira", response.responsavel().nome());
    }

    @Test
    void shouldThrowWhenFindAlunoByIdDoesNotExist() {
        var id = UUID.randomUUID();

        when(alunoRepo.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(AlunoNaoEncontradoException.class, () -> service.findAlunoById(id));

        assertEquals("Aluno não encontrado no banco de dados", ex.getMessage());
    }

    private static Aluno aluno(String nome) {
        var aluno = new Aluno(
            nome,
            LocalDate.of(2010, 1, 1),
            "61999999999",
            "12345678900",
            "ana@example.com",
            "Colégio Aprimorar",
            responsavel(),
            new Endereco("Rua A", "10", "Centro", "Brasilia", "DF", "70000000", "Apto 1")
        );
        setId(aluno, UUID.randomUUID());
        return aluno;
    }

    private static Responsavel responsavel() {
        return new Responsavel(
            "João Pereira",
            "61988887777",
            "98765432100",
            "joao@example.com"
        );
    }

    private static void setId(Aluno aluno, UUID id) {
        ReflectionTestUtils.setField(aluno, "id", id);
    }

}
