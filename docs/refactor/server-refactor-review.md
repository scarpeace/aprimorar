# Server Refactor Review

Review date: 2026-03-08

Second pass notes: this review was revisited after the first draft to look for more refactor opportunities before opening the dedicated branch.

## What is already working well

- Clear Spring layering: controller -> service -> repository.
- DTO validation is already doing a lot of good work close to the API boundary.
- Mappers keep entity exposure under control and are still easy to read.
- Services are small enough that refactoring can stay incremental instead of becoming a rewrite.

## Recommended refactor order

Scale used in this document:

- Complexity: 1 = very easy, 5 = advanced
- Impact: 1 = small gain, 5 = major gain
- Importance: 1 = nice to have, 5 = should happen soon

### 1) Fix the student archive API contract mismatch

- Complexity: 1/5
- Impact: 5/5
- Importance: 5/5
- Why first: the current test suite is failing because the controller exposes `DELETE /v1/students/{studentId}` and `PATCH /v1/students/{studentId}/unarchive`, but not `PATCH /v1/students/{studentId}/archive`.
- Refactor goal: either add the missing `PATCH` endpoint or remove/update the test and API contract so only one archive flow exists.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/StudentController.java:75`
  - `server/api-aprimorar/src/test/java/com/aprimorar/api/controller/StudentControllerTest.java:137`

### 2) Stop using create DTOs for PATCH endpoints

- Complexity: 2/5
- Impact: 5/5
- Importance: 5/5
- Why now: `PATCH` currently behaves like a full replacement for employees and events because it reuses creation DTOs with required fields. That is confusing for beginners and makes the API contract misleading.
- Refactor goal: create `UpdateEmployeeDTO` and `UpdateEventDTO` with optional fields, keep `Create*DTO` strict for creation, and let controller/service names match the real behavior.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EmployeeController.java:82`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EventController.java:66`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EmployeeService.java:65`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EventService.java:80`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/employee/CreateEmployeeDTO.java:12`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/event/CreateEventDTO.java:15`

### 3) Make student nested updates truly partial

- Complexity: 3/5
- Impact: 4/5
- Importance: 5/5
- Why this matters: `StudentMapper.updateFromDto(...)` replaces the whole embedded address and whole parent object when those sections are present. That is simple, but it can accidentally overwrite existing data and makes partial updates less predictable.
- Refactor goal: when `address` exists, update the existing embedded address in place with `AddressMapper.updateFromDto(...)`; when `parent` exists, update only the intended fields or make the API explicit that a full nested replacement is required.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/mapper/StudentMapper.java:96`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/mapper/AddressMapper.java:50`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/mapper/ParentMapper.java:53`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java:103`

### 4) Inject one application clock for business timestamps

- Complexity: 2/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: age calculation already uses a Spring `Clock`, but service timestamps and error timestamps still call `Instant.now()` directly. That makes time behavior harder to test and slightly inconsistent.
- Refactor goal: expose one `Clock` bean for application time and use it in services and the exception handler. This is a good first step into dependency-driven design without adding heavy abstraction.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/config/ClockConfig.java:12`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java:90`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EmployeeService.java:69`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/handler/GlobalExceptionHandler.java:167`

### 5) Extract a small, readable pagination request pattern

- Complexity: 2/5
- Impact: 3/5
- Importance: 4/5
- Why this matters: controllers repeat the same `page`, `size`, `sortBy`, `ALLOWED_SORT_FIELDS`, and `PageableUtils.buildPageable(...)` pattern. The repetition is still manageable, but it will keep growing.
- Refactor goal: introduce a tiny beginner-friendly structure such as `PageQuery(page, size, sortBy)` plus a per-controller whitelist, or a small controller helper. Keep it local and explicit; do not build a generic framework.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/StudentController.java:46`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/ParentController.java:34`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EventController.java:37`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EmployeeController.java:43`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/util/PageableUtils.java:16`

### 6) Revisit overly strict unique constraints

- Complexity: 3/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: unique constraints on `name` and `contact` for students, parents, and employees are likely too strict for real life and will create confusing validation failures during normal usage.
- Refactor goal: keep uniqueness where the business truly needs it, probably `cpf` and maybe `email`, and remove the rest through a migration.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Student.java:12`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Parent.java:11`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Employee.java:14`
  - `server/api-aprimorar/src/main/resources/db/migration/V1__create_schema.sql:10`

### 7) Replace the event filter query with an extensible filter model

- Complexity: 3/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: the `COALESCE` query works, but it becomes awkward when business rules grow, especially for archived students, active employees, date overlap rules, or new filters.
- Refactor goal: move event filters to a small filter object plus `Specification`, or at least split the query into clearer repository methods. This adds a bit of healthy "spice" because it introduces a common Spring Data refactor pattern.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EventController.java:37`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EventService.java:46`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/repository/EventRepository.java:17`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/repository/specification/StudentSpecifications.java:12`

### 8) Fill the most important test gaps before deeper refactors
NOTE: The integrations tests will be part of a future implementation, when executing this refactor let the user know and let it decide if this should be a new epic or storie.
- Complexity: 2/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: the project already has a good testing base, but coverage is uneven. There are no controller tests for employees or parents, and no focused tests for `GlobalExceptionHandler`.
- Refactor goal: add tests around API error contracts, employee endpoints, parent endpoints, and the archive route behavior. This gives you safety for the later refactors.
- Files:
  - `server/api-aprimorar/src/test/java/com/aprimorar/api/controller/StudentControllerTest.java:117`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/handler/GlobalExceptionHandler.java:25`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EmployeeController.java:28`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/ParentController.java:19`

