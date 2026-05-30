package aprimorar.pessoas.responsavel.domain;

import aprimorar.pessoas.common.Person;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tb_parents")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Responsavel extends Person {

    protected Responsavel() {}

    public Responsavel(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String cpf,
        String email
    ) {
        super(name, birthdate, pix, contact, cpf, email);
        this.setRole(Role.PARENT);
    }

    public void updateDetails(
        String name,
        LocalDate birthdate,
        String pix,
        String contact,
        String email
    ) {
        super.update(name, birthdate, pix, contact, email);
    }
}
