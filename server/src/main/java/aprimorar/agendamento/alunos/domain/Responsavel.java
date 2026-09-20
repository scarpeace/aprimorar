package aprimorar.agendamento.alunos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Responsavel {

    @Column(name = "responsavel_nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "responsavel_cpf", nullable = false)
    private String cpf;

    @Column(name = "responsavel_telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "responsavel_email", nullable = false)
    private String email;

    protected Responsavel() {}

    public Responsavel(String nome, String telefone, String cpf, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.email = email;
    }
}
