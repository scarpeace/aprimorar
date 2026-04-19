# Phase 2: Student & Parent Registry Hardening - Context

**Gathered:** 2026-04-19
**Status:** Ready for planning

<domain>
## Phase Boundary

Harden the existing student and responsável registry flows so the secretary can reliably create, update, archive, search, and manage linked records without depending on spreadsheets. This phase improves the current registry model and interactions; it does not introduce a separate enrollment lifecycle, a many-to-many student/responsável redesign, finance behavior, or broader new modules.

</domain>

<decisions>
## Implementation Decisions

### Student Registration Model
- **D-01:** Keep a single student registration flow in Phase 2 rather than introducing a separate enrollment or pre-enrollment workflow.
- **D-02:** A student cannot be created or updated without exactly one linked responsável.
- **D-03:** A responsável can exist in the system without any linked student.

### Student-Responsible Relationship
- **D-04:** Keep the current relationship shape: one student -> exactly one responsável, while one responsável may be linked to many students.
- **D-05:** Do not introduce a many-to-many migration, primary responsável, or financial responsável concept in Phase 2.
- **D-06:** Preserve the existing `parentId`-style linkage in write flows unless a narrower compatibility adjustment is needed for correctness.

### Link Management Rules
- **D-07:** Manage the required responsável directly in the student creation/edit flow.
- **D-08:** If an action would leave a student without a linked responsável, block it.
- **D-09:** Archive and edit rules must preserve registry integrity rather than allowing temporary no-responsável gaps.

### Terminology And Presentation
- **D-10:** Use `Responsável` as the main business term in Phase 2 user-facing flows.
- **D-11:** Keep student list/detail presentation compact and centered on the current responsável, without inventing new relationship-management surfaces.

### the agent's Discretion
- Exact detail-page layout for showing the responsible summary while keeping the screen uncluttered.
- Whether the current selector component should stay as-is or receive a small brownfield-safe UX hardening pass.
- How much responsible summary data should be embedded in student read responses versus loaded separately, as long as the single registration flow stays intact.

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Planning And Scope
- `.planning/PROJECT.md` — Product scope, brownfield constraints, and the spreadsheet-replacement goal for secretary workflows.
- `.planning/REQUIREMENTS.md` — Phase 2 requirement IDs `STUD-01` to `STUD-04` and `PARN-01` to `PARN-04`.
- `.planning/ROADMAP.md` — Phase 2 goal, dependency on Phase 1, and success criteria.
- `.planning/STATE.md` — Current project position and carried-forward Phase 1 decisions.

### Existing Student Flows
- `client/src/features/students/pages/StudentsPage.tsx` — Current list/search/archive UX for students.
- `client/src/features/students/pages/StudentCreatePage.tsx` — Current student creation flow with single responsável selection.
- `client/src/features/students/pages/StudentEditPage.tsx` — Current student edit flow and existing field set.
- `client/src/features/students/pages/StudentDetailsPage.tsx` — Current student detail page assumptions about the current responsável.
- `client/src/features/students/forms/studentFormSchema.ts` — Current student form rules, including required `parentId`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java` — Current persistence model with required `@ManyToOne Parent`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` — Current student create/update/archive/delete business rules.

### Existing Responsible Flows
- `client/src/features/parents/pages/ParentsPage.tsx` — Current responsável list/search/archive UX.
- `client/src/features/parents/pages/ParentCreatePage.tsx` — Current responsável creation flow.
- `client/src/features/parents/pages/ParentEditPage.tsx` — Current responsável edit flow.
- `client/src/features/parents/pages/ParentDetailPage.tsx` — Current responsável detail view and linked-students display.
- `client/src/features/parents/components/ParentSelectDropdown.tsx` — Existing reusable responsável selector used in student forms.
- `client/src/features/parents/forms/parentFormSchema.ts` — Current responsável validation rules.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/Parent.java` — Current responsável entity.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java` — Current responsável archive/delete rules and student-link guardrails.

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `client/src/features/parents/components/ParentSelectDropdown.tsx`: Existing responsável selector already used in student forms; it should remain the starting point because Phase 2 keeps a single required responsável.
- `client/src/features/students/components/StudentsTable.tsx` and `client/src/features/parents/components/ParentsTable.tsx`: Existing paginated list wrappers that should be hardened rather than replaced.
- `client/src/components/ui/list-search-input.tsx`, `toggle-switch.tsx`, and `pagination.tsx`: Shared list/search/archive controls already used across registry screens.
- `client/src/components/ui/section-card.tsx` and `summary-item.tsx`: Existing detail and form layout primitives that should continue shaping registry pages.

### Established Patterns
- Registry pages already use search + pagination + archived toggle from the parent page component.
- Frontend forms use `react-hook-form` + `zodResolver` with Portuguese validation messages.
- Backend services normalize CPF/email/contact and enforce uniqueness in service-layer helper methods.
- Archive is preferred over hard delete for normal operations, while delete paths preserve domain integrity through explicit service rules.

### Integration Points
- Student create/edit/detail flows are the main integration point for hardening the required responsável experience without changing the relationship shape.
- Parent detail already shows linked students, so it should remain the main place to review one responsável -> many students.
- Backend student and parent services/controllers plus generated OpenAPI/Kubb contracts may still need to evolve together so student reads can expose the current responsável cleanly without forcing extra SPA fetches.

</code_context>

<specifics>
## Specific Ideas

- The current CRUD exists, but Phase 2 should make it trustworthy enough for daily registry work instead of adding a separate enrollment process.
- The user wants the concept of `Responsável` to be more central than a strict `Pai/Mãe` framing.
- The user explicitly wants to keep the current one-student-to-one-responsável relation in Phase 2 and remove the planned many-to-many migration scope.

</specifics>

<deferred>
## Deferred Ideas

- Separate enrollment or pre-enrollment lifecycle — defer unless the product later introduces real status/lifecycle semantics.
- Many-to-many student/responsável links — explicitly out of Phase 2 after scope correction.
- Primary or financial responsável designation — likely future finance-phase decision, not Phase 2.

</deferred>

---

*Phase: 02-student-parent-registry-hardening*
*Context gathered: 2026-04-19*
