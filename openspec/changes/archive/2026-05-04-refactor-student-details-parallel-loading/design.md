## Context

The current `StudentDetailsPage` implementation uses a blocking loading guard at the top level: `if (studentQuery.isPending) return <LoadingCard />`. This prevents the `PageLayout`, `StudentKPIs`, and `StudentEventsTable` from mounting until the student's basic profile is fetched. Since these sections are independent in the backend, they can and should be loaded in parallel.

## Goals / Non-Goals

**Goals:**
- Improve perceived performance by rendering the page shell immediately.
- Enable parallel API requests for student profile, summary, and events.
- Modularize the page by extracting logical sub-sections into components.
- Ensure stable date references for child components.

**Non-Goals:**
- Refactoring the internal logic of `StudentForm` or `StudentKPIs`.
- Changing any backend API endpoints.
- Modifying the styles or layout of the page significantly.

## Decisions

### 1. Decentralized Loading State
Instead of a single blocking guard in `StudentDetailsPage`, the loading and error states will be handled within the specific sections (`StudentInfoSection`, `StudentKPIs`, `StudentEventsTable`).
- **Rationale**: This allows the "shell" (Header, Date Filters) to render instantly and each section to pop in as its specific data arrives.
- **Alternatives Considered**: Using `Suspense` and `useSuspenseQuery`. While cleaner, it requires broader architectural changes to the `PageLayout` and error boundaries which is out of scope for this surgical refactor.

### 2. Request Deduplication Strategy
The `useGetStudentById` query will be called in both `StudentDetailsPage` (for the Edit Modal) and `StudentInfoSection` (for the profile display).
- **Rationale**: TanStack Query automatically deduplicates these requests, so no extra network call is made, while keeping components decoupled.

### 3. Custom Filter Hook
Extract URL state logic into a `useStudentDateFilters` hook.
- **Rationale**: Improves readability of the main page and makes the logic reusable. It will return memoized `startDate` and `endDate` objects.

## Risks / Trade-offs

- **[Risk] Layout Shift** → **Mitigation**: Use fixed-height skeleton loaders in `StudentInfoSection` to match the typical size of the data cards, minimizing layout jumps as data arrives.
- **[Risk] State Synchronization** → **Mitigation**: Rely on the URL as the single source of truth for date filters, ensuring all components reacting to `useSearchParams` stay in sync.
