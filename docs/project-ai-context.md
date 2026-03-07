# Project AI Context

## One-Line Summary

Aprimorar is an internal school-operations system focused on student/event management and monthly financial visibility.

## Product Intent

- Replace spreadsheets and manual coordination.
- Optimize for MVP operations and finance, not pedagogy.
- Keep the codebase approachable for a beginner maintainer.
- Prefer small, obvious abstractions over generic frameworks.

## Current Tech Stack

- Frontend: React, TypeScript, Vite, Tailwind, React Router, Zustand, Axios
- Backend: Java 21, Spring Boot, Spring Data JPA, MapStruct, SpringDoc
- Database: PostgreSQL 15 with Flyway

## Domain Model

- `Student`
  - has embedded address
  - may reference one parent/guardian
  - uses `archivedAt` / `lastReactivatedAt`
- `Parent`
  - can be linked to many students
- `Employee`
  - used for teachers/staff
- `Event`
  - belongs to one student and one employee in the current model
  - carries `price`, `payment`, and required `content`

## MVP Truths

- Monthly financial reporting is core.
- Yearly reporting is later.
- Dashboard should show revenue, cost, profit, active counts, and upcoming events.
- Revenue comes from `price`.
- Cost comes from `payment`.
- Profit is `price - payment`.
- Dashboard windows use `America/Sao_Paulo` and event start date.

## Current State

- Backend CRUD baseline exists for students, employees, and events.
- Parent support exists mainly for student flows and active-parent selection.
- Frontend management UI exists for students, employees, and events.
- Frontend refactor conventions already exist and should be preserved.

## Important Frontend Conventions

- Feature structure: `pages/`, `components/`, `utils/`
- Keep page CSS colocated in CSS modules.
- Keep data fetching near pages unless extraction clearly simplifies things.
- Reuse small primitives, not config-driven UI systems.

## Known Important Gaps

### Backend

- Add `GET /v1/dashboard/summary`
- Return formatted event date/time fields and `profit`
- Review/fix possible N+1 event listing queries
- Standardize query param validation (especially page size)
- Revisit uniqueness constraints on `name` and maybe `contact`
- Add safer global handling for parent/argument-related errors if still missing

### Frontend

- Finish dashboard integration against backend summary endpoint once available
- Finish inline parent management in student create/edit flow
- Finish remaining PT-BR/UTF-8 error and empty-state sweep if not yet closed

## Planning Direction

- Keep epics aligned to implementation order.
- Split large work into tasks that can be implemented in one pass.
- Separate calendar sync from calendar UI.
- Separate reporting/payment tracking from payment gateway integration.
- Treat portals and broader privacy hardening as later than MVP finance/ops priorities.

## Things To Avoid

- Reintroducing stale `sessionType` planning
- Referring to the frontend as merely "planned"
- Large speculative abstractions
- Mixing canonical docs with outdated archive material
