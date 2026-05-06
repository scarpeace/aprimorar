## 1. Backend Refactoring

- [x] 1.1 Rename `EmployeeMonthlySummaryDTO.java` to `EmployeeSummaryDTO.java` and update its fields (`totalEvents`, `totalPaid`, `totalUnpaid`).
- [x] 1.2 Update `EventRepository.java`: Add `countByEmployeeId`, `sumPaidByEmployeeId`, and `sumUnpaidByEmployeeId` methods.
- [x] 1.3 Update `EmployeeService.java`: Rename `getMonthlySummary` to `getSummary` and implement the "All Time" logic for null dates.
- [x] 1.4 Update `EmployeeController.java`: Update the endpoint to call `getSummary`, return `EmployeeSummaryDTO`, and update the path/parameters.
- [x] 1.5 Update `EmployeeServiceTest.java`: Refactor tests to match renamed methods and verify the new "All Time" default behavior.

## 2. API Synchronization

- [x] 2.1 Run `npm run sync` in the `client` directory to regenerate Kubb hooks and types.

## 3. Frontend Implementation

- [x] 3.1 Modify `EmployeeKPIs.tsx`: Use the shared `startDate` and `endDate` from search params and pass them to the renamed hook.
- [x] 3.2 Modify `EmployeeKPIs.tsx`: Update title and description to reflect "Resumo Geral" when no dates are selected.
- [x] 3.3 Update `EmployeeEventsTable.tsx`: Remove internal `DateRangeInput` and its handlers.
- [x] 3.4 Update `EmployeeDetailsPage.tsx`: Implement page-level date range management using `useSearchParams`.
- [x] 3.5 Update `EmployeeDetailsPage.tsx`: Add the `DateRangeInput` and clear filters button.

## 4. Verification

- [x] 4.1 Verify that the summary cards show "All Time" data by default.
- [x] 4.2 Verify that selecting a date range updates both KPIs and the events list.
