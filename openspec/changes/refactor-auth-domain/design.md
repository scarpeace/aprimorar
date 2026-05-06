## Context

The backend authentication domain currently utilizes stateful HTTP sessions. While the security configuration is sound, the implementation in `AuthService` and `AuthController` mixes framework-specific integration (Spring Security) with domain business logic (tracking login times, returning DTOs). Additionally, the controller relies on raw `HttpServletRequest` manipulation to manage the session, which obscures the intent and hinders testability.

## Goals / Non-Goals

**Goals:**
- Decouple Spring Security's `UserDetailsService` from the core `AuthService`.
- Remove redundant password verification logic from `AuthService` that is already handled by `AuthenticationManager`.
- Abstract session manipulation in `AuthController` using Spring Security's `SecurityContextRepository`.
- Centralize authentication and authorization error handling by delegating to the global `@ControllerAdvice`.

**Non-Goals:**
- Migrating away from stateful sessions (e.g., to JWT).
- Changing the database schema for users or employees.
- Altering the API contracts (requests/responses) for the `/v1/auth/login` and `/v1/auth/me` endpoints.

## Decisions

- **Decision 1: Extract `JpaUserDetailsService`:** Create a new class specifically implementing `UserDetailsService`. This class will solely be responsible for loading the user from the repository and returning a Spring Security `UserDetails` object. This adheres to the Single Responsibility Principle.
- **Decision 2: Eliminate manual `login` method in `AuthService`:** Since `AuthenticationManager` automatically delegates to the `UserDetailsService` to check credentials, the explicit `passwordEncoder.matches(...)` logic in `AuthService.login` is redundant and error-prone. The controller will solely rely on `AuthenticationManager.authenticate(...)` and then call `AuthService.registerAuthenticatedSession(username)` to handle domain-specific side effects (like updating `lastLoginAt`).
- **Decision 3: Inject `SecurityContextRepository`:** Instead of using `httpServletRequest.getSession(true).setAttribute(...)`, we will use an injected `HttpSessionSecurityContextRepository` in `AuthController`. This makes the controller easier to unit test without relying on heavy MockMvc or raw servlet mocks.
- **Decision 4: Use `HandlerExceptionResolver` for Security Errors:** In `SecurityConfig`, the `AuthenticationEntryPoint` and `AccessDeniedHandler` currently hardcode JSON responses. By injecting `HandlerExceptionResolver`, we can pass these exceptions to the application's standard `@ControllerAdvice`, ensuring a single source of truth for error payload structures.

## Risks / Trade-offs

- **[Risk] Breaking Existing Tests:** The current `AuthControllerTest` or `AuthServiceTest` likely mock the manual login logic or raw `HttpServletRequest`. Refactoring these components will break those tests. → **Mitigation:** Update the tests to reflect the new architecture. `AuthControllerTest` will mock `SecurityContextRepository`, and `AuthServiceTest` will no longer need to test password matching.
- **[Risk] Circular Dependencies:** Care must be taken not to introduce circular dependencies when extracting the `UserDetailsService`, especially if it needs `AuthService` (it shouldn't). → **Mitigation:** Ensure `JpaUserDetailsService` only depends on `UserRepository`.