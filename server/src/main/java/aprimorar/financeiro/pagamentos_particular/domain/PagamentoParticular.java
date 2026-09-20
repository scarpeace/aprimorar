package aprimorar.financeiro.pagamentos_particular.domain;

import aprimorar.financeiro.common.FormaPagamentoEnum;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularDadosInvalidosException;
import aprimorar.financeiro.repasses_particular.domain.RepasseParticular;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "pagamentos_particular")
public class PagamentoParticular {

    @Id
    private UUID id;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 40)
    private FormaPagamentoEnum formaPagamento;

    @Column(name = "comprovante_url", length = 500)
    private String comprovanteUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pagamento", fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<RepasseParticular> repasses = new ArrayList<>();

    protected PagamentoParticular() {
    }

    public PagamentoParticular(
        LocalDate dataPagamento,
        BigDecimal valorTotal,
        FormaPagamentoEnum formaPagamento,
        String comprovanteUrl
    ) {
        validarDataPagamento(dataPagamento);
        this.id = UUID.randomUUID();
        this.dataPagamento = dataPagamento;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.comprovanteUrl = comprovanteUrl;
    }

    private static void validarDataPagamento(LocalDate dataPagamento) {
        if (dataPagamento != null && dataPagamento.isAfter(LocalDate.now())) {
            throw new PagamentoParticularDadosInvalidosException(
                "A data do pagamento não pode ser futura"
            );
        }
    }

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
