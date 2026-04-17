# Project Research Summary

**Project:** Aprimorar
**Domain:** Private school management application for secretary/administrator workflows
**Researched:** 2026-04-17
**Confidence:** HIGH

## Executive Summary

Aprimorar should be extended as a **brownfield secretary-operations system**, not reinvented as a full school ERP. The research is consistent across stack, features, architecture, and pitfalls: keep the existing React SPA + Spring Boot modular monolith, add simple authenticated access for internal staff, formalize school registry workflows, and introduce a lightweight receivables module for charges, payments, overdue tracking, and dashboard summaries. The winning product shape is narrower than a typical SIS suite: it should replace spreadsheet-based operational control, not attempt portals, LMS features, or full accounting.

The recommended approach is to sequence work around dependencies: establish backend-first authentication with session cookies and CSRF protection, wrap the existing SPA in an auth shell, tighten authorization on current modules, then add a dedicated finance module and only afterward expand dashboard summaries. This preserves the repo's current strengths—OpenAPI + Kubb contract flow, TanStack Query, modular domain organization—and avoids the biggest brownfield mistake: bolting new capabilities into dashboard or event code without giving them proper boundaries.

The key risks are also clear. First, auth can fail if it is treated as only a login screen rather than an API boundary. Second, finance can become a rewrite trap if modeled as a paid/unpaid flag instead of charge + payment history. Third, the dashboard can lose trust if KPI semantics are undefined or duplicated across screens. Mitigation is straightforward: enforce security server-side first, model finance as receivables with exact money types and auditability, and define a small set of canonical metrics before dashboard work.

## Key Findings

### Recommended Stack

The stack recommendation is deliberately conservative: keep the current monorepo architecture and add only the minimum missing platform pieces. Spring Boot 3.5, Java 21, React 19, Vite 7, React Router 7, PostgreSQL, springdoc, Kubb, Axios, TanStack Query, React Hook Form, Zod, Recharts, and Sonner are already a strong fit. The only major net-new platform capability for v1 is Spring Security with session-backed authentication.

Version sensitivity is low except in two places: stay on the Spring Boot 3.5 line for Spring Security compatibility, and preserve the existing OpenAPI → Kubb contract workflow because it is central to safe cross-stack delivery. For finance, exact monetary handling is non-negotiable: Java `BigDecimal` plus PostgreSQL `numeric`, never floating-point types.

**Core technologies:**
- **Spring Boot 3.5.x + Java 21:** backend platform — already adopted, stable, and the right base for security, JPA, validation, and OpenAPI.
- **Spring Security 6.5.x line:** authentication and authorization — enables simple in-app session auth without external identity complexity.
- **React 19 + Vite 7:** frontend SPA — already in place and sufficient for an internal dashboard-style product.
- **React Router 7:** routing — keep declarative routing and add protected app routes rather than re-architecting navigation.
- **PostgreSQL 16+ + Flyway:** persistence and schema evolution — best fit for relational school/finance workflows and required for auth/finance changes.
- **springdoc + Kubb:** contract-first integration — prevents backend/frontend drift and must remain the delivery path for new APIs.
- **TanStack Query + Axios:** data fetching — keep generated hooks and configure credentialed requests for session auth.
- **React Hook Form + Zod:** forms and validation — best fit for login, finance forms, and CRUD validation.

### Expected Features

The product should focus on **secretary operations core** rather than suite sprawl. Table stakes for this repo are student, guardian, employee, and event management with strong search/filter/list ergonomics, plus simple internal authentication, a daily-operations dashboard, and basic receivables tracking. Differentiation should come from workflow fit: exception-oriented dashboards, secretary-first finance flows, and record-completeness alerts—not from trying to outgrow the scope into portals, admissions CRM, or accounting.

The clearest v1 boundary is: if a feature does not materially reduce spreadsheet usage for day-to-day school administration, it should wait.

**Must have (table stakes):**
- Student registry linked to guardian/responsável records.
- Guardian/parent contact management with clear billing/contact ownership.
- Employee directory for operational ownership.
- Event/calendar management.
- Search, filters, and paginated admin lists across modules.
- Simple authentication for secretary/admin users.
- Basic receivables tracking: charges, payments, balances, overdue status.
- Narrow dashboard snapshot with operational and finance visibility.
- Basic export/print-ready summaries for core lists.

**Should have (competitive):**
- Action-oriented dashboard with overdue, missing-record, and upcoming-event exceptions.
- Secretary-first finance workflow optimized for charge → payment → overdue follow-up.
- Record completeness / missing-data alerts.
- Reusable charge templates or installment presets after core finance stabilizes.

