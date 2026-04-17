# External Integrations

**Analysis Date:** 2026-04-17

## APIs & External Services

**Internal HTTP integration:**
- Aprimorar backend REST API - primary data source for the SPA
  - SDK/Client: Axios instance in `client/src/lib/shared/api.ts` plus generated React Query hooks configured in `client/kubb.config.ts`
  - Auth: Not detected in current implementation; no auth header wiring was found in `client/src/lib/shared/api.ts` or backend security classes

**Contract/code generation:**
- OpenAPI document served by the backend at `/v3/api-docs`
  - SDK/Client: Springdoc generator in `server/api-aprimorar/pom.xml` and `server/api-aprimorar/src/main/resources/application.yml`
  - Auth: None detected; Kubb reads `http://localhost:8080/v3/api-docs` directly in `client/kubb.config.ts`

**Browser-side libraries:**
- Toast, chart, and date-picker libraries are local UI integrations only (`client/src/App.tsx`, `client/src/features/dashboard/components/PizzaChart.tsx`, `client/src/components/ui/date-time-input.tsx`); no third-party SaaS API calls were detected

**Not detected:**
- No active Stripe, Supabase, AWS, Firebase, Sentry, Redis, Kafka, RabbitMQ, email, SMS, or payment gateway integration was found in tracked source files
- `README.md` mentions JWT and Google Calendar, but current code/config does not show an implemented integration; `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java` contains only a TODO comment for Google Calendar fields

## Data Storage

**Databases:**
- PostgreSQL
  - Connection: configured directly in `server/api-aprimorar/src/main/resources/application-dev.yml`
  - Client: Spring Data JPA + Hibernate via `server/api-aprimorar/pom.xml`

**File Storage:**
- Local filesystem only for source/build artifacts; no external object storage integration detected

**Caching:**
- Client-side request caching via TanStack Query in `client/src/lib/shared/queryClient.ts`
- No Redis or external cache service detected for the backend

## Authentication & Identity

**Auth Provider:**
- Not detected in current codebase
  - Implementation: backend exposes open CORS-configured endpoints in `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java`; no Spring Security or token validation classes were found under `server/api-aprimorar/src/main/java/`

## Monitoring & Observability

**Error Tracking:**
- None detected

**Logs:**
- Spring Boot logging to application logs, with `com.aprimorar` set to `DEBUG` in `server/api-aprimorar/src/main/resources/application-dev.yml`
- Frontend currently logs API/Zod failures to the browser console in `client/src/lib/shared/api.ts`

## CI/CD & Deployment

**Hosting:**
- Not specified by repository configuration

**CI Pipeline:**
- None detected; no GitHub Actions or other CI config was explored in focus-specific files

## Environment Configuration

**Required env vars:**
- `VITE_API_URL` - optional frontend API base URL override in `client/src/lib/shared/api.ts`
- No backend environment variables were detected in `server/api-aprimorar/src/main/resources/` or `server/api-aprimorar/src/main/java/`

**Secrets location:**
- No `.env` files were detected in the repository root, `client/`, or `server/api-aprimorar/`
- Local dev database credentials are configured directly in `server/api-aprimorar/src/main/resources/application-dev.yml`
- `server/api-aprimorar/docker-compose.yml` is present for local infrastructure, but its contents were not quoted here

## Webhooks & Callbacks

**Incoming:**
- None detected

**Outgoing:**
- None detected

---

*Integration audit: 2026-04-17*
