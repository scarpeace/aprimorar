## Context

The frontend still contains full finance and admin feature folders and a boilerplate auth/login page. Finance code currently references stale generated types and hooks, and admin/auth are not part of the active backend behavior. The user wants these feature areas removed for now, while preserving simple route targets that make future rebuilds explicit.

## Goals / Non-Goals

**Goals:**
- Replace finance with one `FinancesPage` placeholder that renders `implementando`.
- Replace admin with one `AdminPage` placeholder that renders `implementando`.
- Keep `LoginPage` as a minimal auth placeholder that renders `implementando`.
- Remove stale finance/admin/auth implementation files that are no longer active.
- Update routes and navigation so users land on the placeholder pages.

**Non-Goals:**
- Do not design the final finance, admin, or authentication experiences.
- Do not change backend endpoints or generated Kubb files.
- Do not preserve stale finance/admin forms, hooks, tables, or user-management flows.
- Do not update student, employee, or event behavior except to remove direct imports from deleted finance modules if required for compilation.

## Decisions

### Decision 1: Collapse each feature to a single page component

Finance, admin, and auth will each keep only a page-level component: `FinancesPage`, `AdminPage`, and `LoginPage`. This matches the requested reset and makes the remaining surface easy to rebuild later.

Alternative considered: keep existing folders and hide the broken subroutes. That would leave stale code in the repository and keep TypeScript/build errors alive.

### Decision 2: Route old entry points to the new placeholders

Existing users or links may still navigate to `/finance/expenses`, `/finance/settlement`, or `/admin/users`. Those paths should render the placeholder page rather than a broken route. Navigation should point to `/finance` and `/admin` as the canonical placeholders.

Alternative considered: remove old routes entirely. That would be cleaner, but it can create unnecessary 404-style redirects during the transition.

### Decision 3: Remove imports from deleted finance modules

Any component outside `client/src/features/finance` that imports finance hooks must be updated so the finance folder can be removed. If those components are already disabled or commented in the UI, remove the stale import path rather than keeping a dead dependency.

Alternative considered: keep a compatibility hook file under finance. That would violate the request to remove the feature folder except for the placeholder page.

## Risks / Trade-offs

- Removing feature code reduces rollback convenience -> the OpenSpec change and git history preserve the previous implementation.
- Existing routes may still be referenced externally -> keep old subroutes mapped to placeholders.
- Build may still fail from unrelated backend/Kubb contract drift -> document remaining errors separately from this placeholder reset.
