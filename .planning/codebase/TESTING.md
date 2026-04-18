# Testing Patterns

**Analysis Date:** 2026-04-17

## Test Framework

**Runner:**
- Backend tests run on JUnit 5 via `spring-boot-starter-test` in `server/api-aprimorar/pom.xml`.
- Config: `server/api-aprimorar/pom.xml`

**Assertion Library:**
- AssertJ is the primary assertion style, visible in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`, and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.

**Run Commands:**
```bash
cd server/api-aprimorar && ./mvnw test                 # Run backend tests
cd server/api-aprimorar && ./mvnw -Dtest=StudentServiceTest test   # Run one backend test class
cd server/api-aprimorar && ./mvnw verify               # Run backend verification and Jacoco report
```

## Test File Organization

**Location:**
- Backend tests live in a separate mirrored tree under `server/api-aprimorar/src/test/java/`.
- Frontend has no dedicated test runner or checked-in test files under `client/src/`, per `client/AGENTS.md` and the absence of `*.test.*` / `*.spec.*` files in `client/src/`.

**Naming:**
- Service/unit tests use `*Test.java`, for example `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.
- The Spring context smoke test uses `ApiAprimorarApplicationTests` in `server/api-aprimorar/src/test/java/com/aprimorar/api/ApiAprimorarApplicationTests.java`.

**Structure:**
```
server/api-aprimorar/src/test/java/com/aprimorar/api/
├── ApiAprimorarApplicationTests.java
├── exception/GlobalExceptionHandlerTest.java
└── domain/
    ├── employee/EmployeeServiceTest.java
    ├── employee/EmployeeTest.java
    ├── event/EventServiceTest.java
    ├── event/EventTest.java
    ├── parent/ParentServiceTest.java
    ├── parent/ParentTest.java
    ├── student/StudentServiceTest.java
    ├── student/StudentTest.java
    └── dashboard/DashboardSummaryResponseDTOTest.java
```

## Test Structure

**Suite Organization:**
```typescript
// Java pattern from `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {
        @Test
        @DisplayName("should create student when input is valid")
        void shouldCreateStudentWhenInputIsValid() { ... }
    }

    @Nested
    @DisplayName("Query methods")
    class QueryMethods {
        @Test
        @DisplayName("should return paged students")
        void shouldReturnPagedStudents() { ... }
    }
}
```

**Patterns:**
- Use `@Nested` to separate command and query behaviors, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`, and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- Use `@DisplayName` on test classes and methods for readable intent, as seen across all backend tests.
- Keep deterministic constants at the top of the class for IDs and timestamps, for example in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.
- Add helper factory methods at the bottom of the test class to build DTOs and entities, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.

## Mocking

**Framework:**
- Mockito with JUnit 5 extension is the default backend mocking approach in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`, and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- Mockito test resource configuration is present at `server/api-aprimorar/src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`.

**Patterns:**
```typescript
// Java pattern from `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`
@Mock
private ParentRepository parentRepo;

@InjectMocks
private ParentService parentService;

when(parentRepo.findById(id)).thenReturn(Optional.of(input));

ParentResponseDTO actual = parentService.findById(id);

assertThat(actual).isEqualTo(expected);
verify(parentRepo).findById(id);
```

**What to Mock:**
- Mock repositories, mappers, and infrastructure collaborators when testing service logic, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`.
- Mock `Clock` for time-sensitive business rules, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.
- Use `MockMvcBuilders.standaloneSetup(...)` for exception-handler/controller boundary tests instead of full application startup, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`.

**What NOT to Mock:**
- Do not mock the domain entity under validation in pure domain tests; instantiate real entities directly, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java`.
- Do not add frontend mocks today because there is no configured frontend test harness under `client/`.

## Fixtures and Factories

**Test Data:**
```typescript
// Java pattern from `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`
private static final UUID STUDENT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
private static final Instant CREATED_AT = Instant.parse("2026-01-05T08:00:00Z");

private static StudentRequestDTO request() {
    return new StudentRequestDTO(...);
}
```

**Location:**
- Fixtures are defined inline inside each backend test class rather than centralized in shared factories, for example `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`.

## Coverage

**Requirements:**
- JaCoCo reporting is configured in `server/api-aprimorar/pom.xml`, but no explicit minimum coverage threshold was detected.
- `./mvnw verify` is the broadest documented backend verification command in `server/api-aprimorar/AGENTS.md`.

**View Coverage:**
```bash
cd server/api-aprimorar && ./mvnw verify   # Generates target/site/jacoco/index.html
```

## Test Types

**Unit Tests:**
- Most backend tests are unit tests around services and domain entities using Mockito or direct object construction, for example `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`.

**Integration Tests:**
- Lightweight MVC integration exists for exception handling in `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java` using `MockMvc` with a standalone controller.
- A minimal Spring context smoke test exists in `server/api-aprimorar/src/test/java/com/aprimorar/api/ApiAprimorarApplicationTests.java`; it excludes datasource, JPA, Flyway, and repository auto-configuration to keep startup isolated from the database.

**E2E Tests:**
- Not used. No Playwright, Cypress, or frontend E2E config was detected in the repository root, `client/`, or `server/api-aprimorar/`.

## Common Patterns

**Async Testing:**
```typescript
Not applicable in current checked-in tests. No async/await-heavy frontend tests or asynchronous backend test flows were detected.
```

**Error Testing:**
```typescript
// Java pattern from `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`
assertThatThrownBy(() -> input.updateDetails(...))
    .isInstanceOf(InvalidEventException.class)
    .hasMessage("Data de fim do evento não pode ser anterior a data de inicio");
```

- Prefer `assertThatThrownBy` with both exception type and Portuguese message checks, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventTest.java`, and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- For HTTP error responses, assert both status and JSON body fields using `jsonPath`, as in `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`.

---

*Testing analysis: 2026-04-17*
