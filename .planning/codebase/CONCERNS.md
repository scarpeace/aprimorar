# Codebase Concerns

**Analysis Date:** 2026-04-17

## Tech Debt

**Duplicated frontend error helpers and leftover debug logging:**
- Issue: Error-message formatting exists in both `client/src/lib/shared/api.ts` and `client/src/lib/shared/api-errors.ts`, with different Axios parsing behavior (`error.message` vs `error.response?.data?.message`). The same area still logs directly to the console, and `client/src/features/students/pages/StudentCreatePage.tsx` contains a leftover `console.log` before submit.
- Files: `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/features/students/pages/StudentCreatePage.tsx`
- Impact: Error handling drifts between screens, debugging noise leaks into production consoles, and future fixes must be applied in multiple places.
- Fix approach: Consolidate on a single shared error helper, remove direct console logging from UI flows, and route unexpected failures through one observable logging path.

**Dead event form abstraction is out of sync with the live contract:**
- Issue: `client/src/features/events/forms/EventForm.tsx` is not imported anywhere and still references `title`, while the live event contract in `client/src/features/events/forms/eventFormSchema.tsx` and `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java` has no `title` field.
- Files: `client/src/features/events/forms/EventForm.tsx`, `client/src/features/events/forms/eventFormSchema.tsx`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java`
- Impact: The next person who revives or reuses `EventForm.tsx` will reintroduce a broken payload shape and create avoidable frontend/backend drift.
- Fix approach: Delete the unused abstraction or realign it with `eventFormSchema` and the generated Kubb contract before reuse.

**UI placeholders and unresolved TODOs remain in reusable components:**
- Issue: Shared UI and list components still carry unresolved TODOs, including `content: unknown[]` in `client/src/components/ui/pagination.tsx`, generic-delete-modal follow-up in `client/src/features/parents/components/DeleteParentButton.tsx`, and table-layout TODOs in `client/src/features/students/components/StudentsTable.tsx` and `client/src/features/employees/components/EmployeesTable.tsx`.
- Files: `client/src/components/ui/pagination.tsx`, `client/src/features/parents/components/DeleteParentButton.tsx`, `client/src/features/students/components/StudentsTable.tsx`, `client/src/features/employees/components/EmployeesTable.tsx`
- Impact: Reusable UI stays loosely typed, duplicated modal behavior persists, and the table layer is harder to standardize safely.
- Fix approach: Tighten the pagination type around the actual page DTO, extract generic confirmation behavior into `client/src/components/ui/`, and finish the row/action refactor in the table wrappers.

**Backend TODOs mark unfinished contract and domain work:**
- Issue: Core backend classes still carry TODO markers for missing validation placement, enum/codegen documentation, and future Google Calendar fields.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/enums/BrazilianStates.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java`
- Impact: Important implementation details remain implicit, and cross-stack contract work depends on tribal knowledge instead of finalized code paths.
- Fix approach: Resolve or convert each TODO into explicit code, tests, or tracked work before expanding the affected modules.

## Known Bugs

**Dashboard requests the wrong year in the SPA:**
- Symptoms: The dashboard queries the API with the current year plus one.
- Files: `client/src/features/dashboard/DashboardPage.tsx`
- Trigger: Open the dashboard without overriding the default period.
- Workaround: Hardcode the desired year in the browser session or patch `getCurrentYearMonth()` to remove `+ 1`.

**Update payloads accept CPF but updates silently ignore CPF changes:**
- Symptoms: Parent, student, and employee update requests accept `cpf`, but the service/entity update paths do not persist it.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/ParentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/Parent.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/dto/EmployeeRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/Employee.java`
- Trigger: Submit an edit form with a changed CPF for a parent, student, or employee.
- Workaround: Treat CPF as immutable in the UI until the backend contract is corrected, or implement full CPF update support including uniqueness checks.

**Invalid dashboard parameters currently fall through as internal server errors:**
- Symptoms: Invalid `year` or `month` values raise `InvalidDashboardRequestException`, but `GlobalExceptionHandler` does not handle that exception explicitly and the generic handler returns `500` with `VALIDATION_ERROR`.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/exception/InvalidDashboardRequestException.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`
- Trigger: Call `GET /v1/dashboard/summary` with a null/out-of-range year or month.
- Workaround: Validate period values in the client and only send month `1..12` and year `2000..2100` until the exception mapping is fixed.

## Security Considerations

