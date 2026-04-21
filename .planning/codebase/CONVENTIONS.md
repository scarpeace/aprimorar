# Convenções de Código

**Data da Análise:** 17-04-2026

## Padrões de Nomenclatura

**Arquivos:**
- As páginas React do frontend usam `*Page.tsx` em `client/src/features/**/pages/`, por exemplo, `client/src/features/students/pages/StudentsPage.tsx` e `client/src/features/events/pages/EventCreatePage.tsx`.
- Arquivos de UI reutilizáveis do frontend usam kebab-case em `client/src/components/ui/`, por exemplo, `client/src/components/ui/list-search-input.tsx` e `client/src/components/ui/page-loading.tsx`.
- Os hooks do frontend são baseados em funções e geralmente residem em `hooks/` com nomes de arquivos em kebab-case, por exemplo, `client/src/features/events/hooks/use-event-mutations.ts` e `client/src/features/students/hooks/student-mutations.ts`.
- Os esquemas do frontend seguem a nomenclatura `*Schema.ts`, por exemplo, `client/src/features/students/forms/studentFormSchema.ts` e `client/src/features/address/forms/addressSchema.ts`.
- Classes, registros (records) e testes do backend usam PascalCase em caminhos de pacotes espelhados, por exemplo, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` e `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`.

**Funções:**
- Componentes e auxiliares (helpers) do frontend usam `camelCase` ou `PascalCase` dependendo do tipo de exportação: `StudentsPage` em `client/src/features/students/pages/StudentsPage.tsx`, `getFriendlyErrorMessage` em `client/src/lib/shared/api-errors.ts`, e `useEmployeeMutations` em `client/src/features/employees/hooks/emlpoyee-mutations.ts`.
- Métodos do backend usam `camelCase` com verbos que revelam a intenção, como `createStudent`, `findById`, `archiveStudent`, e `ensureStudentUniqueness` em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Variáveis:**
- Usar nomes descritivos em `camelCase` para o estado local e resultados de consulta, como `searchTerm`, `currentPage`, `showArchived`, e `studentsQuery` em `client/src/features/students/pages/StudentsPage.tsx`.
- Serviços de backend preferem nomes explícitos como `normalizedContact`, `normalizedEmail`, `savedStudent`, e `studentPage` em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Tipos:**
- Props exportadas do frontend e tipos de esquema usam `PascalCase`, como `ListSearchInputProps` em `client/src/components/ui/list-search-input.tsx` e `StudentFormSchema` em `client/src/features/students/forms/studentFormSchema.ts`.
- Contratos de requisição/resposta do backend são registros (records) Java com `*RequestDTO`, `*ResponseDTO`, `*OptionsDTO`, e wrappers compartilhados como `PageDTO<T>` em `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`.

## Estilo de Código

**Formatação:**
- A formatação do frontend é imposta indiretamente através de `eslint .` em `client/package.json` e a configuração flat em `client/eslint.config.js`.
- O `prettier` está instalado em `client/package.json`, mas nenhum arquivo de configuração do Prettier foi detectado no diretório `client/`; siga o estilo do arquivo ao redor em vez de introduzir formatações em massa.
- O modo estrito do TypeScript está ativado em `client/tsconfig.app.json` com `strict: true`, enquanto variáveis locais e parâmetros não utilizados não bloqueiam a compilação (`noUnusedLocals: false`, `noUnusedParameters: false`).
- Nenhum formatador de backend dedicado ou configuração de lint foi detectado; o estilo do backend é estabelecido pelo layout de origem Java existente em `server/api-aprimorar/src/main/java/` e testes em `server/api-aprimorar/src/test/java/`.

**Linting:**
- O ESLint do frontend estende `@eslint/js`, `typescript-eslint`, `react-hooks`, e `react-refresh` em `client/eslint.config.js`.
- Avisos (warnings) do frontend são configurados para `@typescript-eslint/no-unused-vars` e `@typescript-eslint/no-explicit-any` em `client/eslint.config.js`; estes são tolerados como avisos, não como erros.
- Portões de qualidade do backend são orientados a testes através de Maven e JaCoCo em `server/api-aprimorar/pom.xml`; nenhuma configuração de Checkstyle, SpotBugs ou Spotless foi detectada.

## Organização de Importações

**Ordem:**
1. React/bibliotecas core, por exemplo, `client/src/App.tsx` e `client/src/main.tsx`.
2. Pacotes de terceiros, por exemplo, `@tanstack/react-query`, `react-router-dom`, `sonner`, e `lucide-react` em `client/src/features/employees/hooks/emlpoyee-mutations.ts` e `client/src/features/events/pages/EventCreatePage.tsx`.
3. Importações de alias `@/` para módulos compartilhados e entre funcionalidades, por exemplo, `@/components/layout/PageLayout` e `@/lib/shared/api-errors` em `client/src/features/students/pages/StudentCreatePage.tsx`.
4. Importações relativas para módulos da mesma funcionalidade, por exemplo, `../forms/studentFormSchema` em `client/src/features/students/pages/StudentCreatePage.tsx` e `../components/ContentSelectDropdown` em `client/src/features/events/pages/EventCreatePage.tsx`.

**Aliases de Caminho:**
- Usar o alias `@/*` mapeado para `client/src/*` em `client/tsconfig.app.json`.
- Preferir `import type` para importações de apenas tipos no frontend, como visto em `client/src/features/events/forms/EventForm.tsx`, `client/src/components/layout/PageLayout.tsx`, e `client/src/features/address/components/AddressDetails.tsx`.

## Tratamento de Erros

**Padrões:**
- Os tratadores de mutação do frontend exibem falhas através de notificações toast, geralmente com mensagens em português ou `getFriendlyErrorMessage(error)`, como em `client/src/features/events/hooks/use-event-mutations.ts`, `client/src/features/employees/hooks/emlpoyee-mutations.ts`, e `client/src/features/parents/hooks/parent-mutations.ts`.
- As falhas no nível da página do frontend são roteadas para estados de UI dedicados, como `ErrorBoundary` em `client/src/App.tsx` e componentes de erro compartilhados sob `client/src/components/ui/`.
- A validação de formulários do frontend deve fluir através de esquemas Zod e `zodResolver`, como em `client/src/features/students/pages/StudentCreatePage.tsx`, `client/src/features/events/pages/EventCreatePage.tsx`, e `client/src/features/students/forms/studentFormSchema.ts`.
- Os serviços do backend lançam exceções específicas de domínio e centralizam a formatação da resposta em `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- As mensagens de validação e de regras de negócio do backend são escritas em português, por exemplo, `"Aluno não encontrado no banco de dados"` em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` e respostas do tratador em `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.

## Logging

**Framework:**
- O logging do backend usa SLF4J via `LoggerFactory`, por exemplo, em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` e `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- O frontend ainda usa `console.error` em `client/src/lib/shared/api-errors.ts` e `client/src/lib/shared/api.ts`; ainda não há uma abstração de logging dedicada para o navegador.

**Padrões:**
- Os serviços do backend registram ações de negócio bem-sucedidas com contexto voltado para o usuário, como fluxos de create/update/archive/delete em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- O tratamento de exceções do backend registra erros centralmente em `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- O frontend deve preferir auxiliares de mensagens de erro compartilhados em vez de texto bruto lançado, usando `getFriendlyErrorMessage` de `client/src/lib/shared/api-errors.ts`.

## Comentários

**Quando Comentar:**
- O código existente mantém os comentários esparsos em arquivos de produção e os usa principalmente para TODOs ou rótulos de seção de teste, por exemplo, `//TODO implementar o logging mais pra frente` em `client/src/lib/shared/api-errors.ts` e `/* ----- Query Methods ----- */` em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Os testes às vezes usam comentários Arrange/Act/Assert para legibilidade, especialmente em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.

**JSDoc/TSDoc:**
- Não detectado nos arquivos de frontend amostrados sob `client/src/`.
- A documentação do backend é orientada por anotações em vez de ser pesada em Javadoc, usando anotações Swagger em controllers como `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.

