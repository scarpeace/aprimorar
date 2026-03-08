# React Top 10 Checklist (Project-Specific)

Scope: `client` (Vite + React CRUD screens)
Goal: keep code beginner friendly, organized, and without unnecessary abstraction.

## 1) Parallelize independent requests
- [ ] Use `Promise.all` when fetches do not depend on each other.
- [ ] Avoid sequential `await` chains that create waterfalls.
- File examples:
  - `client/src/features/events/EventCreatePage.tsx`
  - `client/src/features/dashboard/DashboardPage.tsx`

## 2) Keep orchestration in pages
- [ ] Pages own fetch/mutation/loading/error state.
- [ ] Form components stay focused on rendering + submit handling.
- File examples:
  - `client/src/features/students/StudentCreatePage.tsx`
  - `client/src/features/events/EventCreatePage.tsx`

## 3) Avoid early over-abstraction
- [ ] Prefer explicit page logic over generic config hooks for small codebases.
- [ ] Introduce abstraction only when duplication is clearly painful (3+ real uses).
- File examples:
  - `client/src/features/students/StudentCreatePage.tsx`
  - `client/src/features/students/StudentDetailPage.tsx`

## 4) Derive state during render
- [ ] Do not store derived values in state unless necessary.
- [ ] Compute simple derived values inline or via lightweight helpers.
- File examples:
  - `client/src/features/events/EventCreatePage.tsx` (`selectedStudentName`, `selectedEmployeeName`)
  - `client/src/features/students/StudentCreatePage.tsx` (`selectedParentName`)

## 5) Keep interaction logic in event handlers
- [ ] Submit/delete/archive logic should run in click/submit handlers.
- [ ] Avoid effect-driven side effects for user-triggered actions.
- File examples:
  - `client/src/features/events/EventsPage.tsx` (`handleDelete`)
  - `client/src/features/students/StudentsPage.tsx` (`handleArchiveToggle`)

## 6) Keep effects focused and dependencies narrow
- [ ] Effects should synchronize external state only.
- [ ] Depend on specific primitives when possible.
- File examples:
  - `client/src/features/students/StudentDetailPage.tsx`
  - `client/src/features/employees/EmployeeDetailPage.tsx`

## 7) Use functional updates when based on previous state
- [ ] Use `setState((prev) => ...)` when next state depends on previous state.
- [ ] Avoid stale closure risks in callbacks.
- File examples:
  - `client/src/features/events/EventsPage.tsx` (`setEventList((prev) => ...)`)
  - `client/src/features/employees/EmployeesPage.tsx` (`setEmployeeList((prev) => ...)`)

## 8) Keep bundle-friendly import patterns
- [ ] Prefer direct imports and avoid heavy barrel imports when possible.
- [ ] Keep route-level code straightforward and split only when needed.
- File examples:
  - `client/src/features/events/EventDetailPage.tsx`
  - `client/src/components/layout/MainLayout.tsx`

## 9) Standardize user-facing pt-BR strings
- [ ] Validation, error, and empty-state messages should use UTF-8 accents consistently.
- [ ] Keep wording consistent across schemas and screens.
- File examples:
  - `client/src/lib/schemas/student.ts`
  - `client/src/lib/schemas/employee.ts`
  - `client/src/lib/schemas/parent.ts`
  - `client/src/services/api.ts`

## 10) Always model full UX state
- [ ] Handle loading, error, empty, and success states explicitly.
- [ ] Keep retry actions visible where relevant.
- File examples:
  - `client/src/features/students/StudentsPage.tsx`
  - `client/src/features/events/EventsPage.tsx`
  - `client/src/features/dashboard/DashboardPage.tsx`

---

## Quick Review Routine (before PR)
- [ ] `cd client && npm run lint`
- [ ] `cd client && npm run build`
- [ ] Manually test one create/edit/delete flow in students and events.
- [ ] Trigger at least one validation error per form and confirm message clarity.
