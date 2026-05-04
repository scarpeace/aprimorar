## 1. Preparation and Hooks

- [x] 1.1 Create `useStudentDateFilters` hook in `client/src/features/students/hooks/use-student-date-filters.ts` to manage URL date parameters and return memoized `startDate`/`endDate`.
- [x] 1.2 Move `headerProps` constant outside of the `StudentDetailsPage` component function in `client/src/features/students/pages/StudentDetailsPage.tsx`.

## 2. Component Extraction

- [x] 2.1 Create `StudentInfoSection` component in `client/src/features/students/components/StudentInfoSection.tsx` with its own loading and error states.
- [x] 2.2 Create `StudentEditModal` component in `client/src/features/students/components/StudentEditModal.tsx` to wrap the `StudentForm` and modal logic.

## 3. Page Orchestration

- [x] 3.1 Update `StudentDetailsPage` to utilize the new `useStudentDateFilters` hook.
- [x] 3.2 Refactor `StudentDetailsPage` layout to render `PageLayout` immediately and delegate section rendering to the new components.
- [x] 3.3 Remove the top-level blocking `studentQuery.isPending` check to enable parallel fetching.
- [x] 3.4 Ensure the `StudentEditModal` correctly consumes data from the page-level query when opened.

## 4. Verification

- [x] 4.1 Verify that the page shell (Header) and section skeletons appear simultaneously on initial load.
- [x] 4.2 Confirm that changing date ranges updates the KPI and Events sections but does not cause the Profile section to show a loading state.
- [x] 4.3 Verify that the student edit workflow remains functional after being moved to its own component.
