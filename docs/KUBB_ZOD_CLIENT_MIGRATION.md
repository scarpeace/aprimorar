# Kubb Migration Log (Zod + Client)

## Date
2026-03-21

## Objective
Migrate code generation to a cleaner setup focused on:
- Zod schemas for validation
- Generated API client functions
- Custom app-level React Query hooks

## What Was Changed

### 1) Kubb configuration
Updated `client/kubb.config.ts`:
- Removed `plugin-react-query`
- Added `plugin-client`
- Kept `plugin-zod` with:
  - `inferred: true`
  - `typed: true`
  - `group` by OpenAPI tag
- Kept `plugin-oas`
- Kept `output.clean: true`

Note: `plugin-ts` was kept because `plugin-zod` with `inferred: true` requires `plugin-ts` in this setup.

### 2) Package scripts
Updated `client/package.json`:
- Replaced dashboard-only generation flow
- Kept one canonical command:
  - `npm run sync` -> `kubb generate`

### 3) Dependencies
Installed:
- `@kubb/plugin-client`

### 4) Generated output
Ran:
- `npm run sync`

Result:
- Generation succeeded (187 files)
- Output includes:
  - `src/gen/client/**` (generated API functions)
  - `src/gen/schemas/**` (generated Zod schemas)
  - `src/gen/types/**` (generated TypeScript types)
  - `src/gen/.kubb/**` (internal helpers)

### 5) Dashboard migration
Dashboard moved to local query layer + generated client + zod parse:

- `client/src/features/dashboard/api/dashboardApi.ts`
  - Uses generated client function
  - Parses response with generated zod schema

- `client/src/features/dashboard/query/useDashboardQueries.ts`
  - Local React Query hook for dashboard summary

- `client/src/features/dashboard/query/dashboardQueryKeys.ts`
  - Local key builder

- `client/src/features/dashboard/DashboardPage.tsx`
  - Uses `useDashboardSummaryQuery`
  - Uses shared error helper and currency formatter

- `client/src/features/dashboard/components/PizzaChart.tsx`
  - Uses zod-inferred type pattern
  - Adjusted tooltip formatter typing

### 6) Shared infrastructure restored
Created missing shared files referenced across the app:

- `client/src/lib/shared/api.ts`
- `client/src/lib/shared/api-errors.ts`
- `client/src/lib/shared/page-response.ts`
- `client/src/lib/shared/use-debounce.ts`
- `client/src/lib/shared/queryClient.ts`
- `client/src/lib/utils/formatter.ts`
- `client/src/lib/utils/brazilianStates.ts`

### 7) ESLint configuration
Updated `client/eslint.config.js` to ignore generated code under:
- `src/gen/**`

## Verification

| Check | Status |
|-------|--------|
| `npm run sync` | Success |
| `npm run build` | Success |
| `npm run lint` | Warnings/errors in existing app code (pre-existing, not from migration) |

## Important Notes

1. This migration removed generated React Query hooks, but not generated TS types.
2. Full "zod-only without types" was not viable with current `plugin-zod` inferred setup (requires `plugin-ts`).
3. Current recommended workflow:
   - Generate client + schemas from OpenAPI
   - Keep app-owned query/mutation hooks in `src/features/**/query`
   - Parse critical responses via generated zod schemas in API layer

## Next Steps

1. Continue migrating non-dashboard features to generated client + zod parse:
   - students
   - parents
   - employees
   - events
2. Clean existing lint debt in app files.
3. Keep backend `@Tag` names stable to preserve consistent generation grouping.
