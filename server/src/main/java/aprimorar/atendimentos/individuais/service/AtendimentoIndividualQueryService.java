package aprimorar.atendimentos.individuais.service;

import aprimorar.atendimentos.individuais.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.domain.enums.StatusRepasseIndividual;
import aprimorar.atendimentos.individuais.domain.exception.CobrancaIndividualNaoEncontradoException;
import aprimorar.atendimentos.individuais.domain.exception.RepasseIndividualNaoEncontradoException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.atendimentos.individuais.repository.repasse.RepasseIndividualRepository;
import aprimorar.atendimentos.individuais.repository.view.AtendimentoIndividualSpecifications;
import aprimorar.atendimentos.individuais.repository.view.AtendimentoIndividualViewRepository;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.atendimentos.individuais.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.calendario.AtendimentoIndividualCalendarioResponse;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CobrancaIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.cobranca.CobrancaIndividualResponse;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseIndividualResponse;
import aprimorar.atendimentos.individuais.web.dto.repasse.RepasseLoteResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoIndividualQueryService {

    private final AtendimentoIndividualViewRepository atendimentoConsultaRepository;
    private final RepasseIndividualRepository repasseRepository;

    public AtendimentoIndividualQueryService(
        AtendimentoIndividualViewRepository atendimentoConsultaRepository,
        RepasseIndividualRepository repasseRepository
    ) {
        this.atendimentoConsultaRepository = atendimentoConsultaRepository;
        this.repasseRepository = repasseRepository;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoIndividualResponse> buscarAtendimentos(Pageable pageable, AtendimentoIndividualFiltroRequest filtro) {
        Specification<AtendimentoIndividualViewEntity> specification =
            AtendimentoIndividualSpecifications.paraAtendimentos(filtro);

        return atendimentoConsultaRepository.findAll(specification, pageable)
            .map(AtendimentoIndividualResponse::toDto);
    }

    @Transactional(readOnly = true)
    public AtendimentoIndividualResponse buscarAtendimentoPorId(Long id) {
        AtendimentoIndividualViewEntity atendimento = atendimentoConsultaRepository.findById(id)
            .orElseThrow(AtendimentoIndividualNaoEncontradoException::new);

        return AtendimentoIndividualResponse.toDto(atendimento);
    }

    @Transactional(readOnly = true)
    public List<AtendimentoIndividualCalendarioResponse> buscarCalendario(
        AtendimentoIndividualCalendarioFiltroRequest filtro
    ) {
        return atendimentoConsultaRepository.findAll(
            AtendimentoIndividualSpecifications.paraCalendario(filtro),
            Sort.by(Sort.Direction.ASC, "dataHoraInicio")
        ).stream().map(AtendimentoIndividualCalendarioResponse::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<CobrancaIndividualResponse> buscarCobrancas(
        CobrancaIndividualFiltroRequest filtro,
        Pageable pageable
    ) {
        return atendimentoConsultaRepository.findAll(
            AtendimentoIndividualSpecifications.paraCobrancas(filtro), pageable
        ).map(CobrancaIndividualResponse::toDto);
    }

    @Transactional(readOnly = true)
    public CobrancaIndividualResponse buscarCobrancaPorId(Long cobrancaId) {
        return atendimentoConsultaRepository.findByCobrancaId(cobrancaId)
            .map(CobrancaIndividualResponse::toDto)
            .orElseThrow(CobrancaIndividualNaoEncontradoException::new);
    }

    @Transactional(readOnly = true)
    public Page<RepasseIndividualResponse> buscarRepasses(
        RepasseIndividualFiltroRequest filtro,
        Pageable pageable
    ) {
        return atendimentoConsultaRepository.findAll(
            AtendimentoIndividualSpecifications.paraRepasses(filtro), pageable
        ).map(RepasseIndividualResponse::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RepasseLoteResponse> buscarLotesDeRepasse(UUID colaboradorId, Pageable pageable) {
        Pageable pagination = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return repasseRepository.findLotesPorColaboradorId(
            colaboradorId,
            StatusRepasseIndividual.PAGO,
            pagination
        ).map(RepasseLoteResponse::toDto);
    }

    @Transactional(readOnly = true)
    public RepasseIndividualResponse buscarRepassePorId(Long repasseId) {
        return atendimentoConsultaRepository.findByRepasseId(repasseId)
            .map(RepasseIndividualResponse::toDto)
            .orElseThrow(RepasseIndividualNaoEncontradoException::new);
    }
}
