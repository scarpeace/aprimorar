# Aprimorar Agent Notes

## Scope
- This root file is only for repo-wide guidance. Use `server/AGENTS.md` and `client/AGENTS.md` for implementation rules inside each app.

## Real workspace roots
- The actual app roots are `server/` and `client/`.
- Do not trust root `package.json` dev scripts for normal backend/frontend work: `start:backend` still points to `server/api-aprimorar`, which does not match the current layout.

## Verified commands
- Backend: run everything from `server/`.
- Frontend: run everything from `client/`.
- Backend local run: `./mvnw spring-boot:run`
- Backend verify: `./mvnw clean compile` and `./mvnw test`
- Frontend local run: `npm run dev`
- Frontend verify: `npm run lint` and `npm run build`

## Cross-app contract workflow
- `client/src/kubb/` is generated; never edit it manually.
- Backend API contract changes require `npm run sync` in `client/`.
- `npm run sync` pulls from `http://localhost:8080/v3/api-docs`, so the backend must be running first.

## Backend module boundaries
- The backend is a Spring Modulith app. Module boundaries are enforced from each module's `package-info.java`.
- Verified top-level modules today: `auth`, `atendimentos`, `pessoas`, `shared`.
- If you change inter-module dependencies or module boundaries, run `./mvnw test -Dtest=ModuleVerificationTest` in `server/`.
- That test also regenerates Modulith docs into `server/src/main/resources/docs`.

## Local infra and env quirks
- Local database and Sonar services are defined in `server/docker-compose.yml`.
- Minimal app DB startup: `docker compose up -d db` from `server/`.
- Sonar scripts at the repo root expect `SONAR_TOKEN` in `.env.local`.
- Local Sonar stack: `docker compose up -d db sonar_db sonarqube` from `server/`.