## Design de Funções

**Tamanho:**
- Manter as páginas do frontend focadas em orquestração e mover a UI reutilizável para componentes filhos ou auxiliares de funcionalidade, conforme `client/src/features/students/pages/StudentsPage.tsx` e `client/src/features/events/pages/EventCreatePage.tsx`.
- Os serviços de backend mantêm os métodos públicos focados e movem as buscas repetidas para auxiliares privados, como `findStudentOrThrow` e `findParentOrThrow` em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

**Parâmetros:**
- As páginas de formulário do frontend tipam fortemente o `useForm` com tipos de entrada de esquema, por exemplo, `useForm<StudentFormSchema>` em `client/src/features/students/pages/StudentCreatePage.tsx`.
- Os controllers do backend aceitam registros DTO de requisição `@Valid` e variáveis de caminho `UUID`, como em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.

**Valores de Retorno:**
- Os hooks de mutação do frontend geralmente retornam um conjunto de objetos de mutação do TanStack Query, como em `client/src/features/employees/hooks/emlpoyee-mutations.ts` e `client/src/features/events/hooks/use-event-mutations.ts`.
- Os serviços de backend retornam DTOs ou `PageDTO<T>` para leituras e comandos, e `void` para ações de arquivamento/exclusão, como em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.

## Design de Módulos

**Exportações:**
- O frontend prefere exportações nomeadas para a maioria dos componentes, hooks e auxiliares, por exemplo, `StudentsPage`, `ListSearchInput` e `useEventMutations`.
- `export default` limita-se a arquivos especiais de entrada/configuração, como `client/src/App.tsx` e `client/eslint.config.js`.
- O backend usa um tipo público de nível superior por arquivo em `server/api-aprimorar/src/main/java/` e `server/api-aprimorar/src/test/java/`.

**Arquivos Barrel (index.ts):**
- Não foram detectados arquivos barrel escritos manualmente nas funcionalidades de frontend amostradas; as importações geralmente apontam diretamente para os arquivos de implementação.
- As exportações geradas sob `client/src/kubb/` são consumidas diretamente, por exemplo, `useGetStudents` em `client/src/features/students/pages/StudentsPage.tsx` e auxiliares de chaves de consulta em `client/src/features/employees/hooks/emlpoyee-mutations.ts`.

---

*Análise de convenções: 17-04-2026*
