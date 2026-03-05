# Roadmap

## Development Roadmap

### Phase 0 - Planning Structure Migration
- Goal: migrate legacy docs into `docs/` planning structure and stabilize IDs
- Status: DONE
- Epics:
  - E-001 - Planning docs migration

### Phase 1 - Foundation (Weeks 1-4)
- Goal: complete core CRUD + event types + performance + basic reporting APIs
- Status: IN_PROGRESS
- Epics:
  - E-002 - Core data management
  - Notes: includes Event `content` required field rollout (T-012-A, T-012-B, T-012-C) and student domain simplification (remove Student `activity`, relax age-range validation to only prevent future birthdates)

### Phase 2 - Security (Weeks 5-8)
- Goal: authentication, RBAC, and basic hardening
- Status: TODO
- Epics:
  - E-003 - Authentication and authorization

### Phase 3 - Frontend (Weeks 9-14)
- Goal: management UI for all core entities and dashboard
- Status: TODO
- Epics:
  - E-004 - Frontend development
  - Notes: includes UX polish like UTF-8/accents normalization for validation and error messages

### Phase 4 - Integrations (Weeks 15-18)
- Goal: calendar integration and calendar UI views; payment tracking enhancements
- Status: TODO
- Epics:
  - E-005 - Calendar and scheduling

### Phase 5 - Payments (Weeks 19-22)
- Goal: gateway integration, invoices, and financial reports
- Status: TODO
- Epics:
  - E-006 - Payments and billing

## Epic Index

| Epic ID | Name | Status | File |
|---|---|---|---|
| E-001 | Planning docs migration | DONE | docs/planning/epics/E-001-planning-structure-migration.md |
| E-002 | Core data management | IN_PROGRESS | docs/planning/epics/E-002-core-data-management.md |
| E-003 | Authentication and authorization | TODO | docs/planning/epics/E-003-authentication-and-authorization.md |
| E-004 | Frontend development | TODO | docs/planning/epics/E-004-frontend-development.md |
| E-005 | Calendar and scheduling | TODO | docs/planning/epics/E-005-calendar-and-scheduling.md |
| E-006 | Payments and billing | TODO | docs/planning/epics/E-006-payments-and-billing.md |
