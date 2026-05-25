package aprimorar.financeiro.internal.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

import aprimorar.financeiro.internal.Despesa;

public interface DespesaRepositoryCustom {

    BigDecimal sumBySpecification(Specification<Despesa> specification);
}
