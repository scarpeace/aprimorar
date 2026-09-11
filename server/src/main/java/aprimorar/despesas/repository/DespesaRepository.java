package aprimorar.despesas.repository;

import aprimorar.despesas.domain.DespesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DespesaRepository extends JpaRepository<DespesaEntity, Long>, JpaSpecificationExecutor<DespesaEntity> {
}
