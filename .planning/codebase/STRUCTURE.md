# Estrutura da Base de Código

**Data da Análise:** 17-04-2026

## Layout de Diretórios

```text
aprimorar/
├── client/                     # Workspace do SPA React 19 + Vite
│   ├── src/
│   │   ├── components/         # Layout compartilhado e primitivos de UI
│   │   ├── features/           # Módulos de frontend orientados ao domínio
│   │   ├── kubb/               # Hooks de API, tipos DTO e esquemas Zod gerados
│   │   └── lib/                # Infraestrutura e utilitários de frontend compartilhados
│   ├── kubb.config.ts          # Configuração de geração OpenAPI → cliente
│   ├── vite.config.ts          # Configuração do Vite com alias `@`
│   └── package.json            # Scripts e dependências do frontend
├── server/
│   └── api-aprimorar/          # Workspace da API Spring Boot
│       ├── src/main/java/com/aprimorar/api/
│       │   ├── config/         # Beans de configuração do Spring
│       │   ├── domain/         # Módulos de negócio por entidade/caso de uso
│       │   ├── exception/      # Payloads/handlers de exceção global
│       │   └── shared/         # Entidade base, wrapper de página, utilitários de mapper
│       ├── src/main/resources/ # Configuração do Spring, Flyway, dados de semente (seed)
│       ├── src/test/java/      # Testes de backend
│       └── pom.xml             # Build Maven e geração de OpenAPI
├── .planning/codebase/         # Documentos de mapeamento da base de código gerados
├── AGENTS.md                   # Instruções para todo o repositório
└── package.json                # Scripts de orquestração de desenvolvimento raiz
```

## Objetivos dos Diretórios

**`client/src/components/`:**
- Objetivo: Manter a UI compartilhada entre as áreas de negócio.
- Contém: Wrappers de layout e controles reutilizáveis.
- Arquivos chave: `client/src/components/layout/MainLayout.tsx`, `client/src/components/layout/PageLayout.tsx`, `client/src/components/ui/button.tsx`, `client/src/components/ui/pagination.tsx`, `client/src/components/ui/error-boundary.tsx`

**`client/src/features/`:**
- Objetivo: Agrupar o código do SPA por área de negócio.
- Contém: `pages/`, `components/`, `forms/` e `hooks/` da funcionalidade.
- Arquivos chave: `client/src/features/students/pages/StudentsPage.tsx`, `client/src/features/events/forms/EventForm.tsx`, `client/src/features/dashboard/DashboardPage.tsx`, `client/src/features/address/forms/addressSchema.ts`

**`client/src/kubb/`:**
- Objetivo: Armazenar artefatos de contrato de frontend gerados.
- Contém: `hooks/`, `types/`, `zod/`, `.kubb/` e um arquivo barrel grande.
- Arquivos chave: `client/src/kubb/index.ts`, `client/src/kubb/hooks/student/useGetStudents.ts`, `client/src/kubb/types/PageDTOStudentResponseDTO.ts`

**`client/src/lib/`:**
- Objetivo: Centralizar a infraestrutura de frontend que não é de propriedade de uma funcionalidade.
- Contém: Auxiliares de API compartilhados, configuração de cliente de consulta, hook de debounce, formatadores, configuração de validação.
- Arquivos chave: `client/src/lib/shared/queryClient.ts`, `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/lib/validations/zod.ts`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/domain/`:**
- Objetivo: Agrupar o código do backend por área de domínio.
- Contém: Um pacote por área de produto, geralmente com entidade, controller, serviço, mapper, `dto/`, `repository/` e `exception/`.
- Arquivos chave: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `.../parent/ParentController.java`, `.../event/repository/EventRepository.java`, `.../dashboard/DashboardService.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/config/`:**
- Objetivo: Manter beans do Spring de toda a aplicação e configuração MVC.
- Contém: Suporte a Clock, CORS e serialização de página.
- Arquivos chave: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/ClockConfig.java`, `.../WebCorsConfig.java`, `.../JacksonConfig.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/exception/`:**
- Objetivo: Centralizar os payloads de erro HTTP e o mapeamento.
- Contém: Enum de código de erro, handler global, DTO de resposta de problema.
- Arquivos chave: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`, `.../ProblemResponseDTO.java`, `.../ErrorCode.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/shared/`:**
- Objetivo: Deter os tipos de backend reutilizados entre domínios.
- Contém: Entidade JPA base, wrapper de página, auxiliares de normalização de mapper.
- Arquivos chave: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/BaseEntity.java`, `.../PageDTO.java`, `.../MapperUtils.java`

**`server/api-aprimorar/src/main/resources/`:**
- Objetivo: Manter a configuração de tempo de execução e artefatos de inicialização do banco de dados.
- Contém: `application.yml`, `application-dev.yml`, SQL do Flyway, SQL de semente (seed).
- Arquivos chave: `server/api-aprimorar/src/main/resources/application.yml`, `.../application-dev.yml`, `.../db/migration/V1__init.sql`, `.../data.sql`

**`server/api-aprimorar/src/test/java/`:**
- Objetivo: Deter os testes automatizados do backend.
- Contém: Testes de fumaça (smoke tests) da aplicação, testes de serviço/entidade de domínio, testes de handler de exceção.
- Arquivos chave: `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `.../event/EventServiceTest.java`, `.../exception/GlobalExceptionHandlerTest.java`

## Localizações de Arquivos Chave

**Pontos de Entrada:**
- `client/src/main.tsx`: Bootstrap do frontend e configuração do provedor.
- `client/src/App.tsx`: Roteador do frontend e registro de rotas lazy.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`: Ponto de entrada do Spring Boot do backend.
- `package.json`: Orquestração raiz para inicialização simultânea de backend/frontend.