**No authentication or authorization boundary is present:**
- Risk: All CRUD and dashboard endpoints are effectively public to any caller who can reach the API.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardController.java`, `client/src/App.tsx`
- Current mitigation: Not detected in code. There are no Spring Security classes, auth filters, protected routes, or token/session handling paths.
- Recommendations: Add an explicit auth layer in the API first, then gate frontend routes and mutations behind authenticated user/session state.

**CORS is hardcoded for local development only:**
- Risk: The API is pinned to `http://localhost:5173`, which blocks non-local environments and pushes deploy-time origin changes into source edits.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java`
- Current mitigation: Local dev origin is allowed explicitly.
- Recommendations: Externalize allowed origins into environment-backed config and separate dev/prod policies.

## Performance Bottlenecks

**Dashboard summary performs multiple uncached aggregate queries per request:**
- Problem: Each dashboard load triggers separate count/sum/group queries.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventRepository.java`
- Cause: `getSummary()` issues five repository calls for the same month window and there is no cache layer.
- Improvement path: Cache monthly summaries, collapse related aggregates where practical, and add a service-level invalidation strategy when events change.

**Search endpoints rely on `%term%` + `lower(...)` scans without matching indexes:**
- Problem: Student, parent, employee, and event searches use broad case-insensitive LIKE filters that do not align with the current schema indexes.
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/repository/ParentSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/repository/EmployeeSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventSpecifications.java`, `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- Cause: `V1__init.sql` only adds indexes for `parent_id`, `student_id`, `employee_id`, and event start time, not the searched text fields.
- Improvement path: Add indexes that match the most frequent filters, or move to database-native text search/trigram indexes for large datasets.

## Fragile Areas

**Global exception mapping is inconsistent and easy to regress:**
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/ErrorCode.java`
- Why fragile: The handler duplicates some exception classes in the forbidden branch, maps internal failures to `VALIDATION_ERROR`, and does not cover all domain exceptions explicitly.
- Safe modification: Add one explicit branch per business exception family, align each branch with the intended HTTP status/error code pair, and extend tests before changing messages or statuses.
- Test coverage: Only `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java` exists; there are no controller-level tests validating end-to-end exception translation.

**Ghost-record deletion strategy depends on fixed UUIDs seeded in a single migration:**
- Files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeService.java`, `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- Why fragile: Student and employee deletion logic assumes the ghost rows from `V1__init.sql` always exist and always keep the same UUIDs.
- Safe modification: Preserve those seed rows in every environment, add startup/data-integrity verification, and cover deletion/reassignment with integration tests before changing migration history.
- Test coverage: Service-level unit tests exist, but there are no repository/database tests proving the ghost reassignment works against a real schema.

## Scaling Limits

**List and search endpoints are tuned for small datasets:**
- Current capacity: Suitable for the current CRUD-style domain size and pageable UI flows in `client/src/features/students/pages/StudentsPage.tsx`, `client/src/features/parents/pages/ParentsPage.tsx`, `client/src/features/employees/pages/EmployeesPage.tsx`, and `client/src/features/events/pages/EventsPage.tsx`.
- Limit: Response time will degrade as text-searchable rows grow because filters are executed with broad LIKE predicates and no dedicated search indexes.
- Scaling path: Add indexed filter columns, introduce more selective query parameters, and benchmark repository queries before dataset growth makes list pages sluggish.

## Dependencies at Risk

**Generated frontend client is tightly coupled to a live backend during development:**
- Risk: Contract regeneration requires a running API and manual sequencing, so DTO drift can survive locally until someone remembers to regenerate.
- Impact: `client/src/kubb/` can become stale relative to backend DTOs and break forms or query hooks after API changes.
- Migration plan: Keep the current Kubb flow, but automate OpenAPI + Kubb regeneration in CI or pre-merge verification so contract drift fails fast.

## Missing Critical Features

**Event deletion is implemented in the service but not exposed through HTTP or the SPA:**
- Problem: `EventService` contains `deleteEvent(UUID eventId)`, but `EventController` has the delete endpoint commented out and the frontend has no delete action for events.
- Blocks: Operators cannot remove an event through the supported product surface.

## Test Coverage Gaps

**Frontend behavior is untested:**
- What's not tested: Route rendering, form validation, mutation flows, optimistic invalidation behavior, and error states across `client/src/features/**`.
- Files: `client/package.json`, `client/src/App.tsx`, `client/src/features/students/pages/StudentCreatePage.tsx`, `client/src/features/events/pages/EventCreatePage.tsx`, `client/src/features/employees/hooks/emlpoyee-mutations.ts`
- Risk: UI regressions, contract mismatches, and date/validation bugs can ship without automated detection.
- Priority: High

**Backend web and persistence layers are lightly exercised:**
- What's not tested: Controller request/response behavior, repository queries/specifications, dashboard service behavior, and migration-backed deletion flows.
- Files: `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/employee/EmployeeServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`
- Risk: HTTP contract regressions, query bugs, and database-only failures can escape service-unit coverage.
- Priority: High

---

*Concerns audit: 2026-04-17*
