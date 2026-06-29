package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AtendimentoRequest;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import aprimorar.atendimentos.dto.ReagendarAtendimentoRequest;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.exception.BusinessException;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ColaboradorRepository;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoMutationService.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AlunoRepository alunoRepo;
    private final ColaboradorRepository colaboradorRepo;

    public AtendimentoMutationService(AtendimentoRepository atendimentoRepo, AlunoRepository alunoRepo, ColaboradorRepository colaboradorRepo) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoRepo = alunoRepo;
        this.colaboradorRepo = colaboradorRepo;
    }

    @Transactional
    public AtendimentoResponse agendar(AtendimentoRequest dto) {
        Aluno aluno = findAlunoOrThrow(dto.alunoId());
        Colaborador colaborador = findColaboradorOrThrow(dto.colaboradorId());
        Atendimento atendimento = dto.toEntity(aluno, colaborador);

        validarDisponibilidadeDosParticipantes(atendimento);

        Atendimento saved = atendimentoRepo.save(atendimento);

        log.info("Atendimento {} cadastrado com sucesso.", saved.getId());
        return AtendimentoResponse.toDto(saved);
    }

    public AtendimentoResponse update(Long id, AtendimentoRequest dto) {
        Atendimento atendimento = findAtendimentoOrThrow(id);
        Aluno aluno = findAlunoOrThrow(dto.alunoId());
        Colaborador colaborador = findColaboradorOrThrow(dto.colaboradorId());

        atendimento.update(
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            dto.tipo(),
            aluno,
            colaborador,
            dto.pagamentoAluno(),
            dto.repasseColaborador()
        );

        Atendimento saved = atendimentoRepo.save(atendimento);
        log.info("Atendimento {} atualizado com sucesso.", saved.getId());
        return AtendimentoResponse.toDto(saved);
    }

    @Transactional
    public void cancelar(Long id){
        Atendimento atendimento = findAtendimentoOrThrow(id);

        atendimento.cancelar();
        log.info("Atendimento {} cancelado com sucesso.", atendimento.getId());
    }

    @Transactional
    public void reagendar(Long id, ReagendarAtendimentoRequest request){
        Atendimento atendimento = findAtendimentoOrThrow(id);

        atendimento.reagendar(request.novoInicio(), request.novoFim());
        log.info("Atendimento {} reagendado com sucesso.", atendimento.getId());
    }

    @Transactional
    public void concluir(Long id){
        Atendimento atendimento = findAtendimentoOrThrow(id);

        atendimento.concluir();
        log.info("Atendimento {} concluído com sucesso.", atendimento.getId());
    }

    @Transactional
    public void alterarParticipantes(Long id, UUID alunoId, UUID colaboradorId){
        Atendimento atendimento = findAtendimentoOrThrow(id);
        Aluno aluno = findAlunoOrThrow(alunoId);
        Colaborador colaborador = findColaboradorOrThrow(colaboradorId);

        atendimento.alterarParticipantes(aluno, colaborador);
        log.info("Participantes do evento {} atualizados com sucesso", atendimento.getId());
    }

    @Transactional
    public void excluir(Long id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);

        if(atendimento.getStatus() == StatusAtendimento.CONCLUIDO) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Um atendimento concluído não pode ser excluído.");
        }

        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getId());
    }

    private Atendimento findAtendimentoOrThrow(Long id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento não encontrado"));
    }

    private Aluno findAlunoOrThrow(UUID id) {
        return alunoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
    }

    private Colaborador findColaboradorOrThrow(UUID id) {
        return colaboradorRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado"));
    }

    private void validarDisponibilidadeDosParticipantes(
        Atendimento atendimento
    ) {
        if (atendimentoRepo.hasAtendimetoConflitante(
            atendimento.getAlunoId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getId()
        )) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O aluno informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.colaboradorPossuiAtendimentoConflitante(
            atendimento.getColaboradorId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getId()
        )) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
