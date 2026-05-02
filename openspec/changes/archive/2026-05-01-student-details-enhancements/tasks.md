## 1. Backend Data Access (Repository)

- [x] 1.1 Add `countByStudentIdAndStartDateBetween` to `EventRepository`.
- [x] 1.2 Add `sumChargedByStudentIdInPeriod` (where `studentChargeDate` is NOT NULL) to `EventRepository`.
- [x] 1.3 Add `sumPendingByStudentIdInPeriod` (where `studentChargeDate` is NULL) to `EventRepository`.
- [x] 1.4 Add `withStudentCharged(Boolean charged)` to `EventSpecifications`.
- [x] 1.5 Add `withEmployeeNameIgnoreCase(String term)` to `EventSpecifications`.

## 2. Backend Services & Controllers

- [x] 2.1 Create `StudentMonthlySummaryDTO` (if not reusing the employee one, but better to create a specific one `StudentMonthlySummaryDTO`).
- [x] 2.2 Add `getMonthlySummary` method to `StudentService` using the new repository methods.
- [x] 2.3 Add `GET /v1/students/{id}/summary` endpoint to `StudentController`.
- [x] 2.4 Update `EventService.getEventsByStudentId` to accept `search`, `hideCharged`, `startDate`, and `endDate`, and apply the new specifications.
- [x] 2.5 Update `EventController.getEventByStudentId` to accept the new query parameters.

## 3. Kubb Sync

- [x] 3.1 Run `npm run sync --prefix client` to generate the new types and hooks for the frontend.

## 4. Frontend Components

- [x] 4.1 Create `StudentKPIs.tsx` inside `features/students/components`, utilizing `useGetStudentMonthlySummary`.
- [x] 4.2 Create `StudentEventsTable.tsx` inside `features/students/components`, mirroring `EmployeeEventsTable` but using `DateRangeInput`, `search` (for employee name), and a "Hide Charged" toggle. Ensure date parameters default to undefined (show all).
- [x] 4.3 Update `features/students/pages/StudentDetailsPage.tsx` to include `StudentKPIs` and replace the old `EventsTable` with `StudentEventsTable`.

## 5. Verification

- [x] 5.1 Verify the backend returns the correct student summary metrics.
- [x] 5.2 Verify that the `StudentEventsTable` defaults to showing all events when no date is selected.
- [x] 5.3 Verify that date filtering, searching by employee name, and hiding charged events work correctly in the `StudentEventsTable`.
