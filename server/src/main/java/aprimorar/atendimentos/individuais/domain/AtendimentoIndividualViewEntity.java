package aprimorar.atendimentos.individuais.domain;

import aprimorar.atendimentos.individuais.domain.enums.TipoAtendimento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "vw_atendimentos_individuais")
public class AtendimentoIndividualViewEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoAtendimento tipo;

    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim")
    private LocalDateTime dataHoraFim;

    @Column(name = "aluno_id")
    private UUID alunoId;

    @Column(name = "aluno_nome")
    private String alunoNome;

    @Column(name = "colaborador_id")
    private UUID colaboradorId;

    @Column(name = "colaborador_nome")
    private String colaboradorNome;

    @Column(name = "cobranca_id")
    private Long cobrancaId;

    @Column(name = "cobranca_valor")
    private BigDecimal cobrancaValor;

    @Column(name = "cobranca_status")
    private String cobrancaStatus;

    @Column(name = "cobranca_data_pagamento")
    private LocalDateTime cobrancaDataPagamento;

    @Column(name = "cobranca_forma_pagamento")
    private String cobrancaFormaPagamento;

    @Column(name = "cobranca_comprovante_url")
    private String cobrancaComprovanteUrl;

    @Column(name = "repasse_id")
    private Long repasseId;

    @Column(name = "repasse_valor")
    private BigDecimal repasseValor;

    @Column(name = "repasse_status")
    private String repasseStatus;

    @Column(name = "repasse_data_repasse")
    private LocalDateTime repasseDataRepasse;

    @Column(name = "repasse_forma_pagamento")
    private String repasseFormaPagamento;

    @Column(name = "repasse_comprovante_url")
    private String repasseComprovanteUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AtendimentoIndividualViewEntity() {
    }
}
