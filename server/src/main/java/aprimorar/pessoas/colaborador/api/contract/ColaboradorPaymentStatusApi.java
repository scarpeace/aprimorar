package aprimorar.pessoas.colaborador.api.contract;

import java.util.UUID;

public interface ColaboradorPaymentStatusApi {

    boolean possuiPagamentosPendentes(UUID colaboradorId);
}
