# Student ParentId Migration Plan

## Goal

Move the student flow from nested `parent` payloads to a cleaner contract where `Student`
references `Parent` by `parentId`.

## Target Model

- `Parent` is managed through its own controller and service
- `Student` create/update payloads contain `parentId`
- future reassociation becomes a natural `assignParent(studentId, parentId)` use case

## Implementation Plan

1. [x] Update student DTO contracts
   - Replace nested `parent` payload in `StudentRequestDTO` with `parentId: UUID`
   - Do the same for student update payloads

2. [x] Simplify `StudentCommand`
   - Remove nested `ParentCommand` from `StudentCommand`
   - Keep only student-owned data plus `parentId`

3. [x] Resolve parent explicitly in `StudentService`
   - Inject `ParentRepository` or a dedicated parent lookup component
   - Add `findParentByIdOrThrow(...)`
   - In create/update, load the managed `ParentEntity` from `parentId`

4. [x] Stop creating parent through the student mapper
   - Remove parent creation/update from `StudentMapper`
   - Keep `StudentMapper` focused on student and address normalization

5. [x] Keep parent mutation out of the student flow
   - `Student` should only associate to an existing `ParentEntity`
   - parent creation/update stays in `ParentController` / `ParentService`

6. [x] Tighten the student-parent persistence boundary
   - Remove `CascadeType.PERSIST` / `CascadeType.MERGE` from `StudentEntity.parent`
   - Treat parent lifecycle as explicit service responsibility

7. Add dedicated reassociation support later
   - Introduce `assignParent(studentId, parentId)` when needed
   - Keep reassociation rules outside the regular student create/update flow

8. Update frontend orchestration
   - Save or resolve `Parent` first
   - submit `Student` with returned `parentId`
   - keep the UI as a single form if desired, but split backend calls intentionally

9. Add focused tests
   - create student with existing `parentId`
   - update student with another `parentId`
   - reject invalid or missing parent id
   - verify no parent creation happens inside student flow anymore
