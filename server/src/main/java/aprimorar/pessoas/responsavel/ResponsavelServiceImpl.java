package aprimorar.pessoas.responsavel;

import aprimorar.pessoas.Responsavel;
import aprimorar.pessoas.ResponsavelService;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.responsavel.domain.ResponsavelEntity;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelDuplicadoException;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelEstadoInvalidoException;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelNaoEncontradoException;
import aprimorar.pessoas.responsavel.repository.ResponsavelRepository;
import aprimorar.pessoas.responsavel.repository.specifications.ResponsavelSpecifications;
import aprimorar.pessoas.responsavel.web.dto.ResponsavelFiltroRequest;
import aprimorar.pessoas.responsavel.web.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.web.dto.ResponsavelResponseDTO;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResponsavelServiceImpl implements ResponsavelService {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelServiceImpl.class);

    private final ResponsavelRepository responsavelRepo;
    private final AlunoRepository alunoRepo;

    public ResponsavelServiceImpl(ResponsavelRepository responsavelRepo, AlunoRepository alunoRepo) {
        this.responsavelRepo = responsavelRepo;
        this.alunoRepo = alunoRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Responsavel buscarPorId(UUID responsavelId) {
        ResponsavelEntity responsavel = findResponsavelOrThrow(responsavelId);
        return new Responsavel(
            responsavel.getId(),
            responsavel.getNome(),
            responsavel.getEmail(),
            responsavel.getTelefone()
        );
    }

    @Transactional(readOnly = true)
    public Page<ResponsavelResponseDTO> getResponsaveis(ResponsavelFiltroRequest filtro, Pageable pageable) {
        Specification<ResponsavelEntity> spec = ResponsavelSpecifications.comFiltros(filtro);
        Page<ResponsavelEntity> responsaveisPage = responsavelRepo.findAll(spec, pageable);

        log.info("Consulta de responsáveis finalizada, {} registros encontrados.", responsaveisPage.getTotalElements());
        return responsaveisPage.map(ResponsavelResponseDTO::toDto);
    }

    @Transactional(readOnly = true)
    public List<ResponsavelResponseDTO> getResponsaveisList() {
        List<ResponsavelEntity> responsaveis = responsavelRepo.findAll();
        log.info("Consulta de opções de responsáveis finalizada, {} registros encontrados.", responsaveis.size());
        return responsaveis.stream().map(ResponsavelResponseDTO::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ResponsavelResponseDTO findResponsavelById(UUID responsavelId) {
        ResponsavelEntity responsavel = findResponsavelOrThrow(responsavelId);
        log.info("Responsável {} consultado com sucesso.", responsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(responsavel);
    }

    @Transactional
    public ResponsavelResponseDTO createResponsavel(ResponsavelRequestDTO dto) {
        ResponsavelEntity responsavel = dto.toEntity();

        if (responsavelRepo.existsByCpf(responsavel.getCpf())) {
            throw new ResponsavelDuplicadoException("Já existe um responsável cadastrado com este CPF.");
        }

        if (responsavelRepo.existsByEmail(responsavel.getEmail())) {
            throw new ResponsavelDuplicadoException("Já existe um responsável cadastrado com este e-mail.");
        }

        ResponsavelEntity savedResponsavel = responsavelRepo.save(responsavel);
        log.info("Responsável {} cadastrado com sucesso.", savedResponsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(savedResponsavel);
    }

    @Transactional
    public ResponsavelResponseDTO updateResponsavel(UUID responsavelId, ResponsavelRequestDTO dto) {
        ResponsavelEntity responsavel = findResponsavelOrThrow(responsavelId);
        ResponsavelEntity requestedResponsavel = dto.toEntity();

        if (responsavelRepo.existsByCpfAndIdNot(requestedResponsavel.getCpf(), responsavelId)) {
            throw new ResponsavelDuplicadoException("Já existe um responsável utilizando este CPF.");
        }

        if (responsavelRepo.existsByEmailAndIdNot(requestedResponsavel.getEmail(), responsavelId)) {
            throw new ResponsavelDuplicadoException("Já existe um responsável utilizando este e-mail.");
        }

        responsavel.update(
            requestedResponsavel.getNome(),
            requestedResponsavel.getDataNascimento(),
            requestedResponsavel.getTelefone(),
            requestedResponsavel.getEmail()
        );

        log.info("Responsável {} atualizado com sucesso.", responsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(responsavel);
    }

    @Transactional
    public void deleteResponsavel(UUID responsavelId) {
        ResponsavelEntity responsavel = findResponsavelOrThrow(responsavelId);

        if (!alunoRepo.findAllByResponsavelId(responsavelId).isEmpty()) {
            throw new ResponsavelEstadoInvalidoException(
                "Este responsável possui alunos vinculados. Exclua os alunos antes de excluir o responsável."
            );
        }

        responsavelRepo.delete(responsavel);
    }

    private ResponsavelEntity findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo.findById(responsavelId)
            .orElseThrow(() -> new ResponsavelNaoEncontradoException("Responsável não encontrado no banco de dados"));
    }
}
