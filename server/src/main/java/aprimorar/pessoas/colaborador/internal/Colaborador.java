package aprimorar.pessoas.colaborador.internal;

import aprimorar.pessoas.shared.Person;
import aprimorar.pessoas.shared.address.Address;
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
@Table(name = "tb_colaboradores")
public class Colaborador extends Person {

    @Column(name = "funcao", nullable = false)
    @Enumerated(EnumType.STRING)
    private ColaboradorDutyEnum duty;

    @Embedded
    private Address address;

    protected Colaborador() {
    }

    public Colaborador(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String cpf,
            String email,
            ColaboradorDutyEnum duty,
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
            ColaboradorDutyEnum duty,
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

    @Override
    public void unarchive() {
        if (ColaboradorDutyEnum.SYSTEM.equals(this.duty)) {
            throw new IllegalArgumentException("Não é possível desarquivar este registro " + ColaboradorDutyEnum.SYSTEM.getDescription());
        }
        super.unarchive();
    }

    private Address validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Endereço do colaborador é obrigatório");
        }
        return address;
    }

}
