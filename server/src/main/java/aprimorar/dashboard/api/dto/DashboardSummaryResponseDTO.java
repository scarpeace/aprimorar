package aprimorar.dashboard.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
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
}
