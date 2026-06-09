package aprimorar.pessoas.domain;

import aprimorar.pessoas.shared.ColaboradorDutyEnum;
import aprimorar.pessoas.shared.Cpf;
import aprimorar.pessoas.shared.Email;
import aprimorar.shared.MapperUtils;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(name = "colaboradores")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 50)
    private String name;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate birthdate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true))
    private Cpf cpf;

    @Column(name = "telefone", nullable = false)
    private String contact;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Column(name = "pix", nullable = false)
    private String pix;

    @Column(name = "ativo", nullable = false)
    private Boolean active = true;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "funcao", nullable = false)
    @Enumerated(EnumType.STRING)
    private ColaboradorDutyEnum duty;

    @Embedded
    private Endereco endereco;

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
            Endereco endereco
    ) {

        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.pix = validatePix(pix);
        this.contact = validateContact(contact);
        this.cpf = new Cpf(cpf);
        this.email = new Email(email);
        this.endereco = validateEndereco(endereco);
        this.duty = duty;
    }

    public void update(
            String name,
            LocalDate birthdate,
            String pix,
            String contact,
            String email,
            ColaboradorDutyEnum duty,
            Endereco endereco
    ) {
        this.name = validateName(name);
        this.birthdate = validateBirthdate(birthdate);
        this.pix = validatePix(pix);
        this.contact = validateContact(contact);
        this.email = new Email(email);
        this.duty = duty;
        this.endereco = validateEndereco(endereco);
    }

    public boolean isSystemRecord() {
        return ColaboradorDutyEnum.SYSTEM.equals(this.duty);
    }

    public String getCpf() {
        return cpf.value();
    }

    public String getEmail() {
        return email.value();
    }

    public void archive() {
        this.active = false;
    }

    private static String validatePix(String pix) {
        if (pix == null || pix.isBlank()) {
            throw new IllegalArgumentException("Pix é obrigatório");
        }
        return pix;
    }

    public void unarchive() {
        if (isSystemRecord()) {
            throw new IllegalArgumentException("Não é possível desarquivar este registro " + ColaboradorDutyEnum.SYSTEM.getDescription());
        }
        this.active = true;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("STATE: Nome do colaborador é obrigatório");
        }
        return name;
    }

    private LocalDate validateBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            throw new IllegalArgumentException("STATE: Data de nascimento do colaborador é obrigatória");
        }
        return birthdate;
    }

    private String validateContact(String contact) {
        var normalized = MapperUtils.normalizeContact(contact);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("STATE: Contato do colaborador é obrigatório");
        }
        return normalized;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    private Endereco validateEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço do colaborador é obrigatório");
        }
        return endereco;
    }

}