**Defer (v2+):**
- Student/parent/teacher self-service portals.
- LMS/gradebook/attendance suite expansion.
- Payment gateway integration.
- Full accounting/ERP behavior.
- Spreadsheet import tools until core entities and statuses stabilize.
- Student timeline until cross-module data is stable enough to make it useful.

### Architecture Approach

Architecture research strongly supports extending the existing **modular monolith**. The recommended module set is Identity & Access, School Registry, Operations, Finance, Dashboard/Reporting, and Shared Platform. The main pattern is strict ownership: identity owns login/session/roles, finance owns charges/payments/balances, events own scheduling, registry modules own master data, and dashboard only aggregates read models. That separation is what keeps the roadmap sane.

**Major components:**
1. **Identity & Access** — login, logout, current-user bootstrap, roles, session lifecycle, security config.
2. **School Registry** — student, parent, and employee master data plus links across those records.
3. **Operations** — event/scheduling workflows tied to students and employees.
4. **Finance** — receivables, payments, overdue rules, summaries, and ledger-style endpoints.
5. **Dashboard/Reporting** — KPI and exception read models only, never transaction ownership.
6. **Shared Platform** — error shaping, pagination, CORS/CSRF/security config, clock, auditing support.

### Critical Pitfalls

The biggest risks are structural, not cosmetic. Teams will get into trouble if they add auth only in the UI, if they flatten finance into a single status field, or if they ship dashboard cards without fixed business definitions. The pitfalls research also adds two important operational warnings: school records need auditability/archival early, and the product must optimize end-to-end secretary workflows rather than isolated CRUD screens.

1. **Frontend-only authentication** — avoid by securing backend endpoints first, adding session/CSRF handling, and testing anonymous/direct API access explicitly.
2. **No audit trail or recoverability** — avoid by adding created/updated/by metadata, archive over delete, and revision support for sensitive records.
3. **Finance modeled as paid/unpaid** — avoid by separating charge from payment, preserving due date and settlement history, and supporting partial payments.
4. **Dashboard semantics defined after implementation** — avoid by writing KPI definitions and drill-down rules before coding cards.
5. **Workflow fragmentation across modules** — avoid by designing around secretary tasks such as register student, confirm responsible contact, post payment, review overdue queue, and check today's operations.

## Implications for Roadmap

Based on research, suggested phase structure:

### Phase 1: Identity Foundation
**Rationale:** Authentication is the only net-new cross-cutting platform capability and every protected workflow depends on it.
**Delivers:** Spring Security setup, user accounts, password hashing, login/logout/me endpoints, CSRF integration, CORS updates for credentials, baseline roles (`ADMIN`, `SECRETARIA`).
**Addresses:** Simple authentication for secretary/admin users.
**Avoids:** Treating auth as only a login screen; leaving old endpoints publicly callable.

### Phase 2: Protected App Shell and Authorization Retrofit
**Rationale:** Once backend auth exists, the SPA needs a clean protected shell before domain work expands.
**Delivers:** Login UI, session bootstrap query, protected routes/layout, logout flow, nav visibility from `me`, clean `401/403` handling, protection on existing student/parent/employee/event APIs.
**Uses:** React Router, TanStack Query, Axios with credentials, Kubb-generated contracts.
**Implements:** Identity module + frontend auth shell.

### Phase 3: Registry and Workflow Baseline Hardening
**Rationale:** Finance and dashboard work depend on stable student/guardian/employee/event relationships and usable admin lists.
**Delivers:** Dependable CRUD completeness, search/filter/pagination consistency, guardian linkage cleanup, event ownership integrity, archive-over-delete rules, initial audit metadata on sensitive entities.
**Addresses:** Student registry, guardian management, employee directory, event/calendar management, search/filtering.
**Avoids:** Workflow fragmentation, destructive deletes, weak traceability.

### Phase 4: Finance Core
**Rationale:** Finance is the highest domain-risk area and must be modeled correctly before any finance dashboard is trusted.
**Delivers:** Dedicated finance module, charge/payment schema, overdue and balance rules, payment registration, receivables lists, basic summaries, exact money handling, partial payment support.
**Addresses:** Basic receivables tracking and secretary-first finance workflow.
**Avoids:** Paid/unpaid oversimplification, loss of history, date/money inconsistencies.

