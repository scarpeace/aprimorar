# Epic: E-006 — Payments and Billing
**Goal:** Provide payment tracking, billing visibility, and later online payment processing.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 5

## Scope
- In scope:
  - Payment tracking enhancements and reporting
  - Outstanding balance calculation and history where needed
  - Payment gateway evaluation and integration when MVP core is stable
- Out of scope:
  - Full accounting system and tax invoicing

## Workboard
- Current focus: None
- Blocked: None
- Next up: T-050 payment tracking and monthly reporting outputs

## Stories
### Story: S-050 — Payment tracking
**Status:** TODO
**Links:** T-050 (TODO)
**Intent:** Track event payments and generate finance-relevant reporting.

### Story: S-051 — Payment gateway integration
**Status:** TODO
**Links:** T-051 (TODO)
**Intent:** Enable online payment flow once core MVP operations are stable.

## Tasks
### Task: T-050 — Enhance payment tracking
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Improve payment modeling and reporting surfaces.

**DoD**
- [ ] Payment model supports required reporting outputs
- [ ] Verification updated
- [ ] Local verification done

**Verification**
- Backend: validate calculations with unit tests and sample data
- Frontend: N/A
- Manual: generate report and validate expected totals

### Task: T-051 — Payment gateway integration
**Type:** fullstack
**Status:** TODO
**Depends on:** None

**Description**
- Select a payment provider and implement the end-to-end flow.

**DoD**
- [ ] Provider selected and documented
- [ ] Payment flow and webhook handling implemented
- [ ] Local or sandbox verification done

**Verification**
- Backend: verify webhook signature validation and idempotency
- Frontend: verify payment initiation UX and success/failure handling
- Manual: complete sandbox payment end to end

## Archive (DONE)
- None.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Keep finance scope separate from gateway work to avoid mixing reporting and provider concerns.
  - Security: Treat payment provider credentials, webhooks, and audit trails as first-class requirements.
  - Performance: Reporting queries may need dedicated indexes or summary views once implemented.
