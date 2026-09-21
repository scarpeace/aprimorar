# Aprimorar — AGENTS.md

Documento operacional do repositório. Mantenha este arquivo atualizado quando a estrutura, o fluxo de trabalho ou os padrões principais mudarem.

## Estrutura atual

- `server/`: backend Spring Boot
- `client/`: frontend Next.js
- `.opencode/skills/`: skills locais do opencode

### Skills locais

- `spring-boot-skill` e `spring-modulith-verifier`: práticas Spring Boot (SivaLabs)
- `vercel-react-best-practices`: otimização de performance React/Next.js (Vercel)
- `frontend-design`: direção visual e estética de UI (Anthropic)

## Regra básica

- backend: trabalhar em `server/`
- frontend: trabalhar em `client/`
- contrato da API: backend primeiro, frontend depois
- as rotas HTTP não usam o prefixo versionado `/v1`

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

### Convenções

- controller recebe e delega
- service concentra regra de negócio
- repository faz acesso a dados
- DTO na borda HTTP
- entidade JPA não vaza para API
- mudança de schema sempre via Flyway
- dependência entre módulos só pelos contratos permitidos no `package-info.java`
- quando um módulo expõe contratos a outros módulos, eles ficam em uma `api/`
  declarada como `NamedInterface`; implementação, persistência e HTTP ficam nos
  subpacotes internos
- conversão entre entidade, contrato e DTO deve ser explícita
- evitar abstrações sem mais de um uso real

Estrutura atual dos módulos:

```text
aprimorar/
├── auth/
├── common/
├── financeiro/
│   ├── api/{cobrancas_particular,repasses_particular}
│   ├── cobrancas_particular/{domain,repository,service,web}
│   ├── recebimentos_particular/{domain,repository,service,web}
│   ├── repasses_particular/{domain,repository,service,web}
│   ├── pagamentos_particular/{domain,repository,service,web}
│   ├── despesas/{domain,repository,service,web}
│   └── config/
├── agendamento/
│   ├── common/
│   ├── alunos/{domain,repository,service,web}
│   ├── colaboradores/{domain,repository,service,web}
│   └── atendimentos_individuais/{domain,repository,service,web}
└── config/
```

- `auth` concentra autenticação JWT com access token Bearer e refresh token
  HttpOnly; por enquanto contém as roles `ADMIN` e `SECRETARIA`.
  `POST /auth/login`, `POST /auth/refresh` e `POST /auth/logout` são públicos;
  `GET /auth/me` e as APIs de domínio exigem um access token válido.
  O refresh token fica no cookie `refresh_token` (`HttpOnly`, `SameSite=Lax`,
  escopo `/auth`) e somente seu hash é persistido. O usuário atual é retornado
  por `/auth/me` como `id`, `email` e `role`.
- `agendamento/atendimentos_individuais` concentra alunos, colaboradores e
  atendimentos individuais. O atendimento usa relações JPA internas com aluno e
  colaborador e integra cobranças e repasses somente por contratos de
  `financeiro.api`.
- O namespace Java do módulo é `aprimorar.agendamento`; o prefixo HTTP
  `/instituicao` permanece temporariamente por compatibilidade com o contrato
  existente.
- `agendamento/alunos` e `agendamento/colaboradores` mantêm suas implementações
  separadas em `service` e `web`, com DTOs organizados por capacidade.
- `agendamento/common/domain/Endereco` contém o value object de endereço; não é
  uma entidade nem possui ciclo de vida próprio.
- `Responsavel` é um value object embutido em `Aluno`, sem tabela própria.
- `financeiro/despesas` é responsável por lançamentos operacionais de entrada e
  saída, sem relação JPA com instituição.
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

- `AtendimentoIndividual` armazena referências JPA internas para aluno e colaborador;
  as integrações financeiras usam IDs escalares e contratos de `financeiro.api`
- a criação, atualização e exclusão do atendimento, da cobrança e do repasse
  individual acontecem no `AtendimentoIndividualService`
