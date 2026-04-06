# AGENTS.md

## Scope
- This file applies to the whole repo.
- Also read `client/AGENTS.md` for SPA work and `server/api-aprimorar/AGENTS.md` for backend work.

## Trust These Sources
- Prefer repo config over prose docs. `README.md` still mentions a root `.env`, but the verified local dev path is the checked-in Postgres compose file plus `application-dev.yml`.

## Repo Shape
- `client/`: React 19 + TypeScript + Vite SPA.
- `server/api-aprimorar/`: Spring Boot 3.5 / Java 21 API.
- Main product areas visible in both apps: `dashboard`, `students`, `parents`, `employees`, `events`.

## Commands That Matter
- Root dev startup: `npm run dev`
- Backend only from root: `npm run start:backend`
- Frontend only from root without backend wait/sync: `npm run dev:frontend`
- Frontend startup that waits for backend docs and regenerates client first: `npm run start:frontend`

## Contract / Codegen Workflow
- Do not edit `client/src/kubb/` manually. `client/kubb.config.ts` writes there with `clean: true`, so regeneration replaces it.
- Both codegen steps depend on a live backend at `http://localhost:8080`:
  - backend OpenAPI generation: `./mvnw -Pgenerate-openapi generate-resources`
  - frontend client generation: `npm run sync` from `client/`
- If backend DTOs or endpoint signatures change, run them in this order: backend running -> generate OpenAPI -> `npm run sync` from `client/` -> frontend build.
- Root `npm run start:frontend` already waits on `/v3/api-docs`, runs `npm run sync`, then starts Vite.

## Backend Local Assumptions
- Default Spring profile is `dev`.
- Local DB settings are hardcoded for dev: PostgreSQL at `localhost:5432`, database `aprimorar`, user `myuser`, password `mypassword`.
- Start the required DB from `server/api-aprimorar/` with `docker compose up -d db`.
- Flyway is enabled and Hibernate runs with `ddl-auto: validate`; schema changes need a migration under `server/api-aprimorar/src/main/resources/db/migration/`.

## Cross-Stack Conventions
- Keep user-facing strings and backend validation/business messages in Portuguese.
- Backend pagination is a custom `PageDTO<T>` wrapper, not raw Spring `Page` JSON. Preserve that shape when changing contracts.
- Frontend uses the `@/*` alias for `client/src/*`.

## Verification
- Frontend: `npm run lint` and `npm run build` from `client/`.
- Backend: prefer focused Maven tests while iterating, then broaden as needed.
- Cross-stack contract changes usually need backend tests plus frontend build after Kubb sync.
