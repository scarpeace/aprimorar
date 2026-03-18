# Project Refactoring Guide

This guide is tailored for `springboot-best-practices` using the rules in `../AGENTS.md`.

## 1) Refactoring Goals

- Keep behavior stable while improving maintainability, readability, and testability.
- Prefer incremental refactors over large rewrites.
- Preserve architecture boundaries: `controller` -> `service` -> `repository`.
- Keep Java 8 and Spring Boot 2.7.5 compatibility.

## 2) Non-Negotiable Constraints

- Do not expose entities directly from controllers; use DTOs.
- Keep mapping centralized (use `ValueMapper`).
- Keep exception-to-HTTP translation in `@RestControllerAdvice`.
- Use `Optional` + `orElseThrow` for not-found repository lookups.
- No wildcard imports, no dead code, no `System.out.println`.
- Do not hardcode secrets; keep config in `application.yml` and externalized sources.

## 3) Recommended Refactoring Sequence

### Step A - Baseline and Safety Net

1. Run full tests:

```bash
./mvnw test
```

2. Identify fragile areas (large methods, duplicated logic, weakly named classes/methods).
3. Add focused regression tests before changing risky code paths.

### Step B - API Boundary Cleanup

1. Ensure all controller request payloads use DTOs with Bean Validation (`@Valid`, `@NotBlank`, `@Min`, `@Max`).
2. Ensure all controller responses return response DTOs, not entities.
3. Move business decisions out of controllers into services.

### Step C - Service Layer Consolidation

1. Keep service methods intention-revealing and cohesive.
2. Extract repeated business rules into private methods or dedicated service components.
3. Replace null-return patterns with explicit exceptions or empty collections.

### Step D - Repository and Persistence Cleanup

1. Keep repositories persistence-only (no business logic).
2. Standardize not-found handling via `Optional` and domain exceptions.
3. Remove query duplication where possible.

### Step E - Error Handling and Logging Standardization

1. Throw domain-specific exceptions (`*Exception`) for business failures.
2. Centralize API error responses in global advice.
3. Use SLF4J parameterized logs, and choose levels consistently:
   - `info`: business milestones
   - `debug`: detailed diagnostics
   - `error`: failures with context

### Step F - Naming, Types, and Formatting Pass

1. Improve naming clarity (classes in PascalCase, methods/fields in camelCase).
2. Replace raw/loose types with explicit generics and concrete types.
3. Reformat changed files with consistent 4-space indentation and brace style.

## 4) Refactoring Checklist (Per Pull Request)

- [ ] Scope is small and behavior-preserving.
- [ ] Layering boundaries respected.
- [ ] DTO validation present and clear.
- [ ] Mapping remains centralized.
- [ ] Exceptions are meaningful and handled globally.
- [ ] Logging is parameterized and safe (no secrets).
- [ ] Imports are explicit and clean.
- [ ] Tests added/updated for changed behavior.
- [ ] `./mvnw test` passes.

## 5) Suggested Work Units

Use small, reviewable PRs such as:

1. Controller validation and DTO boundary fixes.
2. Service method extraction and naming cleanup.
3. Optional/not-found consistency in repository usage.
4. Global error response alignment.
5. Final style/type/import cleanup.

## 6) Verification Commands

```bash
./mvnw clean compile
./mvnw test
./mvnw clean verify
```

## 7) Anti-Patterns to Avoid

- Big-bang rewrites across many layers at once.
- Moving fast without tests on affected behavior.
- Returning entities directly from APIs.
- Silent exception swallowing.
- Refactoring and feature work mixed in one large change.

---

Following this guide keeps refactoring safe, incremental, and aligned with existing project standards.
