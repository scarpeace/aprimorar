## Context

The `EventsPage` is currently a monthly view by default. Users have expressed a need for a broader "All-Time" default and tactical filters to see which student events haven't been charged and which employee events haven't been paid. The current UI also suffers from a sequential loading waterfall.

## Goals / Non-Goals

**Goals:**
- Extend the global events API to support "pending" status filters.
- Implement an "All-Time" default view on the frontend.
- Optimize the toolbar layout to accommodate new filters in a single row.
- Decouple the table loading from the page shell.

**Non-Goals:**
- Creating an `EventEditModal` (editing remains in the details page).
- Refactoring the `EventForm` component.
- Changing backend data schemas (only filtering logic).

## Decisions

### 1. Backend Specification Integration
We will update `EventController.getEvents` to accept `Boolean hideCharged` and `Boolean hidePaid`. These will be passed to `EventService.getEvents`, which will use the existing `EventSpecifications.withStudentCharged` and `EventSpecifications.withEmployeePaid`.
- **Rationale**: Reuses proven filtering logic already used in student/employee specific views.

### 2. Custom Filter Hook (`useEventsFilters`)
A custom hook will manage the 5 stateful parameters in the URL: `search`, `startDate`, `endDate`, `hideCharged`, `hidePaid`.
- **Rationale**: URL-driven state ensures that filters are bookmarkable and survive page refreshes.

### 3. Toolbar Responsive Layout
The toolbar will use a `flex` container with `flex-wrap`. The search input will be `flex-1` to occupy available space, while the toggles and date picker will have stable widths.
- **Rationale**: Maximizes space efficiency for the search bar while keeping tactical controls accessible.

### 4. EventsListSection Component
The logic for calling `useGetEvents` and displaying the `EventsTable` will move to `EventsListSection.tsx`.
- **Rationale**: Enables non-blocking rendering of the page shell.

## Risks / Trade-offs

- **[Risk] High Default Data Volume** → **Mitigation**: The system already uses pagination (`PageDTO`), so fetching "All-Time" events only returns the first 10-20 results initially.
- **[Risk] Parameter Naming Conflict** → **Mitigation**: Use naming consistent with other features (`hideCharged`, `hidePaid`) to ensure Kubb generates consistent hooks.
