package aprimorar.agendamento.colaboradores.service;

import aprimorar.financeiro.api.repasses_particular.RepasseApi;
import aprimorar.agendamento.colaboradores.domain.Colaborador;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorDuplicadoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
import aprimorar.agendamento.colaboradores.repository.ColaboradorRepository;
import aprimorar.agendamento.colaboradores.repository.specifications.ColaboradorSpecifications;
import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorDetailResponse;
import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorFiltroRequest;
import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorListResponse;
import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradorRequest;
import aprimorar.agendamento.colaboradores.web.dto.colaborador.ColaboradoresOptionsResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorService {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepository colaboradorRepo;
    private final RepasseApi repasseApi;

    public ColaboradorService(ColaboradorRepository colaboradorRepo, RepasseApi repasseApi) {
        this.colaboradorRepo = colaboradorRepo;
        this.repasseApi = repasseApi;
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID colaboradorId) {
        return colaboradorRepo.existsById(colaboradorId);
    }

    @Transactional(readOnly = true)
    public Optional<Colaborador> findEntityById(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorListResponse> getColaboradores(ColaboradorFiltroRequest filtro, Pageable pageable) {
        Specification<Colaborador> spec = ColaboradorSpecifications.comFiltros(filtro);
        Page<Colaborador> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresPage.map(ColaboradorListResponse::toDto);
    }

    @Transactional(readOnly = true)
    public ColaboradorDetailResponse findById(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getNome().toUpperCase());
        return ColaboradorDetailResponse.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresOptionsResponse> getColaboradoresOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        return colaboradorRepo
            .findAll(ColaboradorSpecifications.isActive(), sort)
            .stream()
            .map(colaborador -> new ColaboradoresOptionsResponse(colaborador.getId(), colaborador.getNome()))
            .toList();
    }

    @Transactional
    public UUID createColaborador(ColaboradorRequest request) {
        Colaborador colaborador = request.toEntity();

        if (colaboradorRepo.existsByCpf(colaborador.getCpf())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(colaborador.getEmail())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este e-mail.");
        }

        Colaborador savedColaborador = colaboradorRepo.save(colaborador);
        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getNome().toUpperCase());
        return savedColaborador.getId();
    }

    @Transactional
    public void updateColaborador(UUID colaboradorId, ColaboradorRequest request) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        Colaborador requestedColaborador = request.toEntity();

        if (colaboradorRepo.existsByEmailAndIdNot(requestedColaborador.getEmail(), colaboradorId)) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador utilizando este e-mail.");
        }

        colaborador.update(
            requestedColaborador.getNome(),
            requestedColaborador.getDataNascimento(),
            requestedColaborador.getPix(),
            requestedColaborador.getTelefone(),
            requestedColaborador.getEmail(),
            requestedColaborador.getFuncao(),
            requestedColaborador.getEndereco()
        );

        log.info("Colaborador {} atualizado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void deactivateColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (repasseApi.possuiPendenciaPorColaboradorId(colaboradorId)) {
            throw new ColaboradorPossuiRepassePendenteException();
        }

        colaborador.deactivate();
        log.info("Colaborador {} desativado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void activateColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        colaborador.activate();
        log.info("Colaborador {} ativado com sucesso.", colaborador.getNome().toUpperCase());
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorNaoEncontradoException("Colaborador não encontrado no banco de dados"));
    }

}
