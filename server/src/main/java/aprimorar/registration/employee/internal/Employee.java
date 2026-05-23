package aprimorar.registration.employee.internal;

import aprimorar.registration.employee.api.Duty;
import aprimorar.registration.shared.Person;
import aprimorar.registration.shared.address.Address;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    private Address address;

    protected Employee() {
    }

    public Employee(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            Duty duty,
            Address address
    ) {

        super(name, birthdate, pix, contact, cpf, email);
        this.validateRequiredFields(pix, address);
        this.duty = duty;
        this.address = address;
        this.setRole(Role.EMPLOYEE);
    }

    public void updateDetails(
            String name, LocalDate birthdate, String pix,
            String contact, String email, Duty duty, Address address
    ) {
        super.update(name, birthdate, pix, contact, email);
        this.duty = duty;
        this.address = address;
    }

    private void validateRequiredFields(String pix, Address address) {
        if (pix == null || pix.isBlank()) {
            throw new IllegalArgumentException("Pix é obrigatório");
        }
        if (address == null) {
            throw new IllegalArgumentException("Endereço do colaborador é obrigatório");
        }
    }

}
