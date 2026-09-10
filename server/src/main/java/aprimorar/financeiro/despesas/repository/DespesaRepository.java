package aprimorar.financeiro.despesas.repository;

import aprimorar.financeiro.despesas.domain.DespesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DespesaRepository extends JpaRepository<DespesaEntity, Long>, JpaSpecificationExecutor<DespesaEntity> {
}