**Configuração:**
- `client/vite.config.ts`: Configuração do plugin Vite e alias `@` para `client/src`.
- `client/kubb.config.ts`: Geração Kubb a partir do OpenAPI do backend.
- `client/eslint.config.js`: Regras de lint do frontend.
- `client/prettier.json`: Configurações do formatador do frontend.
- `server/api-aprimorar/pom.xml`: Build Maven, testes e configuração de geração OpenAPI.
- `server/api-aprimorar/src/main/resources/application.yml`: Perfil Spring padrão e configuração springdoc/flyway.
- `server/api-aprimorar/src/main/resources/application-dev.yml`: Configurações de datasource/JPA/logging de dev.

**Lógica Core:**
- `client/src/features/*`: Fluxos de negócio e páginas do frontend.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*`: Entidades, serviços, controllers, DTOs, repositórios do backend.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`: Contrato de paginação de backend compartilhado.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`: Tradução de erro de backend compartilhada.

**Testes:**
- `server/api-aprimorar/src/test/java/com/aprimorar/api/`: Raiz de testes do backend.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java`: Testes de comportamento de entidade.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`: Testes da camada de serviço.
- Testes de frontend: Não detectados em `client/`.

## Convenções de Nomenclatura

**Arquivos:**
- As páginas de rota do frontend usam `*Page.tsx`: `client/src/features/students/pages/StudentsPage.tsx`
- Os formulários de funcionalidades do frontend usam `*Schema.ts`/`*Form.tsx`: `client/src/features/students/forms/studentFormSchema.ts`, `client/src/features/events/forms/EventForm.tsx`
- Os hooks do frontend usam `use*` ou `*-mutations.ts`: `client/src/features/students/hooks/student-mutations.ts`, `client/src/lib/shared/use-debounce.ts`
- Controllers/serviços/mappers/entidades do backend usam nomes de domínio no singular com sufixos de papel: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`, `StudentService.java`, `StudentMapper.java`, `Student.java`
- DTOs do backend usam `*RequestDTO`, `*ResponseDTO`, `*OptionsDTO`: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java`

**Diretórios:**
- Os diretórios de negócio do frontend são funcionalidades no plural: `client/src/features/students`, `client/src/features/parents`
- As partes internas das funcionalidades de frontend usam subpastas estáveis: `pages/`, `components/`, `forms/`, `hooks/`
- Os diretórios de domínio do backend são substantivos no singular: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student`, `.../event`
- Pastas de suporte aninhadas do backend seguem nomes baseados em papéis: `dto/`, `repository/`, `exception/`

## Onde Adicionar Novo Código

**Nova página ou fluxo de funcionalidade do frontend:**
- Código primário: `client/src/features/<feature>/pages/`
- Componentes filhos específicos da funcionalidade: `client/src/features/<feature>/components/`
- Formulários específicos da funcionalidade e esquemas Zod: `client/src/features/<feature>/forms/`
- Wrappers de mutação em torno de hooks gerados: `client/src/features/<feature>/hooks/`

**Novo componente/módulo de frontend reutilizável:**
- Layout compartilhado: `client/src/components/layout/`
- Primitivo de UI reutilizável: `client/src/components/ui/`
- Auxiliares entre funcionalidades/configuração de consulta/auxiliares de API: `client/src/lib/shared/` ou `client/src/lib/utils/`

**Nova capacidade de domínio do backend:**
- Entidade/serviço/controller/mapper: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/`
- Registros (records) de requisição/resposta: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/dto/`
- Código de repositório/specification: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/repository/`
- Exceções específicas do domínio: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/exception/`

**Novo utilitário compartilhado ou infraestrutura de backend:**
- Auxiliares JPA/API compartilhados: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/`
- Beans de configuração de toda a aplicação/configuração MVC: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/`
- Comportamento de exceção global: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/`

**Nova mudança de persistência:**
- Migração de esquema: `server/api-aprimorar/src/main/resources/db/migration/`
- Ajustes de semente (seed) de desenvolvimento: `server/api-aprimorar/src/main/resources/data.sql`

**Novos testes:**
- Testes de domínio de backend: espelhar a estrutura de pacotes em `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/`
- Testes de frontend: nenhum local atual está estabelecido; introduzir um requer a adição de um executor de testes de frontend primeiro.

## Diretórios Especiais

**`client/src/kubb/`:**
- Objetivo: Código de cliente gerado a partir do OpenAPI do backend.
- Gerado: Sim
- Comitado: Sim

**`server/api-aprimorar/src/main/resources/db/migration/`:**
- Objetivo: Migrações de esquema Flyway versionadas.
- Gerado: Não
- Comitado: Sim

**`server/api-aprimorar/src/main/resources/data.sql/`:**
- Objetivo: Dados de semente de inicialização (bootstrap) de dev/teste carregados pela inicialização SQL do Spring no perfil de desenvolvimento.
- Gerado: Não
- Comitado: Sim

**`server/api-aprimorar/target/`:**
- Objetivo: Saída de build do Maven, artefatos OpenAPI gerados e relatórios.
- Gerado: Sim
- Comitado: Não

**`client/dist/`:**
- Objetivo: Saída do bundle de produção do Vite.
- Gerado: Sim
- Comitado: Não

**`.planning/codebase/`:**
- Objetivo: Documentos de mapeamento do repositório para outros fluxos de trabalho GSD.
- Gerado: Sim
- Comitado: Sim

---

*Análise de estrutura: 17-04-2026*
