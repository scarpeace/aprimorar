package aprimorar.dashboard.internal;

import aprimorar.dashboard.api.dto.ClassesByContentDTO;
import aprimorar.dashboard.api.dto.DashboardSummaryResponseDTO;
import aprimorar.dashboard.api.exception.InvalidDashboardRequestException;
import aprimorar.appointment.api.AppointmentService;
import aprimorar.appointment.api.dto.ContentDistributionDTO;

import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardQueryService {

    private static final int REFRESH_SECONDS = 60;
    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2100;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private final AppointmentService appointmentService;
    private final Clock applicationClock;

    public DashboardServiceImpl(
            AppointmentService appointmentService,
            Clock applicationClock
    ) {
        this.appointmentService = appointmentService;
        this.applicationClock = applicationClock;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponseDTO getSummary(Integer year, Integer month) {
        validateInput(year, month);

        YearMonth selectedMonth = YearMonth.of(year, month);
        DateTimeRange monthRange = toMonthRange(selectedMonth);

        long classesInMonth
                = appointmentService.countAppointmentsInPeriod(
                        monthRange.startDateTime(),
                        monthRange.endExclusiveDateTime()
                );

        List<ContentDistributionDTO> groupedByContent
                = appointmentService.findContentDistributionInPeriod(
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
                classesInMonth,
                buildCharts(groupedByContent, classesInMonth),
                applicationClock.instant(),
                REFRESH_SECONDS
        );
    }

    private List<ClassesByContentDTO> buildCharts(
            List<ContentDistributionDTO> distribution,
            long total
    ) {
        if (total == 0) {
            return List.of();
        }
        return distribution
                .stream()
                .map(p -> new ClassesByContentDTO(
                        p.content(),
                        p.count(),
                        p.percentage()
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
