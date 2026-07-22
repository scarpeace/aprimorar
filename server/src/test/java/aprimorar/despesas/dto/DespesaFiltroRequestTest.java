package aprimorar.despesas.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DespesaFiltroRequestTest {

    @Test
    void shouldValidatePeriod() {
        assertTrue(new DespesaFiltroRequest(null, null, null, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31)).getPeriodoValido());
        assertTrue(new DespesaFiltroRequest(null, null, null, null, LocalDate.of(2026, 7, 31)).getPeriodoValido());
        assertFalse(new DespesaFiltroRequest(null, null, null, LocalDate.of(2026, 7, 31), LocalDate.of(2026, 7, 1)).getPeriodoValido());
    }
}
