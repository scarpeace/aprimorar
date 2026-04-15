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

### Employee

Current flow:

- `EmployeeMapper.convertToEntity(...)` creates an entity
- `EmployeeService.updateEmployee(...)` copies fields one by one

Observations:

- simpler than `Student`
- no cross-field validation today, mostly required-field validation
- easiest migration candidate

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
5. `Parent` update behavior needs a business decision on CPF mutability before locking the API down.

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

### Employee

Add:

```java
public void updateDetails(
    String name,
    LocalDate birthdate,
    String pix,
    String contact,
    String cpf,
    String email,
    Duty duty
) {
    validateRequiredFields(name, birthdate, pix, contact, cpf, email, duty);

    this.name = name;
    this.birthdate = birthdate;
    this.pix = pix;
    this.contact = contact;
    this.cpf = cpf;
    this.email = email;
    this.duty = duty;
}
```

### Parent

Add:

```java
public void updateDetails(String name, String email, String contact, String cpf) {
    validateRequiredFields(name, email, contact, cpf);

    this.name = name;
    this.email = email;
    this.contact = contact;
    this.cpf = cpf;
}
```

Decision needed before implementation:

- should CPF remain editable on update?
- current service behavior suggests `no`, but create/update DTO shape suggests `yes`

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

### EmployeeService

Target create/update:

```java
employee.updateDetails(
    request.name(),
    request.birthdate(),
    request.pix(),
    MapperUtils.normalizeContact(request.contact()),
    MapperUtils.normalizeCpf(request.cpf()),
    MapperUtils.normalizeEmail(request.email()),
    request.duty()
);
```

### ParentService

Target create/update:

```java
parent.updateDetails(
    request.name(),
    MapperUtils.normalizeEmail(request.email()),
    MapperUtils.normalizeContact(request.contact()),
    MapperUtils.normalizeCpf(request.cpf())
);
```

Also cleanup:

- remove the duplicate `update(...)` method in `ParentService`
- align uniqueness checks with the final rule for CPF mutability

## Mapper Migration

### StudentMapper

Keep:

- `convertToDto(...)`
- `calculateAge(...)`

Remove from mapper:

- `convertToEntity(...)`

Reason:

- `Student` should no longer be assembled outside the entity/service mutation path

### EmployeeMapper

Keep only DTO mapping.

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
- `Employee`: `setName`, `setBirthdate`, `setPix`, `setContact`, `setCpf`, `setEmail`, `setDuty`
- `Parent`: `setName`, `setEmail`, `setContact`, `setCpf`

## Rollout Order

### Phase 1: Employee

Why first:

- smallest surface area
- no relation or embedded object complexity

Steps:

1. add `Employee.updateDetails(...)`
2. update `EmployeeService`
3. remove `EmployeeMapper.convertToEntity(...)`
4. make setters private
5. update tests

### Phase 2: Parent

Why second:

- also simple
- but needs CPF update decision first

Steps:

1. decide CPF mutability
2. add `Parent.updateDetails(...)`
3. update `ParentService`
4. remove duplicate `update(...)`
5. remove `ParentMapper.convertToEntity(...)`
6. make setters private
7. update tests

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

1. Can `Parent.cpf` be updated after creation?
2. Should `Student.parent` remain mutable on update? Current API says yes.
3. Should `Student.birthdate` keep `LocalDate` for now, or is the `TODO` an active near-term change?

## Recommended Next Step

Start with `Employee` first. It is the smallest, gives a fast proof that the pattern scales beyond `Event`, and will make the later `Parent` and `Student` refactors more predictable.
