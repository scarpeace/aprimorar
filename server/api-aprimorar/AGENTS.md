# Backend AGENTS.md

Spring Boot + Java 21 guidance for coding agents working in `server/api-aprimorar/`.

## Scope
- This file applies to the backend API in `server/api-aprimorar/`
- Read the root `AGENTS.md` first for repo-wide workflow, frontend contract rules, and verification expectations

## Build, Run, And Test Commands
Run from `server/api-aprimorar/`:

```bash
docker compose up -d db
docker compose down
./mvnw spring-boot:run
./mvnw test
./mvnw verify
./mvnw package
./mvnw -Pgenerate-openapi generate-resources
```

### Targeted tests
```bash
./mvnw -Dtest=StudentServiceTest test
./mvnw -Dtest=StudentServiceTest#shouldReturnPagedStudents test
./mvnw -Dtest=EventServiceTest test
./mvnw -Dtest=GlobalExceptionHandlerTest test
./mvnw -Dtest='*ServiceTest' test
./mvnw -Dtest=ApiAprimorarApplicationTests#contextLoads test
```

- Use focused test runs while iterating, then broaden verification before finishing
- Regenerate OpenAPI whenever request/response contracts or endpoint signatures change

## Package Structure
- Domain modules generally live under `com.aprimorar.api.domain.<entity>/`
- Common package patterns are `dto/`, `repository/`, `web/`, and `exception/`
- Shared infra lives outside domain packages, such as config, exception handling, and base entities

## Layer Responsibilities
- Controllers handle HTTP concerns only: routing, status codes, request validation, and response bodies
- Services own business rules and transaction boundaries
- Repositories define persistence operations and query behavior
- Mappers convert DTOs to and from entities
- Validation rules that are not simple Bean Validation belong in service/rule classes, not controllers

## Java And Spring Conventions
- Prefer constructor injection
- DTOs are usually Java records with Bean Validation annotations
- Use `@Transactional(readOnly = true)` for reads and `@Transactional` for writes
- Keep helper methods like `findXOrThrow` and `resolveXOrThrow` private inside services when they are local implementation details
- Favor explicit names over clever abstractions
- Keep logs informative and business-focused; avoid noisy debug-style logging in normal code paths

## Entities And Persistence
- Entities extend or align with shared base entity conventions such as UUID IDs and audit timestamps
- Keep entity relationships explicit and fetch plans intentional
- Use `@EntityGraph` where needed to avoid lazy loading issues in read paths
- Keep repository queries explicit, intention-revealing, and close to the domain language
- Avoid leaking JPA entities outside the service/mapper layer

## DTOs, Mappers, And API Contracts
- DTO field types must stay aligned with the OpenAPI contract consumed by Kubb on the frontend
- If persistence types differ from API types, convert in the mapper rather than bending the repository or frontend types
- For dates and times, be explicit about timezone strategy at the API boundary
- If request and response models use a different temporal type from the entity, centralize that conversion in the mapper
- When updating DTOs used by the SPA, also verify the regenerated client still compiles and parses responses correctly

## Controllers And HTTP Behavior
- Controllers should return `ResponseEntity` only when status/header control is useful; otherwise keep methods simple and consistent with local style
- Keep request parsing, path params, and pageable params in controllers; business validation belongs in services
- Prefer stable API behavior and do not rename fields or endpoints casually
- Follow consistent status code usage:
  - `200` for reads and updates
  - `201` for creates when a body is returned
  - `204` for delete/archive actions without content
  - `400` for invalid input
  - `404` for missing resources
  - `409` for conflicts

## Validation And Error Handling
- Validation messages and business-rule errors must be written in Portuguese
- Throw domain-specific exceptions instead of returning null or generic runtime failures
- Centralize response shaping in `GlobalExceptionHandler`
- Prefer `ProblemDetail` responses with stable custom `code` values for frontend consumption
- When handling validation failures, keep messages actionable and user-focused

## Query Patterns
- Use repository-derived query methods for straightforward lookups
- Use JPQL or specifications when filters become dynamic or multi-field
- Keep search/filter logic readable; extract specification helpers when it clarifies intent
- Be careful with archived/inactive entity rules and enforce them in services when they affect business behavior

## Testing Expectations
- Tests use JUnit 5, AssertJ, and Mockito
- Prefer deterministic fixtures with fixed UUIDs and timestamps
- Use `@Nested` classes when they improve organization between query and command scenarios
- Keep factory methods in tests small and reusable
- Assert both returned DTOs and important side effects such as repository interactions or conflict checks
- If a mapper contract changes, update impacted service tests accordingly

## Common Change Patterns

### New persistent field
- Add Flyway migration
- Update entity
- Update request/response DTOs
- Update mapper
- Update service rules and repository queries if needed
- Update tests
- Regenerate OpenAPI and frontend client if the field is exposed to the SPA

### New endpoint or contract change
- Update controller and docs interface together
- Update DTOs and service logic
- Add or update tests
- Regenerate OpenAPI
- Coordinate with frontend consumers in the same task when the SPA depends on it

### Temporal field mismatch with frontend
- Check DTO type, Jackson serialization, OpenAPI output, and generated Kubb schema together
- Prefer fixing DTO/mapper contracts over hand-editing generated frontend schemas

## Quick Checklist
- Are messages and validation errors in Portuguese?
- Is business logic in the service rather than the controller?
- Did you update tests for changed rules or DTO contracts?
- If the SPA depends on this contract, did you regenerate OpenAPI and Kubb?
