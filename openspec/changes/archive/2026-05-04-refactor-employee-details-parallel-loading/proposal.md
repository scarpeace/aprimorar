## Why

The `EmployeeDetailPage` currently suffers from a "waterfall" loading pattern where the main employee query blocks the rendering and data fetching of other independent sections (KPIs and Events). This results in a poor user experience as the entire page shows a loading state instead of revealing available information incrementally. This change follows the successful refactoring pattern implemented for student details.

## What Changes

- **Parallel Loading Architecture**: Restructure the page to render the layout shell and independent sections immediately, allowing parallel data fetching for the employee profile, KPIs, and events.
- **Component Extraction**: Extract the employee information section into `EmployeeInfoSection` and the edit modal into `EmployeeEditModal` to improve modularity and reduce page complexity.
- **Reference Stability**: Wrap date object creation in `useMemo` to prevent unnecessary re-renders of child components when the page re-renders for unrelated reasons (like opening a modal).
- **Logic Encapsulation**: Create a custom hook `useEmployeeDateFilters` to encapsulate the logic for parsing and updating date-range search parameters in the URL.

## Capabilities

### New Capabilities
- `employee-details-parallel-loading`: Orchestration pattern for the employee details view that enables non-blocking rendering and concurrent data fetching.

### Modified Capabilities
- None

## Impact

- **Affected Files**: `client/src/features/employees/pages/EmployeeDetailPage.tsx`
- **New Files**: 
  - `client/src/features/employees/components/EmployeeInfoSection.tsx`
  - `client/src/features/employees/components/EmployeeEditModal.tsx`
  - `client/src/features/employees/hooks/use-employee-date-filters.ts`
- **Dependencies**: No new external dependencies.