### 9) Make active/archive lookup rules more explicit

- Complexity: 2/5
- Impact: 3/5
- Importance: 4/5
- Why this matters: some operations intentionally filter inactive data (`findByIdAndArchivedAtIsNull`, `findByIdAndActiveTrue`) while others use plain `findById`. The behavior may be correct, but the rule is not obvious from the method names used by the services.
- Refactor goal: decide the business rule for each use case and name repository/service helpers around that rule, for example `findActiveStudentOrThrow`, `findAnyStudentOrThrow`, `findSchedulableEmployeeOrThrow`.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java:63`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EventService.java:114`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EmployeeService.java:45`

### 10) Trim low-value utility and formatting noise

- Complexity: 1/5
- Impact: 2/5
- Importance: 2/5
- Why this matters: `MapperUtils` is helpful, but it already contains an unused `parseBirthdate(...)` method and several formatting methods that can slowly turn into a catch-all class.
- Refactor goal: remove unused methods, keep only transformations that are clearly shared, and avoid turning `MapperUtils` into a miscellaneous bucket.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/util/MapperUtils.java:16`

### 11) Standardize lifecycle state across core entities

- Complexity: 3/5
- Impact: 4/5
- Importance: 4/5
- Why this matters: students use `archivedAt` and `lastReactivatedAt`, while parents and employees use a simple `active` flag. Each approach can work, but mixing both increases mental load and makes service rules harder to remember.
- Refactor goal: pick one main lifecycle style for beginner readability. If audit timestamps matter, move more entities toward timestamp-based soft delete. If simplicity matters more, move toward a single status flag plus optional audit fields.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Student.java:38`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Parent.java:32`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Employee.java:42`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java:81`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EmployeeService.java:59`

### 12) Make JPA mappings describe the same rules as the database

- Complexity: 2/5
- Impact: 3/5
- Importance: 4/5
- Why this matters: the Flyway schema has a lot of `NOT NULL` rules, but most entities do not mirror that with `@Column(nullable = false)` or similar mapping hints. That makes the domain model less self-explanatory and pushes some mistakes to the database layer.
- Refactor goal: align entity annotations with the schema for required fields, lengths, and important constraints so the code documents the same contract as the migration.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Student.java:25`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Parent.java:24`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Employee.java:27`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Event.java:22`
  - `server/api-aprimorar/src/main/resources/db/migration/V1__create_schema.sql:1`

### 13) Consolidate repeated validation rules

- Complexity: 2/5
- Impact: 3/5
- Importance: 3/5
- Why this matters: CPF and contact regex rules are repeated across several DTOs. The duplication is small now, but it becomes annoying when messages or rules change.
- Refactor goal: extract shared regex constants first; if you want a slightly more advanced exercise later, create custom validation annotations such as `@CpfBR`, `@PhoneBR`, and `@ZipCodeBR`.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/CreateStudentDTO.java:25`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/UpdateStudentDTO.java:20`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/employee/CreateEmployeeDTO.java:24`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/parent/CreateParentDTO.java:15`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/address/CreateAddressDTO.java:25`

### 14) Standardize API date and time serialization

