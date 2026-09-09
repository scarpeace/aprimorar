package aprimorar.pessoas.colaborador;

import aprimorar.pessoas.Colaborador;
import aprimorar.pessoas.ColaboradorService;
import aprimorar.pessoas.colaborador.domain.ColaboradorEntity;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorEstadoInvalidoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.pessoas.colaborador.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.repository.specifications.ColaboradorSpecifications;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradoresOptionsDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private final UUID ghostColaboradorId;

    public ColaboradorServiceImpl(
        ColaboradorRepository colaboradorRepo,
        @Value("${aprimorar.ghost-colaborador-id}") String ghostColaboradorId
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.ghostColaboradorId = UUID.fromString(ghostColaboradorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Colaborador buscarPorId(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        return new Colaborador(colaborador.getId(), colaborador.getNome(), colaborador.getActive());
    }

    @Transactional(readOnly = true)
    public Page<ColaboradorResponseDTO> getColaboradores(ColaboradorFiltroRequest filtro, Pageable pageable) {
        Specification<ColaboradorEntity> spec = ColaboradorSpecifications.comFiltros(filtro, ghostColaboradorId);
        Page<ColaboradorEntity> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresPage.map(ColaboradorResponseDTO::toDto);
    }

    @Transactional(readOnly = true)
    public ColaboradorResponseDTO findById(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresOptionsDTO> getColaboradoresOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");

        return colaboradorRepo
            .findAll(ColaboradorSpecifications.isNotArchived().and(ColaboradorSpecifications.isNotGhost(ghostColaboradorId)), sort)
            .stream()
            .map(colaborador -> new ColaboradoresOptionsDTO(colaborador.getId(), colaborador.getNome()))
            .toList();
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        ColaboradorEntity colaborador = dto.toEntity();

        if (colaboradorRepo.existsByCpf(colaborador.getCpf())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(colaborador.getEmail())) {
            throw new ColaboradorDuplicadoException("Já existe um colaborador cadastrado com este e-mail.");
        }

        ColaboradorEntity savedColaborador = colaboradorRepo.save(colaborador);
        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(savedColaborador);
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);
        ColaboradorEntity requestedColaborador = dto.toEntity();

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new ColaboradorEstadoInvalidoException("Não é possível modificar o registro de sistema 'Colaborador Removido'.");
        }

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
        return ColaboradorResponseDTO.toDto(colaborador);
    }

    @Transactional
    public void archiveColaborador(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new ColaboradorEstadoInvalidoException("O registro não pode ser modificado.");
        }

        colaborador.archive();
        log.info("Colaborador {} arquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveColaborador(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new ColaboradorEstadoInvalidoException("O registro não pode ser modificado.");
        }

        colaborador.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        ColaboradorEntity colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new ColaboradorEstadoInvalidoException("O registro não pode ser modificado.");
        }

        ensureColaboradorArquivado(colaborador);
        colaboradorRepo.delete(colaborador);

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getNome().toUpperCase()
        );
    }

    private ColaboradorEntity findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo.findById(colaboradorId)
            .orElseThrow(() -> new ColaboradorNaoEncontradoException("Colaborador não encontrado no banco de dados"));
    }

    private void ensureColaboradorArquivado(ColaboradorEntity colaborador) {
        if (!Boolean.FALSE.equals(colaborador.getActive())) {
            throw new ColaboradorEstadoInvalidoException("O colaborador precisa estar arquivado antes de ser excluído.");
        }
    }
}
