## Context

The EmployeesTable currently displays only archival status (Ativo/Arquivado) via badges. We need to add payment status visualization using daisyUI indicator components.

The system already has:
- `useGetEmployeeMonthlySummary` hook that provides `totalPaid`, `totalUnpaid`, and presumably last payment date per employee
- DaisyUI already installed and in use (based on existing badge usage)

## Goals / Non-Goals

**Goals:**
- Add payment status indicator to EmployeesTable Status column
- Show yellow dot when employee has unpaid amount (pending)
- Show green dot when employee is fully paid and last payment is recent (≤1 month)
- Show red dot when last payment was made more than 1 month ago (late)
- Keep existing row click behavior for navigation

**Non-Goals:**
- Not creating a new API endpoint - use existing `useGetEmployeeMonthlySummary`
- Not modifying the employee detail page (just the table)
- Not adding payment status to StudentsTable (separate change)

## Decisions

### Decision 1: Where to implement the indicator

**Option A:** Inline in EmployeesTable.tsx
- Pros: Simple, no new files
- Cons: Not reusable, harder to test

**Option B:** Separate component in `components/ui/`
- Pros: Reusable, testable, follows existing component pattern
- Cons: Slight extra setup

**Chosen:** Option B - Create `PaymentStatusIndicator` component in `components/ui/`
- The codebase already has reusable UI components in `components/ui/`
- Makes it easy to add to StudentsTable later if needed

### Decision 2: How to get payment data for each employee

**Option A:** Fetch summary for each employee in the table
- Problem: N+1 query problem, performance issue with many employees

**Option B:** Use existing list query with extended fields
- The list query might already include payment summary

**Chosen:** Option A - Fetch per row, accept small overhead for MVP
- The table likely shows limited rows (pagination)
- Simplest approach that works with existing hook
- Can optimize later if needed

### Decision 3: DaisyUI indicator implementation

**Chosen:** Use `indicator` class with `badge badge-xs badge-{color}` pattern
- Matches existing DaisyUI usage in codebase
- Consistent with design system

## Risks / Trade-offs

- [Risk] N+1 query pattern → Mitigation: Table uses pagination, limiting concurrent fetches
- [Risk] Missing last payment date in summary → Mitigation: Spec defines behavior, will verify during implementation

## Open Questions

- Does `useGetEmployeeMonthlySummary` return last payment date? Need to verify in implementation.