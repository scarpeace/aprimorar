## Context

The current implementation of the student financial summary is restricted to a month-by-month view. However, the events table on the same page allows for custom date range filtering. This creates a disjointed user experience where the summary (KPIs) and the list of events may represent different time periods.

## Goals / Non-Goals

**Goals:**
- Unify the date filtering mechanism on the `StudentDetailsPage`.
- Allow the financial summary to reflect the same date range as the events table.
- Move the date selection UI to a more prominent, page-level position.

**Non-Goals:**
- Changing how the summary is calculated (it will still sum prices and count events).
- Modifying other sections of the student details (e.g., student info, address).

## Decisions

### 1. Unified Date State in URL
- **Decision:** Use URL search parameters (`startDate`, `endDate`) as the source of truth for the date range on the `StudentDetailsPage`.
- **Rationale:** This allows for deep linking, browser history support, and easy sharing of the filtered view. It also simplifies state management across sibling components.

### 2. Move DateRangeInput to StudentDetailsPage
- **Decision:** Relocate the `DateRangeInput` from `StudentEventsTable` to the top of the `StudentDetailsPage` (above both KPIs and Table).
- **Rationale:** Visually indicates that the filter applies to the entire page content.

### 3. API Signature Change
- **Decision:** Change `GET /v1/students/{id}/summary` to accept `startDate` and `endDate` (as ISO-8601 dates) instead of `month` and `year`.
- **Rationale:** Provides maximum flexibility for the frontend to request any period.

### 4. Backend Date Usage
- **Decision:** Backend will accept `Instant` for `startDate` and `endDate` parameters.
- **Rationale:** The project consistently uses `Instant` for event date ranges (e.g., in `EventService`). Accepting `Instant` directly aligns with the database model and simplifies the service logic by avoiding manual conversions from `LocalDate`.

## Risks / Trade-offs

- **[Risk]** → Breaking change for the API.
  - **Mitigation** → Since this is an internal application with a synchronized frontend, we will update both simultaneously.
- **[Trade-off]** → The summary was previously "monthly" by name and intent.
  - **Mitigation** → We will keep the default behavior (if no dates provided) to show the current month, but rename the DTO or at least the parameters to reflect the flexible nature.
