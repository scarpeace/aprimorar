# Epic: E-001 — Planning Structure Migration
**Goal:** Migrate legacy project/planning docs into the new `docs/` planning structure with stable IDs.
**Status:** DONE
**Owner:** Gu
**Related milestone/phase:** Phase 0

## Scope
- In scope:
  - Migrate `docs/archive/PROJECT.md` into `docs/project.md` using the standard section structure
  - Extract epics from `docs/archive/PLANNING.md` and assign stable epic IDs (E-002+)
  - Generate `docs/planning/roadmap.md` with phases and an epic index
  - Refine planning docs for consistency with the current planning template (stories + tasks)
- Out of scope:
  - Changing product requirements/semantics (content-only migration)
  - Deleting or rewriting legacy files in `docs/archive/`

## Stories

### Story: S-001 — Migrate project documentation
**As a** maintainer **I want** the project documentation in `docs/project.md` **so that** product/tech context is in a single canonical place.

**Links:** T-001 (DONE) — ST-001, ST-002, ST-003, ST-004, ST-005, ST-006

**Acceptance Criteria**
- [x] AC-001-01 `docs/project.md` includes: Overview, Tech Stack, Architecture, Data Model, API, Diagrams, Decisions
- [x] AC-001-02 Content from `docs/archive/PROJECT.md` is preserved (tables/diagrams/endpoints/decisions)
- [x] AC-001-03 No legacy files are removed

**Test Plan**
- Backend:
  - [x] N/A (docs-only)
- Frontend:
  - [x] N/A (docs-only)
- Manual:
  - [x] Render `docs/project.md` in a Markdown viewer; verify headings, tables, and mermaid blocks
  - [x] Spot-check key sections against `docs/archive/PROJECT.md` (tech stack, endpoints, decisions)

### Story: S-002 — Extract epics into the new planning index
**As a** maintainer **I want** legacy epics mapped to stable epic IDs **so that** future planning work is file-based and indexed.

**Links:** T-002 (DONE) — ST-007, ST-008, ST-009; T-003 (DONE) — ST-010, ST-011, ST-012, ST-013, ST-014

**Acceptance Criteria**
- [x] AC-002-01 Legacy epics are identified from `docs/archive/PLANNING.md`
- [x] AC-002-02 Stable epic IDs E-002..E-006 are reserved for the extracted epics
- [x] AC-002-03 `docs/planning/roadmap.md` lists the extracted epics in the epic index

**Test Plan**
- Backend:
  - [x] N/A (docs-only)
- Frontend:
  - [x] N/A (docs-only)
- Manual:
  - [x] Verify `docs/planning/roadmap.md` reflects the phases from `docs/archive/PLANNING.md`
  - [x] Verify epic ID/name mapping matches the legacy epic titles

### Story: S-003 — Roadmap file is canonical
**As a** contributor **I want** a canonical roadmap in `docs/planning/roadmap.md` **so that** planning is easy to discover and keep current.

**Links:** T-002 (DONE) — ST-007, ST-008, ST-009

**Acceptance Criteria**
- [x] AC-003-01 `docs/planning/roadmap.md` contains phases and an epic index table
- [x] AC-003-02 Epic index includes a file path for every epic file that exists

**Test Plan**
- Backend:
  - [x] N/A (docs-only)
- Frontend:
  - [x] N/A (docs-only)
- Manual:
  - [x] Open `docs/planning/roadmap.md` and confirm epic index links resolve for existing files

### Story: S-004 — Planning docs consistency pass
**As a** contributor **I want** planning docs to follow the current story/task template **so that** future work stays consistent and easy to execute.

**Links:** T-004 (DONE) — ST-015, ST-016, ST-017

**Acceptance Criteria**
- [x] AC-004-01 Stories include Acceptance Criteria and a Test Plan with Backend/Frontend/Manual sections
- [x] AC-004-02 Tasks include the standard task structure (Type, Status, Depends on, Description, Files, DoD, Verification, Notes)
- [x] AC-004-03 No existing IDs (E-/S-/T-/ST-) are renumbered

**Test Plan**
- Backend:
  - [x] N/A (docs-only)
- Frontend:
  - [x] N/A (docs-only)
- Manual:
  - [x] Review `docs/planning/epics/E-001-planning-structure-migration.md` for template compliance
  - [x] Review `docs/planning/roadmap.md` for accurate Phase 0 / E-001 status

## Tasks

### Task: T-001 — Migrate `PROJECT.md` to `docs/project.md`
**Type:** fullstack  
**Status:** DONE
**Depends on:** None

