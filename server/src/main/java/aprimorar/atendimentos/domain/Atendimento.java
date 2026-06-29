package aprimorar.atendimentos.domain;

import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import aprimorar.shared.exception.BusinessException;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Entity
@Getter
@Table(name = "atendimentos")
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    @Column(name = "repasse", precision = 19, scale = 2, nullable = false)
    private BigDecimal repasse;

    @Column(name = "valor", precision = 19, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAtendimento tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusAtendimento status;

    @Column(name = "aluno_id", nullable = false)
    private UUID alunoId;

    @Column(name = "nome_aluno", nullable = false)
    private String nomeAluno;

    @Column(name = "colaborador_id", nullable = false)
    private UUID colaboradorId;

    @Column(name = "nome_colaborador", nullable = false)
    private String nomeColaborador;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Atendimento() {}

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Atendimento(
        String descricao,
        LocalDateTime inicio,
        Double duracao,
        BigDecimal repasse,
        BigDecimal valor,
        TipoAtendimento tipo,
        UUID alunoId,
        String nomeAluno,
        UUID colaboradorId,
        String nomeColaborador
    ) {
        this.fim = calcularFim(inicio, duracao);
        validarDatas(inicio);
        validarValores(repasse, valor);
        this.titulo = tipo.name();
        this.descricao = descricao;
        this.inicio = inicio;
        this.repasse = repasse;
        this.valor = valor;
        this.tipo = tipo;
        this.status = StatusAtendimento.AGENDADO;
        this.alunoId = alunoId;
        this.nomeAluno = nomeAluno;
        this.colaboradorId = colaboradorId;
        this.nomeColaborador = nomeColaborador;
    }

    public void cancelar(){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Evento já cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível cancelar um evento concluído");
        }
        this.status = StatusAtendimento.CANCELADO;
    }

    public void concluir(){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível concluir um evento cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Evento já concluido");
        }

        this.status = StatusAtendimento.CONCLUIDO;
    }

    public void reagendar(LocalDateTime inicio, Double duracao){
        if(this.status == StatusAtendimento.CANCELADO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível reagendar um evento cancelado");
        }

        if(this.status == StatusAtendimento.CONCLUIDO){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"Não é possível reagendar um evento concluído");
        }
        this.inicio = inicio;
        this.fim = calcularFim(inicio, duracao);
    }

    public void alterarParticipantes(UUID alunoId, UUID colaboradorId){
        this.alunoId = alunoId;
        this.colaboradorId = colaboradorId;
    }

    @Transient
    public Double getDuracao() {
        return Duration.between(inicio, fim).toMinutes() / 60.0;
    }

    @Transient
    public BigDecimal getLucro() {
        return valor.subtract(repasse);
    }

    public static LocalDateTime calcularFim(LocalDateTime inicio, Double duracao) {
        if (inicio == null) {
            throw new IllegalStateException("Data de inicio do atendimento é obrigatória");
        }
        if (duracao == null) {
            throw new IllegalStateException("Duração do atendimento é obrigatória");
        }
        return inicio.plus((long) (duracao * 60), ChronoUnit.MINUTES);
    }

    public void validarJanelaEdicao() {
        if (this.fim != null && LocalDateTime.now().isAfter(this.fim.plus(20, ChronoUnit.DAYS))) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A janela de 20 dias para editar as informações do atendimento encerrou");
        }
    }

    private void validarDatas(LocalDateTime inicio) {
        if (this.fim.isBefore(inicio)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode ser anterior a data de inicio");
        }
        if (this.fim.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Data de fim do atendimento nao pode estar no passado");
        }
    }

    private void validarValores(BigDecimal repasse, BigDecimal valor) {
        if (repasse == null) {
            throw new IllegalStateException("Pagamento do atendimento é obrigatório");
        }
        if (valor == null) {
            throw new IllegalStateException("Valor do atendimento é obrigatorio");
        }
        if (valor.compareTo(repasse) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que o pagamento");
        }
        if (valor.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que R$50,00");
        }
    }

    private String montarTitulo(Atendimento atendimento, String nomeColaborador, String nomeAluno) {
        return atendimento.getTipo().name() + ": " + nomeColaborador + " - " + nomeAluno;
    }
}