### Phase 5: Dashboard and Exception Views
**Rationale:** The dashboard should consume stable modules, not invent unfinished business logic.
**Delivers:** Canonical KPI definitions, top-level summary endpoints, overdue/action queues, upcoming-events views, recent payments, record-completeness and exception-oriented cards.
**Addresses:** Dashboard snapshot, action-oriented dashboard, missing-data alerts.
**Avoids:** KPI trust problems, duplicate metric logic, premature caching.

### Phase 6: Operational Refinement and Pre-Launch Readiness
**Rationale:** Once core workflows are stable, the next value comes from adoption aids and data-quality hardening.
**Delivers:** Export/print-ready summaries, optional charge templates/installment presets, optimistic locking, deeper audit/revision support, migration rules for legacy spreadsheet cleanup, workflow polish across modules.
**Addresses:** Export needs, charge templates, data integrity hardening, rollout readiness.
**Avoids:** Silent record collisions, dirty-data imports, secretary fallback to spreadsheets.

### Phase Ordering Rationale

- Authentication comes first because every other phase assumes a protected internal system.
- UI auth shell comes before major domain changes because it wraps existing modules with minimal brownfield disruption.
- Registry hardening precedes finance because finance references students/guardians and depends on reliable ownership data.
- Finance precedes dashboard because dashboard KPIs must consume stable finance semantics, not prototype them.
- Workflow polish and migration support come last because they are highest leverage only after core entities and rules settle.

### Research Flags

Phases likely needing deeper research during planning:
- **Phase 4 (Finance Core):** receivables semantics, aging rules, discounts/cancellations, and rollout edge cases deserve deeper phase-specific validation.
- **Phase 5 (Dashboard and Exception Views):** KPI definitions, drill-down semantics, and query/index strategy need tighter planning before implementation.
- **Phase 6 (Operational Refinement and Pre-Launch Readiness):** spreadsheet migration/normalization rules and audit-history UX need context-specific decisions.

Phases with standard patterns (skip research-phase):
- **Phase 1 (Identity Foundation):** well-documented Spring Security session, CSRF, password, and role patterns.
- **Phase 2 (Protected App Shell and Authorization Retrofit):** standard SPA bootstrap, route protection, and query invalidation patterns.
- **Phase 3 (Registry and Workflow Baseline Hardening):** mostly brownfield product decisions rather than unknown technical research.

## Confidence Assessment

| Area | Confidence | Notes |
|------|------------|-------|
| Stack | HIGH | Strongly grounded in repo inspection plus official Spring Security, PostgreSQL, React Router, TanStack Query, and Kubb guidance. |
| Features | MEDIUM | Feature conclusions are coherent and useful, but market comparison leaned on vendor pages rather than neutral standards. |
| Architecture | HIGH | Strong fit with the existing repo shape and supported by established monolith/session-auth patterns. |
| Pitfalls | HIGH | Security, auditability, optimistic locking, and dashboard concerns are well supported by official framework/security docs. |

**Overall confidence:** HIGH

### Gaps to Address

- **Role scope details:** research supports `ADMIN` and `SECRETARIA`, but exact screen/action permissions still need explicit product decisions during planning.
- **Finance policy specifics:** discount, cancellation, reversal, and partial allocation rules should be validated with real operator workflows before locking API contracts.
- **Dashboard KPI definitions:** counts such as “active students,” “overdue,” and “received this month” need written business definitions before implementation.
- **Migration strategy:** if spreadsheet import becomes part of rollout, normalization rules and review queues must be designed before any bulk import work.
- **Audit depth:** the need for auditability is clear, but whether simple metadata is enough or entity revision history is required should be decided per entity during planning.

## Sources

### Primary (HIGH confidence)
- Repository inspection (`package.json`, `client/package.json`, `client/kubb.config.ts`, backend packages/config, `pom.xml`, CORS config) — existing stack, architecture shape, and integration workflow.
- Spring Security docs — password auth, password storage, session management, CSRF, CORS integration.
- PostgreSQL docs — exact numeric types for money handling.
- Hibernate docs — optimistic locking and auditing capabilities.
- TanStack Query docs — query invalidation and server-state patterns.

### Secondary (MEDIUM confidence)
- React Router documentation — protected-route evolution and routing patterns relevant to future refinement.
- Vendor feature pages (Gradelink, Classter, OpenEduCat) — market expectations for table stakes and differentiators.

### Tertiary (LOW confidence)
- None material; low-confidence claims were avoided in the roadmap recommendations.

---
*Research completed: 2026-04-17*
*Ready for roadmap: yes*
