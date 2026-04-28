---
phase: 01-authentication-protected-access
plan: 03
subsystem: auth
tags: [react, react-router, tanstack-query, session-auth, protected-routing]
requires:
  - phase: 01-authentication-protected-access
    provides: login page, auth gate, session bootstrap hook, current-user query wrapper
provides:
  - a public `/login` route separated from protected secretary/admin routes
  - authenticated app-shell user context with in-shell logout affordance
  - protected query cache cleanup on logout to avoid stale private data
affects: [routing, logout-ux, protected-data]
tech-stack:
  added: []
  patterns: [public login route plus auth-gated app shell, protected query cache cleanup after logout]
key-files:
  created: []
  modified:
    - client/src/App.tsx
    - client/src/components/layout/MainLayout.tsx
    - client/src/lib/shared/queryClient.ts
key-decisions:
  - "Keep `/login` as the only public SPA entry and gate every existing workflow branch with the shared AuthGate."
  - "Remove protected query cache entries after logout before redirecting so signed-out users cannot keep browsing stale secretary data."
patterns-established:
  - "Route protection lives at the router branch level instead of being reimplemented inside each feature page."
  - "Logout UX in the authenticated shell uses useAuthSession plus shared query cache cleanup for immediate access teardown."
requirements-completed: [AUTH-03, AUTH-04]
duration: 1min
completed: 2026-04-18
---

# Phase 1 Plan 3: Protected routing and logout shell Summary

**Protected React Router branches with a public login entry, authenticated shell user chrome, and cache-clearing logout behavior.**

## Performance

- **Duration:** 1 min
- **Started:** 2026-04-18T12:21:28Z
- **Completed:** 2026-04-18T12:22:55Z
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments
- Split the SPA router into a public `/login` route and an auth-gated branch for dashboard, students, parents, employees, and events.
- Added authenticated user context and a visible `Sair` action to the main shell without changing existing brownfield navigation targets.
- Centralized protected-query cache cleanup so logout immediately removes access to cached private data before redirecting back to login.

## Task Commits

Each task was committed atomically:

1. **Task 1: Split the router into public and protected branches** - `06a00fd8` (feat)
2. **Task 2: Add authenticated user chrome and secure logout behavior** - `b53f2f23` (feat)

## Files Created/Modified
- `client/src/App.tsx` - exposes `/login`, wraps the protected app shell with `AuthGate`, and redirects unmatched routes based on session state.
- `client/src/components/layout/MainLayout.tsx` - displays authenticated user context, keeps navigation intact, and adds the `Sair` logout action.
- `client/src/lib/shared/queryClient.ts` - exports protected-query cache cleanup helpers used to tear down stale protected data after logout.

## Decisions Made
- Kept auth enforcement at the router branch level so all existing secretary/admin routes inherit the same redirect and loading behavior.
- Used the authenticated shell itself as the logout surface so users can end the session from anywhere in the protected app.
- Removed protected TanStack Query entries during logout instead of only clearing the current-user query, ensuring stale protected pages cannot linger after sign-out.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered
- Full `npm run lint` still fails because of a pre-existing syntax error in `client/src/components/ui/text-input.tsx`.
- Full `npm run build` still fails because of that same pre-existing syntax error plus a TypeScript parse issue in `react-router` typings in this environment.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- Phase 1 routing and logout UX are in place for secretary/admin workflows.
- The phase is ready for manual browser verification once the unrelated frontend parse blockers are resolved.

## Deferred Issues
- See `.planning/phases/01-authentication-protected-access/deferred-items.md` for the unrelated frontend lint/build blockers discovered during verification.

## Self-Check: PASSED
