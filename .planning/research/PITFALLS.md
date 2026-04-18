# Domain Pitfalls

**Domain:** Private school management application (secretary/admin-first)
**Researched:** 2026-04-17

## Critical Pitfalls

Mistakes that cause rewrites or major issues.

### Pitfall 1: Adding authentication as a login screen instead of a system boundary
**What goes wrong:** Teams add a login page and protected routes in the SPA, but leave backend endpoints, generated OpenAPI clients, or business operations effectively trusting the frontend. In brownfield apps this often leaves old endpoints half-protected and new ones inconsistent.
**Why it happens:** Authentication is new to the codebase, so the first implementation focuses on "getting into the app" instead of protecting records and sensitive actions end to end.
**Consequences:** Unauthorized access to student/parent/employee records, insecure finance data exposure, brittle security rules spread across controllers, and painful retrofits later.
**Warning signs:** Some endpoints still work anonymously in Postman; authorization is checked only in React route guards; secretaries can hit every action regardless of role intent; logout/login bugs leave stale sessions.
**Prevention:** Introduce auth at the API boundary first; define one server-side access policy for secretary/admin flows; persist and clear `SecurityContext` correctly; enable session fixation protection and explicit logout handling; add endpoint-level security tests before wiring all UI flows.
**Detection:** Anonymous requests to business endpoints return domain data; security behavior differs between browser use and direct API calls; session bugs appear after refresh or browser back.
**Phase should address it:** Authentication foundation phase.

### Pitfall 2: Overwriting school records with no audit trail or recoverability
**What goes wrong:** Student, parent, employee, event, and charge records are edited in place with no revision history, no "who changed what", and no archive/restore path.
**Why it happens:** CRUD completion is prioritized over operational traceability, especially when replacing spreadsheets where history was informal.
**Consequences:** Secretary trust collapses after the first accidental overwrite; finance disputes cannot be resolved; school records become legally and operationally weak.
**Warning signs:** Deletes are hard deletes by default; edits do not capture actor/timestamp/reason; support questions require checking the database manually; teams say "just update the row" for sensitive entities.
**Prevention:** Add created/updated/by fields everywhere that matters; prefer archive/active status over destructive delete for operational entities; add revision history for sensitive domains first (student identity/contact data, charges, payments, overdue state); show recent-change context in admin screens.
**Detection:** Users start keeping side spreadsheets "just in case"; disputes about whether a payment or contact change really happened; accidental deletes require manual DB recovery.
**Phase should address it:** Data integrity hardening phase, started alongside finance tracking.

### Pitfall 3: Modeling finance as a single paid/unpaid flag
**What goes wrong:** Charges are stored with one status field, with no separate due date, competence period, payment date, amount paid, discount, surcharge, reversal, or partial payment support.
**Why it happens:** v1 wants "basic finance", so teams compress accounting reality into a simplistic boolean model.
**Consequences:** Overdue tracking becomes wrong, dashboard totals disagree, partial payments are impossible, and future reporting requires a rewrite.
**Warning signs:** Database design has only `status = PAID|UNPAID`; overdue means `today > dueDate && !paid`; one record tries to represent both obligation and settlement; edits replace historical payment facts.
**Prevention:** Model finance as at least two concepts: charge and payment allocation; keep due date, original amount, outstanding amount, and payment timestamps distinct; support partial settlement and cancellation/void without mutating away history; use `BigDecimal` and explicit currency-safe rules.
**Detection:** Secretaries ask how to record "paid half", "paid late", or "paid with discount" and the system has no answer; overdue totals change unexpectedly after edits.
**Phase should address it:** Finance model phase, before dashboard finance widgets.

### Pitfall 4: Building dashboard indicators before defining metric semantics
**What goes wrong:** The dashboard shows "active students", "events today", "overdue amount", or "pending payments" without a precise definition, and numbers are recomputed differently across screens.
**Why it happens:** Dashboards are treated as a UI summary problem instead of a domain-definition problem.
**Consequences:** Operational visibility becomes noise; staff stop trusting the dashboard; roadmap effort gets wasted on debates about numbers instead of decisions.
**Warning signs:** Same concept has different counts in list page vs dashboard; queries live in multiple controllers; stakeholders ask "why is this number different?" every demo; dashboard cards lack drill-down rules.
**Prevention:** Define each KPI in writing before coding; map each indicator to one canonical query or summary service; pair every card with the filter/query behind it; start with a tiny set of trusted indicators instead of a crowded dashboard; instrument slow queries and cache only when semantics are stable.
**Detection:** Teams add ad hoc fixes for each dashboard discrepancy; UI labels get repeatedly renamed to match confusing data.
**Phase should address it:** Dashboard semantics phase, after finance and record status definitions are stable.

### Pitfall 5: Ignoring secretary workflow continuity across modules
**What goes wrong:** Student, parent, event, and finance features are "complete" in isolation, but routine secretary work still requires jumping between screens and retyping the same information.
**Why it happens:** Brownfield teams extend existing CRUD modules independently because that is easiest technically.
**Consequences:** Spreadsheet replacement fails even if many screens exist; duplicate data increases; users keep parallel manual controls.
**Warning signs:** Creating a student requires separate disconnected steps with no guided flow; secretary cannot immediately see parent contacts, financial status, and upcoming events from one place; important actions require memorizing IDs or searching repeatedly.
**Prevention:** Design around top workflows, not entities: register/update student, confirm responsible contacts, record charge/payment, review overdue students, check today's operations; add cross-links, contextual panels, and prefilled flows before adding more isolated CRUD depth.
**Detection:** Users export lists to spreadsheets to reconcile information manually; demos require many tabs to complete one real task.
**Phase should address it:** Workflow consolidation phase, after auth and core finance entities exist.

