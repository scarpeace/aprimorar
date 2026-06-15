package aprimorar.atendimentos.service;

import aprimorar.atendimentos.dto.AtendimentoRequest;
import aprimorar.atendimentos.dto.AtendimentoResponse;
import aprimorar.atendimentos.dto.ReagendarAtendimentoRequest;
import aprimorar.atendimentos.enums.StatusAtendimento;
import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.pessoas.api.AlunoQueryApi;
import aprimorar.pessoas.api.ColaboradorQueryApi;
import aprimorar.shared.exception.BusinessException;

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
    private final AlunoQueryApi alunoQueryApi;
    private final ColaboradorQueryApi colaboradorQueryApi;

    public AtendimentoMutationService(
        AtendimentoRepository atendimentoRepo,
        AlunoQueryApi alunoQueryApi,
        ColaboradorQueryApi colaboradorQueryApi
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoQueryApi = alunoQueryApi;
        this.colaboradorQueryApi = colaboradorQueryApi;
    }

    @Transactional
    public AtendimentoResponse agendar(AtendimentoRequest dto) {
        Atendimento atendimento = dto.toEntity();

        if(!alunoQueryApi.existsById(atendimento.getAlunoId())){
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Aluno não encontrado para a criação do atendimento");
        }

        if(!colaboradorQueryApi.existsById(atendimento.getColaboradorId())){
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Colaborador não encontrado para a criação do atendimento");
        }

        validarDisponibilidadeDosParticipantes(atendimento);

        Atendimento saved = atendimentoRepo.save(atendimento);

//        Transacao entradaAluno = new Transacao(
//                atendimento.getAlunoId(),
//                'idDoAdmin',
//                atendimento.getValor(),
//                null,
//                TipoTransacao.ENTRADA,
//                null,
//                StatusTransacao.PENDENTE,
//                CategoriaTransacao.PGTO_ALUNO
//                );
//
//        Transacao saidaProfessor = new Transacao(
//                'idDoAdmin',
//                atendimento.getColaboradorId(),
//                atendimento.getRepasse(),
//                null,
//                TipoTransacao.SAIDA,
//                null,
//                StatusTransacao.PENDENTE,
//                CategoriaTransacao.PGTO_COLABORADOR
//        );
//
//        transacaoRepo.save(entradaAluno, saidaProfessor);

        log.info("Atendimento {} cadastrado com sucesso.", saved.getTitulo().toUpperCase());
        return AtendimentoResponse.toDto(saved);
    }

    @Transactional
    public void cancelar(UUID id){
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.cancelar();
        log.info("Atendimento {} cancelado com sucesso.", atendimento.getTitulo().toUpperCase());
    }

    @Transactional
    public void reagendar(UUID id, ReagendarAtendimentoRequest request){
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.reagendar(request.novoInicio(), request.duracao());
        log.info("Atendimento {} reagendado com sucesso.", atendimento.getTitulo().toUpperCase());
    }

    @Transactional
    public void concluir(UUID id){
        Atendimento atendimento = findAtendimentoOrThrow(id);
        atendimento.concluir();
        log.info("Atendimento {} concluído com sucesso.", atendimento.getTitulo().toUpperCase());
    }

    @Transactional
    public void alterarParticipantes(UUID id, UUID alunoId, UUID colaboradorId){
        Atendimento atendimento = findAtendimentoOrThrow(id);

        atendimento.alterarParticipantes(alunoId, colaboradorId);
        log.info("Participantes do evento {} atualizados com sucesso", atendimento.getId());
    }

    @Transactional
    public void excluir(UUID id) {
        Atendimento atendimento = findAtendimentoOrThrow(id);

        if(atendimento.getStatus() == StatusAtendimento.CONCLUIDO) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Um atendimento concluído não pode ser excluído.");
        }

        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getTitulo().toUpperCase());
    }

    private Atendimento findAtendimentoOrThrow(UUID id) {
        return atendimentoRepo.findById(id).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Atendimento não encontrado"));
    }

    private void validarDisponibilidadeDosParticipantes(
        Atendimento atendimento
    ) {
        if (atendimentoRepo.alunoPossuiAtendimentoConflitante(atendimento.getAlunoId(), atendimento.getInicio(), atendimento.getFim(), atendimento.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O aluno informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.colaboradorPossuiAtendimentoConflitante(atendimento.getColaboradorId(), atendimento.getInicio(), atendimento.getFim(), atendimento.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
