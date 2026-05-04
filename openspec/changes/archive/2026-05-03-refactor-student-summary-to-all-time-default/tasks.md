## 1. Backend Refactoring

- [x] 1.1 Rename `StudentMonthlySummaryDTO.java` to `StudentSummaryDTO.java` and update its content.
- [x] 1.2 Update `EventRepository.java`: Add `countByStudentId`, `sumChargedByStudentId`, and `sumPendingByStudentId` methods.
- [x] 1.3 Update `StudentService.java`: Rename `getMonthlySummary` to `getSummary` and implement the "All Time" logic for null dates.
- [x] 1.4 Update `StudentController.java`: Update the endpoint to call `getSummary` and return `StudentSummaryDTO`.
- [x] 1.5 Update `StudentServiceTest.java`: Refactor tests to match renamed methods and verify the new "All Time" default behavior.

## 2. API Synchronization

- [x] 2.1 Run `npm run sync` in the `client` directory to regenerate Kubb hooks and types with the new DTO and endpoint structure.

## 3. Frontend Implementation

- [x] 3.1 Update `StudentKPIs.tsx`: Update imports and usage to use `StudentSummaryDTO` and the renamed hook.
- [x] 3.2 Update `StudentKPIs.tsx`: Change the title and description to reflect "Resumo Geral" when no dates are selected.
- [x] 3.3 Update `StudentDetailsPage.tsx`: (If needed) Ensure it correctly passes (or doesn't pass) the dates to the summary component.

## 4. Verification

- [x] 4.1 Verify that the summary cards show "All Time" data by default.
- [x] 4.2 Verify that selecting a date range still correctly filters the summary cards.
