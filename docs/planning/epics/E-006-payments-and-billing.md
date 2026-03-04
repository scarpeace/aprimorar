# Epic: E-006 — Payments and Billing
**Goal:** Provide payment tracking, billing visibility, and (later) online payment processing.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 5

## Scope
- In scope:
  - Payment tracking enhancements and reporting
  - Outstanding balance calculation and history (as needed)
  - Payment gateway evaluation and integration (post-MVP if required)
- Out of scope:
  - Full accounting system and tax invoicing

## Stories

### Story: S-050 — Payment Tracking
**As a** administrator **I want** to track payments for events **so that** I can manage billing.

**Acceptance Criteria**
- [ ] Payment fields support required tracking needs
- [ ] Outstanding balance can be derived
- [ ] Payment reports can be generated
- [ ] Payment history is available (if required)

**Test Plan**
- Backend:
  - [ ] Add service tests for balance calculations
- Frontend:
  - [ ] N/A
- Manual:
  - [ ] Validate report outputs against known fixtures/data sets

### Story: S-051 — Payment Gateway Integration
**As a** student/parent **I want** to pay online **so that** I don't need to pay in person.

**Acceptance Criteria**
- [ ] Gateway option selected and documented
- [ ] Webhooks are handled safely and idempotently
- [ ] Receipts/invoices are generated when payment succeeds

**Test Plan**
- Backend:
  - [ ] Add webhook signature verification tests
- Frontend:
  - [ ] N/A
- Manual:
  - [ ] Test payment flow against sandbox environment

## Tasks

### Task: T-050 — Enhance payment tracking
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Improve payment modeling and add reporting surfaces.

**Subtasks**
- [ ] ST-150 — Enhance payment fields (legacy: T-5.1.1)
- [ ] ST-151 — Calculate outstanding balance (legacy: T-5.1.2)
- [ ] ST-152 — Generate payment reports (legacy: T-5.1.3)
- [ ] ST-153 — Add payment history (legacy: T-5.1.4)

**Files likely affected (best guess)**
- server/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Validate calculations with unit tests and sample data
- Frontend: N/A
- Manual: Generate a report and validate expected totals

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 5 / User Story 5.1

### Task: T-051 — Payment gateway integration
**Type:** fullstack
**Status:** TODO
**Depends on:** None

**Description**
- Select a payment gateway and implement the end-to-end payment flow.

**Subtasks**
- [ ] ST-154 — Evaluate payment gateway options (legacy: T-5.2.1)
- [ ] ST-155 — Implement payment processing (legacy: T-5.2.2)
- [ ] ST-156 — Handle payment webhooks (legacy: T-5.2.3)
- [ ] ST-157 — Implement refund process (legacy: T-5.2.4)
- [ ] ST-158 — Generate invoices/receipts (legacy: T-5.2.5)

**Files likely affected (best guess)**
- server/
- client/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Verify webhook signature validation + idempotency
- Frontend: Verify payment initiation UX and success/failure handling
- Manual: Complete sandbox payment end-to-end

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 5 / User Story 5.2

**Review Notes (append-only)**
- Reviewer notes:
