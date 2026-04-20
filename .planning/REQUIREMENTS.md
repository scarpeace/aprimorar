# Requirements: Aprimorar

**Defined:** 2026-04-17
**Core Value:** The secretary must be able to manage the school day to day from the app without depending on scattered spreadsheets.

## v1 Requirements

Requirements for the initial secretary/administrator-focused release. Existing brownfield modules count only if they satisfy these behaviors reliably enough to replace spreadsheet use.

### Authentication

- [x] **AUTH-01**: Internal user can sign in with email or username and password
- [x] **AUTH-02**: Authenticated user session persists across browser refresh until logout or session expiry
- [x] **AUTH-03**: Authenticated user can log out securely from the app
- [x] **AUTH-04**: Unauthenticated users cannot access protected student, parent, employee, event, finance, or dashboard workflows

### Students

- [x] **STUD-01**: Secretary can create a student record with all required registration data
- [x] **STUD-02**: Secretary can view and update an existing student record
- [x] **STUD-03**: Secretary can archive a student record without losing historical references
- [x] **STUD-04**: Secretary can find students through search, filters, pagination, and archive visibility controls

### Parents

- [x] **PARN-01**: Secretary can create a parent or responsible-party record with billing and contact data
- [x] **PARN-02**: Secretary can view and update an existing parent or responsible-party record
- [x] **PARN-03**: Secretary can archive a parent or responsible-party record without breaking linked records
- [x] **PARN-04**: Secretary can link the correct responsible party to a student and preserve that relationship consistently

### Employees

- [ ] **EMPL-01**: Secretary can create an employee record with operational data needed by the school
- [ ] **EMPL-02**: Secretary can view and update an existing employee record
- [ ] **EMPL-03**: Secretary can archive an employee record without breaking linked operational records
- [ ] **EMPL-04**: Secretary can find employees through search, filters, pagination, and archive visibility controls

### Events

- [ ] **EVNT-01**: Secretary can create an event record for classes, mentorships, or other school activities
- [ ] **EVNT-02**: Secretary can view and update an existing event record
- [ ] **EVNT-03**: Secretary can archive an event record without losing historical visibility
- [ ] **EVNT-04**: Secretary cannot save an event in a state that violates scheduling or ownership business rules
- [ ] **EVNT-05**: Secretary can view upcoming events in operational screens

### Finance

- [ ] **FIN-01**: Secretary can create a charge record linked to the correct student or responsible party
- [ ] **FIN-02**: Secretary can register a payment with amount and payment date against an existing charge
- [ ] **FIN-03**: Secretary can view basic financial summaries for total open balance, received amounts, and charge totals

### Dashboard

- [ ] **DASH-01**: Secretary can view a dashboard snapshot with student and event indicators relevant to daily operations
- [ ] **DASH-02**: Secretary can view a dashboard snapshot with financial indicators relevant to daily decisions

## v2 Requirements

Deferred to later releases. Acknowledged now but not part of the current roadmap.

### Access Expansion

- **ACCS-01**: Teacher users can access teacher-specific workflows
- **ACCS-02**: Parent users can access parent-facing workflows
- **ACCS-03**: Student users can access student-facing workflows
- **ACCS-04**: Additional employee roles can access role-specific workflows

### Reporting and Analytics

- **REPT-01**: Administrator can view advanced reports beyond daily operational summaries
- **REPT-02**: Administrator can analyze richer financial and school-performance indicators over time

### Finance Expansion

- **FINX-01**: System integrates with external payment gateways
- **FINX-02**: System supports richer billing policies such as installment templates, reversals, and more advanced finance operations

## Out of Scope

Explicitly excluded from v1 to prevent scope creep.

| Feature | Reason |
|---------|--------|
| Teacher portal | Deferred until the secretary workflow is stable and replacing spreadsheets successfully |
| Parent portal | Deferred until internal operations are reliable enough to expose external access |
| Student portal | Deferred until internal operations are reliable enough to expose external access |
| Complex multi-role permission matrix | Simple internal authentication is sufficient for the initial release |
| Payment gateway integration | v1 only needs internal charge and payment tracking, not external payment processing |
| Advanced reports and analytics | v1 only needs enough dashboard visibility for daily decisions |
| Notifications and messaging | Not necessary for the first spreadsheet-replacement release |

## Traceability

Which phases cover which requirements. Updated during roadmap creation.

| Requirement | Phase | Status |
|-------------|-------|--------|
| AUTH-01 | Phase 1 | Complete |
| AUTH-02 | Phase 1 | Complete |
| AUTH-03 | Phase 1 | Complete |
| AUTH-04 | Phase 1 | Complete |
| STUD-01 | Phase 2 | Complete |
| STUD-02 | Phase 2 | Complete |
| STUD-03 | Phase 2 | Complete |
| STUD-04 | Phase 2 | Complete |
| PARN-01 | Phase 2 | Complete |
| PARN-02 | Phase 2 | Complete |
| PARN-03 | Phase 2 | Complete |
| PARN-04 | Phase 2 | Complete |
| EMPL-01 | Phase 3 | Pending |
| EMPL-02 | Phase 3 | Pending |
| EMPL-03 | Phase 3 | Pending |
| EMPL-04 | Phase 3 | Pending |
| EVNT-01 | Phase 3 | Pending |
| EVNT-02 | Phase 3 | Pending |
| EVNT-03 | Phase 3 | Pending |
| EVNT-04 | Phase 3 | Pending |
| EVNT-05 | Phase 3 | Pending |
| FIN-01 | Phase 4 | Pending |
| FIN-02 | Phase 4 | Pending |
| FIN-03 | Phase 4 | Pending |
| DASH-01 | Phase 5 | Pending |
| DASH-02 | Phase 5 | Pending |

**Coverage:**
- v1 requirements: 26 total
- Mapped to phases: 26
- Unmapped: 0 ✓

---
*Requirements defined: 2026-04-17*
*Last updated: 2026-04-17 after roadmap creation*
