package aprimorar.registration.student.internal;

import aprimorar.registration.shared.address.Address;
import aprimorar.registration.parent.internal.Parent;
import aprimorar.shared.Person;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

import lombok.Getter;

@Entity
@Table(name = "tb_students")
@Getter
public class Student extends Person {

    @Column(name = "school", nullable = false)
    private String school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @Embedded
    private Address address;

    protected Student() {
    }

    public Student(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            String school,
            Parent parent,
            Address address
    ) {

        super(name, birthdate, pix, contact, cpf, email);
        validateRequiredFields(address, parent);
        this.setRole(Role.STUDENT);
        this.school = school;
        this.parent = parent;
        this.address = address;
    }

    public void updateDetails(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email,
        String school,
        Parent parent,
        Address address
    ) {
        super.update(name, birthdate, pix, contact, email);
        this.school = school;
        this.address = address;
        this.parent = parent;
    }

    private void validateRequiredFields(Address address, Parent parent) {
        if (address == null) {
            throw new IllegalArgumentException("Endereço do aluno é obrigatório");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Aluno não pode ser cadastrado sem um responsável");
        }
    }
}
