package aprimorar.financeiro.pagamento_aluno.repository;

import aprimorar.financeiro.pagamento_aluno.domain.PagamentoAlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoAlunoRepository extends JpaRepository<PagamentoAlunoEntity, Long> {}
