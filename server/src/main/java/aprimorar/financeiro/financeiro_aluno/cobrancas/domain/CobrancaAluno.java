package aprimorar.financeiro.financeiro_aluno.cobrancas.domain;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.parcelas.domain.ParcelaAluno;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity
@Table(name = "cobrancas_alunos")
public class CobrancaAluno {

    private static final BigDecimal VALOR_MINIMO = BigDecimal.valueOf(50);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "origem_id", nullable = false)
    private Long origemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_tipo", nullable = false, length = 40)
    private TipoOrigemCobrancaAluno origemTipo;

    @Column(name = "valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusCobrancaAluno status;

    @OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroParcela ASC")
    private List<ParcelaAluno> parcelas = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected CobrancaAluno() {}

    public CobrancaAluno(
        Long origemId,
        TipoOrigemCobrancaAluno origemTipo,
        UUID alunoId,
        BigDecimal valorTotal
    ) {
        validarOrigem(origemId, origemTipo);
        validarAluno(alunoId);
        validarValor(valorTotal);

        this.origemId = origemId;
        this.origemTipo = origemTipo;
        this.alunoId = alunoId;
        this.valorTotal = valorTotal;
        this.status = StatusCobrancaAluno.PENDENTE;
        adicionarParcelaUnica();
    }

    public void cancelar() {
        if (status != StatusCobrancaAluno.PENDENTE) {
            throw new CobrancaAlunoDadosInvalidosException(
                "A cobrança precisa estar pendente para ser cancelada"
            );
        }

        parcelas.forEach(ParcelaAluno::cancelarPorCobranca);
        this.status = StatusCobrancaAluno.CANCELADA;
    }

    public void atualizar(UUID alunoId, BigDecimal valorTotal) {
        validarAluno(alunoId);
        validarValor(valorTotal);

        if (status != StatusCobrancaAluno.PENDENTE) {
            throw new CobrancaAlunoDadosInvalidosException(
                "Não é possível alterar uma cobrança que não está pendente"
            );
        }

        this.alunoId = alunoId;
        this.valorTotal = valorTotal;
        parcelas.getFirst().atualizarValor(valorTotal);
    }

    public void recalcularStatus() {
        if (status == StatusCobrancaAluno.CANCELADA) {
            return;
        }

        long parcelasPagas = parcelas.stream()
            .filter(ParcelaAluno::estaPaga)
            .count();

        if (parcelasPagas == 0) {
            this.status = StatusCobrancaAluno.PENDENTE;
        } else if (parcelasPagas == parcelas.size()) {
            this.status = StatusCobrancaAluno.PAGA;
        } else {
            this.status = StatusCobrancaAluno.PARCIALMENTE_PAGA;
        }
    }

    private void adicionarParcelaUnica() {
        this.parcelas.add(new ParcelaAluno(this, 1, valorTotal, null));
    }

    private static void validarOrigem(Long origemId, TipoOrigemCobrancaAluno origemTipo) {
        if (origemId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID da origem é obrigatório");
        }
        if (origemTipo == null) {
            throw new CobrancaAlunoDadosInvalidosException("Tipo da origem é obrigatório");
        }
    }

    private static void validarAluno(UUID alunoId) {
        if (alunoId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID do aluno é obrigatório");
        }
    }

    private static void validarValor(BigDecimal valorTotal) {
        if (valorTotal == null) {
            throw new CobrancaAlunoDadosInvalidosException("Valor total da cobrança é obrigatório");
        }
        if (valorTotal.compareTo(VALOR_MINIMO) < 0) {
            throw new CobrancaAlunoDadosInvalidosException(
                "O valor total da cobrança não pode ser menor que R$50,00"
            );
        }
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
