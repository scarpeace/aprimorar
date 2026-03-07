# Epic: E-006 - Reporting, Payments, and Billing Expansion

**Goal:** Strengthen finance reporting and payment tracking before any online payment integration.
**Status:** TODO
**Phase:** Phase 5

## Scope

- In scope:
  - monthly finance reporting improvements
  - payment tracking and history
  - outstanding balance follow-ups if needed
  - later payment gateway evaluation
- Out of scope:
  - full accounting or tax systems

## Workboard

- Current focus: none
- Blocked: reporting foundation should follow E-002 dashboard/reporting contract work
- Next up: monthly reporting endpoint and finance views before gateway evaluation

## Stories

### Story: S-050 - Monthly Finance Reporting

**Status:** TODO
**Intent:** Give staff reliable monthly numbers for students, employees, revenue, cost, and profit.
**Links:** T-050, T-051, T-052

**Acceptance Criteria**

- Monthly report outputs match MVP finance rules.
- Staff can inspect totals by student and employee.

### Story: S-051 - Payment Tracking Improvements

**Status:** TODO
**Intent:** Make payment state easier to understand beyond raw event fields.
**Links:** T-053, T-054

**Acceptance Criteria**

- Payment-related history or balance behavior is explicit.
- Changes do not break current event finance data.

### Story: S-052 - Online Payment Evaluation

**Status:** TODO
**Intent:** Evaluate online payments only after reporting and tracking are stable.
**Links:** T-055

**Acceptance Criteria**

- Gateway work has a documented reason, candidate, and rollout plan before implementation begins.

## Tasks

### Task: T-050 - Add monthly finance reporting endpoint

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Add an endpoint or service contract for monthly totals by student, employee, and overall KPI totals.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Compare report output to sample month data

### Task: T-051 - Add finance reporting tests and edge-case coverage

**Type:** backend
**Status:** TODO
**Depends on:** T-050

**Outcome**

- Cover null values, empty months, and mixed payment completeness.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Review expected totals for at least one empty and one populated month

### Task: T-052 - Add a simple finance report UI

**Type:** frontend
**Status:** TODO
**Depends on:** T-050

**Outcome**

- Add a beginner-friendly screen for monthly finance totals without overdesigning the reporting UI.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Load the report page and verify displayed totals against the API

### Task: T-053 - Decide whether outstanding balance belongs in MVP finance flows

**Type:** docs
**Status:** TODO
**Depends on:** T-050

**Outcome**

- Make a clear product decision on outstanding balance tracking before adding more finance fields.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm the decision is reflected in canonical docs and epic scope

### Task: T-054 - Add payment history only if the MVP reporting flow needs it

**Type:** backend
**Status:** TODO
**Depends on:** T-053

**Outcome**

- Add payment history only if it solves a confirmed reporting or operations problem.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Validate the chosen history behavior with a real workflow example

### Task: T-055 - Evaluate payment gateway options and rollout timing

**Type:** docs
**Status:** TODO
**Depends on:** T-052

**Outcome**

- Document whether online payment processing is still worth doing after MVP operations/reporting goals are met.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm gateway work stays explicitly post-MVP unless priorities change

## Archive

- No completed reporting or billing expansion tasks yet.

## Review Notes

- 2026-03-07: Separated monthly reporting and payment tracking from gateway integration so finance basics can land first.
