package aprimorar.financeiro.pagamentos_colaboradores.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import aprimorar.financeiro.pagamentos_colaboradores.api.commands.AtualizarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.api.commands.CriarRepasseCommandApi;
import aprimorar.financeiro.pagamentos_colaboradores.api.queries.RepasseQueryApi;
import aprimorar.financeiro.pagamentos_colaboradores.domain.Repasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.enums.StatusRepasse;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.RepasseNaoEncontradoException;
import aprimorar.financeiro.pagamentos_colaboradores.infrastructure.RepasseRepository;
import jakarta.transaction.Transactional;

class PagamentosApiImpl  implements  PagamentosApi{

    private final RepasseRepository repasseRepository;

    public PagamentosApiImpl(RepasseRepository repasseRepository) {
        this.repasseRepository = repasseRepository;
    }

    @Override
	public void criarRepasse(CriarRepasseCommandApi command) {
	    if (!CriarRepasseCommandApi.validate(command)) {
               throw new RepasseDadosInvalidosException("ID do atendimento, ID do colaborador e valor são obrigatórios");
           }
           repasseRepository.save(
               new Repasse(
                   command.atendimentoId(),
                   command.colaboradorId(),
                   command.valor()
               )
           );
	}

	@Override
	@Transactional
	public void atualizarRepasse(AtualizarRepasseCommandApi command) {
           if (!AtualizarRepasseCommandApi.validate(command)) {
               throw new RepasseDadosInvalidosException("ID do atendimento, ID do colaborador e valor são obrigatórios");
           }

           Repasse repasse = repasseRepository.findByAtendimentoIdForUpdate(command.atendimentoId())
               .orElseThrow(RepasseNaoEncontradoException::new);

           repasse.atualizar(command.colaboradorId(), command.valor());
	}

	@Override
	@Transactional
	public void cancelarRepasse(Long atendimentoId) {
	    Repasse repasse = repasseRepository.findByAtendimentoIdForUpdate(atendimentoId)
               .orElseThrow(RepasseNaoEncontradoException::new);

           repasse.cancelar();
	}

	@Override
	public boolean possuiRepassePendente(UUID colaboradorId) {
	    return repasseRepository.existsByColaboradorIdAndStatus(colaboradorId,StatusRepasse.PENDENTE);
	}

	@Override
	public RepasseQueryApi getRepasseQueryApiPorAtentimento(Long atendimentoId) {
		return repasseRepository.findByAtendimentoId(atendimentoId)
               .map(RepasseQueryApi::toSummary)
               .orElseThrow(RepasseNaoEncontradoException::new);
	}

	@Override
	public Map<Long, RepasseQueryApi> getRepassesQueryApisPorAtendimentos(Set<Long> atendimentoIds) {
	    if (atendimentoIds == null || atendimentoIds.isEmpty()) {
               return Map.of();
           }

           return repasseRepository.findAllByAtendimentoIdIn(atendimentoIds)
               .stream()
               .collect(Collectors.toUnmodifiableMap(
                   Repasse::getAtendimentoId,
                   RepasseQueryApi::toSummary
               ));
	}
}
