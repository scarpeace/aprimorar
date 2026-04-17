# Architecture Patterns

**Domain:** private school management application
**Researched:** 2026-04-17

## Recommended Architecture

Extend the existing **modular monolith** instead of introducing microservices. Keep the current Spring Boot + PostgreSQL backend and React SPA, but formalize the app into six bounded areas: **Identity & Access**, **School Registry**, **Operations**, **Finance**, **Dashboard/Reporting**, and **Shared Platform**.

The existing codebase already behaves like a domain-organized modular monolith: backend packages are split by domain (`student`, `parent`, `employee`, `event`, `dashboard`), the frontend is split by feature folders, and contracts flow through OpenAPI + Kubb. The safest extension path is to preserve that shape and add missing capabilities as new modules, not as cross-cutting hacks inside `event` or `dashboard`.

```text
React SPA
├── Auth shell (login, session bootstrap, protected app routes)
├── Feature pages (dashboard, students, parents, employees, events, finance)
└── Shared API/query layer (Kubb + TanStack Query)
        ↓
Spring Boot modular monolith
├── identity/         -> login, logout, current user, role checks
├── student/          -> student master data
├── parent/           -> parent/responsável master data
├── employee/         -> employee master data
├── event/            -> scheduling / atendimento operations
├── finance/          -> charges, payments, balance rules
├── dashboard/        -> aggregated read models only
└── shared/           -> error handling, pagination, clock, config
        ↓
PostgreSQL
├── registry tables
├── operations tables
├── finance tables
└── auth tables
```

### Component Boundaries

| Component | Responsibility | Communicates With |
|-----------|---------------|-------------------|
| Auth UI | Login form, logout, session bootstrap, route guard, current-user context | Identity API, protected routes |
| Main app shell | Navigation, section visibility, layout, global error/loading states | Auth UI, feature pages |
| Student module | Student CRUD, search, archive, parent link | Parent module, Dashboard read model |
| Parent module | Parent CRUD and lookup for student assignment | Student module |
| Employee module | Employee CRUD and lookup for scheduling/ownership | Event module, Identity bootstrap |
| Event module | Atendimentos/classes scheduling, conflicts, links to student + employee | Student, Employee, Dashboard |
| Finance module | Receivables/payments ledger, overdue tracking, financial summaries | Student, Parent, Dashboard |
| Dashboard module | Consolidated KPIs and charts exposed as read endpoints only | Event, Finance, Student |
| Identity module | User account, password hash, session lifecycle, role claims | Employee (optional reference), all protected controllers |
| Shared platform | ProblemDetail/error shaping, pagination, clock, CORS/security config, auditing | All backend modules |

## Data Flow

### 1. Authentication and app bootstrap

1. User opens SPA.
2. SPA calls `GET /v1/auth/me` (or equivalent bootstrap endpoint).
3. Backend resolves session from Spring Security and returns current user + role + permitted navigation areas.
4. React app renders either:
   - login route for anonymous users, or
   - protected layout for authenticated users.
5. TanStack Query caches the session payload; logout clears session-related queries.

### 2. Protected business flow

1. User navigates to a protected page.
2. Route guard/layout checks cached session state.
3. Page calls generated Kubb hook.
4. Backend controller delegates to service.
5. Service enforces business rules and authorization.
6. Repository persists/reads data.
7. DTO response returns through OpenAPI-generated client.
8. Mutation invalidates relevant query keys so list/detail/dashboard data refreshes.

### 3. Dashboard flow

1. Dashboard UI calls a small number of aggregated read endpoints.
2. Dashboard service composes summary data from Event + Finance + Student repositories/services.
3. UI renders KPIs/charts without reconstructing business logic client-side.

### 4. Finance flow

1. Secretary creates or updates a charge for a student/responsável.
2. Finance service calculates status (`ABERTO`, `PAGO`, `VENCIDO`, `PARCIAL`, etc.).
3. Payment postings update balance history.
4. Dashboard reads pre-defined finance summaries; finance pages read ledger-style endpoints.

## Patterns to Follow

### Pattern 1: Keep authentication as a first-class module, not a frontend-only guard
**What:** Add a backend identity boundary with session-backed authentication, current-user endpoint, and role-based authorization.
**When:** Immediately, before finance and dashboard visibility changes.
**Why:** In this repo, authentication does not exist yet, and all current routes are implicitly public. Adding page guards without server enforcement creates a false sense of security.

