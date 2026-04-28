<user_constraints>
## User Constraints (from CONTEXT.md)

### Locked Decisions
- **D-01:** The search bar should ONLY match the student name, strictly adhering to requirement TAB-02. It does not need to search event content or types.
- **D-02:** The search should be auto-debounced (filtering as you type) rather than requiring a manual "submit" or "enter" press. This is consistent with other pages (e.g., `EmployeesPage`).
- **D-03:** The search bar should be placed above the events table as a standalone component, making it prominent.
- **D-04:** When a search yields no results, display a simple text message inside the table body (e.g., "Nenhum atendimento encontrado") to keep the table headers visible, rather than using a large standalone `EmptyCard` illustration.

### the agent's Discretion
No specific aesthetic requirements — open to standard approaches using existing UI components like `ListSearchInput`.

### Deferred Ideas (OUT OF SCOPE)
None — discussion stayed within phase scope.
</user_constraints>

<phase_requirements>
## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| TAB-01 | Visualizar tabela exclusiva de eventos do funcionário, ordenada por data decrescente. | Requires the frontend to pass `{ sort: ["startDate,desc"], page: currentPage }` to the `useGetEventsByEmployeeId` hook. Backend `Pageable` automatically handles it. Currently, `currentPage` is not being passed in `EmployeeDetailPage.tsx`. |
| TAB-02 | Barra de busca para filtrar eventos pelo nome do aluno. | Requires updating `GET /v1/events/{id}/employee` to accept `@RequestParam(required = false) String studentName` and filtering via `EventSpecifications.withStudentNameIgnoreCase`. |
</phase_requirements>

# Phase 01: Employee Events Tracking - Research

**Researched:** 2026-04-28
**Domain:** Frontend UI / Backend API (Spring Boot + React)
**Confidence:** HIGH

## Summary
The phase requires adding pagination, sorting, and debounced search to the existing events table on an employee's detail page. The frontend currently calls `useGetEventsByEmployeeId(employeeId)` without arguments for pagination or search, meaning pagination is broken and sorting defaults. We must update `EmployeeDetailPage.tsx` to handle `searchTerm` and pass it alongside `currentPage` and `sort: ["startDate,desc"]`. 

On the backend, the `EventController.getEventByEmployeeId` endpoint only accepts a `Pageable`. To satisfy constraint D-01 ("ONLY match the student name"), we need to update the endpoint to explicitly accept a `studentName` parameter rather than reusing the generic `search` parameter that checks content. We'll introduce a new Specification `withStudentNameIgnoreCase` in `EventSpecifications.java`.

**Primary recommendation:** Update `EventController`'s `/{id}/employee` endpoint with `studentName`, use Kubb to generate the new hook, and integrate `ListSearchInput` + `useDebounce` into `EmployeeDetailPage.tsx` passing `page` and `sort` parameters.

## Architectural Responsibility Map

| Capability | Primary Tier | Secondary Tier | Rationale |
|------------|-------------|----------------|-----------|
| Student Name Filtering | API / Backend | Browser / Client | Searching must happen in the database to correctly paginate results across all records, not just the visible page. |
| Debouncing Search | Browser / Client | — | The client must debounce inputs to prevent overwhelming the backend with requests on every keystroke. |
| Event Sorting | API / Backend | Browser / Client | Order must be handled by the database for correct pagination. |
| Table Empty State | Browser / Client | — | Client handles UI layout, displaying a fallback `<tr>` to keep headers visible when results are empty. |

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Spring Boot | 3.x | API backend | Existing backend framework, provides `Pageable` and `Specification` for elegant sorting/filtering. |
| React Query | 5.x | Data fetching | Existing frontend data layer, managed via Kubb-generated hooks. |
| Kubb | 4.36 | API Client generation | Keeps frontend types and hooks in sync with the Spring/OpenAPI backend. |

## Architecture Patterns

### Recommended Project Structure
```
server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/
├── EventController.java       # Exposes pagination, sorting, and search parameter
├── EventService.java          # Handles Specification combinations
└── repository/
    ├── EventRepository.java   # JpaSpecificationExecutor methods
    └── EventSpecifications.java # withStudentNameIgnoreCase implementation

client/src/features/employees/pages/
└── EmployeeDetailPage.tsx     # Manages pagination, sorting array, and search state
```

### Pattern 1: Debounced Table Search
**What:** Utilizing a custom `useDebounce` hook alongside React state to wait for user typing to pause before fetching.
**When to use:** Whenever filtering a list via backend queries based on text input.
**Example:**
```tsx
const [searchTerm, setSearchTerm] = useState("");
const debouncedSearchTerm = useDebounce(searchTerm, 500);

const employeeEventsQuery = useGetEventsByEmployeeId(employeeId, {
  page: currentPage,
  studentName: debouncedSearchTerm,
  sort: ["startDate,desc"]
});
```

