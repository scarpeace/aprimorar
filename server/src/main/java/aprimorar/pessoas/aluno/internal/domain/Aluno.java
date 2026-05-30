package aprimorar.pessoas.aluno.internal.domain;

import java.time.LocalDate;
import java.util.UUID;

import aprimorar.pessoas.common.Person;
import aprimorar.pessoas.common.address.Address;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "tb_students")
@Getter
public class Aluno extends Person {

    @Column(name = "school", nullable = false)
    private String school;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Embedded
    private Address address;

    protected Aluno() {
    }

    public Aluno(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            String school,
            UUID parentId,
            Address address
    ) {

        super(name, birthdate, pix, contact, cpf, email);
        validateRequiredFields(address, parentId);
        this.setRole(Role.STUDENT);
        this.school = school;
        this.parentId = parentId;
        this.address = address;
    }

    public void update(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email,
        String school,
        UUID parentId,
        Address address
    ) {
        super.update(name, birthdate, pix, contact, email);
        this.school = school;
        this.address = address;
        this.parentId = parentId;
    }

    private void validateRequiredFields(Address address, UUID parentId) {
        if (address == null) {
            throw new IllegalArgumentException("Endereço do aluno é obrigatório");
        }
        if (parentId == null) {
            throw new IllegalArgumentException("Aluno não pode ser cadastrado sem um responsável");
        }
    }
}
