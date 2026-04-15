# Event Refactor Plan

## Main Issues

1. `NotAllowedToUpdateEventException` is not handled in `GlobalExceptionHandler`, so it can leak as `500`.
2. `archived` is accepted by `EventController` but ignored by `EventService`.
3. Event list queries may cause N+1 because the mapper reads lazy `student` and `employee` relations.
4. `Event` validations depend on setter order (`startDate` before `endDate`, `payment` before `price`).
5. `title` is persisted but derived, so it can become stale.
6. Routes are inconsistent: `/v1/events/events/{studentId}` vs `/v1/events/employee/{employeeId}`.
7. There is dead flow: commented delete endpoint and unused `findAllWithFilter(...)`.

## Refactor Order

1. Fix exception mapping.
2. Normalize controller contract.
3. Refactor entity/service mutation flow.
4. Optimize repository read paths.
5. Decide whether `title` should remain persisted.
6. Remove dead code or finish missing flow.

## Recommended Changes

### 1. Handle update-window exception explicitly (DONE)

```java
@ResponseStatus(HttpStatus.FORBIDDEN)
@ExceptionHandler(NotAllowedToUpdateEventException.class)
public ProblemResponseDTO handleNotAllowedToUpdateEvent(
    NotAllowedToUpdateEventException ex,
    HttpServletRequest request
) {
    return new ProblemResponseDTO(
        ErrorCode.BUSINESS_ERROR,
        HttpStatus.FORBIDDEN,
        ex.getMessage(),
        request.getRequestURI()
    );
}
```

### 2. Normalize routes (DONE)

```java
@GetMapping("/student/{studentId}")
public ResponseEntity<PageDTO<EventResponseDTO>> getEventsByStudent(...) { ... }

@GetMapping("/employee/{employeeId}")
public ResponseEntity<PageDTO<EventResponseDTO>> getEventsByEmployee(...) { ... }
```

### 3. Stop spreading field mutation across service methods

Current `updateEvent(...)` manually sets every field. Prefer one method that applies the request consistently.

```java
public void updateDetails(
    String title,
    String description,
    Instant startDate,
    Instant endDate,
    BigDecimal payment,
    BigDecimal price,
    EventContent content,
    Student student,
    Employee employee
) {
    if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
        throw new InvalidEventException("Datas do evento são inválidas");
    }
    if (payment == null || price == null || price.compareTo(payment) < 0 || price.compareTo(BigDecimal.valueOf(50)) < 0) {
        throw new InvalidEventException("Valores do evento são inválidos");
    }
r
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.payment = payment;
    this.price = price;
    this.content = content;
    this.student = student;
    this.employee = employee;
}
```

This keeps cross-field rules together and removes setter-order coupling.

### 4. Fetch relations intentionally on list/detail queries (DONE)

```java
@EntityGraph(attributePaths = {"student", "employee"})
Page<Event> findAll(Specification<Event> spec, Pageable pageable);
```

Or create dedicated query methods if overriding `findAll(...)` is not a good fit for the current repo style.

### 5. Revisit `title`

Two valid directions:

- Keep persisted `title` only if it is needed for history/audit/search.
- Remove persisted `title` and derive it when mapping/logging.

Example derived title:

```java
private String buildTitle(EventContent content, Student student, Employee employee) {
    return content + " - Col: " + employee.getName() + " - " + student.getName();
}
```

## Quick Decisions To Make

1. Should events support archive/unarchive like students? NO
2. Should delete remain available in the API? YES
3. Should `title` be historical persisted data or just derived presentation text? THE TITLE WILL BE USED IN THE FUTURE FOR GOOGLE API IMPLEMENTATION

## Minimum Safe First PR

1. Add missing exception handler.
2. Fix route naming.
3. Implement eager fetch for paged reads.
4. Refactor event mutation into one domain method.
5. Add/update tests for those behaviors.
