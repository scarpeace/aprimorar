# Aprimorar — AGENTS.md

Documento operacional do repositório. Mantenha este arquivo atualizado quando a estrutura, o fluxo de trabalho ou os padrões principais mudarem.

## Estrutura atual

- `server/`: backend Spring Boot
- `client/`: frontend Next.js

## Regra básica

- backend: trabalhar em `server/`
- frontend: trabalhar em `client/`
- contrato da API: backend primeiro, frontend depois

## Backend

### Stack

- Java 21
- Spring Boot 3.5
- Spring MVC
- Spring Security
- Spring Data JPA
- Flyway
- Spring Modulith
- PostgreSQL
- `com.auth0:java-jwt` para emissão e validação de JWT

### Convenções

- controller recebe e delega
- service concentra regra de negócio
- repository faz acesso a dados
- DTO na borda HTTP
- entidade JPA não vaza para API
- mudança de schema sempre via Flyway
- dependência entre módulos só pelos contratos permitidos no `package-info.java`
- contratos públicos ficam na raiz do módulo; implementação, persistência e HTTP
  ficam nos subpacotes internos
- conversão entre entidade, contrato e DTO deve ser explícita
- evitar abstrações sem mais de um uso real

Estrutura atual dos módulos:

```text
aprimorar/
├── auth/
├── atendimentos/
│   └── individuais/
│       ├── domain/*.java
│       │   └── enums/
│       ├── repository/{atendimento,cobranca,repasse,view}
│       └── web/controller/AtendimentoIndividualController.java
├── pessoas/
│   ├── aluno/
│   │   └── api/
│   ├── colaborador/
│   │   └── api/
│   └── shared/endereco/
├── financeiro/
│   ├── despesas/
│   └── cobranca_aluno/
├── common/
└── config/
```

- `auth` concentra login e gerenciamento de usuários.
- `atendimentos/individuais` concentra o fluxo de atendimento, cobrança e
  repasse individuais; há um controller HTTP único e services separados para
  escrita e leitura pela view.
- `pessoas` expõe `AlunoService` e `ColaboradorService` em `api`; esses
  contratos oferecem apenas verificação de existência por ID. Cada subdomínio
  possui seu próprio `service` e `config`.
- `pessoas/shared/endereco` contém o value object `Endereco` e seus DTOs; não
  é uma entidade nem possui ciclo de vida próprio.
- `Responsavel` é um value object embutido em `AlunoEntity`, sem tabela própria.
- `despesas` registra gastos operacionais independentes de atendimento.
- `common` é aberto para modelos, utilitários e anotações compartilhadas.
- `config` contém configuração transversal, não regras de domínio.

### Comandos úteis

Dentro de `server/`:

```bash
./mvnw spring-boot:run
./mvnw clean compile
./mvnw test
./mvnw clean test
./mvnw test -Dtest=ModuleVerificationTest
./sonar.sh
```

### Observações do domínio

- `AtendimentoIndividualEntity` armazena `alunoId` e `colaboradorId` como escalares, sem
  relações JPA com outros módulos
- a criação, atualização e exclusão do atendimento, da cobrança e do repasse
  individual acontecem no `AtendimentoIndividualService`
- `AlunoEntity` e `ColaboradorEntity` usam `Endereco` com `@Embedded`
- `AlunoEntity` usa `Responsavel` com `@Embedded`; não existe tabela ou ID próprio
  para responsável
- o valor da cobrança vive em `cobrancas_individuais`; o valor do repasse vive
  em `repasses_individuais`
- alunos e colaboradores não são excluídos; o campo `ativo` controla ativação e
  desativação
- não existem registros ou realocação para aluno, colaborador ou responsável fantasma
- exceções de negócio são específicas de cada módulo e tratadas pelo handler do
  próprio módulo

### Erros HTTP

- respostas de erro usam `org.springframework.http.ProblemDetail`
- `GlobalExceptionHandler` em `aprimorar.config` tem baixa precedência e trata
  apenas erros transversais
- handlers de `auth`, `pessoas/aluno`, `pessoas/colaborador`,
  `atendimentos/individuais` e `financeiro/despesas` ficam nos pacotes dos
  respectivos subdomínios e tratam as exceções próprias de cada domínio
- anotações OpenAPI reutilizáveis ficam em `common/openapi`

### Autenticação

