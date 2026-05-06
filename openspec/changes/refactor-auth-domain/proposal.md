## Why

The current authentication domain (`server/src/main/java/com/aprimorar/api/domain/auth/`) suffers from a blurring of responsibilities. `AuthService` handles both Spring Security framework requirements and domain business logic, while also containing redundant login verification code. Furthermore, `AuthController` directly manipulates raw Servlet API objects (`HttpServletRequest`, `HttpSession`), making it harder to read and test. This refactoring will align the codebase with standard Spring Boot architectural best practices, improving maintainability and testability.

## What Changes

- Extract the Spring Security `UserDetailsService` implementation from `AuthService` into a dedicated `JpaUserDetailsService` class.
- Remove the redundant, manual `login` method from `AuthService` to rely entirely on Spring Security's `AuthenticationManager`.
- Refactor `AuthController` to use `SecurityContextRepository` for saving the session context, removing direct dependencies on `HttpServletRequest` for session management.
- Update `SecurityConfig` to delegate access denied and authentication entry point handling to the global `@ControllerAdvice` via a `HandlerExceptionResolver`.

## Capabilities

### New Capabilities

- `auth-domain-refactor`: Internal refactoring of the authentication flow to separate framework concerns from domain business logic. (Note: No behavioral requirements are changing, but we will document the structural expectations as a spec for completeness).

### Modified Capabilities


## Impact

- **Backend**: `server/src/main/java/com/aprimorar/api/domain/auth/AuthController.java`
- **Backend**: `server/src/main/java/com/aprimorar/api/domain/auth/AuthService.java`
- **Backend**: `server/src/main/java/com/aprimorar/api/config/SecurityConfig.java`
- **Backend**: New file `server/src/main/java/com/aprimorar/api/domain/auth/JpaUserDetailsService.java`
- **Tests**: Any tests relying on the manual `login` method in `AuthService` or raw `HttpServletRequest` mocks in `AuthControllerTest`.