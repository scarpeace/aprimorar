package aprimorar.financeiro.application;

import aprimorar.financeiro.web.dto.DespesaResponseDTO;
import aprimorar.financeiro.domain.Despesa;

public class DespesaMapper {
    public static DespesaResponseDTO toDto(Despesa despesa) {
        return new DespesaResponseDTO(
            despesa.getId(),
            despesa.getAmount(),
            despesa.getDate(),
            despesa.getCategory(),
            despesa.getDescription(),
            despesa.getPaymentDate(),
            despesa.getCreatedAt(),
            despesa.getUpdatedAt()
        );
    }
}
