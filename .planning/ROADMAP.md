# Roadmap: Aprimorar

## Overview

This roadmap turns the current brownfield school-management monorepo into a secretary-first operational system that can replace spreadsheet-driven daily work. It starts by securing access, then hardens the existing student, parent, employee, and event modules, adds finance as the main net-new domain, and finishes with a dashboard that combines operational and financial visibility.

## Phases

**Phase Numbering:**
- Integer phases (1, 2, 3): Planned milestone work
- Decimal phases (2.1, 2.2): Urgent insertions (marked with INSERTED)

Decimal phases appear between their surrounding integers in numeric order.

- [x] **Phase 1: Authentication & Protected Access** - Add secure sign-in and protect all secretary/admin workflows. (completed 2026-04-18)
- [ ] **Phase 2: Student & Parent Registry Hardening** - Make existing registry flows reliable enough to replace student/responsible-party spreadsheets.
- [ ] **Phase 3: Employee & Event Operations Hardening** - Tighten employee and event workflows for dependable day-to-day scheduling operations.
- [ ] **Phase 4: Finance Tracking Core** - Introduce charges, payments, and simple balance visibility for internal school finance tracking.
- [ ] **Phase 5: Unified Daily Dashboard** - Combine operational and financial indicators into one trustworthy daily snapshot.

## Phase Details

### Phase 1: Authentication & Protected Access
**Goal**: Internal staff can securely access the application, while anonymous users are kept out of protected workflows.
**Depends on**: Nothing (first phase)
**Requirements**: AUTH-01, AUTH-02, AUTH-03, AUTH-04
**Success Criteria** (what must be TRUE):
  1. Internal user can sign in with email or username plus password and reach the protected app.
  2. Authenticated user stays signed in after browser refresh until logout or session expiry.
  3. Authenticated user can log out from the app and protected pages stop being accessible immediately.
  4. Unauthenticated visitor cannot open dashboard, student, parent, employee, event, or finance workflows directly.
**Plans**: 4 plans
Plans:
- [ ] 01-01-PLAN.md — Create Spring Security session auth, internal-user persistence, and protected backend routes.
- [ ] 01-02-PLAN.md — Regenerate auth contracts and build the frontend login/bootstrap infrastructure.
- [ ] 01-03-PLAN.md — Wire protected routing and logout UX across the SPA.
- [ ] 01-04-PLAN.md — Restore auth client bootstrap and real session-backed refresh/logout behavior after UAT gaps.
**UI hint**: yes

### Phase 2: Student & Parent Registry Hardening
**Goal**: Secretary can manage student and responsible-party records reliably enough to stop depending on spreadsheets for registry work.
**Depends on**: Phase 1
**Requirements**: STUD-01, STUD-02, STUD-03, STUD-04, PARN-01, PARN-02, PARN-03, PARN-04
**Success Criteria** (what must be TRUE):
  1. Secretary can create, view, update, and archive student records without losing historical references.
  2. Secretary can create, view, update, and archive parent or responsible-party records with billing and contact data intact.
  3. Secretary can link the correct responsible party to a student and see that relationship preserved consistently when records are viewed or edited later.
  4. Secretary can quickly find the needed student record through search, filters, pagination, and archived-record visibility controls.
**Plans**: 3 plans
**UI hint**: yes

### Phase 3: Employee & Event Operations Hardening
**Goal**: Secretary can manage employees and school activities in the app with dependable business-rule enforcement.
**Depends on**: Phase 2
**Requirements**: EMPL-01, EMPL-02, EMPL-03, EMPL-04, EVNT-01, EVNT-02, EVNT-03, EVNT-04, EVNT-05
**Success Criteria** (what must be TRUE):
  1. Secretary can create, view, update, archive, and find employee records through search, filters, pagination, and archive controls.
  2. Secretary can create, view, update, and archive event records for classes, mentorships, and other school activities while keeping historical visibility.
  3. Secretary cannot save an event that breaks scheduling or ownership business rules and receives clear feedback instead.
  4. Secretary can open operational screens that show upcoming events needed for day-to-day planning.
**Plans**: 3 plans
**UI hint**: yes

### Phase 4: Finance Tracking Core
**Goal**: Secretary can track charges, payments, and balances inside the app instead of informal financial spreadsheets.
**Depends on**: Phase 3
**Requirements**: FIN-01, FIN-02, FIN-03
**Success Criteria** (what must be TRUE):
  1. Secretary can create a charge linked to the correct student or responsible party.
  2. Secretary can register a payment with amount and payment date against an existing charge and see the remaining balance update correctly.
  3. Secretary can view basic financial summaries for total open balance, received amounts, and charge totals.
**Plans**: 3 plans
**UI hint**: yes

### Phase 5: Unified Daily Dashboard
**Goal**: Secretary can review the school's daily operational and financial status from one dashboard snapshot.
**Depends on**: Phase 4
**Requirements**: DASH-01, DASH-02
**Success Criteria** (what must be TRUE):
  1. Secretary can view dashboard indicators for students and events that are relevant to daily operations.
  2. Secretary can view dashboard indicators for financial status that are relevant to daily decisions.
  3. After operational or finance records change, the dashboard snapshot reflects the updated indicators without needing spreadsheet reconciliation.
**Plans**: 2 plans
**UI hint**: yes

## Progress

**Execution Order:**
Phases execute in numeric order: 2 → 2.1 → 2.2 → 3 → 3.1 → 4

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Authentication & Protected Access | 4/4 | Complete   | 2026-04-18 |
| 2. Student & Parent Registry Hardening | 0/3 | Not started | - |
| 3. Employee & Event Operations Hardening | 0/3 | Not started | - |
| 4. Finance Tracking Core | 0/3 | Not started | - |
| 5. Unified Daily Dashboard | 0/2 | Not started | - |
