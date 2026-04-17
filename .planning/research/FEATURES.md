# Feature Landscape

**Domain:** Private school management application focused on secretary/administrator workflows
**Researched:** 2026-04-17

## Table Stakes

Features users expect. Missing = product feels incomplete.

| Feature | Why Expected | Complexity | Notes |
|---------|--------------|------------|-------|
| Student registry with linked guardian record | Every mainstream SIS centers operations around a canonical student profile tied to family/contact data. | Medium | Brownfield fit: extend existing student + parent domains, do not redesign around full academic SIS yet. |
| Guardian/parent contact management | Secretaries need reliable billing/contact ownership, not just student data. | Low | Must support one responsible payer/contact per student at minimum; household-level modeling can come later. |
| Employee directory for operational ownership | Commercial systems routinely include staff accounts/rosters because events, classes, and responsibilities need an owner. | Low | Existing employee domain already supports this direction. |
| School records completeness tracking | Products consistently bundle documents, medical/basic profile data, and record completeness in the student record. | Medium | For this repo, prioritize simple “record completeness” metadata and attachments/notes over a full document workflow engine. |
| Calendar and event management | School operations products almost always expose calendars, class/event schedules, and event-linked records. | Medium | Existing events domain makes this table-stakes for v1. |
| Search, filters, and paginated admin lists | Secretaries live in lists. Without fast lookup/filtering, spreadsheets remain faster. | Low | Treat this as part of every CRUD area, not as a separate “reporting” project. |
| Basic receivables tracking (charges, payments, balances) | Tuition/fee visibility is a standard module across school management products. | Medium | Must cover charge creation, payment registration, open balance, overdue status, and simple monthly totals. |
| Dashboard snapshot for daily operations | Modern SIS products consistently offer summary dashboards for attendance/operations/finance visibility. | Medium | Brownfield fit: keep it narrow—active students, scheduled classes/events, revenue, cost, overdue totals, upcoming items. |
| Simple authentication for secretary/admin | Even small systems assume protected operational access. | Medium | Keep role model minimal in v1; avoid portal explosion. |
| Basic export/print-ready operational summaries | Secretaries often need to hand off lists or reconcile with offline processes. | Low | Prefer CSV/PDF-friendly summaries for core lists before advanced BI/report builders. |

## Differentiators

Features that set product apart. Not expected, but valued.

| Feature | Value Proposition | Complexity | Notes |
|---------|-------------------|------------|-------|
| Action-oriented dashboard with exceptions, not just KPIs | Better than a generic dashboard: highlight overdue balances, missing records, upcoming events, and items needing secretary action today. | Medium | Strong fit for this repo because dashboard already exists; expand from passive summary to action queue. |
| Secretary-first financial workflow | Most systems have billing, but many are finance-heavy. A simpler workflow optimized for “register charge -> receive payment -> see who is overdue” is easier to adopt. | Medium | This is the best product differentiator for a spreadsheet-replacement v1. |
| School record timeline per student | A single timeline of enrollment, guardian changes, payments, events, and notes reduces context switching. | High | Useful, but should follow core CRUD + finance foundation. |
| Spreadsheet import and bulk cleanup tools | Brownfield schools often migrate from Google Sheets. Import tools materially reduce adoption friction. | Medium | High leverage after core entities stabilize. |
| Record completeness / missing-data alerts | Secretaries gain value when the system surfaces incomplete contacts, missing documents, or unpaid items automatically. | Medium | Pairs naturally with dashboard and student profile enhancements. |
| Reusable charge templates/installment presets | Useful for recurring tuition or activity fees without becoming full accounting software. | Medium | Good phase-2 finance enhancement after manual charge/payment tracking works. |

## Anti-Features

Features to explicitly NOT build.

