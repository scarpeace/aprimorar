package aprimorar.atendimentos.internal;

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
@Table(name = "tb_atendimentos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "atualizado_em")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "titulo", nullable = false)
    private String title;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String description;

    @Column(name = "inicio_em", nullable = false)
    private Instant startDate;

    @Column(name = "fim_em", nullable = false)
    private Instant endDate;

    @Column(name = "pagamento_professor", precision = 19, scale = 2, nullable = false)
    private BigDecimal payment;

    @Column(name = "cobranca_aluno", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atendimento", nullable = false)
    private TipoAtendimentoEnum content;

    @Column(name = "data_pagamento_colaborador", nullable = true)
    private Instant employeePaymentDate;

    @Column(name = "data_cobranca_aluno", nullable = true)
    private Instant studentChargeDate;

    @Column(name = "aluno_id", nullable = false)
    private UUID studentId;

    @Column(name = "aluno_nome", nullable = false)
    private String studentName;

    @Column(name = "colaborador_id", nullable = false)
    private UUID employeeId;

    @Column(name = "colaborador_nome", nullable = false)
    private String employeeName;

    protected Atendimento() {}

    public Atendimento(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        TipoAtendimentoEnum content,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant now
    ) {
        this.endDate = calculateEndDate(startDate, duration);
        validateDates(startDate, now);
        validateAmounts(payment, price);
        validateParticipants(studentId, employeeId);
        validateContent(content);

        this.title = buildTitle(content, studentName, employeeName);
        this.description = description;
        this.startDate = startDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.studentId = studentId;
        this.employeeId = employeeId;
        this.studentName = studentName;
        this.employeeName = employeeName;
    }

    public Atendimento update(
        String description,
        Instant startDate,
        Double duration,
        BigDecimal payment,
        BigDecimal price,
        TipoAtendimentoEnum content,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant now
    ) {
        this.endDate = calculateEndDate(startDate, duration);
        validateEditWindow(now);
        validateDates(startDate, now);
        validateAmounts(payment, price);
        validateParticipants(studentId, employeeId);
        validateContent(content);

        this.title = buildTitle(content, studentName, employeeName);
        this.description = description;
        this.startDate = startDate;
        this.payment = payment;
        this.price = price;
        this.content = content;
        this.studentId = studentId;
        this.studentName = studentName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;

        return this;
    }

    @Transient
    public Double getDuration() {
        return Duration.between(startDate, endDate).toMinutes() / 60.0;
    }

    @Transient
    public BigDecimal getProfit() {
        return price.subtract(payment);
    }



    public void toggleStudentCharge(Instant now) {
        if (this.studentChargeDate != null) {
            this.studentChargeDate = null;
        } else {
            this.studentChargeDate = now;
        }
    }

    public void toggleEmployeePayment(Instant now) {
        if (this.employeePaymentDate != null) {
            this.employeePaymentDate = null;
        } else {
            this.employeePaymentDate = now;
        }
    }

    public static Instant calculateEndDate(Instant startDate, Double duration) {
        if (startDate == null) {
            throw new IllegalStateException("Data de inicio do atendimento e obrigatoria");
        }
        if (duration == null) {
            throw new IllegalStateException("Duracao do atendimento e obrigatoria");
        }
        return startDate.plus((long) (duration * 60), ChronoUnit.MINUTES);
    }

    public void validateEditWindow(Instant now) {
        if (this.endDate != null && now.isAfter(this.endDate.plus(20, ChronoUnit.DAYS))) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A janela de 20 dias para editar as informacoes do atendimento encerrou");
        }
    }

    private void validateDates(Instant startDate, Instant now) {
        if (this.endDate.isBefore(startDate)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode ser anterior a data de inicio");
        }
        if (this.endDate.isBefore(now)) {
            throw new IllegalStateException("Data de fim do atendimento nao pode estar no passado");
        }
    }

    private void validateAmounts(BigDecimal payment, BigDecimal price) {
        if (payment == null) {
            throw new IllegalStateException("Pagamento do atendimento e obrigatorio");
        }
        if (price == null) {
            throw new IllegalStateException("Valor do atendimento e obrigatorio");
        }
        if (price.compareTo(payment) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que o pagamento");
        }
        if (price.compareTo(BigDecimal.valueOf(50)) < 0) {
            throw new IllegalStateException("O valor do atendimento nao pode ser menor que R$50,00");
        }
    }

    private void validateParticipants(UUID studentId, UUID employeeId) {
        if (studentId == null) {
            throw new IllegalStateException("Um atendimento nao pode existir sem um estudante");
        }
        if (employeeId == null) {
            throw new IllegalStateException("Um atendimento nao pode existir sem um colaborador");
        }
    }

    private void validateContent(TipoAtendimentoEnum content) {
        if (content == null) {
            throw new IllegalStateException("O conteudo do atendimento e obrigatorio");
        }
    }

    private String buildTitle(TipoAtendimentoEnum content, String studentName, String employeeName) {
        return content + " - Col: " + employeeName + " - " + studentName;
    }
}
