# Roadmap

## Delivery Order

### Phase 0 - Planning Cleanup

- Canonicalize docs
- Keep roadmap and epics aligned
- Remove migrated archive material

### Phase 1 - Core Data and Finance Baseline

- Close backend consistency gaps for students, parents, events, and dashboard data
- Finish parent flow support needed by operations
- Improve event response shape for finance/dashboard use

### Phase 2 - Authentication and Authorization

- Add login and password handling
- Protect API and UI routes
- Add baseline RBAC and security hardening

### Phase 3 - Frontend Operations Polish

- Finish dashboard data flow
- Finish remaining core workflow polish
- Close parent inline flow and remaining UX cleanup

### Phase 4 - Scheduling Expansion

- Google Calendar sync
- Calendar-oriented views

### Phase 5 - Reporting and Billing Expansion

- Monthly finance reporting improvements
- Payment tracking enhancements
- Payment gateway only after core reporting is stable

## Epic Index

| Epic | Status | Intent | Next practical slice |
|---|---|---|---|
| `E-001` | DONE | Keep docs/planning canonical and current | keep epics synced as work lands |
| `E-002` | IN_PROGRESS | Close core data and backend ops/finance gaps | dashboard summary + event response improvements |
| `E-003` | TODO | Add auth, RBAC, and security baseline | login/auth skeleton |
| `E-004` | IN_PROGRESS | Polish operational frontend flows | parent inline flow + dashboard integration |
| `E-005` | TODO | Expand scheduling with sync and calendar views | calendar sync foundation |
| `E-006` | TODO | Expand reporting, payment tracking, and billing | monthly finance reporting endpoint |

## Priority Notes

- MVP priority is operations plus monthly finance visibility.
- Calendar UI and payment gateway work stay behind auth and reporting correctness.
- When a task can be split into backend contract first and frontend usage second, prefer that order.
