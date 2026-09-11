package aprimorar.atendimentos.individuais.repository;

import aprimorar.atendimentos.individuais.domain.AtendimentoConsultaViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AtendimentoConsultaRepository
    extends JpaRepository<AtendimentoConsultaViewEntity, Long>, JpaSpecificationExecutor<AtendimentoConsultaViewEntity> {
}
