# Backend AGENTS.md

Spring Boot + Java 21 patterns for the Aprimorar API.

## Build Commands

```bash
# Database
docker compose up -d db       # Start Postgres
docker compose down           # Stop Postgres

# Running
./mvnw spring-boot:run        # Run API (http://localhost:8080)
./mvnw test                   # Run all tests
./mvnw verify                 # Full verification lifecycle
./mvnw package                # Build JAR artifact

# Single test execution
./mvnw -Dtest=StudentServiceTest test                          # Single class
./mvnw -Dtest=StudentServiceTest#methodName test               # Single method
./mvnw -Dtest=*ServiceTest test                                 # All service tests
./mvnw -Dtest=ApiAprimorarApplicationTests#contextLoads test  # Context smoke test
```

## Architecture

### Domain Module Structure
Each domain module follows a consistent layered structure:

```
domain/{entity}/
├── {Entity}.java                    # JPA entity
├── {Entity}Controller.java          # REST endpoints
├── {Entity}Service.java             # Business logic
├── {Entity}Repository.java         # Data access
├── {Entity}Mapper.java              # DTO <-> Entity mapping
├── {Entity}Rules.java              # Validation rules
├── {Entity}Specifications.java     # JPA specifications
├── dto/
│   ├── {Entity}RequestDTO.java
│   ├── {Entity}ResponseDTO.java
│   └── {Entity}OptionDTO.java
└── exception/
    ├── {Entity}NotFoundException.java
    └── {Entity}AlreadyExistsException.java
```

### Layer Responsibilities
- **Controller**: HTTP concerns only (request/response, status codes)
- **Service**: Business logic, transaction boundaries
- **Repository**: Data access, query execution
- **Mapper**: Bidirectional DTO <-> Entity conversion

## Entity Patterns

### Base Entity with UUID
```java
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "archived_at")
    private Instant archivedAt;
}
```

### Embeddable Pattern
```java
@Embeddable
public class Address {
    @Column(name = "street", nullable = false)
    private String street;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private BrazilianState state;
}
```

## Repository Patterns

### Standard Repository
```java
public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student> {
    List<Student> findAllByParentId(UUID parentId);
    boolean existsByCpf(String cpf);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
```

### Custom Queries
```java
@Modifying
@Query("UPDATE Event e SET e.student.id = :ghostId WHERE e.student.id = :studentId")
void reassignEventsToGhost(@Param("studentId") UUID studentId, @Param("ghostId") UUID ghostId);
```

### EntityGraph for Lazy Loading
```java
@Override
@EntityGraph(attributePaths = {"student", "employee"})
Page<Event> findAll(Pageable pageable);
```

### Specifications for Dynamic Queries
```java
public final class StudentSpecifications {
    private StudentSpecifications() {}

    public static Specification<Student> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(
            cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"
        );
    }

    public static Specification<Student> searchContainsIgnoreCase(String term) {
        return (root, query, cb) -> {
            String likeTerm = "%" + term.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), likeTerm),
                cb.like(cb.lower(root.get("email")), likeTerm)
            );
        };
    }
}
```

## Service Patterns

### Constructor Injection
```java
@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;
    private final StudentMapper studentMapper;

    public StudentService(
            StudentRepository studentRepo,
            ParentRepository parentRepo,
            StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
        this.studentMapper = studentMapper;
    }
}
```

### Transaction Boundaries
```java
@Transactional(readOnly = true)  // Query methods
public Page<StudentResponseDTO> getStudents(Pageable pageable, String search) { ... }

@Transactional  // Command methods (create, update, delete)
public StudentResponseDTO createStudent(StudentRequestDTO dto) { ... }
```

### Helper Methods for Finding Entities
```java
private Student findStudentOrThrow(UUID studentId) {
    return studentRepo.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException("Aluno não encontrado"));
}
```

### Logging Pattern
```java
private static final Logger log = LoggerFactory.getLogger(StudentService.class);

log.info("Aluno {} cadastrado com sucesso", savedStudent.getName().toUpperCase());
```

## Controller Patterns

### REST Controller
```java
@RestController
@RequestMapping("/v1/students")
@Tag(name = "Students", description = "Student management APIs")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @RequestBody @Valid StudentRequestDTO dto) {
        StudentResponseDTO response = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(studentService.getStudents(pageable, search));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
```

## DTO Patterns

### Request DTOs (Java Records)
```java
public record StudentRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotNull(message = "Data de nascimento é obrigatória")
    @PastOrPresent(message = "Data deve ser no passado")
    LocalDate birthdate,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotNull @Valid AddressRequestDTO address,

    @NotNull(message = "Responsável é obrigatório")
    UUID parentId
) {}
```

