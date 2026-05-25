package aprimorar.financeiro.internal.infrastructure.persistence;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

import aprimorar.financeiro.internal.domain.Despesa;

public interface DespesaRepositoryCustom {

    BigDecimal sumBySpecification(Specification<Despesa> specification);
}
