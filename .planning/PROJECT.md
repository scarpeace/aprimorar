# Aprimorar

## What This Is

Aprimorar is a school management application for a private class school that replaces spreadsheet-driven operational and financial control with a centralized system. It is currently oriented around secretary and administrator workflows, with the product expected to expand later to teachers, parents, students, and other employees.

## Core Value

The secretary must be able to manage the school day to day from the app without depending on scattered spreadsheets.

## Requirements

### Validated

- ✓ Dashboard summary endpoint and dashboard UI exist for high-level operational visibility — existing
- ✓ Student management flows already exist in the current codebase — existing
- ✓ Parent management flows already exist in the current codebase — existing
- ✓ Employee management flows already exist in the current codebase — existing
- ✓ Event management flows already exist in the current codebase — existing
- ✓ Simple authentication for secretary/administrator access is implemented with protected internal routes — Phase 1

### Active

- [ ] Make employee, parent, student, and event CRUD fully functional for v1 with the required business rules for school operations
- [ ] Introduce basic financial tracking for charges, payments, overdue balances, and simple summaries
- [ ] Provide a small but useful dashboard summary that combines daily operational visibility with financial visibility
- [ ] Remove the need to use Google Sheets for student and event management in day-to-day work

### Out of Scope

- Teacher, parent, and student self-service access in v1 — deferred until the secretary/administrator workflow is solid
- Advanced reports and richer analytics in v1 — the first release only needs enough visibility for daily decisions
- Notifications and messaging in v1 — not required to replace the spreadsheet workflow
- Payment gateway integrations in v1 — basic internal financial tracking is the priority first
- Complex permissions and role models in v1 — simple authentication is enough for the initial release

## Context

The school currently manages students and events in Google Drive spreadsheets, with no meaningful integration across those flows. Financial control is still fuzzy, there are no reports, and payment invoices or overdue balances are not being tracked in a structured system at all.

This repository is a brownfield monorepo with a Spring Boot backend and a React SPA frontend. The codebase already contains domain areas for dashboard, students, parents, employees, and events, plus generated OpenAPI-based frontend clients. Authentication is now present for internal staff through server-managed sessions and protected SPA/API routes, providing the secured base for the next hardening phases.

The immediate product user is the school secretary/administrator. Future milestones are expected to open access to teachers, parents, students, and other school employees once the operational core is stable.

## Constraints

- **Product scope**: v1 must focus on secretary/administrator workflows — later user portals are intentionally deferred to keep the first release operationally useful
- **Brownfield**: Existing student, parent, employee, event, and dashboard areas must be extended carefully rather than replaced — current code and patterns already exist
- **Tech stack**: Keep working within the current React SPA + Spring Boot + PostgreSQL architecture — this is the established repo direction
- **Integration boundary**: Frontend API types and hooks are generated from backend OpenAPI — backend contract changes must flow through codegen
- **Operational goal**: The app must be good enough to stop relying on spreadsheets for daily school administration — otherwise v1 has not delivered the core value

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Prioritize secretary/administrator workflows first | This is the real day-to-day user and the fastest path to replacing spreadsheet operations | - Pending |
| Treat finance in v1 as basic internal tracking, not a full billing platform | The immediate pain is lack of visibility and control, not advanced invoicing infrastructure | - Pending |
| Keep the first dashboard focused on both operational and financial snapshot data | Daily decision-making needs both school activity visibility and overdue/payment visibility | - Pending |
| Defer teacher, parent, and student access | Multi-role access would expand scope before the core administration workflow is stable | - Pending |
| Use Spring Security server-managed sessions instead of JWT | Immediate logout and browser refresh persistence matter more than token portability for this internal SPA | Implemented in Phase 1 |

## Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `/gsd-transition`):
1. Requirements invalidated? -> Move to Out of Scope with reason
2. Requirements validated? -> Move to Validated with phase reference
3. New requirements emerged? -> Add to Active
4. Decisions to log? -> Add to Key Decisions
5. "What This Is" still accurate? -> Update if drifted

**After each milestone** (via `/gsd-complete-milestone`):
1. Full review of all sections
2. Core Value check - still the right priority?
3. Audit Out of Scope - reasons still valid?
4. Update Context with current state

---
*Last updated: 2026-04-19 after Phase 1*