**Description**
- Reformat legacy consolidated project doc into the standard section structure
- Preserve key requirements tables, API endpoint lists, and decisions (content-only migration)

**Subtasks**
- [x] ST-001 — Migrate Overview (problem, goals, stakeholders, scope)
- [x] ST-002 — Migrate Tech Stack (versions/policies)
- [x] ST-003 — Migrate Architecture (system diagram, repo structure)
- [x] ST-004 — Migrate Data Model (notes + ER diagram)
- [x] ST-005 — Migrate API (base path, endpoints, Swagger)
- [x] ST-006 — Migrate Decisions (open questions + locked MVP decisions)

**Files likely affected (best guess)**
- docs/project.md
- docs/archive/PROJECT.md

**DoD (Definition of Done)**
- [x] `docs/project.md` includes: Overview, Tech Stack, Architecture, Data Model, API, Diagrams, Decisions
- [x] `docs/project.md` renders correctly (tables + mermaid)

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: Open `docs/project.md` in a Markdown viewer; spot-check endpoints and decisions vs `docs/archive/PROJECT.md`

**Notes**
- Risks: low (docs-only)
- Open questions: none

### Task: T-002 — Generate canonical roadmap and epic index
**Type:** fullstack  
**Status:** DONE
**Depends on:** None

**Description**
- Write a canonical roadmap matching the legacy phase structure
- Reserve stable epic IDs for extracted legacy epics and link to each epic file

**Subtasks**
- [x] ST-007 — Create Phase 0 for the migration work
- [x] ST-008 — Migrate Phase 1-5 naming and goals
- [x] ST-009 — Add epic index with stable IDs and file paths

**Files likely affected (best guess)**
- docs/planning/roadmap.md
- docs/archive/PLANNING.md

**DoD (Definition of Done)**
- [x] `docs/planning/roadmap.md` lists phases and goals (Phase 0-5)
- [x] Epic index includes a row for each epic file E-001..E-006

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: Open `docs/planning/roadmap.md` and click/resolve each epic file path

**Notes**
- Risks: low (docs-only)
- Open questions: none

### Task: T-003 — Migrate legacy planning epics into individual epic files
**Type:** fullstack  
**Status:** DONE
**Depends on:** None

**Description**
- Create one epic file per legacy epic (E-002..E-006)
- Convert legacy story/task numbering into stable S-/T-/ST- IDs

**Subtasks**
- [x] ST-010 — Create `E-002-core-data-management.md`
- [x] ST-011 — Create `E-003-authentication-and-authorization.md`
- [x] ST-012 — Create `E-004-frontend-development.md`
- [x] ST-013 — Create `E-005-calendar-and-scheduling.md`
- [x] ST-014 — Create `E-006-payments-and-billing.md`

**Files likely affected (best guess)**
- docs/planning/epics/E-002-core-data-management.md
- docs/planning/epics/E-003-authentication-and-authorization.md
- docs/planning/epics/E-004-frontend-development.md
- docs/planning/epics/E-005-calendar-and-scheduling.md
- docs/planning/epics/E-006-payments-and-billing.md

**DoD (Definition of Done)**
- [x] Each epic file uses the epic template sections (Goal, Scope, Stories, Tasks)
- [x] Story and task IDs are stable and unique within each epic

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: Open each epic file and spot-check against `docs/archive/PLANNING.md`

**Notes**
- Risks: low (docs-only)
- Open questions: none

### Task: T-004 — Refine planning docs for template compliance
**Type:** fullstack  
**Status:** DONE
**Depends on:** None

**Description**
- Update planning docs to match the current story/task template requirements
- Keep all existing IDs stable and avoid renumbering

**Subtasks**
- [x] ST-015 — Ensure stories include Backend/Frontend/Manual test plan sections
- [x] ST-016 — Ensure tasks include required fields (Type/Status/Depends/Files/DoD/Verification/Notes)
- [x] ST-017 — Align Phase 0 / E-001 status in `docs/planning/roadmap.md`

**Files likely affected (best guess)**
- docs/planning/epics/E-001-planning-structure-migration.md
- docs/planning/roadmap.md
- docs/project.md

**DoD (Definition of Done)**
- [x] `docs/planning/epics/E-001-planning-structure-migration.md` tasks follow the required task structure
- [x] `docs/planning/roadmap.md` Phase 0 and E-001 status are consistent with epic status

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: Review markdown rendering and checklist items in `docs/planning/epics/E-001-planning-structure-migration.md`

**Notes**
- Risks: low (docs-only)
- Open questions: none

**Review Notes (append-only)**
- Reviewer notes:
