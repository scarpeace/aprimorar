package aprimorar.financeiro.internal;

import aprimorar.financeiro.api.dto.DespesaResponseDTO;

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
