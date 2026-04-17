# Technology Stack

**Project:** Aprimorar
**Researched:** 2026-04-17
**Confidence:** HIGH

## Recommended Stack

This is a **brownfield extension** recommendation for the existing React SPA + Spring Boot monorepo. Do **not** rewrite the app, split it into microservices, or replace the OpenAPI → Kubb flow. The right 2025/2026 move is to keep the current stack and add the minimum missing capabilities for secretary operations, simple authentication, dashboard summaries, and basic finance.

### Core Framework
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| Spring Boot | 3.5.x (keep repo on 3.5 line) | Backend application framework | Already in repo, current, stable, and aligned with Spring Security 6.5.x, Spring Data JPA, validation, and OpenAPI generation. Best brownfield choice. |
| Java | 21 | Backend language/runtime | Already adopted; strong fit for records, modern time APIs, and stable LTS support. |
| React | 19.x | Frontend UI | Already in repo and current. Good enough for this internal operations SPA without framework migration. |
| Vite | 7.x | Frontend build/dev | Fast dev loop, already configured, no reason to replace. |
| React Router | 7.x (keep declarative mode) | SPA routing | Already present. Keep current BrowserRouter pattern and add protected routes; do not switch to framework/data-router architecture mid-milestone. |

### Database
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| PostgreSQL | 16+ locally compatible; keep repo Postgres setup | Primary database | Already the system database. Strong relational fit for students, guardians, events, charges, payments, and dashboard aggregates. |
| Flyway | Boot-managed current | Schema migrations | Already enabled. Required for all auth and finance schema changes in this repo. |

### Infrastructure
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| Spring Security | Boot-managed 6.5.x line | Authentication and request protection | Standard choice for servlet apps. Supports username/password auth, session persistence, CSRF, and method/route authorization without introducing external identity complexity. |
| HttpSession + secure cookie | Built into servlet stack | Session persistence | Best fit for **simple internal authentication**. Easier and safer than JWT for a same-org SPA + API pair. |
| springdoc-openapi | 2.8.x | Contract generation | Already in repo and required for Kubb-based frontend sync. |
| Kubb + OpenAPI | 4.36.x line | Generated TS types, Zod schemas, React Query hooks | Already the repo contract pattern. Keep it. It prevents backend/frontend drift. |

### Supporting Libraries
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| `spring-boot-starter-security` | Boot-managed | Add login/logout, route protection, password hashing, auth context | Add now for secretary/admin auth. |
| `spring-security-test` | Match Boot line | MockMvc auth/CSRF tests | Add with auth work; required for reliable login/logout and authorization tests. |
| `spring-session-jdbc` | 4.0.x **optional later** | Persist sessions in PostgreSQL | Use only if you need multi-instance deployment or session survival across restarts. Skip for first single-instance rollout. |
| `@tanstack/react-query` | 5.x | Server state and invalidation | Already used; keep as the source of truth for API-backed state. |
| `axios` | 1.x | HTTP transport | Already used; keep it, but enable `withCredentials` for cookie auth. |
| `react-hook-form` + `zod` | 7.x + 4.x | Forms and validation | Already adopted; use for login, charge, payment, and finance filters/forms. |
| `recharts` | 3.x | Dashboard charts | Already present; sufficient for KPI cards and simple receivables charts. |
| `sonner` | 2.x | Toast feedback | Already present; use for auth failures, payment registration, and CRUD success states. |

## Prescriptive Implementation Guidance

### 1. Authentication: use server-side session auth, not JWT

**Recommendation:**
- Add `spring-boot-starter-security`
- Implement an `AuthUser`/`UserAccount` table in PostgreSQL
- Authenticate with username/password through Spring Security
- Persist authentication in `HttpSession`
- Return/session-bind the authenticated principal through cookie-based auth
- Keep only two roles for v1: `ADMIN` and `SECRETARIA` (or reuse `ADMIN` + one new staff role)

