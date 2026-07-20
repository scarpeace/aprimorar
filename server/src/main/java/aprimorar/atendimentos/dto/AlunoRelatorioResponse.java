package aprimorar.atendimentos.dto;

import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.enums.TipoAtendimento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AlunoRelatorioResponse(
    AlunoDados aluno,
    ResponsavelDados responsavel,
    Periodo periodo,
    Resumo resumo,
    List<Item> atendimentos
) {
    public record AlunoDados(UUID id, String nome, String escola) {}

    public record ResponsavelDados(UUID id, String nome, String email, String telefone) {}

    public record Periodo(LocalDate dataInicio, LocalDate dataFim) {}

    public record Resumo(
        int totalAtendimentos,
        BigDecimal valorTotal,
        BigDecimal valorPago,
        BigDecimal valorPendente
    ) {}

    public record Item(
        Long id,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        TipoAtendimento tipo,
        StatusAtendimento status,
        String nomeColaborador,
        BigDecimal pagamentoAluno,
        LocalDateTime dataPagamentoAluno
    ) {}
}
