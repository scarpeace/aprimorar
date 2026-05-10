package aprimorar.dashboard.internal;

import aprimorar.dashboard.api.DashboardService;
import aprimorar.dashboard.api.dto.ClassesByContentDTO;
import aprimorar.dashboard.api.dto.DashboardSummaryResponseDTO;
import aprimorar.dashboard.api.exception.InvalidDashboardRequestException;
import aprimorar.event.internal.repository.EventRepository;
import aprimorar.event.internal.repository.EventRepository.EventContentCount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

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

    public DashboardServiceImpl(
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

        long activeStudentsInMonth
                = eventRepository.countDistinctStudentsInPeriodExcludingStudent(
                        monthRange.startDateTime(),
                        monthRange.endExclusiveDateTime(),
                        GHOST_STUDENT_ID
                );
        long classesInMonth
                = eventRepository.countByStartDateGreaterThanEqualAndStartDateLessThan(
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

        List<EventContentCount> groupedByContent
                = eventRepository.findContentDistributionInPeriod(
                        monthRange.startDateTime(),
                        monthRange.endExclusiveDateTime()
                );

        YearMonth prev = selectedMonth.minusMonths(1);
        YearMonth next = selectedMonth.plusMonths(1);

        return new DashboardSummaryResponseDTO(
                selectedMonth.getYear(),
                selectedMonth.getMonthValue(),
                prev.getYear(),
                prev.getMonthValue(),
                next.getYear(),
                next.getMonthValue(),
                activeStudentsInMonth,
                classesInMonth,
                revenueInMonth,
                costInMonth,
                buildCharts(groupedByContent, classesInMonth),
                applicationClock.instant(),
                REFRESH_SECONDS
        );
    }

    private List<ClassesByContentDTO> buildCharts(
            List<EventContentCount> distribution,
            long total
    ) {
        if (total == 0) {
            return List.of();
        }
        return distribution
                .stream()
                .map(p -> new ClassesByContentDTO(
                        p.getContent().name(),
                        p.getCount(),
                        BigDecimal.valueOf((p.getCount() * 100.0) / total).setScale(2, RoundingMode.HALF_UP)
                ))
                .toList();
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
        ZoneId zone = applicationClock.getZone();
        Instant firstDay = yearMonth.atDay(1).atStartOfDay(zone).toInstant();
        Instant firstDayNextMonth = yearMonth.plusMonths(1).atDay(1).atStartOfDay(zone).toInstant();
        return new DateTimeRange(
                firstDay,
                firstDayNextMonth
        );
    }

    private record DateTimeRange(
            Instant startDateTime,
            Instant endExclusiveDateTime
            ) {

    }
}
