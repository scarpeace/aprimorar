package aprimorar.despesas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import aprimorar.despesas.domain.Despesa;
import aprimorar.despesas.dto.DespesaFiltroRequest;
import aprimorar.despesas.enums.CategoriaDespesa;
import aprimorar.despesas.enums.FormaPagamento;
import aprimorar.despesas.repository.DespesaRepository;
import aprimorar.exception.BusinessException;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DespesaQueryServiceTest {

    @Mock
    private DespesaRepository despesaRepo;

    private DespesaQueryService service;

    @BeforeEach
    void setUp() {
        service = new DespesaQueryService(despesaRepo);
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

        var ex = assertThrows(BusinessException.class, () -> service.findDespesaById(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Despesa não encontrada no banco de dados", ex.getMessage());
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
