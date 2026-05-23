package aprimorar.atendimentos.internal;

import aprimorar.atendimentos.internal.repository.AtendimentoRepository;
import aprimorar.pessoas.colaborador.api.contract.ColaboradorPaymentStatusApi;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ColaboradorPaymentStatusAdapter implements ColaboradorPaymentStatusApi {

    private final AtendimentoRepository appointmentRepository;

    public ColaboradorPaymentStatusAdapter(AtendimentoRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean possuiPagamentosPendentes(UUID colaboradorId) {
        return appointmentRepository.existsByEmployeeIdAndEmployeePaymentDateIsNull(colaboradorId);
    }
}
