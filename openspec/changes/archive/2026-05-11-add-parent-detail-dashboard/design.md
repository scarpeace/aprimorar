## Context

`StudentDetailsPage` composes the student detail experience from a route param, a detail information component, a related events table, and a modal form. `ParentDetailPage` currently keeps parent summary, linked-student state, action controls, and form modal concerns inline, and it references linked-student query variables that are not defined in the current file.

The current generated API surface exposes parent lookup through `useGetParentById` and linked students through `useGetStudentsByParent`. Generated files under `client/src/kubb/` must remain generated-only.

## Goals / Non-Goals

**Goals:**
- Make parent details follow the same composition pattern as `StudentDetailsPage`.
- Move parent summary loading and actions into `ParentInfoSection`.
- Move linked-students rendering into `ParentStudentsTable`.
- Keep `ParentDetailPage` responsible only for route params, modal state, pagination state, and page composition.
- Keep `ParentForm` usable for the edit modal and consistent with existing parent create/update mutations.

**Non-Goals:**
- Do not add or change backend endpoints.
- Do not edit generated Kubb files manually.
- Do not redesign student detail pages beyond using them as the structural reference.
- Do not implement new archive/delete business rules beyond the behavior already exposed by existing buttons and mutations.

## Decisions

### Decision 1: Mirror student detail composition

The parent detail page will use the same high-level shape as `StudentDetailsPage`: `PageLayout`, an info section, a related table section, and a modal containing the form. This keeps page files small and makes the parent experience predictable for future detail-page work.

Alternative considered: keep improving the current inline `ParentDetailPage`. That would preserve fewer files, but it would keep loading, display, actions, and related rows coupled in one component.

### Decision 2: ParentInfoSection owns parent query and actions

`ParentInfoSection` will accept `parentId` and `onEdit`, call `useGetParentById`, and render loading/error states, summary fields, and edit/archive/delete actions. It should use `active` from `ParentResponseDTO` for status instead of stale `archivedAt` fields.

Alternative considered: fetch parent data in `ParentDetailPage` and pass the DTO down. That reduces duplicate query calls, but it makes the page responsible for data-state branching that the student detail pattern already delegates to the section component.

### Decision 3: ParentStudentsTable wraps linked-student query

`ParentStudentsTable` will accept `parentId`, `currentPage`, and `onPageChange`, call `useGetStudentsByParent`, and render a students table focused on the parent detail view. This avoids reusing the general `StudentsTable` when the parent page needs a clearer component boundary and may later need parent-specific empty states or actions.

Alternative considered: continue passing `useGetStudentsByParent` data into `StudentsTable`. That is faster, but it preserves a generic component dependency and does not satisfy the requested `ParentStudentsTable` shape.

### Decision 4: Fix mutation invalidation against current Kubb exports

Parent archive/unarchive invalidation should use existing generated query key helpers, including `getStudentsByParentQueryKey`, and remove stale or misspelled references. This is necessary for the new detail page to compile and refresh linked rows correctly.

Alternative considered: skip mutation cleanup and only build the new components. That would leave the parent feature with known TypeScript breakage and stale cache behavior.

## Risks / Trade-offs

- Current backend/Kubb churn may rename linked-student hooks again -> keep imports constrained to generated root exports and run `npm run sync` after backend API changes.
- `ParentInfoSection` and `ParentDetailPage` may both need parent data for the edit modal -> use React Query cache through `useGetParentById` rather than manually threading partial state through the page.
- Linked-student deletion/archive rules are still evolving -> keep the table read-focused and leave destructive actions in their existing buttons until backend rules settle.