**Recommended shape:**

```text
identity/
├── AuthController      -> login, logout, me, csrf if needed
├── AuthService         -> credential verification, bootstrap DTO
├── UserAccount         -> username/email, password hash, active flag
├── UserRole            -> ADMIN, SECRETARIA (start simple)
└── SecurityConfig      -> Spring Security filter chain
```

**Recommendation:** Use **server-side session auth with secure cookies**, not JWT, for v1. This is a same-product SPA + Spring backend, not a public multi-client API platform. Sessions are simpler to revoke, simpler to reason about, and align with Spring Security defaults. If the SPA stays on a different origin in dev/prod, configure CORS and credentialed requests explicitly.

### Pattern 2: Separate finance from events even if dashboard currently derives money from events
**What:** Create a dedicated `finance` module instead of extending `Event` as the source of truth for receivables.
**When:** As soon as overdue balances and payment tracking are introduced.
**Why:** The existing `Event` entity already contains `price` and `payment`, and the dashboard sums those values today. That works for class economics, but it is the wrong core model for school receivables. Charges/payments need their own lifecycle, due dates, status, and audit trail.

**Recommended finance core:**

```text
finance/
├── Charge             -> amount due, due date, student/parent reference, status
├── Payment            -> amount paid, paid at, method, note
├── FinanceService     -> create charge, register payment, compute balance
├── FinanceController  -> list charges, overdue summary, post payments
└── FinanceSummaryService -> dashboard-oriented aggregation
```

**Integration rule:** link finance records to `student` (primary) and optionally `parent`, but do not make them children of `event`.

### Pattern 3: Dashboard as read model, not transaction owner
**What:** Keep dashboard endpoints thin and aggregate-only.
**When:** Always.
**Why:** Dashboards change faster than transactional rules. If dashboard code becomes the place where finance logic lives, every KPI tweak risks corrupting business rules.

**Good split:**
- `finance` owns overdue and balance calculations.
- `event` owns schedule and class economics.
- `dashboard` only asks those modules for summarized data.

### Pattern 4: Frontend auth shell above feature routes
**What:** Introduce a small app shell that resolves session state before rendering the protected layout.
**When:** First frontend auth increment.
**Why:** The current app mounts `MainLayout` directly for all routes. That makes auth, nav visibility, and anonymous redirects hard to add cleanly.

**Recommended flow:**

```text
<BrowserRouter>
  /login -> public
  /app/* -> protected layout
            ├── dashboard
            ├── students
            ├── parents
            ├── employees
            ├── events
            └── finance
```

If the team later adopts React Router data routers, move auth checks into route loaders/middleware. For now, a protected layout + bootstrap query is the lowest-risk retrofit.

### Pattern 5: Preserve contract-first integration
**What:** All new auth, finance, and dashboard DTOs flow through backend OpenAPI generation and frontend Kubb sync.
**When:** Every contract change.
**Why:** This repo already depends on generated clients. Bypassing that pattern for auth or finance will create the highest integration risk in the milestone.

## Suggested Build Order

### Step 1: Backend identity foundation
Add Spring Security, user accounts, password hashing, login/logout/current-user endpoints, and baseline role checks.

**Why first:** it is the only net-new cross-cutting capability. Finance and dashboard visibility depend on knowing who is logged in.

### Step 2: Frontend auth shell and protected routing
Add login page, bootstrap query, protected layout, logout, and nav visibility.

**Why second:** this exposes auth safely without forcing immediate domain rewrites. Existing student/parent/employee/event pages can remain mostly unchanged behind the shell.

### Step 3: Authorization tightening on existing modules
Protect current backend endpoints and ensure the SPA handles `401/403` cleanly.

**Why third:** minimizes brownfield risk. First make auth work, then make the old app respect it.

### Step 4: Finance domain as a separate module
Add charge/payment tables, services, CRUD/list endpoints, overdue rules, and summary endpoints.

**Why fourth:** finance has the most domain risk and should be implemented after auth, but before dashboard redesign. This avoids building dashboard metrics on temporary finance logic.

### Step 5: Dashboard rewrite to compose operational + financial summaries
Refactor dashboard service so it reads from finance summaries plus existing operational/event metrics.

**Why fifth:** dashboard should be the consumer of finished modules, not the prototype surface where unfinished finance logic leaks.

