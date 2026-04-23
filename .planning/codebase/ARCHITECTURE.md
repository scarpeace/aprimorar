# Arquitetura

**Data da Análise:** 17-04-2026

## Visão Geral dos Padrões

**Geral:** Monorepo full-stack com um limite de contrato gerado entre um backend Spring Boot e um SPA React.

**Principais Características:**
- Manter a lógica de negócios do backend em pacotes de domínio sob `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*` usando o fluxo controller → service → repository/mapper.
- Manter os fluxos de usuário do frontend dentro de pastas de funcionalidades (features) sob `client/src/features/*`, com rotas em `client/src/App.tsx` e primitivos compartilhados em `client/src/components/ui/*`.
- Tratar `client/src/kubb/*` como código de integração gerado a partir do OpenAPI do backend via `client/kubb.config.ts`; o código frontend escrito manualmente deve consumi-lo, não substituí-lo.

## Camadas

**Shell do Frontend e Roteamento:**
- Objetivo: Inicializar o SPA, instalar provedores globais e mapear URLs para páginas de funcionalidades.
- Localização: `client/src/main.tsx`, `client/src/App.tsx`, `client/src/components/layout/MainLayout.tsx`
- Contém: Renderização raiz do React, `QueryClientProvider`, `BrowserRouter`, registro de rotas lazy, layout compartilhado, toast/limites de erro (error boundaries).
- Depende de: `client/src/lib/shared/queryClient.ts`, `client/src/components/ui/error-boundary.tsx`, módulos de páginas de funcionalidades.
- Usado por: Todas as páginas sob `client/src/features/*`.

**Módulos de Funcionalidades do Frontend:**
- Objetivo: Organizar o comportamento do SPA por área de negócio.
- Localização: `client/src/features/students/*`, `client/src/features/parents/*`, `client/src/features/employees/*`, `client/src/features/events/*`, `client/src/features/dashboard/*`, `client/src/features/address/*`
- Contém: `pages/`, `components/`, `forms/`, `hooks/` por funcionalidade.
- Depende de: Hooks Kubb gerados em `client/src/kubb/hooks/*`, UI compartilhada em `client/src/components/ui/*`, auxiliares compartilhados em `client/src/lib/*`.
- Usado por: Definições de rotas em `client/src/App.tsx` e páginas de detalhes cruzadas entre funcionalidades, como `client/src/features/parents/pages/ParentDetailPage.tsx`.

**Camada de Integração do Frontend:**
- Objetivo: Centralizar os contratos da API e o comportamento compartilhado do cliente.
- Localização: `client/src/kubb/*`, `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/lib/shared/queryClient.ts`
- Contém: Hooks do React Query gerados, tipos DTO/Zod gerados, instância do Axios, padrões de consulta, formatação de erros da API.
- Depende de: OpenAPI do backend em `http://localhost:8080/v3/api-docs` configurado em `client/kubb.config.ts`.
- Usado por: Páginas de funcionalidades, wrappers de mutação e formulários em `client/src/features/*`.

