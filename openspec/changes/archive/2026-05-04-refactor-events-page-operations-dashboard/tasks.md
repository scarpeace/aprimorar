## 1. Backend Implementation

- [x] 1.1 Update `EventController.java` to add `hideCharged` and `hidePaid` parameters to the `getEvents` method.
- [x] 1.2 Update `EventService.java` to incorporate `withStudentCharged` and `withEmployeePaid` specifications into the global `getEvents` query.

## 2. API & Hooks Synchronization

- [x] 2.1 Run `npm run sync` in the `client` directory to regenerate Kubb hooks with the new parameters.
- [x] 2.2 Create `useEventsFilters` hook in `client/src/features/events/hooks/use-events-filters.ts` to manage search, dates, and pending status toggles.

## 3. Frontend Refactoring

- [x] 3.1 Create `EventsListSection` component in `client/src/features/events/components/EventsListSection.tsx` to handle data fetching and table rendering.
- [x] 3.2 Update `EventsPage.tsx` to use the new hook and the `EventsListSection`.
- [x] 3.3 Redesign the `EventsPage` toolbar to use a single-row flex layout with the new `ToggleSwitch` components.
- [x] 3.4 Adjust `DateRangeInput` in the page to handle null default dates (All-Time).

## 4. Verification

- [x] 4.1 Verify that the Events page loads all records by default when no date is selected.
- [x] 4.2 Confirm that "Cobrança Pendente" correctly filters for student events without a charge date.
- [x] 4.3 Confirm that "Pagamento Pendente" correctly filters for employee events without a payment date.
- [x] 4.4 Verify that the page shell and filters render immediately on load.
