package aprimorar.despesas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.despesas.domain.DespesaEntity;
import aprimorar.despesas.domain.exception.DespesaNaoEncontradaException;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import aprimorar.despesas.enums.TipoDespesa;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.despesas.web.dto.DespesaFiltroRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DespesaServiceQueryTest {

    @Mock
    private DespesaRepository despesaRepo;

    private DespesaService service;

    @BeforeEach
    void setUp() {
        service = new DespesaService(despesaRepo);
    }

    @Test
    void shouldGetDespesas() {
        var despesa = despesa();
        setId(despesa, 1L);
        var pageable = PageRequest.of(0, 10);
        var filtro = new DespesaFiltroRequest(null, null, null, null, null);

        when(despesaRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(despesa), pageable, 1));

        var response = service.getDespesas(filtro, pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals(1L, response.getContent().getFirst().id());
        assertEquals("Conta de energia", response.getContent().getFirst().titulo());
    }

    @Test
    void shouldFindDespesaById() {
        var despesa = despesa();
        setId(despesa, 1L);

        when(despesaRepo.findById(1L)).thenReturn(Optional.of(despesa));

        var response = service.findDespesaById(1L);

        assertEquals(1L, response.id());
        assertEquals("Conta de energia", response.titulo());
    }

    @Test
    void shouldThrowWhenFindDespesaByIdDoesNotExist() {
        when(despesaRepo.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(DespesaNaoEncontradaException.class, () -> service.findDespesaById(1L));

        assertEquals("Despesa não encontrada no banco de dados", ex.getMessage());
    }

    private static DespesaEntity despesa() {
        return new DespesaEntity(
            "Conta de energia",
            TipoDespesa.SAIDA,
            CategoriaDespesa.CONTAS,
            new BigDecimal("250.00"),
            LocalDate.of(2026, 7, 22),
            FormaPagamento.PIX,
            "Pagamento de julho"
        );
    }

    private static void setId(DespesaEntity despesa, Long id) {
        ReflectionTestUtils.setField(despesa, "id", id);
    }
}
