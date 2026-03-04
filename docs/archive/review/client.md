# Client Review

## Quick Overview
The client is a Vite + React app with a simple route layout, a Tailwind UI, and a small API layer. The structure is clean and approachable for beginners, but the MVP needs a tighter dashboard flow, better error UX, and consistent UI tokens.

## Strengths
- Clear feature separation under `src/features` and a shared layout.
- Centralized API client in `src/services/api.ts`.
- Zod schemas provide a solid base for runtime validation.
- UI components are consistent and easy to reuse.

## MVP Priority (Beginner-Friendly)
These items are ordered for the MVP focus: operational management + financial control.

### P0) Fix UI dev blockers
- The UI components export multiple items (ex: `Button` + `buttonVariants`), which triggers the React Refresh lint warning in VSCode. Add a targeted ESLint override for `client/src/components/ui/**` so beginners are not blocked by noisy warnings.
- `bg-background`, `text-muted-foreground`, and `bg-primary` tokens are used, but `client/src/index.css` does not define semantic CSS variables. Add base tokens so the UI renders consistently, then swap to the company palette later.

Files:
- `client/eslint.config.js`
- `client/src/index.css`

### P1) Dashboard correctness and data flow
- The dashboard currently calculates revenue from `payment`, which is incorrect (payment is teacher cost). It should show Revenue (price), Cost (payment), Profit (price - payment) for the current month, and upcoming events for the next 15 days.
- Replace multiple list calls with a single `GET /v1/dashboard/summary` API call to keep the dashboard simple and fast.

Files:
- `client/src/features/dashboard/DashboardPage.tsx`
- `client/src/services/api.ts`

### P2) MVP navigation flow
- Add quick actions on the dashboard: `Criar evento`, `Novo aluno`, `Novo colaborador`, each routing to a dedicated create page.
- Add placeholder create routes for now (`/events/new`, `/students/new`, `/employees/new`) to avoid dead-end buttons.
 - Defer full calendar view and dashboard graphics until post-MVP.

Files:
- `client/src/App.tsx`
- `client/src/features/events/EventCreatePage.tsx`
- `client/src/features/students/StudentCreatePage.tsx`
- `client/src/features/employees/EmployeeCreatePage.tsx`

### P3) UX polish for beginners
- Translate layout and error messages to PT-BR (professional tone), and show friendly error states instead of only `console.error`.
- Improve card layouts and responsiveness across detail pages and dashboard.

Files:
- `client/src/components/layout/MainLayout.tsx`
- `client/src/features/*/*.tsx`
- `client/src/services/api.ts`

## Low-Risk Refinements
- Add a 404 page route for unknown paths.
- Extract the navigation array to a separate file to keep the layout focused.
- Consider a shared `PageHeader` component for consistent page titles.
