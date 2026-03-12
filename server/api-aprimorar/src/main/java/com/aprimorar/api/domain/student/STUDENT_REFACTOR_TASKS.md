# Student Refactor Tasks

1. Decide and preserve the parent ownership rule
   - Keep `Student` referencing an existing shared `Parent`
   - Stop treating `Parent` as student-owned replacement data during student updates

2. Introduce `StudentCommand`
   - Map `StudentRequestDTO` and `UpdateStudentDTO` into one normalized command object
   - Keep normalization of `cpf`, `email`, `contact`, and embedded address data in the mapper layer

3. Move mutation rules into `StudentEntity`
   - Introduce focused methods such as `create(...)`, `updateDetails(...)`, `archive(...)`, and `unarchive(...)`
   - Let the entity own allowed field mutation instead of the service doing field-by-field updates

4. Tighten `StudentEntity` mutability
   - Reduce broad mutation from Lombok setters and all-args constructor
   - Prefer controlled construction plus explicit domain methods

5. Centralize duplicate validation around normalized values
   - Validate duplicate `cpf` and `email` after normalization
   - Add update-safe repository checks such as excluding the current student id

6. Refactor `StudentService` to match Event/Parent style
   - Keep QUERY / COMMAND / HELPER sections
   - Use `findStudentByIdOrThrow(...)`
   - Replace detached temporary entity copying with command + entity mutation flow

7. Fix parent resolution in the student flow
   - Resolve an existing managed `ParentEntity` instead of attaching a new transient one on every update
   - Reuse the shared parent aggregate consistently

8. Align controller/API semantics
   - Replace manual `PageRequest` handling with `Pageable` + `@PageableDefault`
   - Reevaluate `PATCH` vs `PUT` and keep the endpoint semantics consistent with the actual update behavior

9. Fix DTO and validation inconsistencies
   - Replace invalid `@NotBlank` usage on `LocalDate` with `@NotNull`
   - Remove DTO duplication where it no longer adds value

10. Add focused tests for the refactor
    - Duplicate `cpf` / `email` validation on create and update
    - Shared parent resolution behavior
    - Embedded address replacement
    - Archive/unarchive behavior and time-sensitive rules