- `Aluno` e `Colaborador` usam `Endereco` com `@Embedded`
- `Aluno` usa `Responsavel` com `@Embedded`; não existe tabela ou ID próprio
  para responsável
- o valor da cobrança individual vive em `cobrancas_particular`; o valor do repasse vive
  em `repasses_particular`
- recebimentos de cobranças individuais vivem em `recebimentos_particular`;
  um recebimento pode quitar várias cobranças do mesmo aluno e seu total é calculado
  pela soma das cobranças vinculadas
- toda criação de atendimento individual cria uma cobrança e um repasse
  pendentes; o atendimento não armazena valores financeiros próprios
- alunos e colaboradores não são excluídos; o campo `ativo` controla ativação e
  desativação
- não existem registros ou realocação para aluno, colaborador ou responsável fantasma
- exceções de negócio são específicas de cada módulo e tratadas pelo handler do
  próprio módulo
- `DespesaDadosInvalidosException` representa validações de domínio de despesas
  e é retornada como `400 Bad Request`

### Erros HTTP

- respostas de erro usam `org.springframework.http.ProblemDetail`
- `GlobalExceptionHandler` em `aprimorar.config` tem baixa precedência e trata
  apenas erros transversais
- handlers de `agendamento`, `atendimentos_individuais` e `financeiro/despesas`
  ficam nos pacotes dos respectivos módulos e tratam suas exceções próprias
- `AuthException` é tratada pelo handler global como `401 Unauthorized`
- anotações OpenAPI reutilizáveis ficam em `common/openapi`; os controllers de
  domínio usam `@CommonProblemResponses` e documentam erros específicos com as
  anotações de `400`, `404` e `409` disponíveis

### Testes

- testes unitários de service usam Mockito e ficam próximos ao pacote testado
- não misturar teste de `Specification` dentro de teste de service
- preferir teste pequeno por regra de negócio relevante
- warnings de JaCoCo/ByteBuddy sobre instrumentação podem aparecer no sandbox; considerar o exit code do Maven

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
  - pagamento e repasse usam ações explícitas de registrar e cancelar
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

Configuração de ambiente:

- `dev` é o profile padrão; para produção, definir `SPRING_PROFILES_ACTIVE=prod`
- o backend lê opcionalmente `server/.env` quando iniciado dentro de `server/`
- `server/.env.example` documenta as chaves necessárias; em produção, fornecê-las
  como variáveis de ambiente

SonarQube local:

- dashboard: `http://localhost:9000`
- script de análise: `server/sonar.sh`
- token fica em `server/.env` como `SONAR_TOKEN`

Segredos, tokens e senhas devem permanecer em `server/.env` ou nas variáveis
de ambiente do deploy; nunca os copie para documentação, logs ou respostas de
diagnóstico.

## O que costuma quebrar

- contrato gerado desatualizado
- import antigo de tipo gerado depois de mudança no OpenAPI
- migration Flyway editada depois de aplicada em ambiente persistente ou com versão duplicada
- `target/classes` manter migrations ou `data.sql` removidos da fonte; executar
  `./mvnw clean` antes de repetir a inicialização local
- teste de contexto iniciado sem as variáveis de ambiente necessárias
- lógica demais em um único componente de tela
- script antigo da raiz sendo usado como fonte de verdade

## O que evitar

- editar código gerado
- criar abstração sem uso real
- criar estado global para problema local
- editar migrations já aplicadas em ambiente persistente ou compartilhado; enquanto o
  projeto estiver em desenvolvimento e os bancos puderem ser recriados, a migration
  inicial pode ser ajustada diretamente
- criar entidade JPA para value object sem ciclo de vida próprio
- criar relacionamento JPA entre módulos sem benefício concreto
- vazar entidade JPA pela API
- apagar dados ou resetar banco sem confirmar o alvo exato
- imprimir segredos, senhas ou hashes completos em comandos e logs
- quebrar padrão visual já fechado em telas irmãs
