# AGENTS.md

Operational guide for coding agents working in the Aprimorar repository.

## Project Overview
- **Architecture**: React SPA (`client/`) + Spring Boot API (`server/api-aprimorar/`)
- **Database**: PostgreSQL (Docker Compose for development)
- **Language**: Portuguese for all user-facing text, validation, and error messages
- **Java**: 21 | **React**: 19 | **TypeScript**: 5.9 | **Spring Boot**: 3.5

## Build Commands

### Frontend (`client/`)
```bash
npm install              # Install dependencies
npm run dev               # Dev server (http://localhost:5173)
npm run lint              # ESLint all code
npm run build             # TypeScript check + production bundle
npm run preview           # Preview production build
```

### Backend (`server/api-aprimorar/`)
```bash
# Database
docker compose up -d db   # Start Postgres
docker compose down        # Stop Postgres

# Running
./mvnw spring-boot:run      # Run API (http://localhost:8080)
./mvnw test                # Run all tests
./mvnw package             # Build JAR
./mvnw verify              # Full verification lifecycle (tests + verify)

# Single test execution
./mvnw -Dtest=StudentServiceTest test                    # Single class
./mvnw -Dtest=StudentServiceTest#methodName test          # Single method
./mvnw -Dtest=*ServiceTest test                           # All service tests
./mvnw -Dtest=ApiAprimorarApplicationTests test           # Smoke test
```

## Repository Structure
```
aprimorar/
├── client/                          # React 19 + TypeScript + Vite
│   ├── src/features/                # Feature modules (pages, components, hooks)
│   ├── src/components/ui/           # Reusable UI primitives
│   ├── src/lib/                    # Schemas, API clients, query setup
│   └── src/services/               # API wrappers
└── server/api-aprimorar/
    └── src/main/java/.../domain/   # Domain modules (entity/controller/service/...)
```

## Change Discipline
- Keep diffs scoped; avoid unrelated refactors
- For new persisted fields: migration + entity + DTO + mapper + API consumer
- Do not rename DTO fields/endpoints without full-stack alignment
- Verify changes: run tests (`./mvnw test`) and build (`npm run build`)

## Cursor/Copilot Rules Status
Checked locations (no files found):
- `.cursorrules`, `.cursor/rules/`, `.github/copilot-instructions.md`

## MCP Tools
Use `context7` to search up-to-date library docs when needed.

---

## Frontend Code Style

### Import Order
1. React/core imports
2. Third-party libraries (react-router-dom, lucide-react, react-hook-form)
3. `@/` alias imports (shared components, lib utilities)
4. Relative imports (same-feature)

```typescript
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { useStudentQueries } from "./query/useStudentQueries"
```

### Component Patterns
- **Named exports** only (no `export default`)
- Props as interfaces at top of file
- Variant pattern for styled components:
```tsx
type ButtonVariant = "primary" | "secondary" | "error" | "outline" | "ghost"
const VARIANT_CLASSES: Record<ButtonVariant, string> = { ... }
```

### React Query
```typescript
// Query keys centralized
export const queryKeys = {
  students: {
    all: ["students"] as const,
    list: (params) => ["students", "list", params] as const,
    detail: (id) => ["students", "detail", id] as const,
  },
}

// Use keepPreviousData for pagination
import { keepPreviousData } from "@tanstack/react-query"
```

### Form Validation
- Zod schemas per domain in `lib/schemas/`
- react-hook-form with zod-resolver
- Mode: `"onBlur"`

---

## Backend Code Style

### Layer Responsibilities
| Layer | Responsibility |
|-------|----------------|
| Controller | HTTP (request/response, status codes) |
| Service | Business logic, transactions |
| Repository | Data access, queries |
| Mapper | DTO <-> Entity conversion |

### Service Patterns
```java
@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepo, StudentMapper studentMapper) {
        this.studentRepo = studentRepo;
        this.studentMapper = studentMapper;
    }

    @Transactional(readOnly = true)  // Queries
    public StudentResponseDTO getStudent(UUID id) { ... }

    @Transactional                     // Commands
    public StudentResponseDTO create(StudentRequestDTO dto) { ... }
}
```

### DTOs as Records
```java
public record StudentRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotNull @Valid AddressRequestDTO address
) {}
```

### Entity Base
```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
```

### Exception Handling
- Domain-specific exceptions (e.g., `StudentNotFoundException`)
- Global `@RestControllerAdvice` handler
- HTTP status codes: 200 (GET/PUT), 201 (POST), 204 (DELETE), 400 (validation), 404, 409

---

## Sub-AGENTS Files
Read the relevant sub-AGENTS.md for complete patterns:
- `client/AGENTS.md` - React, TypeScript, React Query, forms, API patterns
- `server/api-aprimorar/AGENTS.md` - Spring Boot, JPA, tests, DTOs, mappers
