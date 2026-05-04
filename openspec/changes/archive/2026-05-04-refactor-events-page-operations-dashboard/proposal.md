## Why

The `EventsPage` currently defaults to a monthly view, which restricts the user's initial perspective on total operations. Additionally, the global events view lacks tactical filters for identifying pending charges and payments, and the UI is becoming crowded as more features are added. This refactor aims to transform the events page into a high-performance "Operations Dashboard."

## What Changes

- **All-Time Default**: Change the initial loading state to show all events (ordered by date) instead of a fixed month.
- **Pending Operation Filters**: Add two new toggle switches to filter for "Cobrança Pendente" (students not yet charged) and "Pagamento Pendente" (employees not yet paid).
- **Backend API Extension**: Update the global `getEvents` endpoint to support `hideCharged` and `hidePaid` parameters.
- **Parallel Shell Refactor**: Restructure the page to render the toolbar and header immediately while loading the table asynchronously.
- **Layout Optimization**: Redesign the toolbar to fit the search bar, toggles, date picker, and "New" button in a single, responsive row.
- **Logic Encapsulation**: Extract URL state management into a `useEventsFilters` custom hook.

## Capabilities

### New Capabilities
- `events-operations-dashboard`: Enhanced global events view with tactical pending-state filters and non-blocking rendering.

### Modified Capabilities
- None

## Impact

- **Backend**: `EventController.java`, `EventService.java`
- **Frontend**: `EventsPage.tsx`, `EventsTable.tsx`
- **Hooks**: New `useEventsFilters.ts`
- **API**: The global `/v1/events` endpoint will have new optional query parameters.
