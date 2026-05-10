package aprimorar.registration.parent.internal;

import aprimorar.registration.parent.api.dto.ParentResponseDTO;
import aprimorar.shared.Person;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tb_parent")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Parent extends Person {

    protected Parent() {}

    public Parent(
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

    public ParentResponseDTO toResponseDto() {
        return new ParentResponseDTO(
            getId(),
            getName(),
            getBirthdate(),
            getCpf(),
            getEmail(),
            getContact(),
            getPix(),
            getActive(),
            getCreatedAt(),
            getUpdatedAt()
        );
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