| Anti-Feature | Why Avoid | What to Do Instead |
|--------------|-----------|-------------------|
| Parent, student, or teacher self-service portals in v1 | Valuable in the market, but expands auth, permissions, UX surfaces, and support burden well beyond the secretary workflow. | Keep one internal secretary/admin experience first. |
| Full LMS / gradebook / assignment / attendance suite | Common in full SIS products, but not aligned with this repo’s immediate spreadsheet-replacement goal. | Keep roadmap centered on administrative records, events, dashboard, and finance. |
| Payment gateway integration | Adds compliance, reconciliation edge cases, and gateway-specific failure flows before internal finance basics are proven. | Start with manual/internal payment registration and overdue tracking. |
| Full accounting / double-entry ERP | Overkill for the stated “basic financial tracking” requirement. | Model charges, receipts, balances, and simple summaries only. |
| Admissions CRM / marketing automation | Real products offer it, but it is net-new workflow surface and not needed to replace current operational spreadsheets. | Capture enrolled students cleanly; defer lead pipeline and applicant portals. |
| Complex RBAC / department-specific permissions | Not needed for initial private-school secretary/admin usage and likely to slow every feature. | Implement simple authenticated access with a small admin model. |
| Broad module sprawl (transport, library, canteen, hostel, procurement) | These are normal in enterprise suites but would dilute delivery in this brownfield monorepo. | Add only if a future milestone proves clear operational pain. |

## Feature Dependencies

```text
Authentication → Protected secretary/admin workflows

Parent/guardian registry → Student registry
Student registry → Event assignment
Employee registry → Event assignment
Student registry + Parent/guardian registry → Basic receivables tracking

Charges/payments/balances → Financial dashboard metrics
Student/event data → Operational dashboard metrics

Core CRUD stability → Bulk import tools
Core CRUD stability + Finance data → Record completeness / exception alerts
Core entity history + Finance/events → Student timeline
```

## MVP Recommendation

Prioritize:
1. **Complete student, parent, employee, and event CRUD with dependable search/filtering**
2. **Add simple authentication for secretary/administrator access**
3. **Add basic finance tracking: charges, payments, overdue balances, and dashboard summaries**

Defer: **student timeline and spreadsheet import tools**: high value, but they depend on core entity shape and finance rules stabilizing first.

## Opinionated Recommendation

For this repo, table stakes are narrower than the full school-software market. Commercial platforms bundle academics, portals, LMS, transport, and accounting because they sell suites. Aprimorar should not copy that scope. The right v1 is a **secretary operations core**: canonical student/guardian/staff records, event/calendar handling, basic receivables, and a dashboard that surfaces what needs attention today.

The most important distinction is this:

- **Table stakes for Aprimorar v1** = replacing spreadsheet-based operational control.
- **Differentiation** = making secretary work faster through alerts, linked records, and simple finance visibility.

If a feature does not help the secretary run the school day-to-day without Google Sheets, it should probably wait.

## Sources

- Project scope and brownfield constraints: `/home/scarpellini/Documents/Projetos/aprimorar/.planning/PROJECT.md` — HIGH confidence
- Gradelink features page (student info, attendance, reports, parent/student access, tuition & billing, scheduling): https://gradelink.com/features/ — MEDIUM confidence, official vendor source, page shows dateModified 2026-03-20
- Classter K12 platform overview (unified admin, portals, admissions, billing, dashboard/integrations): https://www.classter.com/solutions/k12-schools/ — MEDIUM confidence, official vendor source
- Classter Admissions module (applicant management, documents, registration, re-registration, online enrollment, fee management): https://www.classter.com/product/modules/admissions/ — MEDIUM confidence, official vendor source
- Classter Billing & Payments module (installments, reporting, payers, automated mass billing, online payments, import from spreadsheet): https://www.classter.com/product/modules/billing-and-payments/ — MEDIUM confidence, official vendor source
- OpenEduCat features catalog (student management, finance, dashboard, document management, events, parent portal, workflow tools): https://www.openeducat.org/features — MEDIUM confidence, official vendor source, current copyright 2026
