package aprimorar.atendimentos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import aprimorar.atendimentos.domain.Transacao;


public interface TransacaoRepository
    extends JpaRepository<Transacao, Long>, JpaSpecificationExecutor<Transacao>
{
    List<Transacao> findByAtendimentoId(Long atendimentoId);
}
