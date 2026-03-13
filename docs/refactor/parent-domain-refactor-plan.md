# Parent Domain Refactor Plan

Review date: 2026-03-10

## Goal

- Refactor `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/` to follow the cleaner `event` domain structure.
- Keep current behavior stable while improving naming, file responsibilities, and maintainability.
- Use `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/` as the structural reference, not as a feature-parity target.

## Scope

- Structural refactor only.
- No new parent endpoints in this phase.
- No database schema changes in this phase.
- No redesign of student-parent business rules in this phase.

## Current Observations

- `parent` already has the same high-level package shape as `event`: `dto/`, `exception/`, controller, service, mapper, repository, entity.
- `ParentEntity` still uses older naming, while `event` uses `Event`.
- `ParentMapper` currently uses `CreateParentDTO` for both create and update semantics.
- `ParentService` and `ParentController` expose only list behavior today.
- `student` is tightly coupled to the current parent model through `ParentEntity`, `ParentRepository`, `ParentMapper`, and `CreateParentDTO`.

## Invariants

- Keep `tb_parent` and all current column names unchanged.
- Keep current validation and sanitization behavior intact.
- Keep student create/update flows that assign a parent working exactly as they do now.
- Keep `ParentNotFoundException` compatible with `GlobalExceptionHandler`.
- Avoid mixing structural refactor with feature expansion.

## Target Shape

- Keep package layout: `dto/`, `exception/`, controller, service, mapper, repository, entity.
- Align model naming with `event` by renaming `ParentEntity` to `Parent`.
- Keep the controller thin and focused on HTTP concerns.
- Keep the service focused on orchestration and business rules.
- Keep the repository persistence-only.
- Keep DTO boundaries explicit.

## Execution Order

### 1) Document and freeze the baseline

- Record this plan before code changes.
- Use the document to track progress batch by batch.
- Treat behavior changes as out of scope unless they are required to preserve compilation.

### 2) Split DTO responsibilities

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/CreateParentDTO.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/UpdateParentDTO.java`

Work:

- Keep `CreateParentDTO` as create-only input.
- Add `UpdateParentDTO` for partial update semantics.
- Keep validation messages aligned with current backend conventions.

Why first:

- This is the smallest cleanup with clear value.
- It removes the current mapper ambiguity before broader renames.

### 3) Normalize parent response DTO intent

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/ParentResponseDTO.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/ParentSummaryDTO.java`

Work:

- Confirm `ParentResponseDTO` is the canonical full parent API representation.
- Keep `ParentSummaryDTO` as the lightweight list response.
- Expand fields only if needed for structural consistency and only if it does not force an API behavior change.

Why now:

- DTO intent should be clear before renaming the core entity and mapper signatures.

### 4) Rename the entity class

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentEntity.java`

Target:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/Parent.java`

Work:

- Rename the class from `ParentEntity` to `Parent`.
- Keep all JPA mappings, table names, and column names unchanged.
- Avoid any persistence behavior changes.

Why here:

- This is the central naming alignment with the `event` package.
- Doing it after DTO cleanup reduces confusion during the rename.

### 5) Align the mapper with the renamed model

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentMapper.java`

Work:

- Replace `ParentEntity` with `Parent`.
- Keep `toEntity(CreateParentDTO)`.
- Keep `toDto(Parent)`.
- Keep `toSummaryDto(Parent)`.
- Change `updateFromDto(CreateParentDTO, ...)` to `updateFromDto(UpdateParentDTO, Parent)`.
- Preserve formatting and sanitization behavior in `MapperUtils` calls.

Why here:

- The mapper is the bridge between DTO and domain model and should match the renamed entity immediately.

### 6) Update repository typing only

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentRepository.java`

Work:

- Replace `JpaRepository<ParentEntity, UUID>` with `JpaRepository<Parent, UUID>`.
- Keep existing query methods and archive filters unchanged.

Why here:

- Repository changes should stay mechanical and low-risk.

### 7) Align parent service internals without expanding behavior

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java`

Work:

- Replace `ParentEntity` usages with `Parent`.
- Keep `listParents(Pageable, boolean)` behavior unchanged.
- Do not add CRUD or archive/unarchive methods in this phase.

Why here:

- This keeps the refactor structural and avoids introducing new API surface during the rename.

### 8) Keep controller behavior stable

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentController.java`

Work:

- Update imports and types after the rename if needed.
- Keep the current endpoint surface unchanged.
- Preserve pagination and sorting behavior.

Why here:

- Controller changes should be minimal while structural cleanup happens underneath.

### 9) Update student entity integration

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentEntity.java`

Work:

- Replace `ParentEntity` references with `Parent`.
- Keep JPA mapping and relation behavior unchanged.

Why here:

- `StudentEntity` is one of the main cross-domain dependencies and must stay in sync with the rename.

### 10) Update student service integration

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`

Work:

- Replace `ParentEntity` references with `Parent`.
- Keep current parent lookup and parent creation behavior unchanged.
- Keep parent assignment logic inside `StudentService` for this phase.

Why here:

- Moving this logic elsewhere would be a behavior and ownership change, which is out of scope for the structural pass.

### 11) Recheck student DTO references

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/CreateStudentDTO.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/UpdateStudentDTO.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentResponseDTO.java`

Work:

- Update imports and parent-related references only if required by the refactor.
- Avoid contract redesign.

Why here:

- These files depend on parent DTOs and should be reviewed after core parent types settle.

### 12) Validate exception compatibility

Files:

- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/exception/ParentNotFoundException.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`

Work:

- Confirm no exception contract changes are needed.
- Keep the current error code and message behavior intact.

Why last:

- This is mostly a safety review after type and package updates.

## Suggested Batches

### Batch 1 - DTO and mapper contract cleanup

- Add `UpdateParentDTO`.
- Stop using `CreateParentDTO` as an update DTO.
- Align mapper method signatures.

### Batch 2 - Parent entity rename and local alignment

- Rename `ParentEntity` to `Parent`.
- Update mapper, repository, service, and controller type references.

### Batch 3 - Student integration alignment

- Update `StudentEntity` and `StudentService`.
- Recheck student DTO imports and references.

### Batch 4 - Verification and cleanup

- Resolve compile issues.
- Remove stale imports and dead references.
- Re-run backend verification.

## Risks To Watch

- `student` is tightly coupled to current parent types and can break compilation during the rename.
- Expanding parent endpoints during this pass would blur the scope and increase regression risk.
- Changing DTO payload shape too aggressively could create accidental API changes.

## Verification

Run from `server/api-aprimorar/`:

```bash
./mvnw test
```

If test coverage is still too small to give confidence, also run:

```bash
./mvnw package
```

## Progress Tracker

- [x] Plan documented
- [ ] Batch 1 - DTO and mapper contract cleanup
- [ ] Batch 2 - Parent entity rename and local alignment
- [ ] Batch 3 - Student integration alignment
- [ ] Batch 4 - Verification and cleanup
