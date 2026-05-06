## 1. Backend Implementation

- [x] 1.1 Update `StudentController.java`: Change `getMonthlySummary` parameters to `startDate` and `endDate` (Instant).
- [x] 1.2 Update `StudentService.java`: Modify `getMonthlySummary` to use the new date range for filtering events.
- [x] 1.3 Update `StudentServiceTests.java`: (If it exists) Update tests for the summary method.

## 2. API Synchronization

- [x] 2.1 Run `npm run sync` in the `client` directory to regenerate Kubb hooks and types from the updated OpenAPI spec.

## 3. Frontend Refactoring

- [x] 3.1 Modify `StudentDetailsPage.tsx`: Implement `startDate` and `endDate` state management using `useSearchParams`.
- [x] 3.2 Modify `StudentDetailsPage.tsx`: Add `DateRangeInput` and a clear filters button at the page level.
- [x] 3.3 Update `StudentKPIs.tsx`: Use the shared `startDate` and `endDate` from search params and pass them to `useGetStudentMonthlySummary`.
- [x] 3.4 Update `StudentEventsTable.tsx`: Remove the internal `DateRangeInput` and related handler functions.
- [x] 3.5 Update `StudentEventsTable.tsx`: Ensure it uses the shared `startDate` and `endDate` from search params (should already be mostly there).

## 4. Verification

- [x] 4.1 Verify that changing the date range updates both the summary cards and the events list.
- [x] 4.2 Verify that clicking "Clear Filters" resets the date range for both components.
- [x] 4.3 Verify that the default view (no params) still shows data for the current month.
