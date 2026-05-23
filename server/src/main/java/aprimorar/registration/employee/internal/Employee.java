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

        super(name, birthdate, validatePix(pix), contact, cpf, email);
        this.address = validateAddress(address);
        this.duty = duty;
        this.setRole(Role.EMPLOYEE);
    }

    public void update(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String email,
            Duty duty,
            Address address
    ) {
        super.update(name, birthdate, validatePix(pix), contact, email);
        this.duty = duty;
        this.address = validateAddress(address);
    }

    private static String validatePix(String pix) {
        if (pix == null || pix.isBlank()) {
            throw new IllegalArgumentException("Pix é obrigatório");
        }
        return pix;
    }

    private Address validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Endereço do colaborador é obrigatório");
        }
        return address;
    }

}
