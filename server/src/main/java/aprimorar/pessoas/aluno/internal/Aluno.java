package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.shared.address.Address;
import aprimorar.pessoas.shared.Person;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;

@Entity
@Table(name = "tb_alunos")
@Getter
public class Aluno extends Person {

    @Column(name = "escola", nullable = false)
    private String school;

    @Column(name = "responsavel_id", nullable = false)
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

    public void updateDetails(
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
