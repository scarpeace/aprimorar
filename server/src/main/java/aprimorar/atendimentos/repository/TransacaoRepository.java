package aprimorar.atendimentos.repository;


import aprimorar.atendimentos.domain.Transacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransacaoRepository extends JpaRepository<Transacao, Long>, JpaSpecificationExecutor<Transacao> {

    List<Transacao> findByAtendimentoId(Long atendimentoId);


}
