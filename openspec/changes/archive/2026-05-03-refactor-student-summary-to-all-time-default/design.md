## Context

We are shifting the default view of the Student Details page from a month-based perspective to a cumulative "All Time" perspective. This aligns the KPIs with the existing behavior of the events table.

## Goals / Non-Goals

**Goals:**
- Provide a cumulative financial and attendance summary by default.
- Rename DTOs and methods to remove the "Monthly" misnomer.
- Keep the ability to filter the summary by date range when desired.

**Non-Goals:**
- Modifying how individual events are recorded or calculated.
- Changing the layout of the KPI cards themselves.

## Decisions

### 1. DTO and Method Renaming
- **Decision:** Rename `StudentMonthlySummaryDTO` to `StudentSummaryDTO` and `getMonthlySummary` to `getSummary`.
- **Rationale:** The data is no longer strictly monthly. A generic name is more future-proof and accurate.

### 2. Null Parameters = All Time
- **Decision:** In the backend service, if `startDate` and `endDate` are null, execute "All Time" queries.
- **Rationale:** Simplifies the API contract. The absence of filters implies "everything".

### 3. Student-Specific Repository Methods
- **Decision:** Add methods to `EventRepository` that count/sum without date bounds for a specific student.
- **Rationale:** More efficient than passing wide date ranges or using complex null-handling in a single query.

## Risks / Trade-offs

- **[Risk]** → Performance on students with a very high volume of events (thousands).
  - **Mitigation** → Given the nature of the application (student/attendance), the volume per student is unlikely to reach a level where simple count/sum queries become a bottleneck in the near future.
- **[Risk]** → Breaking changes for API consumers.
  - **Mitigation** → This is a private API synchronized with the frontend via Kubb. Regenerating the client will handle the DTO name change.
