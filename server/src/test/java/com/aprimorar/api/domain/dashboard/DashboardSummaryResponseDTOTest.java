package com.aprimorar.api.domain.dashboard;

import static org.assertj.core.api.Assertions.assertThat;

import com.aprimorar.api.domain.dashboard.api.dto.ClassesByContentDTO;
import com.aprimorar.api.domain.dashboard.api.dto.DashboardSummaryResponseDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DashboardSummaryResponseDTOTest {

    @Test
    @DisplayName("Should create DashboardSummaryResponseDTO with correct values")
    void shouldCreateDtoWithCorrectMappingsAndCalculations() {
        BigDecimal revenue = new BigDecimal("1000.00");
        BigDecimal cost = new BigDecimal("400.00");
        Instant now = Instant.now();

        List<ClassesByContentDTO> charts = List.of(
            new ClassesByContentDTO("AULA", 3, new BigDecimal("75.00")),
            new ClassesByContentDTO("MENTORIA", 1, new BigDecimal("25.00"))
        );

        DashboardSummaryResponseDTO dto = new DashboardSummaryResponseDTO(
            2024, 1, 2023, 12, 2024, 2,
            10L, 4L, revenue, cost, charts, now, 60
        );

        assertThat(dto.year()).isEqualTo(2024);
        assertThat(dto.month()).isEqualTo(1);
        assertThat(dto.prevYear()).isEqualTo(2023);
        assertThat(dto.prevMonth()).isEqualTo(12);
        assertThat(dto.nextYear()).isEqualTo(2024);
        assertThat(dto.nextMonth()).isEqualTo(2);

        assertThat(dto.activeStudentsInMonth()).isEqualTo(10L);
        assertThat(dto.classesInMonth()).isEqualTo(4L);
        assertThat(dto.revenueInMonth()).isEqualTo(revenue);
        assertThat(dto.costInMonth()).isEqualTo(cost);
        assertThat(dto.charts()).hasSize(2);
        assertThat(dto.charts())
                .extracting(ClassesByContentDTO::percentage)
                .containsExactlyInAnyOrder(new BigDecimal("75.00"), new BigDecimal("25.00"));
        assertThat(dto.generatedAt()).isEqualTo(now);
        assertThat(dto.refreshSeconds()).isEqualTo(60);
    }

    @Test
    @DisplayName("Should return empty charts when total classes is zero")
    void shouldReturnEmptyChartsWhenTotalClassesIsZero() {
        DashboardSummaryResponseDTO dto = new DashboardSummaryResponseDTO(
            2024, 1, 2023, 12, 2024, 2,
            0, 0L, BigDecimal.ZERO, BigDecimal.ZERO, List.of(), Instant.now(), 60
        );

        assertThat(dto.charts()).isEmpty();
    }
}
