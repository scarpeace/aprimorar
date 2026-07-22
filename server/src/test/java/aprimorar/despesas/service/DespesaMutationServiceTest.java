package aprimorar.despesas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.dto.DespesaRequest;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DespesaMutationServiceTest {

    @Mock
    private DespesaRepository despesaRepo;

    private DespesaMutationService service;

    @BeforeEach
    void setUp() {
        service = new DespesaMutationService(despesaRepo);
    }

    @Test
    void shouldCreateDespesa() {
        var request = despesaRequest();
        var saved = request.toEntity();
        setId(saved, 1L);

        when(despesaRepo.save(any(Despesa.class))).thenReturn(saved);

        var response = service.createDespesa(request);

        assertEquals(1L, response.id());
        assertEquals("Conta de energia", response.titulo());

        ArgumentCaptor<Despesa> captor = ArgumentCaptor.forClass(Despesa.class);
        verify(despesaRepo).save(captor.capture());
        assertEquals("Conta de energia", captor.getValue().getTitulo());
        assertEquals(CategoriaDespesa.CONTAS, captor.getValue().getCategoria());
        assertEquals(FormaPagamento.PIX, captor.getValue().getFormaPagamento());
    }

    @Test
    void shouldUpdateDespesa() {
        var despesa = despesa();
        setId(despesa, 1L);
        var request = new DespesaRequest(
            "Material de limpeza",
            CategoriaDespesa.DESPENSA,
            new BigDecimal("80.00"),
            LocalDate.of(2026, 7, 23),
            FormaPagamento.CARTAO_DEBITO,
            "Compra mensal"
        );

        when(despesaRepo.findById(1L)).thenReturn(Optional.of(despesa));

        var response = service.updateDespesa(1L, request);

        assertEquals("Material de limpeza", response.titulo());
        assertEquals(CategoriaDespesa.DESPENSA, response.categoria());
        assertEquals(new BigDecimal("80.00"), response.valor());
        assertEquals(FormaPagamento.CARTAO_DEBITO, response.formaPagamento());
    }

    @Test
    void shouldThrowWhenUpdateAndDespesaDoesNotExist() {
        when(despesaRepo.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.updateDespesa(1L, despesaRequest()));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Despesa não encontrada no banco de dados", ex.getMessage());
    }

    @Test
    void shouldDeleteDespesa() {
        var despesa = despesa();
        setId(despesa, 1L);

        when(despesaRepo.findById(1L)).thenReturn(Optional.of(despesa));

        service.deleteDespesa(1L);

        verify(despesaRepo).delete(despesa);
    }

    @Test
    void shouldThrowWhenDeleteAndDespesaDoesNotExist() {
        when(despesaRepo.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(BusinessException.class, () -> service.deleteDespesa(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Despesa não encontrada no banco de dados", ex.getMessage());
        verify(despesaRepo, never()).delete(any(Despesa.class));
    }

    @Test
    void shouldTogglePagamentoDespesa() {
        var despesa = new Despesa(
            "Conta de energia",
            CategoriaDespesa.CONTAS,
            new BigDecimal("250.00"),
            null,
            FormaPagamento.PIX,
            "Pagamento de julho"
        );
        setId(despesa, 1L);

        when(despesaRepo.findById(1L)).thenReturn(Optional.of(despesa));

        var response = service.togglePagamento(1L);

        assertNotNull(response.dataPagamento());
    }

    @Test
    void shouldTogglePagamentoDespesaBackToPending() {
        var despesa = despesa();
        setId(despesa, 1L);

        when(despesaRepo.findById(1L)).thenReturn(Optional.of(despesa));

        var response = service.togglePagamento(1L);

        assertNull(response.dataPagamento());
    }

    private static DespesaRequest despesaRequest() {
        return new DespesaRequest(
            "Conta de energia",
            CategoriaDespesa.CONTAS,
            new BigDecimal("250.00"),
            LocalDate.of(2026, 7, 22),
            FormaPagamento.PIX,
            "Pagamento de julho"
        );
    }

    private static Despesa despesa() {
        return new Despesa(
            "Conta de energia",
            CategoriaDespesa.CONTAS,
            new BigDecimal("250.00"),
            LocalDate.of(2026, 7, 22),
            FormaPagamento.PIX,
            "Pagamento de julho"
        );
    }

    private static void setId(Despesa despesa, Long id) {
        ReflectionTestUtils.setField(despesa, "id", id);
    }
}
