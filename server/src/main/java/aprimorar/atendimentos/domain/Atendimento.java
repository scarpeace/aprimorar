package aprimorar.atendimentos.domain;

import aprimorar.atendimentos.enums.TipoAtendimentoEnum;
import aprimorar.shared.exception.BusinessException;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatus;

// TODO: Adicionar campos do Google Calendar para a implementacao
@Entity
@Getter
@Table(name = "atendimentos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "inicio", nullable = false)
    private Instant inicio;

    @Column(name = "fim", nullable = false)
    private Instant fim;

    @Column(name = "repasse", precision = 19, scale = 2, nullable = false)
    private BigDecimal repasse;

    @Column(name = "valor", precision = 19, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAtendimentoEnum tipo;

    @Column(name = "data_pagamento_colaborador")
    private Instant dataPagamentoColaborador;

    @Column(name = "data_cobranca_aluno")
    private Instant dataCobrancaAluno;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "aluno_nome", nullable = false)
    private String alunoNome;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "colaborador_nome", nullable = false)
    private String colaboradorNome;

    protected Atendimento() {}

    public Atendimento(
        String descricao,
        Instant inicio,
        Double duracao,
        BigDecimal repasse,
        BigDecimal valor,
        TipoAtendimentoEnum tipo,
        UUID alunoId,
        String alunoNome,
        UUID colaboradorId,
        String colaboradorNome,
        Instant now
    ) {
        this.fim = calcularFim(inicio, duracao);
        validarDatas(inicio, now);
        validarValores(repasse, valor);
        validarParticipantes(alunoId, colaboradorId);
        validarTipo(tipo);

        this.titulo = montarTitulo(tipo, alunoNome, colaboradorNome);
        this.descricao = descricao;
        this.inicio = inicio;
        this.repasse = repasse;
        this.valor = valor;
        this.tipo = tipo;
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
        this.alunoNome = alunoNome;
        this.colaboradorNome = colaboradorNome;
    }

    public Atendimento update(
        String descricao,
        Instant inicio,
        Double duracao,
        BigDecimal repasse,
        BigDecimal valor,
        TipoAtendimentoEnum tipo,
        UUID alunoId,
        String alunoNome,
        UUID colaboradorId,
        String colaboradorNome,
        Instant now
    ) {
        this.fim = calcularFim(inicio, duracao);
        validarJanelaEdicao(now);
        validarDatas(inicio, now);
        validarValores(repasse, valor);
        validarParticipantes(alunoId, colaboradorId);
        validarTipo(tipo);

        this.titulo = montarTitulo(tipo, alunoNome, colaboradorNome);
        this.descricao = descricao;
        this.inicio = inicio;
        this.repasse = repasse;
        this.valor = valor;
        this.tipo = tipo;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.colaboradorId = colaboradorId;
        this.colaboradorNome = colaboradorNome;

        return this;
    }

    @Transient
    public Double getDuracao() {
        return Duration.between(inicio, fim).toMinutes() / 60.0;
    }

    @Transient
    public BigDecimal getLucro() {
        return valor.subtract(repasse);
    }



    public void alternarCobrancaAluno(Instant now) {
        if (this.dataCobrancaAluno != null) {
            this.dataCobrancaAluno = null;
        } else {
            this.dataCobrancaAluno = now;
        }
    }

    public void alternarPagamentoColaborador(Instant now) {
        if (this.dataPagamentoColaborador != null) {
            this.dataPagamentoColaborador = null;
        } else {
            this.dataPagamentoColaborador = now;
        }
    }

    public static Instant calcularFim(Instant inicio, Double duracao) {
        if (inicio == null) {
            throw new IllegalStateException("Data de inicio do atendimento e obrigatoria");
        }
        if (duracao == null) {
            throw new IllegalStateException("Duracao do atendimento e obrigatoria");
        }
        return inicio.plus((long) (duracao * 60), ChronoUnit.MINUTES);
    }

    public void validarJanelaEdicao(Instant now) {
        if (this.fim != null && now.isAfter(this.fim.plus(20, ChronoUnit.DAYS))) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A janela de 20 dias para editar as informacoes do atendimento encerrou");
        }
    }

    private void validarDatas(Instant inicio, Instant now) {
        if (this.fim.isBefore(inicio)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode ser anterior a data de inicio");
        }
        if (this.fim.isBefore(now)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode estar no passado");
        }
    }

    private void validarValores(BigDecimal repasse, BigDecimal valor) {
        if (repasse == null) {
            throw new IllegalStateException("Pagamento do atendimento e obrigatorio");
        }
        if (valor == null) {
            throw new IllegalStateException("Valor do atendimento e obrigatorio");
        }
        if (valor.compareTo(repasse) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que o pagamento");
        }
        if (valor.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que R$50,00");
        }
    }

    private void validarParticipantes(UUID alunoId, UUID colaboradorId) {
        if (alunoId == null) {
            throw new IllegalStateException("Um atendimento nao pode existir sem um aluno");
        }
        if (colaboradorId == null) {
            throw new IllegalStateException("Um atendimento nao pode existir sem um colaborador");
        }
    }

    private void validarTipo(TipoAtendimentoEnum tipo) {
        if (tipo == null) {
            throw new IllegalStateException("O conteudo do atendimento e obrigatorio");
        }
    }

    private String montarTitulo(TipoAtendimentoEnum tipo, String alunoNome, String colaboradorNome) {
        return tipo + " - Col: " + colaboradorNome + " - " + alunoNome;
    }
}
