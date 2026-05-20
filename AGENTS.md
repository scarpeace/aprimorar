# Aprimorar Agent Notes

## Layout
- Real app roots are `server/` and `client/`; the root `package.json` only wraps helper scripts.
- Root `npm run start:backend` and root `npm run dev` are stale: they still `cd server/api-aprimorar`, which does not exist. Run backend commands from `server/` and frontend commands from `client/` directly.

## Backend
- Backend stack: Java 21, Spring Boot 3.5.7, Spring Modulith, PostgreSQL, Flyway.
- Use JDK 21 for verification. `server/pom.xml` compiles with `release 21`.
- Start local infra from `server/` with `docker compose up -d db`.
- Default backend commands from `server/`: `./mvnw spring-boot:run`, `./mvnw clean compile`, `./mvnw test`, single test `./mvnw test -Dtest=ClassName`.
- `application.yml` enables the `dev` profile by default. `application-dev.yml` points to Postgres at `localhost:5432/aprimorar` with `myuser` / `mypassword` and has `spring.docker.compose.enabled: false`, so Boot will not start containers for you.
- Dev startup does not run `data.sql`: `server/src/main/resources/application-dev.yml` sets `spring.sql.init.mode: never`.
- Spring Modulith boundaries are enforced through top-level `package-info.java` files under `server/src/main/java/aprimorar/*/`. Public cross-module contracts belong under `...api`.
- If you change module boundaries or cross-module dependencies, run `./mvnw test -Dtest=ModuleVerificationTest`. That test also regenerates `server/src/main/resources/docs/`.
- Current modules are `appointment`, `dashboard`, `expense`, `registration`, and `shared`; read their `package-info.java` before adding cross-module wiring.
- Student/employee summary counts intentionally exclude ghosts; active counts also exclude archived records, while total counts still include archived non-ghost records.

## Frontend
- Frontend stack: React 19, Vite 7, TypeScript, Tailwind 4, DaisyUI, TanStack Query.
- Default frontend commands from `client/`: `npm run dev`, `npm run lint`, `npm run build`, `npm run sync`.
- There is no separate frontend test or typecheck script. `npm run build` runs `tsc -b && vite build`, so use `lint` plus `build` as the normal verification pair.
- `npm run sync` runs Kubb against `http://localhost:8080/v3/api-docs`; the backend must already be running.
- Kubb writes generated API clients, hooks, schemas, and types into `client/src/kubb/` with `clean: true`. Do not hand-edit that directory.
- ESLint ignores `client/src/kubb/**`, and the Vite alias `@` resolves to `client/src`.
- `usePageDateFilter()` is local component state only. It is currently used by `FinancesPage`, `StudentDetailsPage`, and `EmployeeDetailPage`, so date filters there are not shared or persisted.
- `ToggleExpensePaymentButton` already owns its mutation via `useExpenseMutations()`; callers only supply `expenseId`, `isPaid`, and optional `iconOnly`.

## Sonar
- Root Sonar scripts load `SONAR_TOKEN` from root `.env.local`.
- Start Sonar locally from `server/` with `docker compose up -d db sonar_db sonarqube`.
- `npm run sonar:backend` runs `./mvnw clean verify ...sonar`; `npm run sonar:frontend` runs the scanner in Docker on `--network=host`.
- `npm run sonar:issues` and `npm run sonar:issues:frontend` depend on `jq`.
