## Why

Currently, the student financial summary (KPIs) is tied to a specific month and year, while the events table below it can be filtered by a custom date range. This inconsistency makes it difficult for users to see a summary that matches the list of events they are viewing. Moving the date filter to the top of the page and sharing it between both components will provide a more cohesive and intuitive user experience.

## What Changes

- **Backend**: **BREAKING** The `/v1/students/{id}/summary` endpoint will be updated to accept `startDate` and `endDate` parameters instead of `month` and `year`.
- **Backend**: `StudentService.getMonthlySummary` will be updated to filter events based on the provided date range.
- **Frontend**: The `DateRangeInput` will be moved from `StudentEventsTable` to `StudentDetailsPage`.
- **Frontend**: Both `StudentKPIs` and `StudentEventsTable` will consume the `startDate` and `endDate` from the URL search parameters.
- **Frontend**: The `StudentKPIs` query will be updated to send the date range to the backend.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- `student-financial-summary`: Update requirements to use a flexible date range instead of fixed month/year parameters.

## Impact

- **API**: Change in request parameters for the student summary endpoint.
- **Backend**: Logic update in `StudentService`.
- **Frontend**: Refactoring of `StudentDetailsPage`, `StudentKPIs`, and `StudentEventsTable`.
- **Documentation**: OpenAPI spec for student summary will change.
