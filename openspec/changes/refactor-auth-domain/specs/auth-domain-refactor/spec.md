## ADDED Requirements

### Requirement: Architecture Separation for Authentication
The backend authentication implementation SHALL separate Spring Security framework concerns from domain business logic to adhere to Spring Boot best practices.

#### Scenario: Code Structure Validation
- **WHEN** developers review the authentication domain package (`com.aprimorar.api.domain.auth`)
- **THEN** they SHALL find a dedicated implementation of `UserDetailsService` (e.g., `JpaUserDetailsService`) responsible solely for loading user details for the framework.
- **AND THEN** they SHALL find that `AuthService` only handles domain-specific business operations, such as tracking login timestamps and mapping entities to DTOs.

### Requirement: Centralized Security Error Handling
Authentication and authorization exceptions occurring within the Spring Security filter chain SHALL be delegated to the application's global exception handler.

#### Scenario: Triggering an Authentication Error
- **WHEN** an unauthenticated user attempts to access a protected endpoint
- **THEN** the security configuration SHALL delegate the `AuthenticationException` to the `HandlerExceptionResolver`.
- **AND THEN** the user SHALL receive a standard `ProblemResponseDTO` payload formatted by the application's global `@ControllerAdvice`.