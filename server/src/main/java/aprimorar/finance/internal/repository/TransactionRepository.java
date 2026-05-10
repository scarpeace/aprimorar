package aprimorar.finance.internal.repository;

import aprimorar.finance.internal.Transaction;
import aprimorar.shared.enums.TransactionCategory;
import aprimorar.shared.enums.TransactionOrigin;
import aprimorar.shared.enums.TransactionStatus;
import aprimorar.shared.enums.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByOriginAndOriginId(TransactionOrigin origin, UUID originId);

    void deleteByOriginAndOriginId(TransactionOrigin origin, UUID originId);

    @Query(
        """
        select coalesce(sum(t.amount), 0)
        from Transaction t
        where t.type = :type
          and t.status = :status
          and (:category is null or t.category = :category)
        """
    )
    BigDecimal sumByTypeStatusAndCategory(
        @Param("type") TransactionType type,
        @Param("status") TransactionStatus status,
        @Param("category") TransactionCategory category
    );

    @Query(
        """
        select coalesce(sum(t.amount), 0)
        from Transaction t
        where t.origin = :origin
          and t.status = :status
        """
    )
    BigDecimal sumByOriginAndStatus(@Param("origin") TransactionOrigin origin, @Param("status") TransactionStatus status);

    @Query(
        """
        select coalesce(sum(t.amount), 0)
        from Transaction t
        join Event e on e.id = t.originId
        where t.origin = :origin
          and e.studentId = :studentId
          and (:startDate is null or e.startDate >= :startDate)
          and (:endDate is null or e.startDate <= :endDate)
          and (:status is null or t.status = :status)
        """
    )
    BigDecimal sumStudentTransactions(
        @Param("origin") TransactionOrigin origin,
        @Param("studentId") UUID studentId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("status") TransactionStatus status
    );

    @Query(
        """
        select coalesce(sum(t.amount), 0)
        from Transaction t
        join Event e on e.id = t.originId
        where t.origin = :origin
          and e.employeeId = :employeeId
          and (:startDate is null or e.startDate >= :startDate)
          and (:endDate is null or e.startDate <= :endDate)
          and (:status is null or t.status = :status)
        """
    )
    BigDecimal sumEmployeeTransactions(
        @Param("origin") TransactionOrigin origin,
        @Param("employeeId") UUID employeeId,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("status") TransactionStatus status
    );
}
