# Phase 04: Financial Tracking Core - Pattern Map

**Mapped:** 2026-04-21
**Files analyzed:** ~12 new files (implied)
**Analogs found:** 6 / 6

## File Classification

| New/Modified File | Role | Data Flow | Closest Analog | Match Quality |
|-------------------|------|-----------|----------------|---------------|
| `BillingController.java` | controller | request-response | `EventController.java` | exact |
| `BillingService.java` | service | CRUD | `EventService.java` | exact |
| `Billing.java` | model | entity | `Event.java` | exact |
| `PaymentController.java` | controller | request-response | `EventController.java` | exact |
| `PaymentService.java` | service | CRUD | `EventService.java` | exact |
| `FinancialSummaryController.java` | controller | request-response | `DashboardController.java` | exact |
| `BillingsPage.tsx` | page | CRUD/List | `StudentsPage.tsx` | exact |
| `BillingCreatePage.tsx` | page | Form/Creation | `StudentCreatePage.tsx` | exact |
| `BillingForm.tsx` | component | Form | `StudentCreatePage.tsx` (inline) | role-match |
| `PaymentsTable.tsx` | component | List | `StudentsTable.tsx` | exact |
| `FinancialSummaryCards.tsx` | component | transform | `DashboardKpiCard.tsx` | exact |

## Pattern Assignments

### Backend: Controller Pattern

**Analog:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventController.java`

**Core Pattern (Constructor & Mapping):**
```java
@Slf4j
@RestController
@RequestMapping("/v1/events")
@Tag(name = "Events", description = "Events management APIs")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
}
```

**CRUD Pattern (POST/GET/PUT):**
```java
@PostMapping
@Operation(operationId = "createEvent", description = "Cria um novo evento...")
public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid EventRequestDTO createEventDto) {
    EventResponseDTO response = eventService.createEvent(createEventDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@GetMapping
public ResponseEntity<PageDTO<EventResponseDTO>> getEvents(
    @ParameterObject Pageable pageable,
    @RequestParam(required = false) String search,
    ...
) {
    return ResponseEntity.ok(eventService.getEvents(pageable, search, ...));
}
```

---

### Backend: Service Pattern

**Analog:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventService.java`

**Core Pattern (Transaction & Mapping):**
```java
@Service
public class EventService {
    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        // 1. Fetch related entities
        // 2. Validate availability/rules
        // 3. Create & Save
        // 4. Return DTO via Mapper
    }
}
```

**Helper Pattern (Find or Throw):**
```java
private Student findStudentOrThrow(UUID studentId) {
    return studentRepo.findById(studentId)
        .orElseThrow(() -> new StudentNotFoundException("..."));
}
```

---

### Backend: Entity Pattern

**Analog:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java`

**Core Pattern (Validation & Encapsulation):**
```java
@Entity
@Table(name = "tb_events")
public class Event extends BaseEntity {
    // 1. Private fields with JPA annotations
    // 2. Public constructor with validation
    // 3. Internal validation methods
    private void validateAmounts(BigDecimal payment, BigDecimal price) {
        if (price.compareTo(payment) < 0) {
            throw new InvalidEventException("O valor do evento não pode ser menor que o pagamento");
        }
    }
}
```

---

### Frontend: List Page Pattern

**Analog:** `client/src/features/students/pages/StudentsPage.tsx`

**Core Pattern (Search & Toggle):**
```typescript
export function StudentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const studentsQuery = useGetStudents({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });
  // ...
}
```

---

### Frontend: Form Page Pattern

**Analog:** `client/src/features/students/pages/StudentCreatePage.tsx`

**Core Pattern (React Hook Form & Zod):**
```typescript
export function StudentCreatePage() {
  const { register, handleSubmit, control, formState: { errors } } = useForm<StudentFormSchema>({
    resolver: zodResolver(studentFormSchema),
    mode: "onBlur",
  });
  const registerWithMask = useHookFormMask(register);
  // ...
  const onSubmit = handleSubmit((data: StudentFormSchema) => {
    createStudent.mutate({ data });
  });
}
```

---

### Frontend: Summary/KPI Pattern

**Analog:** `client/src/features/dashboard/components/DashboardKpiCard.tsx`

**Core Pattern (Visual Indicators):**
```typescript
export function DashboardKpiCard({ title, value, icon: Icon, trend, color }: KpiCardProps) {
  return (
    <div className="card bg-base-100 shadow-xl">
      <div className="card-body p-4 flex-row items-center gap-4">
        <div className={`p-3 rounded-xl ${color}`}>
          <Icon className="w-6 h-6 text-white" />
        </div>
        <div>
          <p className="text-sm font-medium text-base-content/60">{title}</p>
          <p className="text-2xl font-bold">{value}</p>
        </div>
      </div>
    </div>
  );
}
```

## Shared Patterns

### Error Handling
**Source:** `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`
**Frontend Handling:** `client/src/lib/shared/api-errors.ts`
**Apply to:** All new controllers and mutation hooks.

### Validation
**Backend:** Jakarta Bean Validation (`@NotNull`, `@Min`, etc.) + Entity-level business validation.
**Frontend:** Zod schemas in `...formSchema.ts`.

### Money Handling
**Backend:** Use `BigDecimal` with `precision = 19, scale = 2`.
**Frontend:** Formatting via `Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })`.

## No Analog Found

| File | Role | Data Flow | Reason |
|------|------|-----------|--------|
| `PaymentReceipt.pdf` | Utility | File-I/O | No PDF generation exists yet (if required) |

## Metadata

**Analog search scope:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/`, `client/src/features/`
**Files scanned:** 83 (backend), 41 (frontend)
**Pattern extraction date:** 2026-04-21