### Step 6: Optional visibility refinement
Only after the above, add more granular role-based visibility, feature flags, or future portals for teachers/parents/students.

**Why last:** v1 explicitly does not need complex permissions.

## Integration Guidance for Authentication and Finance in This Existing Stack

### Backend integration guidance

- Add `spring-boot-starter-security`; it is not present today in `pom.xml`.
- Keep security config centralized under `config/` plus an `identity/` domain package.
- Reuse the existing global error shape by adding explicit `401` and `403` handlers/entry points.
- Because the SPA runs on `http://localhost:5173`, update CORS for credentialed requests when session auth is introduced.
- Add auth-related Flyway migrations before exposing login.
- Keep `ADMIN` and `SECRETARIA` only for v1; do not model the full future role matrix yet.

### Frontend integration guidance

- Add a dedicated auth feature folder instead of spreading auth logic through `MainLayout`.
- Keep session state in TanStack Query, not in ad hoc global state.
- On successful login/logout, invalidate or clear `me`, dashboard, and any user-scoped queries.
- Derive navigation visibility from the `me` payload instead of hardcoding every menu item forever.
- Keep generated Kubb hooks as the source of truth; do not special-case auth with handwritten fetch code unless unavoidable.

### Brownfield migration guidance

- Do **not** rewrite students/parents/employees/events to “fit auth”. Wrap them with auth first.
- Do **not** retrofit finance into the existing `event` table just because revenue/cost already exists there.
- Do **not** expand permissions beyond what the current milestone needs.
- Change dashboard last among the user-facing surfaces; it is the easiest place to recompose data once the underlying modules are stable.

## Anti-Patterns to Avoid

### Anti-Pattern 1: Using `Employee` as the login account model
**What:** Treating employee records as authentication records.
**Why bad:** HR/person data and login credentials evolve differently. Not every employee should become a user, and password/account lock concerns do not belong in the employee aggregate.
**Instead:** Create `UserAccount` and optionally link it to `Employee`.

### Anti-Pattern 2: Keeping finance inside dashboard queries
**What:** Computing overdue balances directly inside dashboard services/controllers.
**Why bad:** Business rules become duplicated and untestable.
**Instead:** Put balance rules in `finance`, expose dashboard-friendly summary methods.

### Anti-Pattern 3: JWT-first complexity for an internal admin SPA
**What:** Adding token issuance, refresh token rotation, storage strategy, and custom interceptors for v1.
**Why bad:** More moving parts than the product needs right now.
**Instead:** Use secure session cookies first; revisit JWT only if external/mobile/public clients appear.

### Anti-Pattern 4: Frontend-only authorization
**What:** Hiding routes/menu items without server-side protection.
**Why bad:** Anyone can still call the API.
**Instead:** Enforce auth in Spring Security and use frontend visibility only as UX.

## Scalability Considerations

| Concern | At 100 users | At 10K users | At 1M users |
|---------|--------------|--------------|-------------|
| Authentication | In-memory/local session okay for dev; DB-backed accounts | Consider Spring Session if multiple app instances | Dedicated identity platform likely needed |
| Dashboard aggregation | Direct repository aggregation is fine | Add summary queries/materialized views for finance KPIs | Separate analytics/read pipeline |
| Finance ledger | Simple charge/payment tables sufficient | Add indexes on due date, student, status | Partitioning/archive strategy likely needed |
| API composition | Module-to-module service calls inside monolith | Keep clear boundaries, avoid circular service dependencies | Consider extraction only if org/scale demands it |
| Frontend data loading | Page-level queries are fine | Consolidate summary endpoints to reduce waterfalls | BFF/edge caching may be justified |

## Sources

- Repository inspection: existing domain packages, routing structure, dashboard/event coupling, CORS config, and OpenAPI/Kubb workflow in this codebase. **Confidence: HIGH**
- Spring Security session management reference: https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html **Confidence: HIGH**
- Spring Security CSRF reference for SPAs: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html **Confidence: HIGH**
- Spring Security CORS integration reference: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html **Confidence: HIGH**
- React Router route object and middleware docs: https://reactrouter.com/start/data/route-object **Confidence: MEDIUM** (useful for future router evolution; current app still uses declarative routes)
- TanStack Query invalidation guidance: https://tanstack.com/query/latest/docs/framework/react/guides/invalidations-from-mutations **Confidence: HIGH**
