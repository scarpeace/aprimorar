package com.aprimorar.api.domain.dashboard.dto;

import com.aprimorar.api.domain.event.EventRepository.EventContentCountProjection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

public record DashboardSummaryResponseDTO(
    PeriodDTO period,
    KpisDTO kpis,   
    ChartsDTO charts,
    MetaDTO meta
) {
    public static DashboardSummaryResponseDTO of(
        YearMonth selectedMonth,
        long activeStudentsInMonth,
        long classesInMonth,
        BigDecimal revenueInMonth,
        BigDecimal costInMonth,
        List<EventContentCountProjection> contentDistribution,
        Instant generatedAt,
        int refreshSeconds
    ) {
        return new DashboardSummaryResponseDTO(
            PeriodDTO.of(selectedMonth),
            new KpisDTO(
                activeStudentsInMonth,
                classesInMonth,
                revenueInMonth,
                costInMonth
            ),
            new ChartsDTO(
                toContentDistribution(contentDistribution, classesInMonth)
            ),
            new MetaDTO(generatedAt, refreshSeconds)
        );
    }

    private static List<ClassesByContentDTO> toContentDistribution(
        List<EventContentCountProjection> groupedByContent,
        long classesInMonth
    ) {
        if (classesInMonth == 0) return List.of();

        return groupedByContent
            .stream()
            .map(item ->
                new ClassesByContentDTO(
                    item.getContent().name(),
                    item.getCount(),
                    calculatePercentage(item.getCount(), classesInMonth)
                )
            )
            .toList();
    }

    private static BigDecimal calculatePercentage(long count, long total) {
        return BigDecimal.valueOf(count)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
    }

    public record PeriodDTO(
        int year,
        int month,
        MonthPointerDTO prev,
        MonthPointerDTO next
    ) {
        public static PeriodDTO of(YearMonth month) {
            return new PeriodDTO(
                month.getYear(),
                month.getMonthValue(),
                MonthPointerDTO.from(month.minusMonths(1)),
                MonthPointerDTO.from(month.plusMonths(1))
            );
        }
    }

    public record MonthPointerDTO(int year, int month) {
        public static MonthPointerDTO from(YearMonth month) {
            return new MonthPointerDTO(month.getYear(), month.getMonthValue());
        }
    }

    public record KpisDTO(
        long activeStudentsInMonth,
        long classesInMonth,
        BigDecimal revenueInMonth,
        BigDecimal costInMonth
    ) {}

    public record ChartsDTO(List<ClassesByContentDTO> classesByContent) {}

    public record ClassesByContentDTO(
        String content,
        long count,
        BigDecimal percentage
    ) {}

    public record MetaDTO(Instant generatedAt, int refreshSeconds) {}
}
