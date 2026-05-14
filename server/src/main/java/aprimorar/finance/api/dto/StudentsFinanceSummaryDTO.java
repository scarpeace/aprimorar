package aprimorar.finance.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class StudentsFinanceSummaryDTO {
    Instant startDate;
    Instant endDate;
    List<StudentFinanceSummaryDTO> studentFinanceSummaryDTOs;
    String studentName;
    String studentId;
    BigDecimal studentTotalCharged;
    BigDecimal studentTotalPending;
}
