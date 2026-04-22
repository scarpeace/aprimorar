---
phase: 04-financial-tracking-core
plan: 01
subsystem: finance
tags: [backend, domain, migration]
requires: []
provides: [FIN-01]
affects: [event, finance]
tech-stack: [java, spring-boot, jpa, flyway, postgresql]
key-files: [
  server/api-aprimorar/src/main/java/com/aprimorar/api/enums/FinancialStatus.java,
  server/api-aprimorar/src/main/java/com/aprimorar/api/enums/GeneralExpenseCategory.java,
  server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java,
  server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventResponseDTO.java,
  server/api-aprimorar/src/main/java/com/aprimorar/api/domain/finance/GeneralExpense.java,
  server/api-aprimorar/src/main/resources/db/migration/V4__financial_core.sql
]
decisions:
  - FinancialStatus: Added to track income and expense status for events.
  - GeneralExpense: Created as a base for tracking non-event school expenses.
  - Precision: All monetary values use BigDecimal(19, 2) in Java and NUMERIC(19, 2) in SQL.
metrics:
  duration: 25 min
  completed_date: "2026-04-21T20:25:00Z"
---

# Phase 04 Plan 01: Financial Core Setup Summary

Established the core financial domain models and database schema for financial tracking. This included updating the Event entity and creating the GeneralExpense entity.

## Key Accomplishments

- Created `FinancialStatus` and `GeneralExpenseCategory` enums with localized descriptions.
- Implemented Flyway migration `V4__financial_core.sql` to update `tb_events` and create `tb_general_expenses`.
- Updated `Event` entity and `EventResponseDTO` to include `incomeStatus` and `expenseStatus`.
- Implemented `GeneralExpense` entity using `BaseEntity` and `BigDecimal` for monetary values.
- Updated `EventMapper` to handle the new financial status fields.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking Issue] Updated EventResponseDTO and EventMapper**
- **Found during:** Task 3
- **Issue:** Changing `Event.java` to include new financial fields without updating the DTO and Mapper would result in incomplete API responses and potentially compilation errors if types were mismatched (though they weren't in this case, the response would be incomplete).
- **Fix:** Added `incomeStatus` and `expenseStatus` to `EventResponseDTO` and updated `EventMapper` to map these fields.
- **Files modified:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventResponseDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventMapper.java`
- **Commit:** `1124d8e3`

## Known Stubs

None.

## Self-Check: PASSED

- [x] All tasks executed
- [x] Each task committed individually
- [x] All deviations documented
- [x] Entities compiled without errors
- [x] Migration file syntax verified
- [x] BigDecimal precision/scale verified
