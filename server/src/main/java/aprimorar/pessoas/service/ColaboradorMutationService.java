package aprimorar.pessoas.service;

import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.dto.colaborador.ColaboradorRequestDTO;
import aprimorar.pessoas.dto.colaborador.ColaboradorResponseDTO;
import aprimorar.pessoas.repository.ColaboradorRepository;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorMutationService {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorMutationService.class);

    private final ColaboradorRepository colaboradorRepo;
    private final AtendimentoRepository atendimentoRepo;
    private final UUID ghostColaboradorId;

    public ColaboradorMutationService(
        ColaboradorRepository colaboradorRepo,
        AtendimentoRepository atendimentoRepo,
        @Value("${aprimorar.ghost-colaborador-id}") String ghostColaboradorId
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.atendimentoRepo = atendimentoRepo;
        this.ghostColaboradorId = UUID.fromString(ghostColaboradorId);
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        Colaborador colaborador = dto.toEntity();

        if (colaboradorRepo.existsByCpf(colaborador.getCpf())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(colaborador.getEmail())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        Colaborador savedColaborador = colaboradorRepo.save(colaborador);
        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(savedColaborador);
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {

        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        Colaborador requestedColaborador = dto.toEntity();

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível modificar o registro de sistema 'Colaborador Removido'.");
        }

        if (colaboradorRepo.existsByEmailAndIdNot(requestedColaborador.getEmail(), colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
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
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        ensureColaboradorPodeSerArquivado(colaboradorId);
        colaborador.archive();
        log.info("Colaborador {} arquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        colaborador.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (ghostColaboradorId.equals(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        ensureColaboradorArquivado(colaborador);
        ensureColaboradorPodeSerArquivado(colaboradorId);

        atendimentoRepo.reassignAtendimentosColaboradorToGhost(colaboradorId, colaboradorRepo.getReferenceById(ghostColaboradorId));
        colaboradorRepo.delete(colaborador);

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getNome().toUpperCase()
        );
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado no banco de dados"));
    }

    private void ensureColaboradorPodeSerArquivado(UUID colaboradorId) {
        if (atendimentoRepo.colaboradorPossuiAtendimentoAgendado(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível arquivar um colaborador com atendimentos agendados.");
        }

        if (atendimentoRepo.colaboradorPossuiRepassePendente(colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível arquivar um colaborador com repasse pendente.");
        }
    }

    private void ensureColaboradorArquivado(Colaborador colaborador) {
        if (!Boolean.FALSE.equals(colaborador.getActive())) {
            throw new BusinessException(HttpStatus.CONFLICT, "O colaborador precisa estar arquivado antes de ser excluído.");
        }
    }

}