**Camada HTTP do Backend:**
- Objetivo: Expor endpoints REST e mapear preocupações HTTP para os serviços.
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`, especialmente `StudentController.java`, `ParentController.java`, `EmployeeController.java`, `EventController.java`, `DashboardController.java`
- Contém: Classes `@RestController`, mapeamentos de caminho sob `/v1/*`, tratamento de parâmetros pagináveis/de consulta, validação de requisição, códigos de status.
- Depende de: Serviços de domínio, registros DTO, `com.aprimorar.api.shared.PageDTO`.
- Usado por: Clientes gerados pelo SPA em `client/src/kubb/hooks/*` e geração de OpenAPI a partir de `server/api-aprimorar/pom.xml`.

**Serviços de Domínio do Backend:**
- Objetivo: Deter as regras de negócio, orquestração, transações, normalização e efeitos colaterais do ciclo de vida.
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Service.java`
- Contém: Orquestração de CRUD, comportamento de arquivamento/desarquivamento, verificações de conflito, fluxos de reatribuição fantasma (ghost/phantom), agregação de dashboard.
- Depende de: Repositórios, mappers, utilitários compartilhados, `Clock`, exceções.
- Usado por: Apenas Controllers.

**Persistência e Mapeamento do Backend:**
- Objetivo: Persistir agregados e traduzir entidades em DTOs da API.
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Repository.java`, `*Specifications.java`, `*Mapper.java`, classes de entidade como `Student.java`, `Event.java`, `Parent.java`, `Employee.java`
- Contém: Entidades JPA, interfaces de repositório, consultas JPQL/specification, conversores DTO, objetos de valor (value objects) incorporados como `Address`.
- Depende de: Spring Data JPA, classes base compartilhadas em `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`.
- Usado por: Serviços de domínio.

**Infraestrutura Compartilhada do Backend:**
- Objetivo: Fornecer comportamento de tempo de execução entre domínios.
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`
- Contém: Configuração de CORS, bean do Clock, modo de serialização de página, mapeamento global de exceções, `BaseEntity`, `PageDTO`, auxiliares de normalização.
- Depende de: Recursos core/web/data do Spring Boot.
- Usado por: Todos os domínios do backend.

## Fluxo de Dados

**Fluxo de leitura/lista (página de lista do SPA para API paginada):**

1. Uma página de rota como `client/src/features/students/pages/StudentsPage.tsx` detém o estado da UI como `searchTerm`, `currentPage` e `showArchived`.
2. A página chama um hook gerado como `useGetStudents` do `client/src/kubb`, que usa o cliente compartilhado do React Query de `client/src/lib/shared/queryClient.ts`.
3. `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java` aceita parâmetros pagináveis/de consulta e os encaminha para `StudentService`.
4. `StudentService` compõe uma consulta `StudentSpecifications`, carrega as entidades via `StudentRepository`, as mapeia através do `StudentMapper` e retorna `PageDTO<StudentResponseDTO>` de `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`.
5. A página entrega o resultado para uma tabela de apresentação como `client/src/features/students/components/StudentsTable.tsx` mais a UI de paginação compartilhada em `client/src/components/ui/pagination.tsx`.

**Fluxo de escrita (formulário SPA para entidade persistida):**

1. Uma página de formulário como `client/src/features/students/pages/StudentCreatePage.tsx` valida a entrada do usuário com esquemas Zod como `client/src/features/students/forms/studentFormSchema.ts`.
2. A página envia através de um wrapper de mutação de funcionalidade como `client/src/features/students/hooks/student-mutations.ts`, que delega para mutações Kubb geradas e invalida as chaves de consulta.
3. O controller do backend valida o registro DTO da requisição, por exemplo, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`.
4. O serviço normaliza os dados com `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/MapperUtils.java`, resolve entidades relacionadas, impõe regras de unicidade/conflito e salva através do repositório.
5. O mapper converte a entidade em um DTO de resposta, e o frontend redireciona/invalida os caches em caso de sucesso.

**Fluxo de regeneração de contrato:**

1. Controllers e DTOs do backend definem a superfície da API sob `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*`.
2. O Springdoc expõe `/v3/api-docs` conforme configurado em `server/api-aprimorar/src/main/resources/application.yml`.
3. O perfil Maven `generate-openapi` em `server/api-aprimorar/pom.xml` e a configuração Kubb em `client/kubb.config.ts` geram os artefatos do contrato.
4. Os arquivos gerados chegam em `client/src/kubb/*` e são importados pelo código de funcionalidade escrito manualmente.

**Gerenciamento de Estado:**
- O estado do servidor reside no TanStack Query via `client/src/lib/shared/queryClient.ts` e hooks gerados em `client/src/kubb/hooks/*`.
- O estado da UI local da rota permanece dentro dos componentes da página com `useState`, como o estado de paginação/busca em `client/src/features/students/pages/StudentsPage.tsx` e `client/src/features/employees/pages/EmployeesPage.tsx`.
- O estado de persistência do backend reside no PostgreSQL com migrações Flyway de `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql` e dados de semente (seed) de desenvolvimento em `server/api-aprimorar/src/main/resources/data.sql`.

## Principais Abstrações

**Pasta de funcionalidade (feature folder):**
- Objetivo: Manter a orquestração da UI, widgets específicos da funcionalidade e lógica de formulário juntos.
- Exemplos: `client/src/features/students/*`, `client/src/features/events/*`, `client/src/features/parents/*`
- Padrão: `pages/` orquestram o carregamento de dados, `components/` renderizam a UI do domínio, `forms/` detêm os esquemas/composição do formulário, `hooks/` envolvem as mutações geradas.

**Limite da API Gerada:**
- Objetivo: Tornar o contrato do backend a fonte da verdade para os tipos e hooks do cliente.
- Exemplos: `client/src/kubb/index.ts`, `client/src/kubb/hooks/student/useGetStudents.ts`, `client/src/kubb/zod/studentRequestDTOSchema.ts`
- Padrão: Gerar a partir do OpenAPI e consumir a partir do código escrito manualmente; não coloque lógica de negócios customizada dentro dos arquivos gerados.

**Agregado/Entidade de Domínio:**
- Objetivo: Representar registros core de negócio com validação e campos de ciclo de vida.
- Exemplos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java`, `.../event/Event.java`, `.../parent/Parent.java`, `.../employee/Employee.java`
- Padrão: As entidades estendem `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/BaseEntity.java`, mantêm métodos de mutação como `updateDetails(...)` e impõem invariantes internamente.

**Wrapper PageDTO:**
- Objetivo: Padronizar as respostas paginadas enviadas para o SPA.
- Exemplos: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`, tipos de frontend gerados como `client/src/kubb/types/PageDTOStudentResponseDTO.ts`
- Padrão: Controllers/serviços retornam `PageDTO<T>` em vez do JSON bruto `Page` do Spring.

**Abstração de consulta/specification do Repositório:**
- Objetivo: Manter a filtragem dinâmica e os planos de busca otimizados fora dos controllers/serviços.
- Exemplos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java`, `.../event/repository/EventRepository.java`, `.../parent/repository/ParentSpecifications.java`
- Padrão: Usar Spring Data Specifications para filtros de texto/arquivo e JPQL no nível do repositório para conflitos de agenda, agregações e atualizações de reatribuição.

**Fallback de registro fantasma (ghost/phantom):**
- Objetivo: Preservar a integridade referencial de eventos ao excluir alunos ou funcionários vinculados.
- Exemplos: constantes em `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` e `.../employee/EmployeeService.java`, consultas de reatribuição em `.../event/repository/EventRepository.java`
- Padrão: Os serviços reatribuem referências de chave estrangeira antes de excluir os registros primários.

## Pontos de Entrada

**Bootstrap do SPA:**
- Localização: `client/src/main.tsx`
- Gatilhos: Inicialização do navegador via Vite.
- Responsabilidades: Montar o React, instalar o `QueryClientProvider`, carregar o CSS global e a configuração de localidade do Zod.

**Roteador do SPA:**
- Localização: `client/src/App.tsx`
- Gatilhos: Navegação no navegador dentro do `BrowserRouter`.
- Responsabilidades: Carregar as páginas de funcionalidades de forma lazy, envolver o app em `ErrorBoundary` e `Suspense`, e anexar o `MainLayout` a todas as rotas principais.

**Aplicação Backend:**
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- Gatilhos: Inicialização do Spring Boot.
- Responsabilidades: Iniciar o contexto da aplicação API.

**Superfície REST do Backend:**
- Localização: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`
- Gatilhos: Requisições HTTP para `/v1/students`, `/v1/parents`, `/v1/employees`, `/v1/events`, `/v1/dashboard/summary`.
- Responsabilidades: Vincular dados da requisição, chamar serviços e moldar as respostas HTTP.

**Pipeline de contrato/codegen:**
- Localização: `server/api-aprimorar/pom.xml`, `client/kubb.config.ts`, `package.json` raiz.
- Gatilhos: `./mvnw -Pgenerate-openapi generate-resources`, `npm run sync`, `npm run start:frontend`.
- Responsabilidades: Publicar a documentação do backend e regenerar `client/src/kubb/*`.

## Tratamento de Erros

**Estratégia:** Mapeamento centralizado de exceções no backend mais fallbacks de página/componente no frontend.

**Padrões:**
- Os serviços do backend lançam exceções específicas de domínio de pacotes como `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/exception/*` e `.../event/exception/*`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java` converte exceções em payloads `ProblemResponseDTO`.
- As páginas do frontend fazem curto-circuito para os componentes `ErrorCard`/`LoadingCard`, como em `client/src/features/parents/pages/ParentDetailPage.tsx` e `client/src/features/dashboard/DashboardPage.tsx`.
- Um limite de classe React global em `client/src/components/ui/error-boundary.tsx` captura falhas em tempo de renderização fora do tratamento no nível da consulta (query).

## Preocupações Transversais (Cross-Cutting Concerns)

**Logging:** Os serviços/controllers do backend registram eventos de negócio com SLF4J em arquivos como `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` e `.../event/EventService.java`; os auxiliares de API do frontend atualmente registram via `console.error` em `client/src/lib/shared/api.ts` e `client/src/lib/shared/api-errors.ts`.

**Validação:** Os DTOs de requisição do backend usam anotações de Bean Validation em arquivos como `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java` e `.../event/dto/EventRequestDTO.java`; os formulários do frontend estendem esquemas gerados com Zod em arquivos como `client/src/features/students/forms/studentFormSchema.ts` e `client/src/features/address/forms/addressSchema.ts`.

**Autenticação:** Não detectada. O SPA chama a API diretamente através de hooks gerados e Axios sem middleware de autenticação, e o backend expõe controllers sem configuração de Spring Security.

---

*Análise de arquitetura: 17-04-2026*
