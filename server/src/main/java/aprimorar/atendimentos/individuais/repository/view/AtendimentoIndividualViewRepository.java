package aprimorar.atendimentos.individuais.repository.view;

import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AtendimentoIndividualViewRepository
    extends JpaRepository<AtendimentoIndividualViewEntity, Long>, JpaSpecificationExecutor<AtendimentoIndividualViewEntity> {

    Optional<AtendimentoIndividualViewEntity> findByCobrancaId(Long cobrancaId);

    Optional<AtendimentoIndividualViewEntity> findByRepasseId(Long repasseId);
}
