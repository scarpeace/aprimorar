package aprimorar.financeiro.internal.infrastructure.persistence;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

import aprimorar.financeiro.internal.domain.Despesa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DespesaRepositoryImpl implements DespesaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigDecimal sumBySpecification(Specification<Despesa> specification) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<Despesa> root = cq.from(Despesa.class);

        Predicate predicate = specification == null ? null : specification.toPredicate(root, cq, cb);
        cq.select(cb.coalesce(cb.sum(root.get("amount")), BigDecimal.ZERO));
        if (predicate != null) {
            cq.where(predicate);
        }

        return entityManager.createQuery(cq).getSingleResult();
    }
}
