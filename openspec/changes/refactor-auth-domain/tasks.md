## 1. Extract UserDetailsService

- [ ] 1.1 Create a new class `JpaUserDetailsService.java` in `com.aprimorar.api.domain.auth` that implements `UserDetailsService`.
- [ ] 1.2 Inject `UserRepository` into `JpaUserDetailsService` and move the `loadUserByUsername` implementation from `AuthService` to this new class.
- [ ] 1.3 Remove `implements UserDetailsService` and the `loadUserByUsername` method from `AuthService`.
- [ ] 1.4 Update `SecurityConfig.java` to inject and use `JpaUserDetailsService` in the `AuthenticationProvider` bean instead of `AuthService`.

## 2. Refactor Login Logic

- [ ] 2.1 In `AuthService.java`, remove the manual `login(String identifier, String password)` method entirely as it's redundant.
- [ ] 2.2 In `AuthController.java`, update the `login` endpoint to call `authService.registerAuthenticatedSession(authentication.getName())` after a successful `authenticationManager.authenticate(...)`.

## 3. Abstract Session Management

- [ ] 3.1 In `AuthController.java`, inject `SecurityContextRepository` (defaulting to `HttpSessionSecurityContextRepository`).
- [ ] 3.2 Refactor the `login` endpoint to use `securityContextRepository.saveContext(context, request, response)` instead of manually manipulating the `HttpServletRequest` session. Ensure the method signature is updated to receive `HttpServletResponse`.

## 4. Centralize Security Exception Handling

- [ ] 4.1 In `SecurityConfig.java`, inject `@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver`.
- [ ] 4.2 Refactor the `authenticationEntryPoint` bean to use `exceptionResolver.resolveException(request, response, null, authException)` instead of writing JSON manually.
- [ ] 4.3 Refactor the `accessDeniedHandler` bean to use `exceptionResolver.resolveException(request, response, null, accessDeniedException)` instead of writing JSON manually.
- [ ] 4.4 Remove the private `writeProblemResponse` method and `ObjectMapper` dependency from `SecurityConfig` if no longer needed.

## 5. Testing and Verification

- [ ] 5.1 Run `npm run lint` and `./mvnw test` to identify any failing tests due to the refactoring.
- [ ] 5.2 Fix `AuthControllerTest` and `AuthServiceTest` (or any related tests) by updating mocked dependencies (e.g., mocking `SecurityContextRepository` in controller tests and removing password matching assertions in service tests).
- [ ] 5.3 Verify the application compiles and all tests pass with `./mvnw clean verify`.