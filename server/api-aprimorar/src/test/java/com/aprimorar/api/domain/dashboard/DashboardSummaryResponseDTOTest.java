package com.aprimorar.api.domain.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;
import com.aprimorar.api.domain.event.EventRepository.EventContentCount;
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
        YearMonth selectedMonth = YearMonth.of(2024, 1);
        long activeStudents = 10L;
        long totalClasses = 4L;
        BigDecimal revenue = new BigDecimal("1000.00");
        BigDecimal cost = new BigDecimal("400.00");
        Instant now = Instant.now();
        int refreshSeconds = 60;

        EventContentCount proj1 = mock(EventContentCount.class);
        when(proj1.getContent()).thenReturn(EventContent.AULA);
        when(proj1.getCount()).thenReturn(3L);

        EventContentCount proj2 = mock(EventContentCount.class);
        when(proj2.getContent()).thenReturn(EventContent.MENTORIA);
        when(proj2.getCount()).thenReturn(1L);

        List<EventContentCount> distribution = List.of(proj1, proj2);

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
            .extracting(DashboardSummaryResponseDTO.ClassesByContentDTO::percentage)
            .containsExactlyInAnyOrder(
                new BigDecimal("75.00"),
                new BigDecimal("25.00")
            );

        assertThat(dto.generatedAt()).isEqualTo(now);
        assertThat(dto.refreshSeconds()).isEqualTo(60);
    }

    @Test
    @DisplayName("Should return empty charts when total classes is zero")
    void shouldReturnEmptyChartsWhenTotalClassesIsZero() {
        YearMonth selectedMonth = YearMonth.of(2024, 1);
        long totalClasses = 0L;
        List<EventContentCount> distribution = List.of();

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

        assertThat(dto.charts()).isEmpty();
    }
}
