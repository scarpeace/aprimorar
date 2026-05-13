## Why

Parent details should follow the same workflow pattern as student details: a focused detail page with a reusable information section, a related-records table, and an edit form in a modal. The current parent detail page mixes these concerns inline and references stale linked-student query state, which makes it harder to maintain while the frontend adapts to recent backend changes.

## What Changes

- Rework the parent detail page to mirror the structure of `StudentDetailsPage`.
- Add a `ParentInfoSection` component responsible for loading and displaying parent data plus edit/archive/delete actions.
- Add a `ParentStudentsTable` component for students linked to a responsible parent.
- Align `ParentForm` with the detail-page modal workflow, keeping create/update behavior and validation intact.
- Keep the parent detail page focused on layout, route params, modal state, and composition.

## Capabilities

### New Capabilities
- `parent-detail-dashboard`: Parent detail page composition, parent information display, linked student table, and parent edit modal workflow.

### Modified Capabilities

## Impact

- `client/src/features/parents/pages/ParentDetailPage.tsx`: simplify into a composition page similar to student details.
- `client/src/features/parents/components/ParentInfoSection.tsx`: new component for parent summary and actions.
- `client/src/features/parents/components/ParentStudentsTable.tsx`: new component for linked student rows.
- `client/src/features/parents/components/ParentForm.tsx`: adapt as needed to behave consistently with the detail modal.
- `client/src/features/parents/hooks/parent-mutations.ts`: may need query invalidation cleanup for current generated Kubb hooks.
- Generated Kubb hooks/types must not be edited manually; use the current generated API surface.
