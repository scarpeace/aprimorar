# Server Review

## Quick Overview
The server is a Spring Boot REST API with DTOs, services, repositories, MapStruct mappers, and Flyway migrations. The structure is clean and beginner-friendly, with clear separation of concerns.

## Strengths
- Consistent layering: controller -> service -> repository.
- DTOs and MapStruct keep entities isolated from API contracts.
- Validation annotations are used well in DTOs.
- Centralized error responses via `GlobalExceptionHandler`.

## Issues and Improvement Ideas (Beginner-Friendly)

### P0) API error handling gaps
`StudentService` throws `ParentNotFoundException` and `IllegalArgumentException`, but the global handler does not catch them. This can result in 500 responses instead of clear 400/404 messages, which is confusing for beginners.

Suggested fix:
- Add a handler for `ParentNotFoundException`.
- Add a handler for `IllegalArgumentException` (return 400 with a clear message).

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/handler/GlobalExceptionHandler.java`

### P1) Event DTO should return what the frontend needs (financial + formatted date/time)
The frontend currently calculates `profit` and formats date/time. For MVP, this logic should move to the API and be returned in the response.

Suggested fix:
- Add `profit` (price - payment), plus `startDate`, `startTime`, `endDate`, `endTime` formatted as `dd/MM/yyyy` and `HH:mm`.
- Keep existing `startDateTime` and `endDateTime` for compatibility.
- If `price` or `payment` is null, return `profit` as null.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/event/EventResponseDTO.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/mapper/EventMapper.java`

### P1) Add a dashboard summary endpoint for MVP
The dashboard needs monthly KPIs and upcoming events without multiple calls or client-side aggregation.

Suggested fix:
- Add `GET /v1/dashboard/summary` returning:
  - Active counts (students, employees, events)
  - Month KPIs: revenue (price), cost (payment), profit (price - payment)
  - Upcoming events list (next 15 days, max 10 items)
- Use `America/Sao_Paulo` for month and upcoming windows.
- Use event start date for month window.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/DashboardController.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/service/DashboardService.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/dashboard/*`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/repository/EventRepository.java`

### P2) Event listing can cause N+1 query issues
`EventController` already has a TODO. The `Event` entity references `Student` and `Employee`, so listing events may trigger extra queries per event.

Suggested fix:
- Use `@EntityGraph` or a custom query with `JOIN FETCH` in `EventRepository`.
- Alternatively, project directly into `EventResponseDTO`.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/EventController.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/repository/EventRepository.java`

### P2) Validation of query parameters is inconsistent
Some endpoints limit `size` with `@Max(100)` while others do not. This creates inconsistent behavior for beginners.

Suggested fix:
- Add `@Max(100)` to any `size` parameter that is missing it.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/StudentController.java`

### P3) Unique constraints are very strict for real data
Entities enforce unique constraints on `name`, `contact`, `email`, and `cpf`. Real people can share names. This can confuse beginners during testing.

Suggested fix:
- Keep uniqueness for `cpf` and maybe `email`, but consider removing uniqueness from `name` and `contact` unless required.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Student.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Employee.java`
- `server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Parent.java`

### P3) MapStruct utility includes unused methods
`MapperUtils.parseBirthdate` is present but not used. Extra unused code can confuse beginners.

Suggested fix:
- Remove unused methods or document where they should be used.

Files:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/util/MapperUtils.java`

## Low-Risk Refinements
- Add `updatedAt` to `StudentResponseDTO` for consistency.
- Consider returning `id` in `ParentResponseDTO` if the client needs it.
- Add a `@ControllerAdvice` handler for `ConstraintViolationException` to cover query param validation errors.
- Standardize controller `@RequestMapping` to include leading `/` for clarity.
