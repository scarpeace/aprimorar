package aprimorar.atendimentos.individuais.atendimentos.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.atendimentos.individuais.atendimentos.domain.AtendimentoIndividualViewEntity;

public interface AtendimentoIndividualViewRepository
    extends JpaRepository<AtendimentoIndividualViewEntity, Long>, JpaSpecificationExecutor<AtendimentoIndividualViewEntity> {

}
