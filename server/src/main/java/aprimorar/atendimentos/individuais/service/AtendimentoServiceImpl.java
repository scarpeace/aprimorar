package aprimorar.atendimentos.individuais.service;

import aprimorar.atendimentos.individuais.api.Atendimento;
import aprimorar.atendimentos.individuais.api.AtendimentoService;
import aprimorar.atendimentos.individuais.domain.AtendimentoEntity;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoConflitanteException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoNaoEncontradoException;
import aprimorar.atendimentos.individuais.repository.AtendimentoRepository;
import aprimorar.atendimentos.individuais.repository.specifications.AtendimentoSpecifications;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoFiltroRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoRecorrenteRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoRequest;
import aprimorar.atendimentos.individuais.web.dto.AtendimentoResponse;
import aprimorar.atendimentos.individuais.web.dto.ReagendarAtendimentoRequest;
import aprimorar.pessoas.aluno.api.Aluno;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.colaborador.api.Colaborador;
import aprimorar.pessoas.colaborador.api.ColaboradorService;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class AtendimentoServiceImpl implements AtendimentoService {

    private static final Logger log = LoggerFactory.getLogger(AtendimentoServiceImpl.class);

    private final AtendimentoRepository atendimentoRepo;
    private final AlunoService alunoService;
    private final ColaboradorService colaboradorService;

    public AtendimentoServiceImpl(
        AtendimentoRepository atendimentoRepo,
        AlunoService alunoService,
        ColaboradorService colaboradorService
    ) {
        this.atendimentoRepo = atendimentoRepo;
        this.alunoService = alunoService;
        this.colaboradorService = colaboradorService;
    }

    @Override
    @Transactional(readOnly = true)
    public Atendimento buscarPorId(Long atendimentoId) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(atendimentoId);

        return new Atendimento(
            atendimento.getId(),
            atendimento.getAlunoId(),
            atendimento.getColaboradorId()
        );
    }

    @Transactional(readOnly = true)
    public Page<AtendimentoResponse> getAtendimentos(Pageable pageable, AtendimentoFiltroRequest filtro) {
        Specification<AtendimentoEntity> spec = AtendimentoSpecifications.comFiltros(filtro);
        Page<AtendimentoEntity> atendimentoPage = atendimentoRepo.findAll(spec, pageable);

        log.info("Consulta de atendimentos finalizada, {} registros encontrados.", atendimentoPage.getTotalElements());
        return atendimentoPage.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public AtendimentoResponse findAtendimentoById(Long id) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);

        log.info("Atendimento {} consultado com sucesso.", atendimento.getId());
        return toResponse(atendimento);
    }

    @Transactional
    public AtendimentoResponse agendar(AtendimentoRequest dto) {
        Aluno aluno = alunoService.buscarPorId(dto.alunoId());
        Colaborador colaborador = colaboradorService.buscarPorId(dto.colaboradorId());
        AtendimentoEntity atendimento = dto.toEntity();

        validarDisponibilidadeDosParticipantes(atendimento);

        AtendimentoEntity saved = atendimentoRepo.save(atendimento);

        log.info("Atendimento {} cadastrado com sucesso.", saved.getId());
        return AtendimentoResponse.toDto(saved, aluno, colaborador);
    }

    @Transactional
    public List<AtendimentoResponse> agendarRecorrente(AtendimentoRecorrenteRequest request) {
        AtendimentoRequest dto = request.atendimento();
        Aluno aluno = alunoService.buscarPorId(dto.alunoId());
        Colaborador colaborador = colaboradorService.buscarPorId(dto.colaboradorId());
        List<AtendimentoEntity> atendimentos = new ArrayList<>();
        LocalDateTime inicio = dto.dataHoraInicio();
        LocalDateTime fim = dto.dataHoraFim();

        while (!inicio.toLocalDate().isAfter(request.dataFimRecorrencia())) {
            AtendimentoEntity atendimento = new AtendimentoEntity(
                inicio,
                fim,
                dto.tipo(),
                dto.alunoId(),
                dto.colaboradorId(),
                dto.pagamentoAluno(),
                dto.repasseColaborador()
            );

            validarDisponibilidadeDosParticipantes(atendimento);
            atendimentos.add(atendimento);

            inicio = inicio.plusWeeks(1);
            fim = fim.plusWeeks(1);
        }

        List<AtendimentoEntity> saved = atendimentoRepo.saveAll(atendimentos);

        log.info("{} atendimentos recorrentes cadastrados com sucesso.", saved.size());
        return saved.stream().map(atendimento -> AtendimentoResponse.toDto(atendimento, aluno, colaborador)).toList();
    }

    @Transactional
    public AtendimentoResponse update(Long id, AtendimentoRequest dto) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);
        Aluno aluno = alunoService.buscarPorId(dto.alunoId());
        Colaborador colaborador = colaboradorService.buscarPorId(dto.colaboradorId());

        atendimento.update(
            dto.dataHoraInicio(),
            dto.dataHoraFim(),
            dto.tipo(),
            dto.alunoId(),
            dto.colaboradorId(),
            dto.pagamentoAluno(),
            dto.repasseColaborador()
        );

        AtendimentoEntity saved = atendimentoRepo.save(atendimento);
        log.info("Atendimento {} atualizado com sucesso.", saved.getId());
        return AtendimentoResponse.toDto(saved, aluno, colaborador);
    }

    @Transactional
    public void reagendar(Long id, ReagendarAtendimentoRequest request) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);

        atendimento.reagendar(request.novoInicio(), request.novoFim());
        log.info("Atendimento {} reagendado com sucesso.", atendimento.getId());
    }

    @Transactional
    public void alterarParticipantes(Long id, UUID alunoId, UUID colaboradorId) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);
        alunoService.buscarPorId(alunoId);
        colaboradorService.buscarPorId(colaboradorId);

        atendimento.alterarParticipantes(alunoId, colaboradorId);
        log.info("Participantes do evento {} atualizados com sucesso", atendimento.getId());
    }

    @Transactional
    public void excluir(Long id) {
        AtendimentoEntity atendimento = findAtendimentoOrThrow(id);

        atendimentoRepo.delete(atendimento);
        log.info("Atendimento {} deletado com sucesso.", atendimento.getId());
    }

    private AtendimentoEntity findAtendimentoOrThrow(Long id) {
        return atendimentoRepo.findById(id).orElseThrow(AtendimentoNaoEncontradoException::new);
    }

    private AtendimentoResponse toResponse(AtendimentoEntity atendimento) {
        return AtendimentoResponse.toDto(
            atendimento,
            alunoService.buscarPorId(atendimento.getAlunoId()),
            colaboradorService.buscarPorId(atendimento.getColaboradorId())
        );
    }

    private void validarDisponibilidadeDosParticipantes(AtendimentoEntity atendimento) {
        if (atendimentoRepo.alunoPossuiAtendimentoConflitante(
            atendimento.getAlunoId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getId()
        )) {
            throw new AtendimentoConflitanteException("O aluno informado ja possui um atendimento no intervalo");
        }

        if (atendimentoRepo.colaboradorPossuiAtendimentoConflitante(
            atendimento.getColaboradorId(),
            atendimento.getDataHoraInicio(),
            atendimento.getDataHoraFim(),
            atendimento.getId()
        )) {
            throw new AtendimentoConflitanteException("O colaborador informado ja possui um atendimento no intervalo");
        }
    }
}
