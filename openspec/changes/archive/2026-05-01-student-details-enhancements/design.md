## Context

We need to provide parity between `EmployeeDetailPage` and `StudentDetailsPage` regarding financial and event overviews. The employee page already has `EmployeeKPIs` and a filtered `EmployeeEventsTable`. We need equivalent structures for students.

## Goals / Non-Goals

**Goals:**
- Provide a monthly summary of events and charges for a specific student.
- Allow filtering of a student's events by date range, employee name, and charge status.
- Reuse existing components like `KpiCard`, `SectionCard`, and the new `DateRangeInput`.

**Non-Goals:**
- Modify the global `EventsPage`.
- Introduce new data model fields on the `Event` entity.

## Decisions

### 1. Backend: Student Monthly Summary
- **Decision**: Add `getMonthlySummary` to `StudentController` (mapped to `/v1/students/{id}/summary`) delegating to `StudentService`. 
- **Rationale**: Keeps the API symmetric with the Employee domain. `EventRepository` will need new `@Query` methods to sum `price` for a specific student, similar to `sumPaidByEmployeeIdInPeriod`. Note that for students, the relevant financial fields are `price` (how much the student owes/paid) and `studentChargeDate` (when they paid).

### 2. Backend: Enhanced `getEventsByStudentId`
- **Decision**: Update `EventController.getEventByStudentId` to accept `search`, `startDate`, `endDate`, and `hideCharged` parameters.
- **Rationale**: Reuses the specification pattern already established in `EventService`. We will need a new specification `withStudentCharged(Boolean charged)` to filter based on `studentChargeDate`.

### 3. Frontend: `StudentKPIs` and `StudentEventsTable`
- **Decision**: Create these components mirroring their Employee counterparts. Use `DateRangeInput` in `StudentEventsTable`. If `startDate` and `endDate` are not set in the URL search params, they will default to `undefined` (or null), passing nulls to the backend which will cause the backend specifications to ignore the date bounds and return ALL events.
- **Rationale**: The user requested that if no date is selected, all events are shown. The `DateRangeInput` already supports clearable dates (or we can handle undefined initialization) which differs from the Employee table that defaults to the current month.

## Risks / Trade-offs

- **[Risk] Date Handling**: Ensuring the "show all" logic works seamlessly requires the backend to handle `null` values for `startDate` and `endDate` gracefully (which `EventSpecifications` currently does).
- **[Risk] Financial Field Confusion**: It's crucial to query `price` and `studentChargeDate` for the student summary, not `payment` and `employeePaymentDate`.
