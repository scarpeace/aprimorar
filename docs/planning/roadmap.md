# Roadmap

Canonical context lives in `AGENTS.md`.
Use this file for phase order and epic indexing only.

## Planning Operations

- Sprint automation quickstart: `docs/planning/sprint-automation-quickstart.md`
- Sprint automation full guide: `docs/planning/sprint-automation-guide.md`
- Sprint template: `docs/planning/sprint-template.md`
- Scrum MCP prompt: `.opencode/prompts/scrum-agent-mcp.md`

## Development Roadmap

### Phase 0 - Planning Structure Migration
- Goal: migrate legacy docs into `docs/` planning structure and stabilize IDs
- Status: DONE
- Epics:
  - E-001 - Planning docs migration

### Phase 1 - Foundation
- Goal: complete core CRUD + event types + performance + basic reporting APIs
- Status: IN_PROGRESS
- Epics:
  - E-002 - Core data management
  - Notes: core CRUD, event `content`, and student archive/domain cleanup

### Phase 2 - Security
- Goal: authentication, RBAC, and basic hardening
- Status: TODO
- Epics:
  - E-003 - Authentication and authorization

### Phase 3 - Frontend
- Goal: management UI for all core entities and dashboard
- Status: IN_PROGRESS
- Epics:
  - E-004 - Frontend development
  - Notes: management UI and UX polish

### Phase 4 - Integrations
- Goal: calendar integration and calendar UI views; payment tracking enhancements
- Status: TODO
- Epics:
  - E-005 - Calendar and scheduling

### Phase 5 - Payments
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
| E-004 | Frontend development | IN_PROGRESS | docs/planning/epics/E-004-frontend-development.md |
| E-005 | Calendar and scheduling | TODO | docs/planning/epics/E-005-calendar-and-scheduling.md |
| E-006 | Payments and billing | TODO | docs/planning/epics/E-006-payments-and-billing.md |
