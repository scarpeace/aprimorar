## Context

The project uses Spring Boot with SpringDoc to generate OpenAPI documentation. This documentation is then used by Kubb in the frontend to generate TypeScript types and hooks. Currently, some enums like `BrazilianStates` are missing the necessary annotations for optimal code generation.

## Goals / Non-Goals

**Goals:**
- Add `@Schema` annotation to `BrazilianStates` enum.
- Standardize enum documentation across the backend.
- Ensure the DTOs using `BrazilianStates` are properly recognized by the OpenAPI generator.

**Non-Goals:**
- Changing the actual values or logic of the enum.
- Refactoring the entire frontend to use the generated enum (this will be a follow-up task).

## Decisions

### Decision 1: Use io.swagger.v3.oas.annotations.media.Schema

We will use the `@Schema` annotation from the `io.swagger.v3.oas.annotations` package, as it's the standard for SpringDoc-based OpenAPI generation in this project (as seen in `Duty.java`).

### Decision 2: Add Javadoc

We will add Javadoc to the enum class and its constants to provide additional context that can be picked up by some generators.
