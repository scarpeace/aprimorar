package aprimorar.atendimentos.individuais.atendimentos.service;

import aprimorar.atendimentos.individuais.atendimentos.domain.AtendimentoIndividualViewEntity;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.atendimentos.individuais.atendimentos.repository.AtendimentoIndividualViewRepository;
import aprimorar.atendimentos.individuais.atendimentos.repository.specifications.AtendimentoIndividualSpecifications;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.atendimento.AtendimentoIndividualFiltroRequest;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.atendimento.AtendimentoIndividualResponse;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioFiltroRequest;
import aprimorar.atendimentos.individuais.atendimentos.web.dto.calendario.AtendimentoIndividualCalendarioResponse;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoIndividualQueryService {

    private final AtendimentoIndividualViewRepository atendimentoConsultaRepository;

    public AtendimentoIndividualQueryService(AtendimentoIndividualViewRepository atendimentoConsultaRepository) {
        this.atendimentoConsultaRepository = atendimentoConsultaRepository;
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

}
