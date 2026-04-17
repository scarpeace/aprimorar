# Coding Conventions

**Analysis Date:** 2026-04-17

## Naming Patterns

**Files:**
- Frontend React pages use `*Page.tsx` under `client/src/features/**/pages/`, for example `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/events/pages/EventCreatePage.tsx`.
- Frontend reusable UI files are kebab-case under `client/src/components/ui/`, for example `client/src/components/ui/list-search-input.tsx` and `client/src/components/ui/page-loading.tsx`.
- Frontend hooks are function-based and usually live in `hooks/` with kebab-case filenames, for example `client/src/features/events/hooks/use-event-mutations.ts` and `client/src/features/students/hooks/student-mutations.ts`.
- Frontend schemas follow `*Schema.ts` naming, for example `client/src/features/students/forms/studentFormSchema.ts` and `client/src/features/address/forms/addressSchema.ts`.
- Backend classes, records, and tests use PascalCase in mirrored package paths, for example `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`.

**Functions:**
- Frontend components and helpers use `camelCase` or `PascalCase` depending on export type: `StudentsPage` in `client/src/features/students/pages/StudentsPage.tsx`, `getFriendlyErrorMessage` in `client/src/lib/shared/api-errors.ts`, and `useEmployeeMutations` in `client/src/features/employees/hooks/emlpoyee-mutations.ts`.
- Backend methods use `camelCase` with intention-revealing verbs, such as `createStudent`, `findById`, `archiveStudent`, and `ensureStudentUniqueness` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Variables:**
- Use descriptive `camelCase` names for local state and query results, such as `searchTerm`, `currentPage`, `showArchived`, and `studentsQuery` in `client/src/features/students/pages/StudentsPage.tsx`.
- Backend services prefer explicit names like `normalizedContact`, `normalizedEmail`, `savedStudent`, and `studentPage` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Types:**
- Frontend exported props and schema types use `PascalCase`, such as `ListSearchInputProps` in `client/src/components/ui/list-search-input.tsx` and `StudentFormSchema` in `client/src/features/students/forms/studentFormSchema.ts`.
- Backend request/response contracts are Java records with `*RequestDTO`, `*ResponseDTO`, `*OptionsDTO`, and shared wrappers like `PageDTO<T>` in `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`.

## Code Style

**Formatting:**
- Frontend formatting is enforced indirectly through `eslint .` in `client/package.json` and the flat config in `client/eslint.config.js`.
- `prettier` is installed in `client/package.json`, but no checked-in Prettier config file was detected under `client/`; follow the surrounding file style instead of introducing mass reformatting.
- TypeScript strict mode is enabled in `client/tsconfig.app.json` with `strict: true`, while unused locals and parameters are not compile-blocking (`noUnusedLocals: false`, `noUnusedParameters: false`).
- No dedicated backend formatter or lint config was detected; backend style is established by the existing Java source layout in `server/api-aprimorar/src/main/java/` and tests in `server/api-aprimorar/src/test/java/`.

**Linting:**
- Frontend ESLint extends `@eslint/js`, `typescript-eslint`, `react-hooks`, and `react-refresh` in `client/eslint.config.js`.
- Frontend warnings are configured for `@typescript-eslint/no-unused-vars` and `@typescript-eslint/no-explicit-any` in `client/eslint.config.js`; these are tolerated as warnings, not errors.
- Backend quality gates are test-oriented through Maven and JaCoCo in `server/api-aprimorar/pom.xml`; no Checkstyle, SpotBugs, or Spotless config was detected.

## Import Organization

**Order:**
1. React/core libraries, for example `client/src/App.tsx` and `client/src/main.tsx`.
2. Third-party packages, for example `@tanstack/react-query`, `react-router-dom`, `sonner`, and `lucide-react` in `client/src/features/employees/hooks/emlpoyee-mutations.ts` and `client/src/features/events/pages/EventCreatePage.tsx`.
3. `@/` alias imports for shared and cross-feature modules, for example `@/components/layout/PageLayout` and `@/lib/shared/api-errors` in `client/src/features/students/pages/StudentCreatePage.tsx`.
4. Relative imports for same-feature modules, for example `../forms/studentFormSchema` in `client/src/features/students/pages/StudentCreatePage.tsx` and `../components/ContentSelectDropdown` in `client/src/features/events/pages/EventCreatePage.tsx`.

