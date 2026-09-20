package aprimorar.financeiro.financeiro_aluno.cobrancas.service;

import aprimorar.financeiro.api.financeiro_aluno.AtualizarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.CobrancaAlunoApi;
import aprimorar.financeiro.api.financeiro_aluno.CobrancaAlunoResumo;
import aprimorar.financeiro.api.financeiro_aluno.CriarCobrancaAlunoCommand;
import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.enums.StatusCobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception.CobrancaAlunoNaoEncontradoException;
import aprimorar.financeiro.financeiro_aluno.cobrancas.repository.CobrancaAlunoRepository;
import aprimorar.financeiro.financeiro_aluno.cobrancas.repository.CobrancaAlunoSpecifications;
import aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto.CobrancaAlunoFiltroRequest;
import aprimorar.financeiro.financeiro_aluno.cobrancas.web.dto.CobrancaAlunoResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CobrancaAlunoService implements CobrancaAlunoApi {

    private final CobrancaAlunoRepository cobrancaRepository;

    public CobrancaAlunoService(CobrancaAlunoRepository cobrancaRepository) {
        this.cobrancaRepository = cobrancaRepository;
    }

    @Override
    @Transactional
    public void criar(CriarCobrancaAlunoCommand command) {
        if (command == null) {
            throw new CobrancaAlunoDadosInvalidosException("Dados da cobrança são obrigatórios");
        }
        validarDados(command.origemId(), command.origemTipo(), command.alunoId(), command.valorTotal());

        if (cobrancaRepository.existsByOrigemIdAndOrigemTipo(command.origemId(), command.origemTipo())) {
            throw new CobrancaAlunoDadosInvalidosException(
                "Já existe uma cobrança para a origem informada"
            );
        }

        cobrancaRepository.save(
            new CobrancaAluno(
                command.origemId(),
                command.origemTipo(),
                command.alunoId(),
                command.valorTotal()
            )
        );
    }

    @Override
    @Transactional
    public void atualizar(AtualizarCobrancaAlunoCommand command) {
        if (command == null) {
            throw new CobrancaAlunoDadosInvalidosException("Dados da cobrança são obrigatórios");
        }
        validarDados(command.origemId(), command.origemTipo(), command.alunoId(), command.valorTotal());

        CobrancaAluno cobranca = cobrancaRepository
            .findByOrigemIdAndOrigemTipoForUpdate(command.origemId(), command.origemTipo())
            .orElseThrow(CobrancaAlunoNaoEncontradoException::new);

        cobranca.atualizar(command.alunoId(), command.valorTotal());
    }

    @Override
    @Transactional
    public void cancelarPorOrigem(Long origemId, TipoOrigemCobrancaAluno origemTipo) {

        if (origemId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID da origem é obrigatório");
        }
        if (origemTipo == null) {
            throw new CobrancaAlunoDadosInvalidosException("Tipo da origem é obrigatório");
        }

        CobrancaAluno cobranca = cobrancaRepository
            .findByOrigemIdAndOrigemTipoForUpdate(origemId, origemTipo)
            .orElseThrow(CobrancaAlunoNaoEncontradoException::new);

        cobranca.cancelar();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPendenciaPorAlunoId(UUID alunoId) {
        if (alunoId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID do aluno é obrigatório");
        }

        return cobrancaRepository.existsByAlunoIdAndStatusIn(
            alunoId,
            List.of(StatusCobrancaAluno.PENDENTE, StatusCobrancaAluno.PARCIALMENTE_PAGA)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CobrancaAlunoResumo> buscarResumoPorOrigemId(
        Long origemId,
        TipoOrigemCobrancaAluno origemTipo
    ) {
        if (origemId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID da origem é obrigatório");
        }
        if (origemTipo == null) {
            throw new CobrancaAlunoDadosInvalidosException("Tipo da origem é obrigatório");
        }

        return cobrancaRepository.findByOrigemIdAndOrigemTipo(origemId, origemTipo)
            .map(CobrancaAlunoService::toResumo);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, CobrancaAlunoResumo> buscarResumosPorOrigemIds(
        Set<Long> origemIds,
        TipoOrigemCobrancaAluno origemTipo
    ) {
        if (origemIds == null || origemIds.isEmpty()) {
            return Map.of();
        }
        if (origemTipo == null) {
            throw new CobrancaAlunoDadosInvalidosException("Tipo da origem é obrigatório");
        }

        return cobrancaRepository.findAllByOrigemIdInAndOrigemTipo(origemIds, origemTipo)
            .stream()
            .collect(Collectors.toUnmodifiableMap(
                CobrancaAluno::getOrigemId,
                CobrancaAlunoService::toResumo
            ));
    }

    @Transactional(readOnly = true)
    public Page<CobrancaAlunoResponse> buscarCobrancas(
        CobrancaAlunoFiltroRequest filtro,
        Pageable pageable
    ) {
        return cobrancaRepository.findAll(
            CobrancaAlunoSpecifications.comFiltros(filtro),
            pageable
        ).map(CobrancaAlunoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public CobrancaAlunoResponse buscarCobrancaPorId(Long cobrancaId) {
        return cobrancaRepository.findById(cobrancaId)
            .map(CobrancaAlunoResponse::toDto)
            .orElseThrow(CobrancaAlunoNaoEncontradoException::new);
    }

    private static CobrancaAlunoResumo toResumo(CobrancaAluno cobranca) {
        return new CobrancaAlunoResumo(
            cobranca.getId(),
            cobranca.getValorTotal(),
            cobranca.getStatus().name()
        );
    }

    private static void validarDados(
        Long origemId,
        TipoOrigemCobrancaAluno origemTipo,
        UUID alunoId,
        BigDecimal valorTotal
    ) {
        if (origemId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID da origem é obrigatório");
        }
        if (origemTipo == null) {
            throw new CobrancaAlunoDadosInvalidosException("Tipo da origem é obrigatório");
        }
        if (alunoId == null) {
            throw new CobrancaAlunoDadosInvalidosException("ID do aluno é obrigatório");
        }
        if (valorTotal == null) {
            throw new CobrancaAlunoDadosInvalidosException("Valor total da cobrança é obrigatório");
        }
    }
}
