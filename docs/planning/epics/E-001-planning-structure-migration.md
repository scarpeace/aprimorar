# Epic: E-001 - Planning and Documentation Canonicalization

**Goal:** Keep project and planning docs concise, current, and safe for incremental AI-assisted delivery.
**Status:** DONE
**Phase:** Phase 0

## Scope

- In scope:
  - canonical project docs
  - roadmap and epic hygiene
  - archive migration into `docs/`
  - stable planning structure for small tasks
- Out of scope:
  - feature implementation outside docs

## Workboard

- Current focus: none
- Blocked: none
- Next up: keep epics synced as implementation moves

## Stories

### Story: S-001 - Canonical project docs

**Status:** DONE
**Intent:** Keep one concise human doc and one high-context AI doc.
**Links:** T-001, T-002

**Acceptance Criteria**

- `docs/project.md` is concise and current.
- `docs/project-ai-context.md` exists and captures implementation-relevant context.

### Story: S-002 - Canonical planning structure

**Status:** DONE
**Intent:** Keep roadmap, template, and epic files aligned to small executable slices.
**Links:** T-003, T-004

**Acceptance Criteria**

- `docs/planning/roadmap.md` is present and current.
- Epics use smaller task slices and clearer next steps.

### Story: S-003 - Archive retirement

**Status:** DONE
**Intent:** Move useful archive knowledge into canonical docs, then delete archive content.
**Links:** T-005

**Acceptance Criteria**

- Useful archive information is reflected in canonical docs.
- `docs/archive/` no longer contains stale planning/reference files.

## Tasks

### Task: T-001 - Rewrite human-facing project doc

**Type:** docs
**Status:** DONE
**Depends on:** None

**Outcome**

- Replace the long mixed `docs/project.md` with a concise canonical project overview.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Review for concise scope, MVP truths, diagrams, and decisions

### Task: T-002 - Create AI context project doc

**Type:** docs
**Status:** DONE
**Depends on:** None

**Outcome**

- Add `docs/project-ai-context.md` with implementation-relevant context and known gaps.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm document captures architecture, current state, conventions, and gaps

### Task: T-003 - Refresh roadmap and planning hub

**Type:** docs
**Status:** DONE
**Depends on:** T-001

**Outcome**

- Recreate `docs/planning/roadmap.md` and align `docs/planning/project.md`.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Check roadmap links and planning guidance for consistency

### Task: T-004 - Normalize epic template and active epics

**Type:** docs
**Status:** DONE
**Depends on:** T-003

**Outcome**

- Update the template and active epics so next work is split into smaller practical slices.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Spot-check all active epics for clear next slices and consistent format

### Task: T-005 - Retire archive files after migration

**Type:** docs
**Status:** DONE
**Depends on:** T-004

**Outcome**

- Delete migrated archive docs after confirming useful content was preserved.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm no needed archive-only knowledge remains

## Archive

### Task: T-010 - Original planning structure migration

- Status: DONE
- Scope: Earlier work created the canonical `docs/planning/` structure and split planning into epic files.
- Verification: Legacy files were mapped into the new planning layout.

## Review Notes

- 2026-03-07: Reframed this epic from one-time migration to ongoing canonicalization so docs hygiene stays trackable.