**Path Aliases:**
- Use the `@/*` alias mapped to `client/src/*` in `client/tsconfig.app.json`.
- Prefer `import type` for type-only frontend imports, as seen in `client/src/features/events/forms/EventForm.tsx`, `client/src/components/layout/PageLayout.tsx`, and `client/src/features/address/components/AddressDetails.tsx`.

## Error Handling

**Patterns:**
- Frontend mutation handlers surface failures through toast notifications, usually with Portuguese messages or `getFriendlyErrorMessage(error)`, as in `client/src/features/events/hooks/use-event-mutations.ts`, `client/src/features/employees/hooks/emlpoyee-mutations.ts`, and `client/src/features/parents/hooks/parent-mutations.ts`.
- Frontend page-level failures are routed into dedicated UI states such as `ErrorBoundary` in `client/src/App.tsx` and shared error components under `client/src/components/ui/`.
- Frontend form validation should flow through Zod schemas and `zodResolver`, as in `client/src/features/students/pages/StudentCreatePage.tsx`, `client/src/features/events/pages/EventCreatePage.tsx`, and `client/src/features/students/forms/studentFormSchema.ts`.
- Backend services throw domain-specific exceptions and centralize response shaping in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Backend validation and business-rule messages are written in Portuguese, for example `"Aluno não encontrado no banco de dados"` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and handler responses in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.

## Logging

**Framework:**
- Backend logging uses SLF4J via `LoggerFactory`, for example in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Frontend still uses `console.error` in `client/src/lib/shared/api-errors.ts` and `client/src/lib/shared/api.ts`; there is no dedicated browser logging abstraction yet.

**Patterns:**
- Backend services log successful business actions with user-facing context, such as create/update/archive/delete flows in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Backend exception handling logs errors centrally in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Frontend should prefer shared error-message helpers over raw thrown text, using `getFriendlyErrorMessage` from `client/src/lib/shared/api-errors.ts`.

## Comments

**When to Comment:**
- Existing code keeps comments sparse in production files and uses them mainly for TODOs or test section labels, for example `//TODO implementar o logging mais pra frente` in `client/src/lib/shared/api-errors.ts` and `/* ----- Query Methods ----- */` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Tests sometimes use Arrange/Act/Assert comments for readability, especially in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.

**JSDoc/TSDoc:**
- Not detected in the sampled frontend files under `client/src/`.
- Backend documentation is annotation-driven rather than Javadoc-heavy, using Swagger annotations in controllers such as `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.

## Function Design

**Size:**
- Keep frontend pages orchestration-focused and push reusable UI into child components or feature helpers, matching `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/events/pages/EventCreatePage.tsx`.
- Backend services keep public methods focused and move repeated lookups into private helpers such as `findStudentOrThrow` and `findParentOrThrow` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Parameters:**
- Frontend form pages strongly type `useForm` with schema input types, for example `useForm<StudentFormSchema>` in `client/src/features/students/pages/StudentCreatePage.tsx`.
- Backend controllers accept `@Valid` request DTO records and `UUID` path variables, as in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.

**Return Values:**
- Frontend mutation hooks usually return a bag of TanStack Query mutation objects, as in `client/src/features/employees/hooks/emlpoyee-mutations.ts` and `client/src/features/events/hooks/use-event-mutations.ts`.
- Backend services return DTOs or `PageDTO<T>` for reads and commands, and `void` for archive/delete actions, as in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

## Module Design

**Exports:**
- Frontend prefers named exports for most components, hooks, and helpers, for example `StudentsPage`, `ListSearchInput`, and `useEventMutations`.
- `export default` is limited to special entry/config files such as `client/src/App.tsx` and `client/eslint.config.js`.
- Backend uses one public top-level type per file throughout `server/api-aprimorar/src/main/java/` and `server/api-aprimorar/src/test/java/`.

**Barrel Files:**
- No handwritten barrel files were detected in the sampled frontend features; imports usually point directly to implementation files.
- Generated exports under `client/src/kubb/` are consumed directly, for example `useGetStudents` in `client/src/features/students/pages/StudentsPage.tsx` and query-key helpers in `client/src/features/employees/hooks/emlpoyee-mutations.ts`.

---

*Convention analysis: 2026-04-17*
