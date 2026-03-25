# Frontend AGENTS.md

React + TypeScript + Vite guidance for coding agents working in `client/`.

## Scope
- This file applies to the SPA in `client/`
- Read the root `AGENTS.md` first for repo-wide workflow and verification expectations
- Generated Kubb files in `client/src/kubb/` are off-limits for manual edits

## Build And Verification
Run from `client/`:

```bash
npm install
npm run dev
npm run lint
npm run build
npm run preview
npm run sync
```

- `npm run build` runs `tsc -b && vite build`
- `npm run sync` regenerates hooks, schemas, and types from the backend OpenAPI document
- There is no dedicated frontend test runner configured right now
- For frontend-only work, the expected minimum verification is `npm run lint` and `npm run build`

## Project Structure
- `src/features/` contains route pages, domain UI, and feature-specific logic
- `src/components/ui/` contains reusable UI primitives shared across features
- `src/lib/` contains shared utilities, formatting helpers, API helpers, and infra code
- `src/kubb/` contains generated API code; regenerate instead of editing

## Imports And Module Boundaries
- Import order: React/core, third-party, `@/` aliases, then relative imports
- Use `import type` for type-only imports
- Prefer `@/` aliases for cross-feature imports; use relative imports within the same feature when clearer
- Use named exports for components, hooks, and helpers; avoid `export default`
- Keep feature internals inside the feature folder unless they are clearly reusable across domains

## Components And Files
- Route pages use `*Page.tsx`
- Feature components belong in `src/features/<feature>/components/`
- Shared building blocks belong in `src/components/ui/`
- Keep page components focused on orchestration; move repeated logic into hooks, helpers, or child components
- Prefer explicit props types near the top of the file
- Prefer early returns for loading, empty, and error states rather than nested conditionals

## Styling And UI
- Preserve the visual language already present in the touched feature
- Reuse shared UI primitives before creating new one-off controls
- Keep layouts responsive for desktop and mobile
- Avoid adding comments unless a block is genuinely non-obvious
- Follow the file's existing formatting style; do not mass-reformat unrelated lines

## Types, Zod, And Forms
- Use Zod schemas and inferred types at form and API boundaries
- Treat generated Kubb schemas/types/hooks as the source of truth for server contracts
- Prefer `z.input<typeof schema>` for form values when schema transforms or coercions are involved
- React Hook Form should normally use `zodResolver(schema)` and `mode: "onBlur"`
- Hidden fields and custom inputs should update form state through `setValue` with validation/dirty flags when needed
- Avoid unsafe casts such as `as unknown as Resolver<...>` unless the root mismatch is understood and unavoidable

## Data Fetching And State
- Use TanStack Query for server state
- Reuse stable query keys; do not invent ad hoc string keys per component
- Use `enabled` for queries that depend on route params or other conditional inputs
- Prefer keeping pagination, filters, and search term state in the parent page or feature container
- Let shared table-like components render data and pagination, but keep query orchestration in the parent
- Use placeholder data or `keepPreviousData` for paginated UX where it matches existing patterns
- Invalidate relevant queries after mutations succeed

## API And Error Handling
- Use shared helpers such as `getFriendlyErrorMessage` when available
- Prefer UI-level Portuguese error messages over raw thrown messages
- Do not swallow API failures with `console.log` only; surface them through page states or alerts
- If Zod parsing fails on generated schemas, inspect the backend contract and Kubb output before weakening validation
- When Kubb types mark paginated fields as optional, handwritten abstractions should accept that shape rather than forcing stricter local types

## Naming Conventions
- Components, pages, interfaces, and exported types use PascalCase
- Hooks use `useXxx`
- Variables, functions, and object properties use camelCase
- CSS modules use PascalCase file names when that pattern already exists nearby
- Prefer descriptive names such as `currentPage`, `studentEvents`, or `handleSearchChange` over abbreviations

## Reusable Patterns

### Tables
- Keep generic `Table` components domain-agnostic and focused on rendering
- Put domain-specific columns, formatting, and context rules into wrappers such as `EventTable`
- Pass `currentPage` and `onPageChange` from the parent page instead of managing them inside a generic table

### Search Inputs
- Reusable search input components should be presentational
- Parent pages should own `searchTerm`, debounce behavior, and query params
- Reset pagination in the same event handler that changes the search term when possible

### Route Params
- `useParams()` values are optional; guard invalid IDs with `enabled` in queries and explicit UI fallbacks
- Do not call hooks conditionally after an early return

## Kubb And Generated Contracts
- After changing backend DTOs or endpoint signatures, run the backend OpenAPI generation and then `npm run sync`
- Do not patch generated schemas to work around backend mismatches unless the user explicitly asks for a temporary hack
- If generated schemas reject backend dates or enums, fix the backend contract or mapper first

## Suggested Verification By Change Type
- Form/UI only: `npm run lint` and `npm run build`
- Query or API consumption change: `npm run lint`, `npm run build`, and verify relevant screens manually in dev mode
- Contract change: backend OpenAPI generation, `npm run sync`, then frontend build

## Quick Checklist
- Did you keep user-facing strings in Portuguese?
- Did you avoid editing `src/kubb/` manually?
- Did you keep generic components generic and push domain rules into wrappers?
- Did you verify with `npm run lint` and `npm run build` when practical?
