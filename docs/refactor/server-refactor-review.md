# Server Refactor Review

Review date: 2026-03-08

## Status

- Overall status: backend refactor review is complete.
- Review outcome: all identified server-side refactor items in this review were completed.
- Verification baseline: `cd server/api-aprimorar && ./mvnw test`.
- Next focus is no longer backend cleanup in this review; it is cross-stack alignment and future feature work.

## What improved

- API contracts are clearer, especially for student archive behavior and PATCH request models.
- Service behavior is more consistent through shared `Clock` usage and clearer lifecycle rules.
- Persistence mapping is closer to the real schema and uses less misleading uniqueness/lifecycle behavior.
- Validation and mapper code now contain less duplication and lower-value utility noise.
- Test coverage improved in controller and exception-handling areas.
- Logging is more consistent and exposes less unnecessary personal data.

## Completed items

### 1) Student archive API contract mismatch
- Result: added explicit `PATCH /archive` support and kept `DELETE` as an alias.
- Commit: `fix student archive endpoint contract`

### 2) Create DTOs reused by PATCH endpoints
- Result: employee and event PATCH endpoints now use dedicated update DTOs.
- Commit: `refactor employee and event patch contracts`

### 3) Student nested updates were not truly partial
- Result: nested address and parent updates now happen in place when possible.
- Commit: `refactor student nested partial updates`

### 4) Mixed time sources in business timestamps
- Result: shared application `Clock` now drives service and error timestamps.
- Commit: `refactor shared application clock usage`

### 5) Repeated pagination input in controllers
- Result: controllers now share `PageQuery` and cleaner validation responsibilities.
- Commit: `refactor shared pagination request input`
- Follow-up: `cleanup pageable validation responsibilities`

### 6) Overly strict unique constraints
- Result: `name` and `contact` are no longer unique for students, parents, and employees; `cpf` and `email` remain unique.
- Commit: `update database constraints for real-world data`

### 7) Event filter model cleanup
- Result: kept a small `EventFilter` model but simplified the repository back to one readable filtered query.
- Commit: `refactor event filtering model`

### 8) Important test gaps
- Result: added focused controller tests for employees and parents plus dedicated `GlobalExceptionHandler` coverage.
- Commit: `test controller error and coverage gaps`

### 9) Lookup helper names hid lifecycle rules
- Result: helper names now distinguish any record vs active/schedulable record.
- Commit: `rename service lookup helpers for clarity`

### 10) `MapperUtils` low-value noise
- Result: removed unused helper logic and trimmed the class to active responsibilities.
- Commit: `cleanup mapper utility helpers`

### 11) Lifecycle state across core entities
- Result: parent and employee persistence now use timestamp-based archival fields like student.
- Commit: `refactor entity lifecycle rules`
- Follow-up: temporary `/active` aliases were later removed after client alignment.

### 12) JPA mappings aligned with schema
- Result: entity mappings now describe required columns, archive timestamps, embedded address nullability, and event numeric/text fields more explicitly.
- Commit: `align jpa mappings with schema`

### 13) Repeated validation regex rules
- Result: shared validation pattern constants now replace repeated CPF/contact/CEP regex strings.
- Commit: `refactor shared validation patterns`

### 14) API date/time serialization
- Result: response DTOs now use a consistent Brazilian human-readable date/time format.
- Commit: `standardize api date serialization`

### 15) Logging cleanup
- Result: service logs are more action-oriented and avoid unnecessary personal data.
- Commit: `cleanup service logging`

## Outcome

- The backend is in a healthier state for incremental feature work.
- Main contracts are more explicit and less surprising.
- Refactor work stayed lightweight; the codebase did not need a major architectural rewrite.
- The remaining work is product work, auth/security implementation, and client alignment rather than more foundational backend cleanup.

## Guidance from here

- Keep using small, single-concern commits.
- Make public API changes explicit in commit messages and PR notes.
- Add new Flyway migrations instead of rewriting old ones.
- Re-run `./mvnw test` before and after backend changes.
- Treat auth, reporting, and cross-stack alignment as the next meaningful areas.

## Open notes

- `spring.jpa.open-in-view` is still not configured explicitly in `server/api-aprimorar/src/main/resources/application.yml`.
- The codebase is still small enough for targeted refactors without introducing a heavier architecture layer.
