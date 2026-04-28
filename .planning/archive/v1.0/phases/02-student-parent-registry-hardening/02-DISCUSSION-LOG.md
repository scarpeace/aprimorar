# Phase 2: Student & Parent Registry Hardening - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-19
**Phase:** 02-student-parent-registry-hardening
**Areas discussed:** registration flow, student-responsible relationship, link management, terminology, screen presentation

---

## Registration Flow

| Option | Description | Selected |
|--------|-------------|----------|
| Single flow | Keep one student creation flow and defer a separate enrollment lifecycle | ✓ |
| Separate enrollment | Introduce a distinct enrollment/pre-enrollment step in this phase | |
| Unsure, discuss more | Talk through what an enrollment step would actually mean | |

**User's choice:** Single flow
**Notes:** The user raised enrollment as a possibility but agreed not to add a separate lifecycle in Phase 2.

---

## Student-Responsible Relationship

| Option | Description | Selected |
|--------|-------------|----------|
| Multi + primary | Student has one or more responsibles, with one marked primary/financial | |
| Multi, no primary | Student has one or more responsibles, all treated equally | |
| Single responsible | Keep exactly one responsible per student | ✓ |

**User's choice:** Single responsible
**Notes:** Superseded on 2026-04-19 during Phase 2 quick update. Phase 2 keeps the current one-student-to-one-responsável relation and removes the many-to-many migration scope.

---

## Link Management

| Option | Description | Selected |
|--------|-------------|----------|
| Student form first | Select the required responsável during student creation/edit | ✓ |
| Detail page manage | Create the student, then change the responsável later | |
| Both | Allow basic linking in form and later adjustments in detail/edit screens | |

**User's choice:** Student form first
**Notes:** The student flow should own the required responsável experience.

---

## Relationship Type On Link

| Option | Description | Selected |
|--------|-------------|----------|
| Yes, required | Every link must say mãe, pai, tutor, avó, or similar | |
| Yes, optional | Support relationship type, but do not force it on every link yet | |
| No, just linked | Only store the link for now | |

**User's choice:** Deferred / not in Phase 2 scope
**Notes:** Relationship-type metadata was tied to the removed many-to-many direction and is no longer part of the regenerated Phase 2 plans.

---

## Last Linked Responsible Rule

| Option | Description | Selected |
|--------|-------------|----------|
| Block it | Do not allow the student to end up without any linked responsible | ✓ |
| Require replacement | Only allow removal/archive if another responsible is linked in the same flow | |
| Allow temporary gap | Student may temporarily stay without responsible until later fix | |

**User's choice:** Block it
**Notes:** Registry integrity should be preserved immediately.

---

## Terminology

| Option | Description | Selected |
|--------|-------------|----------|
| Use Responsável | Treat `responsável` as the main business term in screens and flows | ✓ |
| Keep mixed wording | Continue using `pais e responsáveis` mixed labels | |
| Use Pai/Mãe | Treat parent language as the main concept | |

**User's choice:** Use Responsável
**Notes:** The user wants the broader school/business concept, not only biological-parent framing.

---

## Student Screen Presentation

| Option | Description | Selected |
|--------|-------------|----------|
| Count + full detail | List shows quantity/summary; detail shows full responsible information | |
| Show names inline | List and detail screens show responsible names directly wherever possible | |
| Minimal for now | Only show the responsible clearly on student detail/edit flow, keep list pages simpler | ✓ |

**User's choice:** Minimal for now
**Notes:** The user prefers simpler list/detail surfaces while keeping the current single-responsável model.

---

## the agent's Discretion

- Exact UI pattern for the current responsible selector inside the student flow.
- Exact detail-page layout for the responsible summary while keeping screens uncluttered.

## Deferred Ideas

- Separate enrollment workflow — future phase if the product needs real enrollment lifecycle/status.
- Many-to-many student/responsável links — removed from Phase 2 scope.
- Primary or financial responsible — likely future finance-phase decision.