- Complexity: 2/5
- Impact: 3/5
- Importance: 3/5
- Why this matters: `StudentResponseDTO` customizes several date formats, `EmployeeResponseDTO` formats only `birthdate`, and `EventResponseDTO` leaves everything to default Jackson serialization. This gives the API an inconsistent contract.
- Refactor goal: define one clear rule for date/time output. Either standardize globally with Jackson configuration or make all DTOs explicitly consistent.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/StudentResponseDTO.java:18`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/employee/EmployeeResponseDTO.java:14`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/event/EventResponseDTO.java:8`
  - `server/api-aprimorar/src/main/resources/application.yml:1`

### 15) Clean up logging to avoid leaking personal data and to improve consistency

- Complexity: 1/5
- Impact: 2/5
- Importance: 3/5
- Why this matters: logging is still light, which is good, but current messages mix debug/info levels and may expose names or identifiers that do not add much operational value.
- Refactor goal: prefer consistent log levels, log action + entity id when useful, and avoid unnecessary personal data in production logs.
- Files:
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java:70`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EmployeeService.java:52`
  - `server/api-aprimorar/src/main/java/com/aprimorar/api/service/EventService.java:65`

## Extra notes from the review

- `./mvnw test` currently fails because of the student archive endpoint mismatch described in item 1.
- `spring.jpa.open-in-view` is not configured in `server/api-aprimorar/src/main/resources/application.yml:1`; consider turning it off explicitly once service/repository boundaries are stable.
- There is only one migration file right now. Once the refactor branch starts changing constraints and lifecycle rules, add new Flyway migrations instead of editing `V1__create_schema.sql` directly.
- The codebase is still small enough that most of these refactors can be done one by one without introducing a large architecture layer.

## Suggested learning path

1. Fix the failing route/test mismatch.
2. Introduce dedicated update DTOs.
3. Improve partial update behavior for nested student data.
4. Inject `Clock` everywhere business time matters.
5. Refactor event filters into a small specification-based model.
6. Standardize entity lifecycle and persistence constraints.
7. Consolidate validation and date serialization rules.

## Branch-ready execution plan

Use this section as the implementation roadmap for the dedicated refactor branch.

### Branch goal

- Improve API clarity, keep the code beginner-friendly, and remove misleading or inconsistent behavior without introducing heavy abstractions.
- Keep each step small enough to be reviewed and tested independently.

### Ground rules for the branch

- Prefer small commits grouped by one concern.
- Keep public API changes explicit in the commit message and in the PR description.
- When database rules change, add a new Flyway migration; do not rewrite `V1__create_schema.sql`.
- Prefer local helper classes or small value objects over generic frameworks.
- Integration tests remain a future epic/story unless you decide to pull them into this branch.

### Phase 0 - Stabilize the branch first

- Scope:
  - item 1: student archive API contract mismatch
  - item 8 partially: add or fix tests around the archive route behavior
- Why first:
  - the branch should start from a green baseline so later refactors are easier to trust
- Deliverables:
  - one clear archive contract for students
  - matching controller/service/test behavior
  - `./mvnw test` passing again
- Suggested commit shape:
  - `fix student archive endpoint contract`

### Phase 1 - Make update flows honest and safe

- Scope:
  - item 2: dedicated update DTOs for employee and event
  - item 3: truly partial nested updates for student address/parent
  - item 9: clearer active/archive lookup helper naming
- Why now:
  - these changes improve correctness and readability without changing the architecture much
- Deliverables:
  - `UpdateEmployeeDTO`
  - `UpdateEventDTO`
  - mapper methods that clearly support partial updates
  - service helper names that describe the lookup rule in business terms
- Suggested commit shape:
  - `refactor employee and event patch contracts`
  - `refactor student nested partial updates`
  - `rename service lookup helpers for clarity`

### Phase 2 - Reduce repetition without over-abstracting

- Scope:
  - item 4: shared `Clock`
  - item 5: small pagination request pattern
  - item 10: trim low-value mapper utility noise
  - item 13: consolidate repeated validation rules
- Why here:
  - this is good cleanup after the core update behavior is stable
- Deliverables:
  - one application time source
  - a tiny reusable pagination input pattern
  - lighter `MapperUtils`
  - shared regex constants or small validation building blocks
- Suggested commit shape:
  - `refactor application time handling`
  - `refactor pagination request inputs`
  - `cleanup shared mapper and validation utilities`

### Phase 3 - Improve querying and API consistency

- Scope:
  - item 7: event filter model/specification
  - item 14: consistent API date/time serialization
  - item 15: logging cleanup
- Why here:
  - these changes are valuable, but easier to do once contracts and helpers are already cleaner
- Deliverables:
  - clearer event filtering code
  - predictable date/time JSON output
  - quieter, safer logs
- Suggested commit shape:
  - `refactor event filtering model`
  - `standardize api date serialization`
  - `cleanup service logging`

### Phase 4 - Change persistence rules carefully

- Scope:
  - item 6: unique constraints review
  - item 11: lifecycle standardization
  - item 12: align JPA mappings with schema
- Why last:
  - these are the most invasive changes because they affect persistence, existing data assumptions, and future migrations
- Deliverables:
  - explicit lifecycle strategy for core entities
  - new Flyway migrations for dropped/changed constraints
  - entities that document required columns clearly
- Suggested commit shape:
  - `refactor entity lifecycle rules`
  - `update database constraints for real-world data`
  - `align jpa mappings with schema`

### Recommended checkpoint sequence

1. Run `./mvnw test` before changing anything on the branch.
2. Finish Phase 0 and make sure tests are green.
3. After each Phase 1 and Phase 2 item, rerun `./mvnw test`.
4. Before Phase 4, review whether existing local/dev data needs migration handling.
5. After every migration change, test against a clean database and, if possible, an existing one.

### Risks to watch

- PATCH contract changes may affect frontend requests if it currently sends full create payloads.
- Student nested update behavior may change silently if replacement becomes in-place merge logic.
- Constraint changes can break local databases unless Flyway migrations are explicit and tested.
- Lifecycle standardization can affect repository methods, filters, and business expectations at the same time.

### Done criteria for the branch

- Tests pass locally with `./mvnw test`.
- No controller uses create DTOs for partial update endpoints.
- Student update behavior is explicit and predictable for nested fields.
- Persistence changes are backed by new Flyway migrations.
- The API contract is more consistent for paging, date output, and status/lifecycle behavior.