**Why this is the right fit here:**
- The app is an internal school operations system, not a public multi-client API platform.
- Spring Security explicitly recommends exchanging long-term credentials for short-term credentials like a **session**.
- Session auth avoids DIY token refresh, localStorage risk, logout invalidation problems, and unnecessary auth plumbing in a brownfield SPA.

**Backend pattern:**
- `SecurityFilterChain`
- `DaoAuthenticationProvider`
- `UserDetailsService` backed by your user table
- `PasswordEncoderFactories.createDelegatingPasswordEncoder()`
- Permit `/login`, `/logout`, `/csrf`, `/v3/api-docs`, Swagger in dev; secure business endpoints
- Use `401` for unauthenticated, `403` for unauthorized, and keep `ProblemDetail`/stable `code` values

**Frontend pattern:**
- Add an auth bootstrap call (`GET /me` or `GET /auth/session`) on app startup
- Add route guards in React Router around existing layout/routes
- Configure Axios with `withCredentials: true`
- Keep auth state minimal: current user, auth status, logout action
- Store no access token in localStorage/sessionStorage

### 2. CSRF: keep it enabled for the SPA

For a browser-based SPA with cookie/session auth, **do not disable CSRF**.

**Recommendation:**
- Use Spring Security CSRF support for SPA integration
- Expose `/csrf` or use the SPA-oriented CSRF setup from Spring Security
- Fetch a CSRF token on app startup and again after login/logout if needed
- Send it in Axios headers for unsafe methods

**Repo-specific note:** current CORS config allows origin and methods but does **not** enable credentials. Session auth will require updating CORS to allow credentials and tightening allowed origins via config.

### 3. Passwords: use adaptive hashing, not plaintext and not custom crypto

**Recommendation:** use Spring Security’s `DelegatingPasswordEncoder` with bcrypt-backed defaults.

**Why:**
- Official Spring Security guidance still centers on adaptive one-way functions and short-term credentials.
- It gives safe defaults and future migration flexibility.

**Do not use:**
- plaintext passwords
- custom hash utilities
- raw SHA-256 for password storage
- `User.withDefaultPasswordEncoder()` in production

### 4. Finance: model it as lightweight accounts receivable

Do **not** build a billing platform. Build **internal receivables tracking**.

**Recommended domain model:**
- `charge` / `financial_entry_receivable`
  - student or responsible party reference
  - competence/month or due-period
  - description/category
  - original amount
  - due date
  - status (`PENDING`, `PARTIAL`, `PAID`, `OVERDUE`, `CANCELLED`)
- `payment`
  - charge reference
  - paid amount
  - paid at
  - method (`PIX`, `DINHEIRO`, `TRANSFERENCIA`, `CARTAO`, etc.)
  - note/reference
- optional later: `discount`/`adjustment`

**Money handling:**
- Java: `BigDecimal`
- PostgreSQL: `numeric(12,2)` or `numeric(14,2)`
- Never use `double`, `float`, or PostgreSQL `money`

**Why:** PostgreSQL official docs recommend `numeric` for exact monetary amounts; floating-point types are inexact.

### 5. Dashboard: keep one summary endpoint, expand its aggregates

**Recommendation:** keep the existing dashboard area and evolve it, not replace it.

Add aggregate queries for:
- overdue total amount
- amount received this month
- open charges count
- overdue students/responsible parties count
- upcoming events count
- recent payments list

**Pattern:**
- one summary endpoint for top-level cards/charts
- separate paged list endpoints for detailed finance screens
- keep TanStack Query for caching/invalidation

### 6. Frontend data layer: stay with React Query + generated hooks

**Recommendation:** continue using Kubb-generated React Query hooks and Zod schemas.

**Repo-specific fixes to make with auth work:**
- Remove hardcoded Kubb `baseURL` once auth integration is done
- Prefer a single shared Axios client configured for credentials and CSRF
- Keep business query orchestration in page/feature containers, not generic components

