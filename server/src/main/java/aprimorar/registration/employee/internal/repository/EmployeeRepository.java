package aprimorar.registration.employee.internal.repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.registration.employee.internal.Employee;
import aprimorar.registration.employee.api.Duty;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByNameContainingIgnoreCase(String name);

    Optional<Employee> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByIdAndActiveFalse(UUID id);

    long countByDutyNotAndActiveTrue(Duty duty);

    long countByDutyNot(Duty duty);

    List<Employee> findAllByDutyNotAndActiveTrue(Duty duty, Sort sort);

    List<Employee> findAllByDutyNotAndActiveTrueOrderByNameAsc(Duty duty);
}
