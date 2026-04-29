# Phase 01: Employee Events Tracking - Pattern Map

**Mapped:** 2026-04-28
**Files analyzed:** 5
**Analogs found:** 5 / 5

## File Classification

| New/Modified File | Role | Data Flow | Closest Analog | Match Quality |
|-------------------|------|-----------|----------------|---------------|
| `server/.../EventController.java` | controller | request-response | `server/.../EmployeeController.java` | exact |
| `server/.../repository/EventSpecifications.java` | model | CRUD | `server/.../EmployeeSpecifications.java` | exact |
| `server/.../EventService.java` | service | CRUD | `server/.../EmployeeService.java` | exact |
| `client/.../pages/EmployeeDetailPage.tsx` | component | request-response | `client/.../pages/EmployeesPage.tsx` | role-match |
| `client/.../components/EventsTable.tsx` | component | transform | `client/.../finance/components/SettlementTable.tsx` | exact |

## Pattern Assignments

### `server/.../EventController.java` (controller, request-response)

**Analog:** `server/.../employee/EmployeeController.java`

**Pagination and Search pattern** (lines 62-72):
```java
@GetMapping
@Operation(operationId = "getEmployees", description = "Retorna uma lista paginada de colaboradores.")
@ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
public ResponseEntity<PageDTO<EmployeeResponseDTO>> getEmployees(
    @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
    @RequestParam(required = false) String search,
    @RequestParam(required = false) boolean archived
) {
    PageDTO<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable, search, archived);
    return ResponseEntity.ok(employees);
}
```

### `server/.../repository/EventSpecifications.java` (model, CRUD)

**Analog:** `server/.../employee/repository/EmployeeSpecifications.java`

**Search Specification pattern** (lines 28-40):
```java
public static Specification<Employee> searchContainsIgnoreCase(String term) {
    return (root, query, cb) -> {
        String pattern = "%" + term.toLowerCase() + "%";
        return cb.and(
                cb.notEqual(root.get("duty"), Duty.SYSTEM),
                cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(root.get("duty").as(String.class)), pattern)
                )
        );
    };
}
```

### `client/.../pages/EmployeeDetailPage.tsx` (component, request-response)

**Analog:** `client/.../employees/pages/EmployeesPage.tsx`

**Debounced Search State pattern** (lines 14-25):
```tsx
const [searchTerm, setSearchTerm] = useState("");
const [currentPage, setCurrentPage] = useState(0);
// ...
const debouncedSearchTerm = useDebounce(searchTerm, 500);

const employeesQuery = useGetEmployees({
  page: currentPage,
  search: debouncedSearchTerm,
  archived: showArchived,
});
```

**Search Input pattern** (lines 45-51):
```tsx
<ListSearchInput
  className="grow"
  placeholder="Buscar colaborador por nome, email ou CPF"
  ariaLabel="Buscar colaborador"
  value={searchTerm}
  onChange={setSearchTerm}
/>
```

### `client/.../components/EventsTable.tsx` (component, transform)

**Analog:** `client/.../finance/components/SettlementTable.tsx`

**Table Empty State pattern** (lines 35-41):
```tsx
<tbody>
  {events.length === 0 ? (
    <tr>
      <td colSpan={6} className="text-center py-8 text-base-content/50">
        Nenhum atendimento encontrado para os filtros selecionados.
      </td>
    </tr>
  ) : (
    events.map((event) => (
      <tr key={event.eventId}>
        {/* ... */}
      </tr>
    ))
  )}
</tbody>
```

## Shared Patterns

### Pagination Handling
**Source:** `client/src/components/ui/pagination.tsx`
**Apply to:** `EventsTable.tsx` and `EmployeeDetailPage.tsx`
Ensure `onPageChange` and `currentPage` are correctly propagated from the Page to the Table and then to the Pagination component.

### Debouncing
**Source:** `client/src/lib/shared/use-debounce.ts`
**Apply to:** All search-enabled pages (EmployeeDetailPage).
```tsx
import { useDebounce } from "@/lib/shared/use-debounce";
const debouncedValue = useDebounce(value, 500);
```

### JPA Specifications (Join search)
**Source:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventSpecifications.java`
**Apply to:** `withStudentNameIgnoreCase`
```java
cb.like(cb.lower(root.join("student").get("name")), pattern)
```

## Metadata

**Analog search scope:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain`, `client/src/features`
**Files scanned:** ~20
**Pattern extraction date:** 2026-04-28
