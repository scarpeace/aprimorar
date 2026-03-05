# Epic: E-006 — Payments and Billing
**Goal:** Provide payment tracking, billing visibility, and (later) online payment processing.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 5

## Scope
- In scope:
  - Payment tracking enhancements and reporting
  - Outstanding balance calculation and history (as needed)
  - Payment gateway evaluation and integration (post-MVP if needed)
- Out of scope:
  - Full accounting system and tax invoicing

## Workboard
- Current focus: None started
- Blocked: None
- Next up: T-050 payment tracking and monthly reporting outputs

## Stories
### Story: S-050 — Payment Tracking
**Status:** TODO
**Links:** T-050 (TODO)
**Intent:** Track event payments and generate finance-relevant reports.

### Story: S-051 — Payment Gateway Integration
**Status:** TODO
**Links:** T-051 (TODO)
**Intent:** Enable online payment flow when MVP core is stable.

## Tasks
### Task: T-050 — Enhance payment tracking
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Improve payment modeling and add reporting surfaces.

**Subtasks**
- [ ] ST-150 — Enhance payment fields
- [ ] ST-151 — Calculate outstanding balance
- [ ] ST-152 — Generate payment reports
- [ ] ST-153 — Add payment history

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: validate calculations with unit tests and sample data
- Frontend: N/A
- Manual: generate report and validate expected totals

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 5 / User Story 5.1

### Task: T-051 — Payment gateway integration
**Type:** fullstack
**Status:** TODO
**Depends on:** None

**Description**
- Select a payment gateway and implement end-to-end payment flow.

**Subtasks**
- [ ] ST-154 — Evaluate payment gateway options
- [ ] ST-155 — Implement payment processing
- [ ] ST-156 — Handle payment webhooks
- [ ] ST-157 — Implement refund process
- [ ] ST-158 — Generate invoices/receipts

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: verify webhook signature validation + idempotency
- Frontend: verify payment initiation UX and success/failure handling
- Manual: complete sandbox payment end-to-end

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 5 / User Story 5.2

## Archive (DONE)
- No DONE tasks archived yet.

## Review Notes (append-only)
- Reviewer notes:
