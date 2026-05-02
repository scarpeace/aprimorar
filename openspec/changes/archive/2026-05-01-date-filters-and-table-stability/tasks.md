## 1. Backend Fixes and Enhancements

- [x] 1.1 Fix `EventSpecifications.withEmployeePaid` to use `employeePaymentDate` field.
- [x] 1.2 Verify `EventController.getEvents` and `getEventsByEmployeeId` parameters support `startDate` and `endDate`.

## 2. Frontend Infrastructure

- [x] 2.1 Update `useGetEvents` and `useGetEventsByEmployeeId` hooks (or call sites) to include `id,asc` in the `sort` parameter.
- [x] 2.2 Implement a helper to calculate start/end of month for default filtering.

## 3. EventsPage Implementation

- [x] 3.1 Replace `month`/`year` search params logic with `startDate`/`endDate` in `EventsPage.tsx`.
- [x] 3.2 Add two `DatePicker` components (using `react-datepicker`) to `EventsPage` header for full date selection.
- [x] 3.3 Link DatePickers to `useSearchParams` to ensure URL synchronization.
- [x] 3.4 Pass the new date filters to the `useGetEvents` hook.

## 4. Verification

- [x] 4.1 Verify that picking a specific day (e.g., May 10th to May 10th) filters correctly.
- [x] 4.2 Verify that toggling payment status in `EventsTable` and `EmployeeEventsTable` no longer shifts row positions.
- [x] 4.3 Verify that the "Hide Paid" toggle in `EmployeeEventsTable` works correctly after the backend fix.
