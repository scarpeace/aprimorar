## Context

O servidor usa pacotes planos sob `com.aprimorar.api.domain.*` sem qualquer enforcement de boundaries. O Spring Modulith oferece verificação baseada em convenção de pacotes: cada subpacote imediato de `com.aprimorar.api` é tratado como um módulo, e módulos só podem acessar o subpacote `api/` de outros módulos.

## Goals / Non-Goals

**Goals:**
- Adicionar `spring-modulith-starter-core` e `spring-modulith-starter-test` ao projeto
- Criar `package-info.java` em cada módulo declarando dependências
- Configurar shared packages (`shared`, `enums`, `exception`, `config`)
- Rodar primeira verificação e registrar violações como dependências permitidas temporariamente
- Integrar verificação ao ciclo de build (test phase)

**Non-Goals:**
- Resolver as violações — isso é escopo das fases 2-5
- Mudar código de negócio existente
- Criar testes de módulo (será na fase 5)

## Decisions

### 1. Módulos definidos por subpacote direto

Os seguintes subpacotes de `com.aprimorar.api` serão módulos:

| Módulo | Pacote | Dependências permitidas (iniciais) |
|--------|--------|-----------------------------------|
| auth | `.auth` | employee (JPA @OneToOne) |
| student | `.student` | address, parent, event |
| parent | `.parent` | student |
| employee | `.employee` | auth, event |
| event | `.event` | student, employee, finance |
| finance | `.finance` | event, student, employee |
| dashboard | `.dashboard` | event |
| address | `.address` | (nenhuma) |

**Rationale:** Mapeamento direto da estrutura existente. As dependências refletem os acessos reais (que serão progressivamente eliminados).

### 2. Shared packages

Os seguintes pacotes são compartilhados (isentos de verificação):

| Pacote | Conteúdo |
|--------|----------|
| `com.aprimorar.api.shared` | BaseEntity, MapperUtils, PageDTO |
| `com.aprimorar.api.enums` | Todos os enums compartilhados |
| `com.aprimorar.api.exception` | GlobalExceptionHandler, ProblemResponseDTO, ErrorCode |
| `com.aprimorar.api.config` | Configurações Spring (security, CORS, Jackson, clock, password encoder) |

**Rationale:** Esses pacotes contêm infraestrutura transversal que não pertence a nenhum módulo de domínio específico.

### 3. package-info.java por módulo

Cada módulo recebe um `package-info.java` com `@ApplicationModule(displayName = "...", allowedDependencies = {...})`. As dependências iniciais são permissivas (refletem o estado atual) e serão restritas a cada fase.

**Alternatives considered:**
- Usar `module-info.java` do Java (JPMS): muito mais restritivo, quebraria Spring Boot + Hibernate + bibliotecas.
- Usar apenas ArchUnit sem Modulith: perderia a integração nativa com Spring, documentação automática, e `@ModuleTest`.

### 4. Base package adjustment

O pacote base `com.aprimorar.api` contém a classe principal `ApiAprimorarApplication`. Ela precisa ficar acessível. O pacote raiz não é um módulo — é o "application module" por padrão.

## Risks / Trade-offs

- [Violações descobertas na primeira verificação] → Registrar como `allowedDependencies` temporárias. Serão removidas ao longo das fases 2-5.
- [package-info.java entra em conflito com convenção de lint] → Verificar se o compilador/lint aceita sem configuração adicional.
- [Testes podem quebrar se o contexto Spring mudar] → Ajustar `@SpringBootTest` para escanear pacotes corretamente.
