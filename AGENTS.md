# Aprimorar Agent Notes

## Layout
- Real app roots are `server/` and `client/`; the root `package.json` is only helper scripts.
- Root `npm run start:backend` and `npm run dev` are stale because they `cd server/api-aprimorar`. Run backend commands from `server/` directly.
- There is currently no `openspec/` directory. If one appears, read relevant `openspec/specs/` files before changing behavior.

## Backend
- Stack: Java 21, Spring Boot 3.5.7, Spring Modulith, PostgreSQL, Flyway.
- Start the local database from `server/` with `docker compose up -d db`.
- Main backend commands from `server/`: `./mvnw spring-boot:run`, `./mvnw clean compile`, `./mvnw test`, single test `./mvnw test -Dtest=ClassName`.
- Use JDK 21 for verification; the project targets release 21 and Modulith/JaCoCo checks are not reliable on newer JDKs.
- `application.yml` activates the `dev` profile by default. `application-dev.yml` expects Postgres at `localhost:5432/aprimorar` with `myuser` / `mypassword`.
- `application-dev.yml` has `spring.sql.init.mode: never`; `data.sql` does not seed dev startup unless that is temporarily changed to `always`.
- Modules are declared in top-level `package-info.java`; public contracts belong under `...api`, implementations under `...internal`.
- If you change module boundaries or cross-module dependencies, run `./mvnw test -Dtest=ModuleVerificationTest`; it also regenerates `server/src/main/resources/docs/`.
- Student/employee summary counts exclude ghosts via `isNotGhost()`; active counts also apply `isNotArchived()` while total counts include archived non-ghost records.

## Frontend
- Stack: React 19, Vite 7, TypeScript, Tailwind 4, DaisyUI, TanStack Query.
- Main frontend commands from `client/`: `npm run dev`, `npm run build`, `npm run lint`, `npm run sync`.
- There is no frontend test script in `client/package.json`; normal verification is `npm run lint` and `npm run build`.
- `npm run sync` runs Kubb against `http://localhost:8080/v3/api-docs`, so the backend must be running first.
- Generated API code lives in `client/src/kubb/`, is cleaned by Kubb, and is ignored by ESLint. Do not hand-edit it or manually create API hooks/types.
- Imports use the Vite alias `@` for `client/src`.

## Frontend Behavior Gotchas
- `usePageDateFilter()` is page-local state, not persisted storage. `PageDateFilterWidget` is used on `FinancesPage`, `StudentDetailsPage`, and `EmployeeDetailPage`, not the list pages.
- Student and employee list pages show registration count KPIs only; financial KPIs belong on `FinancesPage`.
- `ToggleExpensePaymentButton` owns its toggle mutation through `useExpenseMutations()`. Callers pass only `expenseId`, `isPaid`, and optional `iconOnly`.

## Sonar
- Root Sonar scripts expect SonarQube services from `server/docker-compose.yml` and `SONAR_TOKEN` in root `.env.local`.
- Start Sonar locally from `server/` with `docker compose up -d db sonar_db sonarqube`; the dashboard is `http://localhost:9000`.
