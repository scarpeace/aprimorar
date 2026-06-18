package aprimorar.atendimentos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aprimorar.atendimentos.domain.Transacao;
import aprimorar.atendimentos.dto.TransacaoFiltroRequest;
import aprimorar.atendimentos.dto.TransacaoResponseDTO;
import aprimorar.atendimentos.repository.TransacaoRepository;
import aprimorar.atendimentos.repository.specifications.TransacaoSpecifications;

@Service
public class TransacaoQueryService {

    private final TransacaoRepository transacaoRepository;

    public TransacaoQueryService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> findAll(Pageable pageable, TransacaoFiltroRequest filtro) {
        Specification<Transacao> spec = TransacaoSpecifications.comFiltros(filtro);

        Page<Transacao> transacoes = transacaoRepository.findAll(spec, pageable);
        Page<TransacaoResponseDTO> transacaoResponseDTOPage = transacoes.map(TransacaoResponseDTO::toDto);

        return transacaoResponseDTOPage;
    }

    @Transactional(readOnly = true)
    public TransacaoResponseDTO findById(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        return TransacaoResponseDTO.toDto(transacao);
    }
}
