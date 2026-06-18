package aprimorar.atendimentos.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TransacaoRepository transacaoRepository;

    public TransacaoMutationService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    //TODO: Mover isso para uma variável de ambiente no aplication.properties
    private final UUID ADMIN_ID = UUID.fromString("b3a092e0-fc48-43ff-8b35-149eb81a033f");

    @Transactional
    public void criarEntradaAluno(UUID alunoId, BigDecimal valor) {
        var transacao = new Transacao(
                alunoId,
                ADMIN_ID,
                valor,
                null,
                TipoTransacao.ENTRADA,
                null,
                StatusTransacao.PENDENTE,
                CategoriaTransacao.PGTO_ALUNO
        );

        transacaoRepository.save(transacao);
    }

    @Transactional
    public void criarSaidaColaborador(UUID colaboradorId, BigDecimal repasse) {
        var transacao = new Transacao(
                ADMIN_ID,
                colaboradorId,
                repasse,
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
                ADMIN_ID,
                null,
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
}
