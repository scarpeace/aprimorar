package aprimorar.atendimentos.individuais.service;

import aprimorar.atendimentos.individuais.domain.AtendimentoConsultaViewEntity;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoNaoEncontradoException;
import aprimorar.atendimentos.individuais.repository.AtendimentoConsultaRepository;
import aprimorar.atendimentos.individuais.repository.specifications.AtendimentoConsultaSpecifications;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoQueryService {

    private final AtendimentoConsultaRepository atendimentoConsultaRepository;

    public AtendimentoQueryService(AtendimentoConsultaRepository atendimentoConsultaRepository) {
        this.atendimentoConsultaRepository = atendimentoConsultaRepository;
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoResponse> getAtendimentos(Pageable pageable, AtendimentoFiltroRequest filtro) {
        Specification<AtendimentoConsultaViewEntity> specification =
            AtendimentoConsultaSpecifications.comFiltros(filtro);

        return atendimentoConsultaRepository.findAll(specification, pageable)
            .map(AtendimentoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponse findAtendimentoById(Long id) {
        AtendimentoConsultaViewEntity atendimento = atendimentoConsultaRepository.findById(id)
            .orElseThrow(AtendimentoNaoEncontradoException::new);

        return AtendimentoResponse.toDto(atendimento);
    }
}
