package com.aprimorar.api.domain.dashboard.dto;

import com.aprimorar.api.domain.event.EventRepository.EventContentCount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

public record DashboardSummaryResponseDTO(
    int year,
    int month,
    int prevYear,
    int prevMonth,
    int nextYear,
    int nextMonth,
    long activeStudentsInMonth,
    long classesInMonth,
    BigDecimal revenueInMonth,
    BigDecimal costInMonth,
    List<ClassesByContentDTO> charts,
    Instant generatedAt,
    int refreshSeconds
) {

    public static DashboardSummaryResponseDTO of(
        YearMonth month,
        long activeStudentsInMonth,
        long classesInMonth,
        BigDecimal revenueInMonth,
        BigDecimal costInMonth,
        List<EventContentCount> contentDistribution,
        Instant generatedAt,
        int refreshSeconds
    ) {
        YearMonth prev = month.minusMonths(1);
        YearMonth next = month.plusMonths(1);
        List<ClassesByContentDTO> charts = buildCharts(
            contentDistribution,
            classesInMonth
        );

        return new DashboardSummaryResponseDTO(
            month.getYear(),
            month.getMonthValue(),
            prev.getYear(),
            prev.getMonthValue(),
            next.getYear(),
            next.getMonthValue(),
            activeStudentsInMonth,
            classesInMonth,
            revenueInMonth,
            costInMonth,
            charts,
            generatedAt,
            refreshSeconds
        );
    }

    private static List<ClassesByContentDTO> buildCharts(
        List<EventContentCount> distribution,
        long total
    ) {
        if (total == 0) {
            return List.of();
        }

        return distribution
            .stream()
            .map(p ->
                new ClassesByContentDTO(
                    p.getContent().name(),
                    p.getCount(),
                    BigDecimal.valueOf((p.getCount() * 100.0) / total).setScale(
                        2,
                        RoundingMode.HALF_UP
                    )
                )
            )
            .toList();
    }

    public record ClassesByContentDTO(
        String content,
        long count,
        BigDecimal percentage
    ) {}
}
