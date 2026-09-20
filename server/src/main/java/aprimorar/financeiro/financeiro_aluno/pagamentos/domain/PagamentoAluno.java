package aprimorar.financeiro.financeiro_aluno.pagamentos.domain;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoDadosInvalidosException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pagamentos_alunos")
public class PagamentoAluno {

    @Id
    private UUID id;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 40)
    private FormaPagamentoEnum formaPagamento;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected PagamentoAluno() {}

    public PagamentoAluno(
        UUID alunoId,
        LocalDate dataPagamento,
        FormaPagamentoEnum formaPagamento,
        String comprovanteUrl
    ) {
        validarAluno(alunoId);
        validarDataPagamento(dataPagamento);
        this.id = UUID.randomUUID();
        this.alunoId = alunoId;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
    }

    private static void validarAluno(UUID alunoId) {
        if (alunoId == null) {
            throw new PagamentoAlunoDadosInvalidosException(
                "ID do aluno é obrigatório"
            );
        }
    }

    private static void validarDataPagamento(LocalDate dataPagamento) {
        if (dataPagamento == null) {
            throw new PagamentoAlunoDadosInvalidosException(
                "Data do pagamento é obrigatória"
            );
        }
        if (dataPagamento.isAfter(LocalDate.now())) {
            throw new PagamentoAlunoDadosInvalidosException(
                "A data do pagamento não pode ser futura"
            );
        }
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