**Do not introduce:**
- Redux for server state
- Zustand for auth/session unless a tiny local UI state need appears
- hand-written duplicate API types where Kubb already generates them

### 7. Authorization: keep it simple

For this milestone, use coarse-grained authorization only:
- `ADMIN`
- `SECRETARIA`

If useful, add read-only staff roles later. Do **not** build a permissions matrix yet.

**Pattern:**
- endpoint-level checks now
- optional service-level `@PreAuthorize` for finance/auth-sensitive operations
- hide forbidden navigation items in the SPA, but rely on backend enforcement as source of truth

### 8. Session storage scaling decision

**Initial recommendation:** use default servlet session storage first.

**Upgrade path:** add `spring-session-jdbc` only when one of these becomes true:
- multiple backend instances
- load balancing without sticky sessions
- requirement to preserve sessions across restarts

That keeps v1 simpler while leaving a standard path for later scaling.

## Alternatives Considered

| Category | Recommended | Alternative | Why Not |
|----------|-------------|-------------|---------|
| Auth transport | Session cookie | JWT access/refresh tokens | Adds avoidable complexity for an internal SPA, especially logout, refresh, storage, and CSRF/XSS tradeoffs. |
| Auth platform | Spring Security in-app | External IdP/OAuth server now | Too heavy for v1; no current requirement for SSO, multi-tenant identity, or third-party clients. |
| Session persistence | Default servlet session | Spring Session JDBC immediately | Useful later, but unnecessary complexity for first rollout if backend is single-instance. |
| Frontend state | TanStack Query + local auth context | Redux/Zustand global store | Overkill for mostly server-backed state. Query already solves cache/invalidation needs. |
| Money type | `BigDecimal` + Postgres `numeric` | `double`/`float`/Postgres `money` | Incorrect or awkward for exact financial tracking. |
| Architecture | Extend monolith | Microservices split | No business need; would slow delivery and complicate auth, reporting, and transactions. |
| Frontend framework | Keep React SPA + Vite | Next.js / full frontend rewrite | No fit advantage for this internal dashboard-style app; high migration cost. |

## Installation

```bash
# Backend - add security for auth
./mvnw dependency:tree

# Add to pom.xml
# - org.springframework.boot:spring-boot-starter-security
# - org.springframework.security:spring-security-test (test scope)
# Optional later for clustered sessions:
# - org.springframework.session:spring-session-jdbc

# Frontend - keep existing stack, add only if needed
npm install

# Optional dev tooling if you want frontend auth tests later
# npm install -D vitest @testing-library/react @testing-library/user-event jsdom
```

## What Not To Use

- **No full rewrite** of the monorepo
- **No JWT/localStorage auth** for v1
- **No Spring Authorization Server / Keycloak rollout** unless SSO becomes a real requirement
- **No Redux migration**
- **No financial over-modeling** like invoicing engine, ledger, tax module, or payment gateway integration yet
- **No floating-point money fields**
- **No manual edits in `client/src/kubb/`**

## Sources

- Repo evidence: `package.json`, `client/package.json`, `client/kubb.config.ts`, `server/api-aprimorar/pom.xml`, `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java`
- Spring Security username/password authentication: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
- Spring Security password storage: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
- Spring Security CSRF for servlet/SPA apps: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
- Spring Session JDBC guide: https://docs.spring.io/spring-session/reference/guides/java-jdbc.html
- TanStack Query React overview: https://tanstack.com/query/latest/docs/framework/react/overview
- TanStack Query invalidation guidance via Context7 (`/tanstack/query`)
- Kubb React Query/OpenAPI/Zod docs via Context7 (`/kubb-labs/kubb`)
- React Router 7 docs: https://reactrouter.com/home
- PostgreSQL numeric types: https://www.postgresql.org/docs/current/datatype-numeric.html