### Pitfall 6: Silent record collisions from concurrent edits
**What goes wrong:** Two secretaries edit the same student/contact/charge, and the last save wins with no warning.
**Why it happens:** Existing CRUD paths usually assume one-user-at-a-time behavior; concurrency is invisible until adoption rises.
**Consequences:** Lost updates, incorrect contact data, wrong balances, and user distrust.
**Warning signs:** "It was saved when I opened it" reports; updates unexpectedly revert fields; finance totals change after unrelated edits.
**Prevention:** Add optimistic locking/version fields on mutable business entities; surface friendly conflict messages in Portuguese; avoid large multi-entity edit forms that save everything blindly; record who last changed the entity and when.
**Detection:** Users report that values "changed by themselves"; audit logs show rapid alternating updates on the same row.
**Phase should address it:** Data integrity hardening phase.

## Moderate Pitfalls

### Pitfall 1: Hard-deleting operational records that should be archived
**What goes wrong:** Students, parents, events, or charges disappear from history when they become inactive.
**Warning signs:** Inactive students vanish from finance history; historical dashboards shift after cleanup; foreign-key issues drive manual deletes.
**Prevention:** Prefer status-based archival and effective dates; reserve hard delete for obviously invalid seed/test data only.
**Phase should address it:** Data integrity hardening phase.

### Pitfall 2: Spreadsheet migration without data normalization rules
**What goes wrong:** Existing spreadsheet data is imported with duplicate guardians, inconsistent names, missing due dates, and ambiguous statuses.
**Warning signs:** Same parent appears multiple ways; imported records need manual cleanup before use; lists cannot be filtered reliably.
**Prevention:** Define import mapping rules, required fields, canonical status values, and manual review queues before bulk migration; expect one cleanup pass, not zero.
**Phase should address it:** Migration/pre-launch phase.

### Pitfall 3: Treating overdue tracking as a report instead of an operational queue
**What goes wrong:** Overdue balances exist numerically, but the secretary cannot act on them by owner, age bucket, student, or last contact.
**Warning signs:** Dashboard shows totals but no drill-down list; users still maintain cobrança follow-up in Sheets/WhatsApp notes.
**Prevention:** Build overdue views as actionable lists with filters, aging buckets, and last-action notes later if needed; make the summary card open the exact underlying queue.
**Phase should address it:** Finance operations phase.

## Minor Pitfalls

### Pitfall 1: Date semantics are inconsistent across events and finance
**What goes wrong:** Event dates, due dates, payment dates, and "today" logic are treated with different timezone or end-of-day assumptions.
**Warning signs:** An item is overdue in one view but not another; daily dashboard counts shift unexpectedly after midnight.
**Prevention:** Standardize date/time rules early: local school timezone, explicit overdue cutoff, and separate date-only versus timestamp fields.
**Phase should address it:** Finance model phase.

### Pitfall 2: Dashboard performance is solved with caching before correctness
**What goes wrong:** Teams cache inconsistent metrics instead of fixing query definitions and indexes.
**Warning signs:** Numbers are fast but wrong; manual cache busting becomes part of support work.
**Prevention:** First unify metric semantics and query paths, then add indexes and observability, then selective caching if still needed.
**Phase should address it:** Dashboard semantics phase.

## Phase-Specific Warnings

| Phase Topic | Likely Pitfall | Mitigation |
|-------------|---------------|------------|
| Authentication foundation | Protecting only the SPA and forgetting direct API access | Secure backend endpoints first, add auth integration tests, verify session lifecycle explicitly |
| Finance model | Using one status field for the whole receivables lifecycle | Separate charge, payment, due date, and outstanding balance concepts before building screens |
| Dashboard indicators | Shipping KPIs without canonical definitions | Write KPI definitions, owners, and drill-down queries before implementing cards |
| Workflow consolidation | Improving modules separately while secretaries still need spreadsheets | Map end-to-end secretary tasks and add cross-module navigation/context |
| Data integrity hardening | No audit/versioning on sensitive records | Add audit metadata, archival rules, and optimistic locking on critical entities |
| Migration / rollout | Importing dirty spreadsheet data directly into production flows | Create normalization rules, review queues, and rehearsal imports |

## Sources

- PROJECT context: `/home/scarpellini/Documents/Projetos/aprimorar/.planning/PROJECT.md` (HIGH)
- OWASP Authentication Cheat Sheet — authentication boundaries, generic auth failures, throttling, secure password handling: https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html (HIGH)
- Spring Security 7 reference — authentication architecture and explicit `SecurityContext` persistence/session handling: https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html (HIGH)
- Spring Security 7 reference — authentication persistence, session management, session fixation, concurrent sessions: https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html (HIGH)
- Hibernate ORM User Guide — optimistic locking and Envers/auditing capabilities for mutable records: https://docs.hibernate.org/orm/current/userguide/html_single/Hibernate_User_Guide.html#locking-optimistic and https://docs.hibernate.org/orm/current/userguide/html_single/Hibernate_User_Guide.html#envers-basics (HIGH)
- Spring Boot reference — Actuator/Micrometer metrics for operational visibility and dashboard observability support: https://docs.spring.io/spring-boot/reference/actuator/metrics.html (HIGH)

## Confidence Notes

- **HIGH confidence:** Authentication/session pitfalls, audit/versioning needs, and dashboard observability recommendations are strongly supported by official framework/security docs.
- **MEDIUM confidence:** Secretary workflow and school-finance modeling pitfalls are domain-informed and consistent with brownfield admin-system patterns, but less directly documented by authoritative school-sector standards.