- a aplicação é stateless e usa `auth/config/JwtAuthenticationFilter`
- `JwtService` usa `com.auth0:java-jwt` com HS256
- `JWT_SECRET` é texto bruto, obrigatório e deve ter ao menos 32 bytes em UTF-8
- o token usa issuer `aprimorar-api`, UUID no `subject` e expiração de oito horas
- authorities são carregadas do usuário no banco, não de claims do token
- `UserEntity` implementa `UserDetails`
- `AuthService` implementa `UserDetailsService` e usa `DaoAuthenticationProvider`
  com BCrypt
- login fica em `POST /v1/auth/login`; gerenciamento de usuários fica em
  `/v1/auth/users` e exige role `ADMIN`
- o usuário administrador é sincronizado no boot usando
  `APP_ADMIN_USERNAME` e `APP_ADMIN_PASSWORD`
- não usar OAuth2 Resource Server, Nimbus, JWKS ou provedores externos

### Testes

- testes unitários de service usam Mockito e ficam próximos ao pacote testado
- não misturar teste de `Specification` dentro de teste de service
- preferir teste pequeno por regra de negócio relevante
- testar o JWT sem expor segredos
- warnings de JaCoCo/ByteBuddy sobre instrumentação podem aparecer no sandbox; considerar o exit code do Maven
- após mudanças de segurança ou configuração, executar ao menos `compile` e
  `test-compile`; rodar a suíte completa quando possível

## Frontend

### Stack

- Next.js App Router
- React Query
- React Hook Form
- Zod
- DaisyUI
- Kubb
- date-fns

### Convenções

- `page.tsx` compõe a página
- componentes cuidam da UI e interação local
- hooks de mutação concentram invalidation/toast/efeitos
- usar tipos e hooks gerados sempre que possível
- não editar manualmente `src/lib/api/generated/`
- formulários usam RHF + `FormProvider` quando distribuídos
- inputs reutilizáveis ficam em `src/components/ui/forms/`
- datas usam `date-fns` ou helpers em `src/lib/utils/date-utils`
- backend continua responsável por sanitização e normalização final

### Padrões visuais já consolidados

- detalhes de aluno e colaborador:
  - cabeçalho simples
  - bloco de dados
  - filtro por período com inputs `date`
  - cards de resumo financeiro acima da tabela
  - tabela de atendimentos vinculados
- tabela de atendimentos vinculados:
  - filtros aplicam direto quando alterados
  - busca pode usar debounce
  - pagamento/repasse usa toggle do DaisyUI
- ações:
  - editar: `primary`
  - arquivar: `warning`
  - excluir: `error`
- tabelas de detalhe preferem linha clicável em vez de botão extra
- calendário usa visualização cheia só a partir de `lg`; abaixo disso usa lista

### Comandos úteis

Dentro de `client/`:

```bash
npm run dev
npm run lint
npm run build
npm run sync
```

### Observações

- `npm run sync` depende do backend em `http://localhost:8080/v3/api-docs`
- PDF autenticado deve passar pelo proxy em `src/app/api/proxy/[...path]/route.ts`
- evitar estado global sem necessidade real

## Fluxo backend -> frontend

Quando o backend mudar contrato:

1. subir o backend
2. rodar `npm run sync` em `client/`
3. ajustar a feature no frontend

## Infra local

Dentro de `server/`:

```bash
docker compose up -d db
docker compose up -d sonarqube
```

Banco local esperado no profile `dev`:

- host: `localhost`
- porta: `5432`
- database: `aprimorar`
- user: `myuser`
- password: `mypassword`

SonarQube local:

- dashboard: `http://localhost:9000`
- script de análise: `server/sonar.sh`
- token fica em `.env.local` ou `.env` como `SONAR_TOKEN`

Segredos, tokens e senhas devem permanecer em `.env`/`.env.local`; nunca os
copie para documentação, logs ou respostas de diagnóstico.

## O que costuma quebrar

- contrato gerado desatualizado
- import antigo de tipo gerado depois de mudança no OpenAPI
- migration Flyway editada depois de aplicada ou com versão duplicada
- token antigo usado depois da troca da chave efetiva do JWT
- teste de contexto iniciado sem as variáveis de ambiente necessárias
- lógica demais em um único componente de tela
- script antigo da raiz sendo usado como fonte de verdade

## O que evitar

- editar código gerado
- criar abstração sem uso real
- criar estado global para problema local
- editar migrations já aplicadas
- criar entidade JPA para value object sem ciclo de vida próprio
- criar relacionamento JPA entre módulos sem benefício concreto
- vazar entidade JPA pela API
- reintroduzir dependência OAuth2/Nimbus para autenticação JWT
- apagar dados ou resetar banco sem confirmar o alvo exato
- imprimir segredos, senhas ou hashes completos em comandos e logs
- quebrar padrão visual já fechado em telas irmãs
