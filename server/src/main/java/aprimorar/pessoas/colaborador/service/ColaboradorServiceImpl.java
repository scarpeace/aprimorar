package aprimorar.pessoas.colaborador.service;

import aprimorar.pessoas.colaborador.api.ColaboradorService;
import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.pessoas.colaborador.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.repository.specifications.ColaboradorSpecifications;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorDetailResponseDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorListResponseDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradoresOptionsDTO;
import java.util.List;
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
public class ColaboradorServiceImpl implements ColaboradorService {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorServiceImpl.class);

    private final ColaboradorRepository colaboradorRepo;

    public ColaboradorServiceImpl(ColaboradorRepository colaboradorRepo) {
        this.colaboradorRepo = colaboradorRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID colaboradorId) {
        return colaboradorRepo.existsById(colaboradorId);
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorListResponseDTO> getColaboradores(ColaboradorFiltroRequest filtro, Pageable pageable) {
        Specification<ColaboradorEntity> spec = ColaboradorSpecifications.comFiltros(filtro);
        Page<ColaboradorEntity> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresPage.map(ColaboradorListResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ColaboradorDetailResponseDTO findById(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getNome().toUpperCase());
        return ColaboradorDetailResponseDTO.from(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresOptionsDTO> getColaboradoresOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        return colaboradorRepo
            .findAll(ColaboradorSpecifications.isActive(), sort)
            .stream()
            .map(colaborador -> new ColaboradoresOptionsDTO(colaborador.getId(), colaborador.getNome()))
            .toList();
    }

    @Transactional
    public UUID createColaborador(ColaboradorRequestDTO dto) {
        ColaboradorEntity colaborador = dto.toEntity();

        if (colaboradorRepo.existsByCpf(colaborador.getCpf())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(colaborador.getEmail())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este e-mail.");
        }

        ColaboradorEntity savedColaborador = colaboradorRepo.save(colaborador);
        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getNome().toUpperCase());
        return savedColaborador.getId();
    }

    @Transactional
    public void updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        ColaboradorEntity requestedColaborador = dto.toEntity();

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
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        colaborador.deactivate();
        log.info("Colaborador {} desativado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void activateColaborador(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        colaborador.activate();
        log.info("Colaborador {} ativado com sucesso.", colaborador.getNome().toUpperCase());
    }

    private ColaboradorEntity findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorNaoEncontradoException("Colaborador não encontrado no banco de dados"));
    }

}