### Response DTOs (with computed fields)
```java
public record StudentResponseDTO(
    UUID id,
    String name,
    String email,
    LocalDate birthdate,
    Integer age,          // Computed from birthdate
    Address address,
    Instant createdAt
) {}
```

## Mapper Patterns

### Manual Mapper with Clock
```java
@Component
public class StudentMapper {
    private final Clock applicationClock;
    private final AddressMapper addressMapper;

    public StudentMapper(Clock applicationClock, AddressMapper addressMapper) {
        this.applicationClock = applicationClock;
        this.addressMapper = addressMapper;
    }

    public Student toEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.name());
        student.setEmail(normalizeEmail(dto.email()));
        student.setAddress(addressMapper.toEntity(dto.address()));
        return student;
    }

    public StudentResponseDTO toDto(Student entity) {
        return new StudentResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getBirthdate(),
            calculateAge(entity.getBirthdate()),
            entity.getAddress(),
            entity.getCreatedAt()
        );
    }

    private Integer calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now(applicationClock)).getYears();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
```

## Exception Patterns

### Domain-Specific Exceptions
```java
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

public class StudentAlreadyExistsException extends RuntimeException {
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Clock applicationClock;

    // 404 Not Found
    @ExceptionHandler({
        StudentNotFoundException.class,
        ParentNotFoundException.class,
        EventNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // 409 Conflict
    @ExceptionHandler({
        StudentAlreadyExistsException.class,
        ParentAlreadyExistsException.class,
        DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    // 400 Bad Request (validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return buildErrorResponse("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ApiError> buildErrorResponse(RuntimeException ex, HttpStatus status, HttpServletRequest request) {
        ApiError error = new ApiError(
            status.value(),
            status.getReasonPhrase(),
            ex.getClass().getSimpleName().replace("Exception", "").replaceAll("([A-Z])", "_$1").toUpperCase(),
            ex.getMessage(),
            request.getRequestURI(),
            Instant.now(applicationClock)
        );
        return ResponseEntity.status(status).body(error);
    }
}
```

## API Error Response Format
```json
{
  "status": 404,
  "error": "Not Found",
  "code": "STUDENT_NOT_FOUND",
  "message": "Aluno não encontrado",
  "path": "/v1/students/xxx-xxx",
  "timestamp": "2026-03-19T10:00:00Z"
}
```

## Test Patterns

### Test Class Structure
```java
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService tests")
class StudentServiceTest {
    private static final UUID STUDENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @Mock private StudentRepository studentRepo;
    @Mock private ParentRepository parentRepo;
    @Mock private StudentMapper studentMapper;

    @InjectMocks private StudentService studentService;
```

### Test Factory Methods
```java
private static StudentRequestDTO request(UUID parentId) {
    return new StudentRequestDTO(
        "Aluno Teste",
        LocalDate.of(2012, 5, 10),
        "123.456.789-01",
        "aluno@teste.com",
        addressRequest(),
        parentId
    );
}

private static Student student(Parent parent) {
    Student student = new Student();
    student.setId(STUDENT_ID);
    student.setName("Aluno Teste");
    student.setBirthdate(LocalDate.of(2012, 5, 10));
    student.setParent(parent);
    return student;
}
```

### Assertion Patterns
```java
// Standard assertions
assertThat(actual.getName()).isEqualTo("Expected Name");
assertThat(result.getId()).isNotNull();

// Exception assertions
assertThatThrownBy(() -> service.deleteStudent(STUDENT_ID))
    .isInstanceOf(StudentNotFoundException.class)
    .hasMessage("Aluno não encontrado");

// Null checks
assertThat(savedStudent.getCreatedAt()).isNotNull();
```

### Nested Test Classes
```java
@Nested
@DisplayName("Command methods")
class CommandMethods {
    @Test
    @DisplayName("should create student when valid data is provided")
    void shouldCreateStudentWhenValidDataIsProvided() { ... }
}

@Nested
@DisplayName("Query methods")
class QueryMethods {
    @Test
    @DisplayName("should return paginated students")
    void shouldReturnPaginatedStudents() { ... }
}
```

## HTTP Status Code Conventions

| Status | Use Case |
|--------|----------|
| 200 | Successful GET, PUT |
| 201 | Successful POST (create) |
| 204 | Successful DELETE, PATCH (no body) |
| 400 | Validation errors, invalid domain data |
| 404 | Entity not found |
| 409 | Conflict (duplicate, integrity violation) |
| 500 | Unexpected server errors |