### Anti-Patterns to Avoid
- **Client-side Filtering:** Fetching all events for an employee and filtering them in memory. This breaks pagination and fails for large datasets.
- **Reusing Generic Search:** Passing the generic `search` query parameter to the backend instead of a targeted one, which would violate D-01 by matching event contents.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Debouncing | Custom `setTimeout` wrappers | `useDebounce` hook | Already exists in `lib/shared/use-debounce.ts` and handles unmounting cleanly. |
| Pagination & Sorting | Custom offset/limit math | Spring `Pageable` | Natively supported by Spring Data JPA and correctly parses URL parameters like `?sort=startDate,desc`. |
| Search Input UI | Standard `<input>` tag | `ListSearchInput` | Standardizes placeholder, clearing, and styling across the app. |

## Common Pitfalls

### Pitfall 1: Forgetting to pass Pageable details
**What goes wrong:** The table does not change pages or sort correctly.
**Why it happens:** The Kubb hook `useGetEventsByEmployeeId` is invoked without the optional params object.
**How to avoid:** Always pass `{ page: currentPage, sort: ["startDate,desc"] }` to the hook.

### Pitfall 2: Replacing the entire table on empty state
**What goes wrong:** The table headers disappear when no results are found.
**Why it happens:** Conditionally rendering a separate `<EmptyCard>` instead of the `<table>`.
**How to avoid:** Keep the `<table>` element and render a `<tr><td colSpan={8}>Nenhum atendimento encontrado</td></tr>` inside the `<tbody>`.

## Code Examples

### Spring Data JPA Specification for Search
```java
// EventSpecifications.java
public static Specification<Event> withStudentNameIgnoreCase(String term) {
    return (root, query, cb) -> {
        if (term == null || term.trim().isEmpty()) return null;
        String pattern = "%" + term.toLowerCase() + "%";
        return cb.like(cb.lower(root.join("student").get("name")), pattern);
    };
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Custom Axios calls | Kubb-generated React Query hooks | Baseline | Strongly typed API interactions, requires running `npm run sync` after backend changes. |

## Assumptions Log

| # | Claim | Section | Risk if Wrong |
|---|-------|---------|---------------|
| A1 | Kubb is correctly configured to read `@RequestParam` updates automatically | Architecture Patterns | The frontend hook will not accept the new parameters. |

## Open Questions

None. The integration path is fully verified.

## Environment Availability

| Dependency | Required By | Available | Version | Fallback |
|------------|------------|-----------|---------|----------|
| Java / Maven | Backend | ✓ | — | — |
| Node / npm | Frontend | ✓ | — | — |
| Kubb CLI | API Generation | ✓ | 4.36.1 | manual Axios request |

## Validation Architecture

### Test Framework
| Property | Value |
|----------|-------|
| Framework | Spring Boot Starter Test (Backend) / Manual UI tests |
| Config file | `pom.xml` |
| Quick run command | `cd server/api-aprimorar && ./mvnw test -Dtest=EventControllerTest` |
| Full suite command | `cd server/api-aprimorar && ./mvnw test` |

### Phase Requirements → Test Map
| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| TAB-01 | Sorting explicitly by date descending | integration | `mvn test -Dtest=EventControllerTest#testSorting` | ❌ Wave 0 |
| TAB-02 | Filtering specifically by studentName | integration | `mvn test -Dtest=EventControllerTest#testStudentSearch` | ❌ Wave 0 |

### Wave 0 Gaps
- [ ] `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventControllerTest.java` — Need to verify new `studentName` parameter filters correctly.
- Frontend lacks a `vitest` or `jest` test runner in `package.json`. UI functionality relies on manual or E2E validation.

## Security Domain

### Applicable ASVS Categories

| ASVS Category | Applies | Standard Control |
|---------------|---------|-----------------|
| V2 Authentication | yes | Spring Security (existing) |
| V4 Access Control | yes | Existing `@PreAuthorize` or standard Spring roles |
| V5 Input Validation | yes | Spring `@RequestParam` validation and JPA Specifications |

### Known Threat Patterns for Spring Boot

| Pattern | STRIDE | Standard Mitigation |
|---------|--------|---------------------|
| SQL Injection in Search | Tampering | Use JPA CriteriaBuilder (`cb.like`) instead of string concatenation |
| Parameter Tampering | Tampering | Validate `sort` parameters to prevent sorting by restricted/internal fields |

## Sources

### Primary (HIGH confidence)
- `.planning/phases/01-employee-events-tracking/01-CONTEXT.md` - Locked Decisions
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventController.java` - Endpoint parameters
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventSpecifications.java` - Specification queries
- `client/src/features/employees/pages/EmployeeDetailPage.tsx` - Current usage

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - Directly observed in `package.json` and `pom.xml`.
- Architecture: HIGH - Verified Spring Data and React patterns.
- Pitfalls: HIGH - Extracted from existing frontend file omissions (`currentPage` not passed).

**Research date:** 2026-04-28
**Valid until:** 30 days
