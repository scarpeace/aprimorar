package com.aprimorar.api.domain.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;
import com.aprimorar.api.domain.event.EventRepository.EventContentCountProjection;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DashboardSummaryResponseDTOTest {

    @Test
    @DisplayName(
        "Should create DashboardSummaryResponseDTO with correct mappings and calculations"
    )
    void shouldCreateDtoWithCorrectMappingsAndCalculations() {
        // Given
        YearMonth selectedMonth = YearMonth.of(2024, 1);
        long activeStudents = 10L;
        long totalClasses = 4L;
        BigDecimal revenue = new BigDecimal("1000.00");
        BigDecimal cost = new BigDecimal("400.00");
        Instant now = Instant.now();
        int refreshSeconds = 60;

        // Mocking projections for chart distribution
        EventContentCountProjection proj1 = mock(
            EventContentCountProjection.class
        );
        when(proj1.getContent()).thenReturn(EventContent.AULA);
        when(proj1.getCount()).thenReturn(3L);

        EventContentCountProjection proj2 = mock(
            EventContentCountProjection.class
        );
        when(proj2.getContent()).thenReturn(EventContent.MENTORIA);
        when(proj2.getCount()).thenReturn(1L);

        List<EventContentCountProjection> distribution = List.of(proj1, proj2);

        // When
        DashboardSummaryResponseDTO dto = DashboardSummaryResponseDTO.of(
            selectedMonth,
            activeStudents,
            totalClasses,
            revenue,
            cost,
            distribution,
            now,
            refreshSeconds
        );

        // Then
        assertThat(dto.period().year()).isEqualTo(2024);
        assertThat(dto.period().month()).isEqualTo(1);
        assertThat(dto.period().prev().year()).isEqualTo(2023);
        assertThat(dto.period().prev().month()).isEqualTo(12);
        assertThat(dto.period().next().year()).isEqualTo(2024);
        assertThat(dto.period().next().month()).isEqualTo(2);

        assertThat(dto.kpis().activeStudentsInMonth()).isEqualTo(10L);
        assertThat(dto.kpis().classesInMonth()).isEqualTo(4L);
        assertThat(dto.kpis().revenueInMonth()).isEqualTo(revenue);
        assertThat(dto.kpis().costInMonth()).isEqualTo(cost);

        assertThat(dto.charts().classesByContent()).hasSize(2);

        // Assert percentages (3/4 = 75%, 1/4 = 25%)
        assertThat(dto.charts().classesByContent())
            .extracting(
                DashboardSummaryResponseDTO.ClassesByContentDTO::percentage
            )
            .containsExactlyInAnyOrder(
                new BigDecimal("75.00"),
                new BigDecimal("25.00")
            );

        assertThat(dto.meta().generatedAt()).isEqualTo(now);
        assertThat(dto.meta().refreshSeconds()).isEqualTo(60);
    }

    @Test
    @DisplayName("Should return empty charts when total classes is zero")
    void shouldReturnEmptyChartsWhenTotalClassesIsZero() {
        // Given
        YearMonth selectedMonth = YearMonth.of(2024, 1);
        long totalClasses = 0L;
        List<EventContentCountProjection> distribution = List.of();

        // When
        DashboardSummaryResponseDTO dto = DashboardSummaryResponseDTO.of(
            selectedMonth,
            0,
            totalClasses,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            distribution,
            Instant.now(),
            60
        );

        // Then
        assertThat(dto.charts().classesByContent()).isEmpty();
    }
}
