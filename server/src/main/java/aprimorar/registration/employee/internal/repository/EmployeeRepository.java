package aprimorar.registration.employee.internal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import aprimorar.registration.employee.api.contract.DutyEnum;
import aprimorar.registration.employee.internal.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByDutyNotAndActiveTrue(DutyEnum duty);

    long countByDutyNot(DutyEnum duty);

    List<Employee> findAllByDutyNotAndActiveTrueOrderByNameAsc(DutyEnum duty);
}
