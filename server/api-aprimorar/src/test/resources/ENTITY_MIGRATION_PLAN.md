# Entity Migration Plan

## Goal

Migrate `Student`, `Employee`, and `Parent` to the same approach now used by `Event`:

- entities own their mutation flow
- services orchestrate only loading, uniqueness checks, and persistence
- mappers become DTO-only where possible
- field setters stop being the public mutation API

## Current State

### Student

Current flow:

- `StudentMapper.convertToEntity(...)` creates a partial entity
- `StudentService.createStudent(...)` adds `parent` later
- `StudentService.updateStudent(...)` copies fields one by one

Observations:

- `Student` is the most complex of the three because it combines:
  - scalar fields
  - `Parent` relation
  - embedded `Address`
- the aggregate is assembled across mapper + service
- this is the closest match to the old `Event` shape before refactor

### Parent

Current flow:

- `ParentMapper.convertToEntity(...)` creates an entity
- `ParentService.updateParent(...)` copies fields manually

Observations:

- simplest entity
- `updateParent(...)` does not update `cpf` today
- there is also a second `update(...)` method at the bottom of `ParentService` that looks redundant and risky

## Main Risks To Address

1. Public setters are still the mutation API.
2. Mappers still build domain entities directly.
3. Services still copy fields one by one.
4. Tests and fixtures build entities through setters heavily.
5. `Parent` and `Employee` update flows must keep CPF immutable.

## Recommended Entity APIs

### Student

Add one aggregate mutation method:

```java
public void updateDetails(
    String name,
    String contact,
    String email,
    LocalDate birthdate,
    String cpf,
    String school,
    Parent parent,
    Address address
) {
    validateRequiredFields(name, contact, email, birthdate, cpf, school, parent, address);

    this.name = name;
    this.contact = contact;
    this.email = email;
    this.birthdate = birthdate;
    this.cpf = cpf;
    this.school = school;
    this.parent = parent;
    this.address = address;
}
```

Migration notes:

- this method should receive already-normalized data
- `AddressMapper` can still convert request DTO -> `Address`
- `StudentMapper` should stop creating `Student`

### Parent

Add:

```java
public void createDetails(String name, String email, String contact, String cpf) {
    validateRequiredFields(name, email, contact, cpf);

    this.name = name;
    this.email = email;
    this.contact = contact;
    this.cpf = cpf;
}

public void updateDetails(String name, String email, String contact) {
    validateRequiredFields(name, email, contact);

    this.name = name;
    this.email = email;
    this.contact = contact;
}
```

## Service Migration

### StudentService

Target create flow:

```java
Student student = new Student();
Parent parent = findParentOrThrow(dto.parentId());
Address address = addressMapper.convertToEntity(dto.address());

student.updateDetails(
    dto.name(),
    MapperUtils.normalizeContact(dto.contact()),
    MapperUtils.normalizeEmail(dto.email()),
    dto.birthdate(),
    MapperUtils.normalizeCpf(dto.cpf()),
    dto.school(),
    parent,
    address
);
```

Then uniqueness checks run against the normalized values already in the entity.

Target update flow:

- remove `updatedStudentData`
- call `student.updateDetails(...)` directly on the managed entity

### ParentService

Target create/update:

```java
parent.createDetails(
    request.name(),
    MapperUtils.normalizeEmail(request.email()),
    MapperUtils.normalizeContact(request.contact()),
    MapperUtils.normalizeCpf(request.cpf())
);

parent.updateDetails(
    request.name(),
    MapperUtils.normalizeEmail(request.email()),
    MapperUtils.normalizeContact(request.contact())
);
```

Also cleanup:

- remove the duplicate `update(...)` method in `ParentService`
- keep CPF immutable on update

## Mapper Migration

### StudentMapper

Keep:

- `convertToDto(...)`
- `calculateAge(...)`

Remove from mapper:

- `convertToEntity(...)`

Reason:

- `Student` should no longer be assembled outside the entity/service mutation path

### ParentMapper

Keep only DTO mapping.

## Setter Strategy

Same direction used in `Event`:

1. Add `updateDetails(...)`.
2. Switch services away from setter-by-setter mutation.
3. Remove `convertToEntity(...)` from mappers.
4. Make domain setters `private`.
5. Update tests/fixtures.

Suggested setters to lock down:

- `Student`: `setName`, `setContact`, `setEmail`, `setBirthdate`, `setCpf`, `setSchool`, `setParent`, `setAddress`
- `Parent`: `setName`, `setEmail`, `setContact`, `setCpf`

## Rollout Order

### Phase 2: Parent

Current phase.

Steps:

1. add `Parent.createDetails(...)` and `Parent.updateDetails(...)`
2. update `ParentService`
3. remove duplicate `update(...)`
4. remove `ParentMapper.convertToEntity(...)`
5. make setters private
6. update tests

### Phase 3: Student

Why last:

- most complex aggregate
- depends on `Parent` relation and `Address`
- likely most test fallout

Steps:

1. add `Student.updateDetails(...)`
2. move create/update assembly into `StudentService`
3. keep `AddressMapper` for embedded address conversion
4. remove `StudentMapper.convertToEntity(...)`
5. make setters private
6. update tests and fixtures

## Testing Impact

Expected breakage:

- service tests that stub `convertToEntity(...)`
- entity tests that validate setters directly
- fixture builders that call setters

New preferred fixture style:

```java
Student student = new Student();
student.updateDetails(
    "João Silva",
    "61999999999",
    "joao@email.com",
    LocalDate.of(2010, 5, 10),
    "12345678901",
    "Escola Central",
    parent,
    address
);
```

## Open Decisions

1. Reminder: remove employee CPF editing from update flow and contract.
2. Should `Student.parent` remain mutable on update? Current API says yes.
3. Should `Student.birthdate` keep `LocalDate` for now, or is the `TODO` an active near-term change?

## Recommended Next Step

Finish `Parent`, then move to `Student`.
