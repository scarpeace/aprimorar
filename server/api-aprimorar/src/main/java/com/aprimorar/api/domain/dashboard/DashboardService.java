package com.aprimorar.api.domain.dashboard;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;
import com.aprimorar.api.domain.dashboard.exception.InvalidDashboardRequestException;
import com.aprimorar.api.domain.event.EventRepository;
import com.aprimorar.api.domain.event.EventRepository.EventContentCountProjection;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private static final int REFRESH_SECONDS = 60;
    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2100;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final UUID GHOST_STUDENT_ID = UUID.fromString(
        "00000000-0000-0000-0000-000000000000"
    );

    private final EventRepository eventRepository;
    private final Clock applicationClock;

    public DashboardService(
        EventRepository eventRepository,
        Clock applicationClock
    ) {
        this.eventRepository = eventRepository;
        this.applicationClock = applicationClock;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponseDTO getSummary(Integer year, Integer month) {
        validateInput(year, month);

        YearMonth selectedMonth = YearMonth.of(year, month);
        DateTimeRange monthRange = toMonthRange(selectedMonth);

        long activeStudentsInMonth =
            eventRepository.countDistinctStudentsInPeriodExcludingStudent(
                monthRange.startDateTime(),
                monthRange.endExclusiveDateTime(),
                GHOST_STUDENT_ID
            );
        long classesInMonth =
            eventRepository.countByStartDateGreaterThanEqualAndStartDateLessThan(
                monthRange.startDateTime(),
                monthRange.endExclusiveDateTime()
            );
        BigDecimal revenueInMonth = eventRepository.sumPriceInPeriod(
            monthRange.startDateTime(),
            monthRange.endExclusiveDateTime()
        );
        BigDecimal costInMonth = eventRepository.sumPaymentInPeriod(
            monthRange.startDateTime(),
            monthRange.endExclusiveDateTime()
        );

        List<EventContentCountProjection> groupedByContent =
            eventRepository.findContentDistributionInPeriod(
                monthRange.startDateTime(),
                monthRange.endExclusiveDateTime()
            );

        return DashboardSummaryResponseDTO.of(
            selectedMonth,
            activeStudentsInMonth,
            classesInMonth,
            revenueInMonth,
            costInMonth,
            groupedByContent,
            applicationClock.instant(),
            REFRESH_SECONDS
        );
    }

    private void validateInput(Integer year, Integer month) {
        if (year == null || year < MIN_YEAR || year > MAX_YEAR) {
            throw new InvalidDashboardRequestException(
                "Ano invalido. Informe um ano entre 2000 e 2100."
            );
        }

        if (month == null || month < MIN_MONTH || month > MAX_MONTH) {
            throw new InvalidDashboardRequestException(
                "Mes invalido. Informe um valor entre 1 e 12."
            );
        }
    }

    private DateTimeRange toMonthRange(YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate firstDayNextMonth = yearMonth.plusMonths(1).atDay(1);
        return new DateTimeRange(
            firstDay.atStartOfDay(),
            firstDayNextMonth.atStartOfDay()
        );
    }

    private record DateTimeRange(
        LocalDateTime startDateTime,
        LocalDateTime endExclusiveDateTime
    ) {}
}
