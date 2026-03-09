# Client Refactor Review

Review date: 2026-03-08

## Current status

- Goal: improve client readability and consistency without a large rewrite
- Recommended next item: 1
- Current stack assessment: `shadcn/ui` + Tailwind + CSS Modules is acceptable for this project if each one keeps a clear role

Scale used in this document:

- Complexity: 1 = very easy, 5 = advanced
- Impact: 1 = small gain, 5 = major gain
- Importance: 1 = nice to have, 5 = should happen soon

## What is already working well

- Feature folders are easy to scan
- API access is centralized in `client/src/services/api.ts`
- Zod + React Hook Form is a good fit for beginner-friendly forms
- `shadcn/ui` primitives already give the project a decent base
- The app is still small enough for incremental refactors

## Styling direction

- Keep `shadcn/ui` for reusable primitives like button, card, input, table
- Keep Tailwind for short utility composition inside JSX
- Keep CSS Modules for page layout, feature-specific structure, and longer visual rules
- Avoid styling the same concern in both Tailwind and CSS Modules at the same time

## Recommended items

### 1) Extract a small page-state fetching pattern

- Complexity: 2/5
- Impact: 5/5
- Importance: 5/5
- Why this matters: many pages repeat the same `loading`, `error`, `try/catch/finally`, and `load*` structure
- Refactor goal: introduce one small reusable pattern such as `useAsyncPage` or `usePageRequest`, but keep it explicit and local
- Files:
  - `client/src/features/students/StudentsPage.tsx`
  - `client/src/features/students/StudentDetailPage.tsx`
  - `client/src/features/employees/EmployeesPage.tsx`
  - `client/src/features/employees/EmployeeDetailPage.tsx`
  - `client/src/features/events/EventsPage.tsx`
  - `client/src/features/events/EventDetailPage.tsx`
  - `client/src/features/dashboard/DashboardPage.tsx`
- Suggested commit: `refactor shared page fetch state`

### 2) Consolidate repeated form layout components

- Complexity: 2/5
- Impact: 4/5
- Importance: 5/5
- Why this matters: student, employee, and event create pages repeat the same label, help, error, action-row, and grid patterns
- Refactor goal: create a few lightweight form helpers like `FormField`, `FormError`, `FormActions`, or one shared form layout module
- Files:
  - `client/src/features/students/StudentCreatePage.tsx`
  - `client/src/features/employees/EmployeeCreatePage.tsx`
  - `client/src/features/events/EventCreatePage.tsx`
  - `client/src/features/students/StudentCreatePage.module.css`
  - `client/src/features/events/EventCreatePage.module.css`
  - `client/src/features/employees/EmployeeCreatePage.module.css`
- Suggested commit: `refactor shared form layout patterns`

### 3) Extract shared validation patterns on the client too

- Complexity: 2/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: CPF, phone, CEP, and date validation logic is repeated across Zod schemas just like it used to be on the backend
- Refactor goal: create a small `validation.ts` or `schema-helpers.ts` with shared regex/constants/refinements
- Files:
  - `client/src/lib/schemas/student.ts`
  - `client/src/lib/schemas/employee.ts`
  - `client/src/lib/schemas/parent.ts`
  - `client/src/lib/schemas/event.ts`
- Suggested commit: `refactor shared client validation patterns`

### 4) Separate data formatting from page rendering

- Complexity: 2/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: several pages format money, labels, status text, and summary values inline, which makes the JSX longer than it needs to be
- Refactor goal: move small formatting helpers to `client/src/lib` and keep components focused on rendering
- Files:
  - `client/src/features/students/StudentDetailPage.tsx`
  - `client/src/features/employees/EmployeeDetailPage.tsx`
  - `client/src/features/events/EventDetailPage.tsx`
  - `client/src/features/events/EventsPage.tsx`
  - `client/src/lib/utils.ts`
- Suggested commit: `refactor client display formatters`

### 5) Clean up API query string building

- Complexity: 2/5
- Impact: 3/5
- Importance: 4/5
- Why this matters: `client/src/services/api.ts` still manually concatenates query strings in several methods, which is readable now but gets brittle as filters grow
- Refactor goal: use Axios `params` objects or a tiny query helper for list endpoints
- Files:
  - `client/src/services/api.ts`
- Suggested commit: `refactor api query param building`

### 6) Make page-level CSS Modules more consistent

- Complexity: 2/5
- Impact: 3/5
- Importance: 4/5
- Why this matters: the CSS Modules are not messy, but they repeat the same `.page`, `.header`, `.field`, `.actions`, `.tableWrap`, and responsive rules across features
- Refactor goal: define a small convention for page shells and form/table layouts, or centralize only the most repeated patterns
- Files:
  - `client/src/components/layout/MainLayout.module.css`
  - `client/src/features/students/*.module.css`
  - `client/src/features/employees/*.module.css`
  - `client/src/features/events/*.module.css`
  - `client/src/features/dashboard/DashboardPage.module.css`
- Suggested commit: `refactor page css module conventions`

### 7) Introduce route-level code splitting

- Complexity: 3/5
- Impact: 3/5
- Importance: 3/5
- Why this matters: the build already warns that the main JS chunk is larger than `500 kB`
- Refactor goal: use `React.lazy` and route-level splitting for feature pages, not for tiny components
- Files:
  - `client/src/App.tsx`
- Suggested commit: `refactor route level lazy loading`

### 8) Improve state naming and user-facing wording consistency

- Complexity: 1/5
- Impact: 2/5
- Importance: 3/5
- Why this matters: some names still reflect implementation details like `deleteError` for archive actions, or generic `loadData` naming that hides page intent
- Refactor goal: rename local state and handlers so they describe the real user action
- Files:
  - `client/src/features/students/StudentsPage.tsx`
  - `client/src/features/employees/EmployeesPage.tsx`
  - `client/src/features/events/EventsPage.tsx`
- Suggested commit: `cleanup client state naming`

## Recommended order

1. Item 1 - shared page fetch state
2. Item 2 - shared form layout patterns
3. Item 3 - shared client validation patterns
4. Item 4 - display formatters
5. Item 5 - API query param building
6. Item 6 - CSS module conventions
7. Item 8 - state naming cleanup
8. Item 7 - route-level lazy loading

## Suggested branch guide

### Goal

- Keep the client easy to read while removing the most repeated patterns
- Avoid a broad component architecture rewrite right before epic/story delivery

### Ground rules

- Prefer small refactors grouped by one concern
- Avoid adding large state libraries or data-fetching frameworks right now
- Keep hooks/components explicit and easy to trace
- Refactor page patterns only when at least 2-3 screens benefit from the change

### Risks to watch

- Over-abstracting form fields can make pages harder to follow
- Overusing Tailwind + CSS Modules in the same block can make styling harder to maintain
- Route splitting adds async boundaries, so loading states must stay clear

### Done criteria

- Common client patterns are easier to spot and reuse
- Create pages share more structure without hiding behavior
- Validation rules are defined in fewer places
- API access stays centralized and easier to extend
- Styling responsibilities are clearer between `shadcn/ui`, Tailwind, and CSS Modules
