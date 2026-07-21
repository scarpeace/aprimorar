package aprimorar.atendimentos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import aprimorar.atendimentos.dto.AlunoRelatorioResponse;
import aprimorar.atendimentos.dto.AlunoResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.CalendarioAtendimentosResponse;
import aprimorar.atendimentos.dto.CalendarioMensalAtendimentosResponse;
import aprimorar.atendimentos.dto.ColaboradorResumoFinanceiroResponse;
import aprimorar.atendimentos.dto.RelatorioAtendimentosResponse;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.repository.projections.AtendimentosReportProjection;
import aprimorar.atendimentos.repository.specifications.AtendimentoSpecifications;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ColaboradorRepository;

@Service
public class AtendimentoQueryService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoQueryService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AlunoRepository alunoRepo;
    private final ColaboradorRepository colaboradorRepo;

    public AtendimentoQueryService(
        AtendimentoRepository atendimentoRepo,
        AlunoRepository alunoRepo,
        ColaboradorRepository colaboradorRepo
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoRepo = alunoRepo;
        this.colaboradorRepo = colaboradorRepo;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoResponse> getAtendimentos(Pageable pageable,AtendimentoFiltroRequest filtro) {
        Specification<Atendimento> spec = AtendimentoSpecifications.comFiltros(filtro);
        Page<Atendimento> atendimentoPage = atendimentoRepo.findAll(spec, pageable);

        log.info("Consulta de atendimentos finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());
        return atendimentoPage.map(AtendimentoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponse findAtendimentoById(Long id) {
        Atendimento atendimento = atendimentoRepo.findById(id).orElseThrow(
            () -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento não encontrado"));

        log.info("Atendimento {} consultado com sucesso.", atendimento.getId());
        return AtendimentoResponse.toDto(atendimento);
    }

    @Transactional(readOnly = true)
    public CalendarioMensalAtendimentosResponse getCalendarioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);
        var relatorio = getRelatorioAtendimentos(mes);
        List<CalendarioAtendimentosResponse> eventos = atendimentoRepo.getCalendarioMensal(dataInicio, dataFim);

        return new CalendarioMensalAtendimentosResponse(
            mes,
            relatorio.totalAtendimentos(),
            relatorio.totalAulas(),
            relatorio.totalMentoria(),
            relatorio.totalTerapia(),
            relatorio.totalOV(),
            relatorio.totalENEM(),
            relatorio.totalPAS(),
            relatorio.totalOutros(),
            eventos
        );
    }

    @Transactional(readOnly = true)
    public RelatorioAtendimentosResponse getRelatorioAtendimentos(YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        AtendimentosReportProjection resumo = atendimentoRepo.getRelatorioMensal(dataInicio, dataFim);

        var totalAtendimentos = resumo.getTotalAulas() + resumo.getTotalMentoria() + resumo.getTotalTerapia() + resumo.getTotalOV() + resumo.getTotalENEM() + resumo.getTotalPAS() + resumo.getTotalOutros();

        return new RelatorioAtendimentosResponse(
            totalAtendimentos,
            resumo.getTotalAulas(),
            resumo.getTotalMentoria(),
            resumo.getTotalTerapia(),
            resumo.getTotalOV(),
            resumo.getTotalENEM(),
            resumo.getTotalPAS(),
            resumo.getTotalOutros()
        );
    }

    @Transactional(readOnly = true)
    public ColaboradorResumoFinanceiroResponse getResumoFinanceiroColaborador(UUID colaboradorId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A data final não pode ser anterior à data inicial");
        }

        if (!colaboradorRepo.existsById(colaboradorId)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado");
        }

        return atendimentoRepo.getResumoFinanceiroColaborador(
            colaboradorId,
            dataInicio.atStartOfDay(),
            dataFim.atTime(LocalTime.MAX)
        );
    }

    @Transactional(readOnly = true)
    public AlunoResumoFinanceiroResponse getResumoFinanceiroAluno(UUID alunoId, YearMonth mes) {
        var dataInicio = mes.atDay(1).atStartOfDay();
        var dataFim = mes.atEndOfMonth().atTime(LocalTime.MAX);

        return new AlunoResumoFinanceiroResponse(
            atendimentoRepo.getTotalPagamentoPagoAluno(alunoId, dataInicio, dataFim),
            atendimentoRepo.getTotalPagamentoPendenteAluno(alunoId, dataInicio, dataFim)
        );
    }

    @Transactional(readOnly = true)
    public AlunoRelatorioResponse getRelatorioAluno(UUID alunoId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "A data final não pode ser anterior à data inicial");
        }

        var aluno = alunoRepo.findById(alunoId).orElseThrow(
            () -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado")
        );
        var responsavel = aluno.getResponsavel();
        var atendimentos = atendimentoRepo.findRelatorioAluno(
            alunoId,
            dataInicio.atStartOfDay(),
            dataFim.atTime(LocalTime.MAX)
        );

        var valorTotal = atendimentos.stream()
            .map(Atendimento::getPagamentoAluno)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        var valorPago = atendimentos.stream()
            .filter(atendimento -> atendimento.getDataPagamentoAluno() != null)
            .map(Atendimento::getPagamentoAluno)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AlunoRelatorioResponse(
            new AlunoRelatorioResponse.AlunoDados(aluno.getId(), aluno.getNome(), aluno.getEscola()),
            new AlunoRelatorioResponse.ResponsavelDados(
                responsavel.getId(),
                responsavel.getNome(),
                responsavel.getEmail(),
                responsavel.getTelefone()
            ),
            new AlunoRelatorioResponse.Periodo(dataInicio, dataFim),
            new AlunoRelatorioResponse.Resumo(
                atendimentos.size(),
                valorTotal,
                valorPago,
                valorTotal.subtract(valorPago)
            ),
            atendimentos.stream()
                .map(atendimento -> new AlunoRelatorioResponse.Item(
                    atendimento.getId(),
                    atendimento.getDataHoraInicio(),
                    atendimento.getDataHoraFim(),
                    atendimento.getTipo(),
                    atendimento.getStatus(),
                    atendimento.getColaborador().getNome(),
                    atendimento.getPagamentoAluno(),
                    atendimento.getDataPagamentoAluno()
                ))
                .toList()
        );
    }
}
