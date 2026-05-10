package aprimorar.registration.employee.internal;

import aprimorar.registration.employee.api.dto.EmployeeResponseDTO;
import aprimorar.registration.shared.enums.Duty;
import aprimorar.shared.Person;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDate;

import lombok.Getter;

@Getter
@Entity
@Table(name = "tb_employees")
public class Employee extends Person {

    @Column(name = "duty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Duty duty;

    protected Employee() {
    }

    public Employee(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            Duty duty
    ) {

        super(name, birthdate, pix, contact, cpf, email);
        this.duty = duty;
        this.setRole(Role.EMPLOYEE);
    }

    public void updateDetails(
            String name, LocalDate birthdate, String pix,
            String contact, String email, Duty duty
    ) {
        super.update(name, birthdate, pix, contact, email);
        this.duty = duty;
    }

    public EmployeeResponseDTO toResponseDto() {
        return new EmployeeResponseDTO(
                getId(),
                getName(),
                getBirthdate(),
                getPix(),
                getContact(),
                getCpf(),
                getEmail(),
                getDuty(),
                getActive(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
