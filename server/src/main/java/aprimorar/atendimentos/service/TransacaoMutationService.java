package aprimorar.atendimentos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.domain.Atendimento;
import aprimorar.atendimentos.domain.Transacao;
import aprimorar.atendimentos.dto.TransacaoRequestDTO;
import aprimorar.atendimentos.dto.TransacaoResponseDTO;
import aprimorar.atendimentos.enums.CategoriaTransacao;
import aprimorar.atendimentos.enums.FormaPagamento;
import aprimorar.atendimentos.enums.StatusTransacao;
import aprimorar.atendimentos.enums.TipoTransacao;
import aprimorar.atendimentos.repository.TransacaoRepository;

@Service
public class TransacaoMutationService {
    private final UUID adminId;
    private final TransacaoRepository transacaoRepository;

    public TransacaoMutationService(TransacaoRepository transacaoRepository, @Value("${aprimorar.admin-id}") UUID adminId) {
        this.transacaoRepository = transacaoRepository;
        this.adminId = adminId;
    }


    @Transactional
    public void criarEntradaAluno(Atendimento atendimento) {
        var transacao = new Transacao(
                atendimento,
                atendimento.getAlunoId(),
                atendimento.getNomeAluno(),
                adminId,
                "Aprimorar",
                atendimento.getValor(),
                null,
                TipoTransacao.ENTRADA,
                null,
                StatusTransacao.PENDENTE,
                CategoriaTransacao.PGTO_ALUNO
        );

        transacaoRepository.save(transacao);
    }

    @Transactional
    public void criarSaidaColaborador(Atendimento atendimento) {
        var transacao = new Transacao(
                atendimento,
                adminId,
                "Aprimorar",
                atendimento.getColaboradorId(),
                atendimento.getNomeColaborador(),
                atendimento.getRepasse(),
                null,
                TipoTransacao.SAIDA,
                null,
                StatusTransacao.PENDENTE,
                CategoriaTransacao.PGTO_COLABORADOR
        );

        transacaoRepository.save(transacao);
    }

    @Transactional
    public TransacaoResponseDTO criarSaidaDespesa(TransacaoRequestDTO request) {
        var transacao = new Transacao(
                null,
                adminId,
                "Aprimorar",
                adminId,
                request.categoria().name(),
                request.valor(),
                null,
                TipoTransacao.SAIDA,
                null,
                StatusTransacao.PENDENTE,
                request.categoria()
        );

        transacaoRepository.save(transacao);
        return TransacaoResponseDTO.toDto(transacao);
    }

    @Transactional
    public void efetivarTransacao(Long transacaoId, FormaPagamento formaPagamento) {
        var transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        transacao.efetivar(formaPagamento);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void cancelarTransacao(Long transacaoId) {
        var transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        transacao.cancelar();
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void cancelarTransacoesByAtendimentoId(Long atendimentoId) {
        List<Transacao> transacoes = transacaoRepository.findByAtendimentoId(atendimentoId);
        for (Transacao transacao : transacoes) {
            transacao.cancelar();
        }
        transacaoRepository.saveAll(transacoes);
    }
}
