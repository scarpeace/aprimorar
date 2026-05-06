## Why

The `StudentDetailsPage` currently suffers from a "waterfall" loading pattern where the main student query blocks the rendering and data fetching of other independent sections (KPIs and Events). This results in a poor user experience as the entire page shows a loading state instead of revealing available information incrementally. Additionally, the page component is becoming bloated with mixed responsibilities (URL parsing, modal state, data fetching, and layout).

## What Changes

- **Parallel Loading Architecture**: Restructure the page to render the layout shell and independent sections immediately, allowing parallel data fetching for the student profile, KPIs, and events.
- **Component Extraction**: Extract the student information section into `StudentInfoSection` and the edit modal into `StudentEditModal` to improve modularity and reduce page complexity.
- **Reference Stability**: Wrap date object creation in `useMemo` to prevent unnecessary re-renders of child components when the page re-renders for unrelated reasons (like opening a modal).
- **Logic Encapsulation**: Create a custom hook `useStudentDateFilters` to encapsulate the logic for parsing and updating date-range search parameters in the URL.

## Capabilities

### New Capabilities
- `student-details-parallel-loading`: Orchestration pattern for the student details view that enables non-blocking rendering and concurrent data fetching.

### Modified Capabilities
- None

## Impact

- **Affected Files**: `client/src/features/students/pages/StudentDetailsPage.tsx`
- **New Files**: 
  - `client/src/features/students/components/StudentInfoSection.tsx`
  - `client/src/features/students/components/StudentEditModal.tsx`
  - `client/src/features/students/hooks/use-student-date-filters.ts` (or similar name)
- **Dependencies**: No new external dependencies.
