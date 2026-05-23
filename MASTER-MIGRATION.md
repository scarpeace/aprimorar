# Master Migration

## Objetivo

Este arquivo e o guia mestre da migracao arquitetural do backend do projeto.

Escopo desta migracao:

- reorganizar o backend para uma estrutura Spring Modulith mais clara e mais rigida
- traduzir o dominio principal do backend para portugues
- traduzir entidades, services, repositories, DTOs, exceptions, controllers, eventos, tabelas, colunas e endpoints HTTP
- remover acoplamentos internos indevidos entre modulos
- manter `auth` sem traducao
- manter `shared` sem traducao
- remover `dashboard` por enquanto
- ignorar o frontend nesta fase

Esta aplicacao ainda nao esta em producao. Existe controle total sobre banco e schema. Portanto, esta migracao pode quebrar compatibilidade sem necessidade de legado temporario.

---

## Decisoes Fechadas

### Decisoes de linguagem

- O dominio de negocio sera traduzido para portugues.
- No codigo, banco, pacotes, classes, tabelas, colunas e paths HTTP sera usado ASCII sem acentos.
- Exemplo: fala-se "responsavel", nao `responsável`, no codigo.

### Decisoes de modulos

- `appointment` vira `atendimento`
- `registration` deixa de existir como supermodulo
- `student` vira `pessoas/aluno`
- `employee` vira `pessoas/colaborador`
- `parent` vira `pessoas/responsavel`
- `expense` vira modulo `financeiro`
- entidade principal de `financeiro` sera `Despesa`
- `dashboard` deixa de existir por enquanto
- `auth` continua `auth`
- `shared` continua `shared`

### Decisoes de nomenclatura de dominio

- `Appointment` -> `Atendimento`
- `AppointmentContent` -> `TipoAtendimento`
- `Employee` -> `Colaborador`
- `Parent` -> `Responsavel`
- `Student` -> `Aluno`
- `Expense` -> `Despesa`

---

## Principios da Migracao

### 1. Primeiro fronteira, depois traducao em massa

Traduzir em cima da bagunca atual so espalha a bagunca em portugues. Antes da traducao ampla, a arquitetura precisa ficar coerente.

### 2. Um modulo so conversa com outro por API publica

Nenhum modulo pode importar `internal` de outro modulo.

### 3. Controller nao depende de implementacao concreta

Controller deve depender de contrato de aplicacao do proprio modulo, nunca de `*ServiceImpl`.

### 4. Nada de SQL cruzado furando modulo

Se um modulo precisa de informacao de outro, isso deve passar por contrato publico ou evento, nao por acesso direto a tabela alheia.

### 5. O pacote raiz do modulo define a API publica

Tudo que estiver no pacote raiz do modulo e considerado publico.
Tudo que estiver em `internal/` e privado.

### 6. O estado intermediario deve ser o menor possivel

Evitar longos periodos com nomes metade ingles metade portugues.

---

## Estrutura Alvo

```text
src/main/java/aprimorar/
|
├── atendimento/
│   ├── AtendimentoService.java
│   ├── dto/
│   │   ├── AtendimentoResponseDTO.java
│   │   ├── AtendimentoFinanceiroResumoDTO.java
│   │   ├── AtendimentosAlunoResponseDTO.java
│   │   ├── AtendimentosColaboradorResponseDTO.java
│   │   └── ...
│   ├── event/
│   │   └── AtendimentoAgendadoEvent.java
│   └── internal/
│       ├── Atendimento.java
│       ├── TipoAtendimento.java
│       ├── AtendimentoMapper.java
│       ├── AtendimentoRepository.java
│       ├── AtendimentoSpecifications.java
│       ├── AtendimentoController.java
│       ├── AtendimentoRequestDTO.java
│       ├── AtendimentoServiceImpl.java
│       └── listener/
|
├── pessoas/
│   ├── aluno/
│   │   ├── AlunoService.java
│   │   ├── dto/
│   │   │   ├── AlunoResponseDTO.java
│   │   │   ├── AlunoRequestDTO.java
│   │   │   └── AlunoOptionsDTO.java
│   │   ├── event/
│   │   └── internal/
│   │       ├── Aluno.java
│   │       ├── AlunoMapper.java
│   │       ├── AlunoRepository.java
│   │       ├── AlunoController.java
│   │       ├── AlunoServiceImpl.java
│   │       └── exception/
│   │
│   ├── colaborador/
│   │   ├── ColaboradorService.java
│   │   ├── dto/
│   │   │   ├── ColaboradorResponseDTO.java
│   │   │   ├── ColaboradorRequestDTO.java
│   │   │   └── ColaboradorOptionsDTO.java
│   │   ├── event/
│   │   └── internal/
│   │       ├── Colaborador.java
│   │       ├── FuncaoColaborador.java
│   │       ├── ColaboradorMapper.java
│   │       ├── ColaboradorRepository.java
│   │       ├── ColaboradorController.java
│   │       ├── ColaboradorServiceImpl.java
│   │       └── exception/
│   │
│   └── responsavel/
│       ├── ResponsavelService.java
│       ├── dto/
│       │   ├── ResponsavelResponseDTO.java
│       │   ├── ResponsavelRequestDTO.java
│       │   └── ResponsavelOptionsDTO.java
│       ├── event/
│       └── internal/
│           ├── Responsavel.java
│           ├── ResponsavelMapper.java
│           ├── ResponsavelRepository.java
│           ├── ResponsavelController.java
│           ├── ResponsavelServiceImpl.java
│           └── exception/
|
├── financeiro/
│   ├── FinanceiroService.java
│   ├── dto/
│   │   ├── DespesaResponseDTO.java
│   │   ├── DespesaRequestDTO.java
│   │   └── ResumoDespesasDTO.java
│   └── internal/
│       ├── Despesa.java
│       ├── CategoriaDespesa.java
│       ├── DespesaRepository.java
│       ├── DespesaController.java
│       ├── FinanceiroServiceImpl.java
│       └── exception/
|
├── auth/
│   ├── api/
│   └── internal/
|
├── shared/
├── config/
└── ApiAprimorarApplication.java
```

Observacoes:

- `pessoas/` e apenas agrupador de pacotes.
- `aluno`, `colaborador` e `responsavel` sao modulos reais ou submodulos com fronteira publica clara.
- `dashboard` nao entra na estrutura alvo desta fase.

---

## Arquitetura Alvo dos Modulos

### Atendimento

Responsabilidades:

- CRUD e consultas de atendimentos
- validacao de conflito de agenda
- relatorios financeiros derivados dos atendimentos
- eventos do dominio de atendimento
- integracao com aluno, colaborador e financeiro apenas por contratos publicos

Dependencias permitidas desejadas:

- `financeiro::api`
- `pessoas.aluno::api`
- `pessoas.colaborador::api`
- `shared::*`
- `shared`

### Pessoas.Aluno

Responsabilidades:

- CRUD de alunos
- ligacao do aluno com responsavel
- opcoes de selecao e consultas publicas para outros modulos
- regras de arquivamento, ghost, integridade e exclusao

Dependencias permitidas desejadas:

- `pessoas.responsavel::api`
- `atendimento::api`
- `shared::*`
- `shared`

### Pessoas.Colaborador

Responsabilidades:

- CRUD de colaboradores
- consulta publica para atendimento
- regras de arquivamento e ghost
- eventos de colaborador quando necessario

Dependencias permitidas desejadas:

- `atendimento::api`
- `shared::*`
- `shared`

### Pessoas.Responsavel

Responsabilidades:

- CRUD de responsaveis
- exposicao de consultas publicas para aluno
- regras de exclusao com vinculo de alunos

Dependencias permitidas desejadas:

- `shared::*`
- `shared`

### Financeiro

Responsabilidades:

- CRUD de despesas
- totais financeiros do modulo de despesas
- informacoes agregadas que `atendimento` precise consultar por API

Dependencias permitidas desejadas:

- `shared::*`
- `shared`

### Auth

Responsabilidades:

- autenticacao
- usuarios e seguranca
- integracao com JWT e resource server

Dependencias permitidas desejadas:

- `shared::*`
- `shared`

### Shared

Responsabilidades:

- contratos genericos realmente compartilhados
- classes transversais como Problem Details, paginacao, enums realmente comuns
- nenhum conceito de negocio dono de modulo deve vazar para ca

---

## Estado Atual Resumido

Pontos identificados no backend atual antes da migracao:

- o projeto ja usa Spring Modulith, mas com fronteiras inconsistentes
- `registration` funciona como supermodulo acoplado internamente
- `student` depende de `parent.internal`
- existem contratos publicos com nomes ambiguos como `api.event` e `api.contract`
- controllers dependem de implementacoes concretas
- existe SQL cruzado acessando tabela de outro modulo
- `appointment` e o modulo com maior acoplamento funcional
- o teste `ModuleVerificationTest` ja existe e deve ser usado como guarda da migracao

---

## Estrategia Geral

### Fase 0. Preparacao

Objetivo:

- consolidar este guia
- congelar a nomenclatura alvo
- alinhar a ordem da migracao

Entregaveis:

- `MASTER-MIGRATION.md`
- dicionario oficial de renomeacao
- plano de execucao por fase

### Fase 1. Limpeza Arquitetural Antes da Traducao

Objetivo:

- corrigir fronteiras modulares antes da renomeacao ampla

Trabalho esperado:

- remover imports entre modulos que apontam para `internal`
- criar ou consolidar APIs publicas minimas para `aluno`, `colaborador`, `responsavel`, `financeiro` e `atendimento`
- substituir dependencia de controller em `*ServiceImpl` por dependencia em contrato publico do proprio modulo
- remover contratos mal nomeados em `employee` como `api.event.EmployeeService` e `EventQueryApi`
- matar consultas SQL cruzadas em `registration/shared/PendingFinancialBalanceChecker`
- isolar `student`, `employee` e `parent` para deixarem de operar como um bloco interno sem fronteiras

Criticos de saida da fase:

- nenhum modulo depende de `internal` de outro modulo
- `ModuleVerificationTest` continua verde
- nomenclatura ainda pode estar em ingles, desde que as fronteiras estejam corretas

### Fase 2. Quebra do Supermodulo Registration

Objetivo:

- desmontar `registration` como dono unico de tudo e promover os subdominios para modulos claros sob `pessoas/`

Trabalho esperado:

- mover `student` para `pessoas/aluno`
- mover `employee` para `pessoas/colaborador`
- mover `parent` para `pessoas/responsavel`
- criar `package-info.java` por modulo ou submodulo real quando necessario
- ajustar `allowedDependencies`
- definir ownership de DTOs, exceptions e services em cada modulo

Criticos de saida da fase:

- `registration` deixa de concentrar regras e contratos dos tres dominos
- ownership de codigo fica claro

### Fase 3. Traducao de Pessoas

Objetivo:

- traduzir os tres modulos de pessoas de ponta a ponta

Ordem sugerida:

1. `employee` -> `colaborador`
2. `parent` -> `responsavel`
3. `student` -> `aluno`

Trabalho esperado em cada modulo:

- renomear pacote
- renomear entidade
- renomear enum
- renomear repository
- renomear service e implementacao
- renomear DTOs
- renomear exceptions
- renomear controller
- renomear eventos
- renomear paths HTTP
- renomear testes relacionados

Criticos de saida da fase:

- nenhum nome antigo relevante permanece nos modulos de pessoas, salvo legado tecnico muito consciente

### Fase 4. Traducao de Financeiro

Objetivo:

- converter `expense` para `financeiro` com entidade `Despesa`

Trabalho esperado:

- `expense` -> `financeiro`
- `Expense` -> `Despesa`
- `ExpenseCategory` -> `CategoriaDespesa`
- endpoints `/expenses` -> `/despesas`
- DTOs e relatorios do modulo para portugues

Criticos de saida da fase:

- modulo financeiro isolado, com API publica coerente

### Fase 5. Traducao de Atendimento

Objetivo:

- converter `appointment` para `atendimento`

Trabalho esperado:

- `appointment` -> `atendimento`
- `Appointment` -> `Atendimento`
- `AppointmentContent` -> `TipoAtendimento`
- requests, responses e relatorios para portugues
- listeners e eventos para portugues
- repositorio, specifications e projections para portugues

Observacao:

- esta e a fase com maior risco tecnico do backend por concentrar muita regra e integracao

Criticos de saida da fase:

- modulo atendimento traduzido e consumindo apenas APIs publicas dos demais modulos

### Fase 6. Ajustes de Auth e Shared por Impacto Colateral

Objetivo:

- preservar `auth` e `shared` em ingles, ajustando apenas o necessario para compor com a nova linguagem dos outros modulos

Trabalho esperado:

- manter pacote `auth`
- manter pacote `shared`
- ajustar imports, mensagens e integracoes quando houver impacto dos novos nomes
- evitar traduzir por traduzir conceitos tecnicos que continuarao melhores em ingles

Criticos de saida da fase:

- `auth` e `shared` continuam coesos e sem naming quebrado

### Fase 7. Banco de Dados

Objetivo:

- traduzir schema e alinhar banco com o novo dominio

Trabalho esperado:

- renomear tabelas
- renomear colunas
- renomear constraints
- renomear foreign keys
- renomear indices
- revisar dados seed de ghost/removido
- revisar enums persistidos
- revisar scripts Flyway antigos e novos
- alinhar anotacoes JPA ao novo schema

Observacao:

- como nao ha producao e existe controle total, esta fase pode ser mais agressiva
- ainda assim, as migrations devem ser feitas com disciplina

Criticos de saida da fase:

- schema e mapeamento JPA contam a mesma historia do dominio

### Fase 8. Higienizacao Final

Objetivo:

- remover residuos e garantir consistencia final

Trabalho esperado:

- eliminar nomes antigos restantes
- revisar logs e mensagens de erro
- revisar OpenAPI
- revisar testes quebrados por nomes antigos
- revisar documentacao gerada do Modulith

Criticos de saida da fase:

- o projeto nao aparenta ter sido remendado; ele parece nativamente modelado no novo dominio

---

## Sequencia de Execucao Recomendada

1. consolidar APIs publicas e fronteiras
2. desmontar `registration`
3. migrar `employee` para `pessoas/colaborador`
4. migrar `parent` para `pessoas/responsavel`
5. migrar `student` para `pessoas/aluno`
6. migrar `expense` para `financeiro`
7. migrar `appointment` para `atendimento`
8. ajustar `auth` e `shared` por impacto colateral
9. traduzir schema do banco
10. revisar documentacao, OpenAPI e testes

---

## Riscos Principais

### 1. Registration esta acoplado demais por dentro

Risco:

- a traducao mascarar o problema sem corrigi-lo

Mitigacao:

- quebrar fronteiras e ownership antes da traducao em massa

### 2. Atendimento concentra muita regra

Risco:

- regressao em regras financeiras, filtros e conflitos de agenda

Mitigacao:

- mexer no modulo somente depois de pessoas e financeiro estarem estaveis

### 3. Renomeacao de banco em larga escala

Risco:

- inconsistencias entre Flyway, JPA e testes

Mitigacao:

- manter checklist de tabela/coluna/constraint por modulo

### 4. Estado hibrido ingles-portugues

Risco:

- piorar legibilidade do projeto por semanas

Mitigacao:

- migrar por blocos coerentes e fechar cada bloco completamente

### 5. Remocoes de dashboard

Risco:

- sobrar acoplamento morto, DTO esquecido ou dependencia de build

Mitigacao:

- mapear e eliminar todas as referencias a `dashboard` no backend

---

## Regras Operacionais da Migracao

- cada mudanca deve respeitar `AGENTS.md` do backend
- toda fronteira modular alterada exige rodar `./mvnw test -Dtest=ModuleVerificationTest`
- mudancas amplas exigem `./mvnw clean compile` e `./mvnw test`
- nenhuma alteracao manual em codigo gerado do frontend sera feita nesta fase
- toda mudanca de schema deve passar por Flyway
- preferir mudancas pequenas e coerentes por modulo, nao um Big Bang em um unico commit de codigo sem verificacao

---

## Checklist Por Modulo

Para cada modulo migrado, verificar:

- pacote renomeado
- `package-info.java` coerente
- dependencias permitidas revisadas
- service publico definido
- implementacao em `internal/`
- controller dependente de contrato, nao de `Impl`
- repository privado ao modulo
- DTOs no ownership correto
- exceptions no ownership correto
- eventos no ownership correto
- imports antigos removidos
- nomes antigos removidos do schema JPA
- endpoints HTTP traduzidos
- testes atualizados
- `ModuleVerificationTest` verde

---

## Dicionario Oficial de Renomeacao

Esta secao e a fonte principal de nomes antigos -> novos.

### Modulos e pacotes

| Atual | Alvo |
|---|---|
| `appointment` | `atendimento` |
| `registration` | remover como supermodulo |
| `registration.student` | `pessoas.aluno` |
| `registration.employee` | `pessoas.colaborador` |
| `registration.parent` | `pessoas.responsavel` |
| `expense` | `financeiro` |
| `dashboard` | remover nesta fase |
| `auth` | `auth` |
| `shared` | `shared` |

### Entidades

| Atual | Alvo |
|---|---|
| `Appointment` | `Atendimento` |
| `Student` | `Aluno` |
| `Employee` | `Colaborador` |
| `Parent` | `Responsavel` |
| `Expense` | `Despesa` |
| `User` | `User` |

### Enums e tipos de dominio

| Atual | Alvo |
|---|---|
| `AppointmentContent` | `TipoAtendimento` |
| `AppointmentContentEnum` | `TipoAtendimentoEnum` ou eliminar duplicidade |
| `ExpenseCategory` | `CategoriaDespesa` |
| `ExpenseCategoryEnum` | `CategoriaDespesaEnum` ou eliminar duplicidade |
| `Role` | `Role` |
| `RoleEnum` | `RoleEnum` ou consolidar |
| `duty` | `funcao` |
| `Duty` | `FuncaoColaborador` |

### Services

| Atual | Alvo |
|---|---|
| `AppointmentService` | `AtendimentoService` |
| `AppointmentServiceImpl` | `AtendimentoServiceImpl` |
| `StudentService` | `AlunoService` |
| `StudentServiceImpl` | `AlunoServiceImpl` |
| `EmployeeService` | `ColaboradorService` |
| `EmployeeServiceImpl` | `ColaboradorServiceImpl` |
| `ParentServiceImpl` | `ResponsavelServiceImpl` |
| `ExpenseService` | `FinanceiroService` |
| `ExpenseServiceImpl` | `FinanceiroServiceImpl` |
| `DashboardServiceImpl` | remover |

### Repositories

| Atual | Alvo |
|---|---|
| `AppointmentRepository` | `AtendimentoRepository` |
| `StudentRepository` | `AlunoRepository` |
| `EmployeeRepository` | `ColaboradorRepository` |
| `ParentRepository` | `ResponsavelRepository` |
| `ExpenseRepository` | `DespesaRepository` |
| `UserRepository` | `UserRepository` |

### Controllers

| Atual | Alvo |
|---|---|
| `AppointmentController` | `AtendimentoController` |
| `StudentController` | `AlunoController` |
| `EmployeeController` | `ColaboradorController` |
| `ParentController` | `ResponsavelController` |
| `ExpenseController` | `DespesaController` |
| `DashboardController` | remover |
| `AuthController` | `AuthController` |
| `UserController` | `UserController` |

### DTOs de request/response

| Atual | Alvo |
|---|---|
| `AppointmentRequestDTO` | `AtendimentoRequestDTO` |
| `AppointmentResponseDTO` | `AtendimentoResponseDTO` |
| `StudentRequestDTO` | `AlunoRequestDTO` |
| `StudentResponseDTO` | `AlunoResponseDTO` |
| `ParentRequestDTO` | `ResponsavelRequestDTO` |
| `ParentResponseDTO` | `ResponsavelResponseDTO` |
| `EmployeeRequestDTO` | `ColaboradorRequestDTO` |
| `EmployeeResponseDTO` | `ColaboradorResponseDTO` |
| `ExpenseRequestDTO` | `DespesaRequestDTO` |
| `ExpenseResponseDTO` | `DespesaResponseDTO` |
| `DashboardSummaryResponseDTO` | remover |
| `AuthRequestDTO` | `AuthRequestDTO` |
| `AuthResponseDTO` | `AuthResponseDTO` |
| `UserRequestDTO` | `UserRequestDTO` |
| `UserResponseDTO` | `UserResponseDTO` |

### DTOs compostos e relatorios de atendimento

| Atual | Alvo |
|---|---|
| `AppointmentFinanceSummaryDTO` | `AtendimentoFinanceiroResumoDTO` |
| `FinanceSummaryDTO` | `ResumoFinanceiroDTO` |
| `StudentSummaryDTO` | `ResumoAlunoDTO` |
| `EmployeeSummaryDTO` | `ResumoColaboradorDTO` |
| `StudentAppointmentsResponseDTO` | `AtendimentosAlunoResponseDTO` |
| `EmployeeAppointmentsResponseDTO` | `AtendimentosColaboradorResponseDTO` |
| `StudentsFinanceSummaryResponseDTO` | `ResumoFinanceiroAlunosResponseDTO` |
| `EmployeesFinanceSummaryResponseDTO` | `ResumoFinanceiroColaboradoresResponseDTO` |
| `StudentsWithFinanceResponseDTO` | `AlunosComFinanceiroResponseDTO` |
| `EmployeesWithFinanceResponseDTO` | `ColaboradoresComFinanceiroResponseDTO` |
| `StudentFinanceSummaryDTO` | `ResumoFinanceiroAlunoDTO` |
| `EmployeeFinanceSummaryDTO` | `ResumoFinanceiroColaboradorDTO` |
| `StudentWithFinanceDTO` | `AlunoComFinanceiroDTO` |
| `EmployeeWithFinanceDTO` | `ColaboradorComFinanceiroDTO` |
| `ContentDistributionDTO` | `DistribuicaoTipoAtendimentoDTO` |

### DTOs de opcoes e apoio

| Atual | Alvo |
|---|---|
| `ParentOptionsDTO` | `ResponsavelOptionsDTO` |
| `AddressRequestDTO` | `EnderecoRequestDTO` |
| `AddressResponseDTO` | `EnderecoResponseDTO` |
| `PageDTO` | `PageDTO` |
| `ProblemResponseDTO` | `ProblemResponseDTO` |

### Excecoes

| Atual | Alvo |
|---|---|
| `AppointmentNotFoundException` | `AtendimentoNaoEncontradoException` |
| `AppointmentScheduleConflictException` | `ConflitoAgendaAtendimentoException` |
| `InvalidAppointmentException` | `AtendimentoInvalidoException` |
| `NotAllowedToUpdateAppointmentException` | `NaoPermitidoAtualizarAtendimentoException` |
| `ExpenseNotFoundException` | `DespesaNaoEncontradaException` |
| `ParentNotFoundException` | `ResponsavelNaoEncontradoException` |
| `ParentAlreadyExistsException` | `ResponsavelJaExisteException` |
| `InvalidParentException` | `ResponsavelInvalidoException` |
| `ParentHasLinkedStudentsException` | `ResponsavelPossuiAlunosVinculadosException` |
| `PersonHasPendingFinancialsException` | `PessoaPossuiPendenciasFinanceirasException` |
| `EmployeeNotFoundException` | `ColaboradorNaoEncontradoException` |
| `EmployeeAlreadyExistsException` | `ColaboradorJaExisteException` |
| `EmployeeBusinessException` | `ColaboradorBusinessException` ou nome mais especifico |

Observacao:

- excecoes devem ser traduzidas integralmente, inclusive os casos hoje em ingles como `NotFoundException`

### Eventos

| Atual | Alvo |
|---|---|
| `EventQueryApi` | remover e substituir por nome correto |
| `AppointmentEventReactor` | `ReatorEventosAtendimento` ou nome funcional mais preciso |
| evento de employee atual | renomear para algo semanticamente correto |
| `AulaAgendadaEvent` do exemplo conceitual | referencia conceitual apenas |

### Tabelas

| Atual | Alvo |
|---|---|
| `tb_appointments` | `tb_atendimentos` |
| `tb_employees` | `tb_colaboradores` |
| `tb_parent` | `tb_responsaveis` |
| `tb_students` | `tb_alunos` |
| `tb_expenses` | `tb_despesas` |
| `tb_users` | `tb_users` |

### Colunas principais

| Atual | Alvo |
|---|---|
| `employee_id` | `colaborador_id` |
| `employee_name` | `colaborador_nome` |
| `employee_payment_date` | `data_pagamento_colaborador` |
| `student_id` | `aluno_id` |
| `student_name` | `aluno_nome` |
| `student_charge_date` | `data_cobranca_aluno` |
| `start_date` | `inicio_em` |
| `end_date` | `fim_em` |
| `created_at` | `criado_em` |
| `updated_at` | `atualizado_em` |
| `birthdate` | `data_nascimento` |
| `name` | `nome` |
| `contact` | `contato` |
| `school` | `escola` |
| `street` | `rua` |
| `district` | `bairro` |
| `city` | `cidade` |
| `state` | `estado` |
| `zip` | `cep` |
| `complement` | `complemento` |
| `duty` | `funcao` |
| `active` | `ativo` |
| `description` | `descricao` |
| `title` | `titulo` |
| `content` | `tipo_atendimento` |
| `amount` | `valor` |
| `date` | `data` |
| `category` | `categoria` |
| `price` | `cobranca_aluno` |
| `payment` | `pagamento_professor` |
| `parent_id` | `responsavel_id` |

Observacao importante:

- os nomes `cobranca_aluno` e `pagamento_professor` foram adotados como decisao oficial atual para a migracao do dominio `atendimento`

### Endpoints HTTP

| Atual | Alvo |
|---|---|
| `/v1/appointments` | `/v1/atendimentos` |
| `/v1/expenses` | `/v1/despesas` |
| `/v1/students` | `/v1/alunos` |
| `/v1/employees` | `/v1/colaboradores` |
| `/v1/parents` | `/v1/responsaveis` |
| `/v1/dashboard/*` | remover |
| `/v1/auth/*` | `/v1/auth/*` |

### Seeds especiais

| Atual | Alvo |
|---|---|
| `Aluno Removido` | `Aluno Removido` |
| `Colaborador Removido` | `Colaborador Removido` |
| `Responsavel Removido` | `Responsavel Removido` |

---

## Pontos Que Precisam de Validacao Durante a Execucao

Mesmo com decisoes fechadas, estes pontos ainda precisam ser revisitados durante a implementacao:

- nome final de alguns DTOs compostos de relatorio para nao ficarem excessivamente verbosos
- nome final de algumas excecoes genericas para evitar nomes excessivamente longos mantendo clareza
- necessidade de manter ou eliminar duplicidades atuais como `Role` e `RoleEnum`

---

## Sessao de Status

### Feito

- direcao arquitetural da migracao definida
- modulos alvo definidos
- decisoes principais de traducao definidas
- decisoes de nao traducao definidas para `auth` e `shared`
- decisao de remocao de `dashboard` nesta fase definida
- dicionario inicial completo de renomeacao consolidado neste arquivo
- Fase 1 iniciada no codigo
- controllers desacoplados de `*ServiceImpl` por contratos internos de aplicacao
- evento confuso `EventQueryApi` substituido por `EmployeeDeletedEvent`
- ambiguidade entre `EmployeeService` publico e `EmployeeQueryApi` reduzida, com consumo do `appointment` apontando para `EmployeeQueryApi`
- excecoes de `employee` movidas de `internal` para `api.exception` e alinhadas no handler global
- SQL cruzado de `registration` em tabela de `appointment` removido
- consulta de pendencias financeiras invertida para portas de `registration` implementadas por adapters em `appointment`
- ciclo modular `appointment <-> registration` removido
- `registration` nao depende mais de `appointment::api` em `allowedDependencies`
- Fase 2 iniciada no codigo
- `student` deixou de referenciar `parent.internal.Parent` diretamente no modelo
- vinculo de `student` com responsavel passou para `parentId` (UUID) no agregado
- `student` passou a validar responsavel via contrato `ParentQueryApi`, removendo dependencia de `ParentRepository`
- contrato `StudentParentLinkQueryApi` criado para consultas de vinculo entre alunos e responsavel
- `parent` passou a usar contrato publico de `student` para validar restricoes de arquivamento/exclusao
- regra de bloqueio de arquivamento/exclusao de responsavel com alunos vinculados ficou explicita no service
- `parent` deixou de converter entidade para DTO diretamente no agregado, com mapeamento movido para `ParentMapper`
- fronteira API x dominio no modulo `parent` ficou mais limpa (entidade sem dependencia de DTO de API)
- `address` deixou de converter para DTO diretamente no value object, com mapeamento consolidado no `AddressMapper`
- `parent/api` passou a declarar interface nomeada (`@NamedInterface("api")`) para fronteira publica explicita
- naming dos contratos de integracao foi padronizado para sufixo `*Api` (eliminando mistura com `*Port` e `*QueryApi`)
- contratos renomeados: `EmployeeReadApi`, `EmployeePaymentStatusApi`, `ParentReadApi`, `StudentParentLinkApi`, `StudentChargeStatusApi`
- split fisico iniciado: pacote `registration.parent` migrado para `registration.pessoas.responsavel` (api + internal + repository)
- contratos e imports de `student` e `exception handler` ajustados para o novo caminho `pessoas.responsavel`
- split fisico de `registration.student` concluido para `registration.pessoas.aluno` (api + internal + repository)
- contratos de aluno renomeados para novo padrao: `AlunoService`, `AlunoChargeStatusApi`, `AlunoResponsavelLinkApi`
- `appointment`, `responsavel` e handlers globais ajustados para os novos contratos/pacotes de aluno
- split fisico de `registration.employee` concluido para `registration.pessoas.colaborador` (api + internal + repository)
- contratos e dependencias de `appointment`, `registration` e testes ajustados para `pessoas.colaborador`
- `package-info.java` de `pessoas.responsavel.api.dto` e `pessoas.responsavel.api.exception` adicionados com `@NamedInterface("api")`
- renomeacao de artefatos do modulo de colaborador iniciada e aplicada no backend:
  - `EmployeeReadApi` -> `ColaboradorReadApi`
  - `EmployeePaymentStatusApi` -> `ColaboradorPaymentStatusApi`
  - `Employee*DTO` (contratos de `pessoas.colaborador`) -> `Colaborador*DTO`
  - `Employee*Exception` -> `Colaborador*Exception`
  - `EmployeeDeletedEvent` -> `ColaboradorDeletedEvent`
  - `Employee*` internos (`entity/service/controller/mapper/repository/specifications`) -> `Colaborador*`
  - adapter `appointment/internal/EmployeePaymentStatusAdapter` -> `ColaboradorPaymentStatusAdapter`
- `./mvnw clean compile -DskipTests` validado
- `./mvnw test -Dtest=ModuleVerificationTest` validado
- `./mvnw compile -DskipTests` e nova execucao de `./mvnw test -Dtest=ModuleVerificationTest` validadas apos consolidacao de `NamedInterface`
- nova rodada de validacao apos renomeacao de colaborador:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- traducao da superficie de API de `pessoas.colaborador` aplicada:
  - `ColaboradorReadApi`: `getColaboradores(...)` e `buscarPorId(...)`
  - `ColaboradorPaymentStatusApi`: `possuiPagamentosPendentes(...)`
  - `ColaboradorDeletedEvent`: payload `colaboradorId`
  - `ColaboradorController`: endpoint base `/v1/colaboradores`, `@Tag`/`operationId` em portugues e `colaboradorId` em path params
  - `ColaboradorService` e `ColaboradorServiceImpl`: assinaturas alinhadas (`listarOpcoes`, `buscarColaboradorOuFalhar`, etc.)
  - consumidores em `appointment` ajustados para os novos contratos
- validacao apos traducao de superficie de API de colaborador:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- Fase 3 iniciada em `pessoas.aluno` com foco em tipos/arquivos (sem traducao ampla de metodos de negocio):
  - renomeados `Student*` para `Aluno*` em `api/dto`, `api/exception`, `api/event`, `internal` e `internal/repository`
  - contratos e imports cruzados ajustados para `AlunoResponseDTO`, `AlunoDeletedEvent`, `AlunoRepository` e `AlunoSpecifications`
  - ajuste de estabilidade aplicado para manter `appointment` consistente no bloco atual (`StudentSummaryDTO` preservado no modulo de atendimento)
- validacao apos bloco de renome de tipos em `pessoas.aluno`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- Fase 3 avancou em `pessoas.responsavel` no mesmo padrao (tipos/arquivos primeiro):
  - renomeados `Parent*` para `Responsavel*` em `api/dto`, `api/exception`, `internal` e `internal/repository`
  - `ResponsavelReadApi`, handler global e consumidores ajustados para novos tipos de `responsavel`
  - ajustes de consistencia aplicados em construtores/classes apos renome de arquivos (ex.: `ResponsavelController`, `ResponsavelRepository`, `Responsavel`)
- validacao apos bloco de renome de tipos em `pessoas.responsavel`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- Fase 4 iniciada com migracao estrutural de `expense` para `financeiro` (sem rodada ampla de renome de metodos):
  - pacote movido: `aprimorar.expense` -> `aprimorar.financeiro`
  - tipos renomeados: `Expense*` -> `Despesa*` / `Financeiro*` / `CategoriaDespesa*`
  - dependencias cruzadas atualizadas (`appointment` passou a consumir `financeiro.api`)
  - metadata de modulo ajustada (`financeiro` + `financeiro::api` em dependencias modulith)
- validacao apos inicio da Fase 4 (`financeiro`):
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- Fase 5 iniciada com migracao estrutural de `appointment` para `atendimento` (sem rodada ampla de renome de metodos):
  - pacote movido: `aprimorar.appointment` -> `aprimorar.atendimento`
  - tipos nucleares renomeados: `Appointment*` -> `Atendimento*`, `AppointmentContent*` -> `TipoAtendimento*`
  - dependencias cruzadas e imports atualizados para `atendimento`
  - metadata de modulo ajustada (`atendimento`) e dependencia do `dashboard` corrigida para `atendimento::api`
- validacao apos inicio da Fase 5 (`atendimento`):
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK
- limpeza de nomenclatura residual concluida no modulo `atendimento` (tipos/arquivos, sem rodada ampla de metodos):
  - `Student*` -> `Aluno*` em DTOs de consultas e relatorios de atendimento
  - `Employee*` -> `Colaborador*` em DTOs de consultas e relatorios de atendimento
  - adapter interno `StudentChargeStatusAdapter` -> `AlunoChargeStatusAdapter`
  - projections e consultas de repositorio alinhadas para `Aluno*`/`Colaborador*`
- validacao apos limpeza de nomenclatura em `atendimento`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)
- remocao do modulo `dashboard` concluida:
  - removidos controller/service/dtos/exceptions e metadata de modulo em `aprimorar.dashboard`
  - removido teste dedicado de DTO de dashboard
  - removidas referencias residuais ao modulo `dashboard` no backend
- validacao apos remocao de `dashboard`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)
- fase de schema/Flyway iniciada com migracao de nomenclatura para portugues:
  - criada migration `V8__rename_tables_and_columns_to_portuguese.sql`
  - renome de tabelas: `tb_appointments` -> `tb_atendimentos`, `tb_expenses` -> `tb_despesas`, `tb_students` -> `tb_alunos`, `tb_employees` -> `tb_colaboradores`, `tb_parent` -> `tb_responsaveis`
  - renome de colunas principais de `atendimento`, `financeiro` e `registration` alinhadas ao dicionario oficial
  - renome de endereco embutido aplicado em schema (`rua`, `bairro`, `cidade`, `estado`, `cep`, `complemento`)
- alinhamento JPA concluido para novo schema:
  - `@Table` e `@Column` atualizados em `Atendimento`, `Despesa`, `Aluno`, `Colaborador`, `Responsavel`, `Person` e `Address`
- validacao apos inicio da fase de schema:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)
- padronizacao da superficie HTTP/OpenAPI avancada para nomenclatura em portugues:
  - endpoints ajustados: `/v1/expenses` -> `/v1/despesas`, `/v1/students` -> `/v1/alunos`, `/v1/parents` -> `/v1/responsaveis`
  - ajustes de rotas internas de atendimento: `/student` -> `/aluno`, `/employee` -> `/colaborador`, rotas de relatorio/finance em `alunos`/`colaboradores`
  - `operationId` de `atendimento`, `financeiro`, `aluno` e `responsavel` traduzidos para portugues
- validacao apos padronizacao de superficie HTTP/OpenAPI:
  - `./mvnw clean compile` OK
  - `./mvnw test` OK (com warnings nao bloqueantes de JaCoCo/JDK em instrumentacao no ambiente atual)
- higienizacao de diretorios legados vazios concluida:
  - removidos `aprimorar/dashboard/` (casca sem codigo)
  - removidos `registration/employee/`, `registration/parent/` e `registration/student/` (cascas sem codigo)
  - mantida estrutura ativa em `registration/pessoas/*` e `registration/shared`
- validacao apos higienizacao de diretorios:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)
- promocao estrutural de `registration` para modulo raiz `pessoas` concluida:
  - codigo movido de `aprimorar.registration.pessoas.*` para `aprimorar.pessoas.*`
  - pacotes de apoio movidos: `aprimorar.registration.shared.*` -> `aprimorar.pessoas.shared.*` e `aprimorar.registration.api.*` -> `aprimorar.pessoas.api.*`
  - `package-info` do modulo atualizado para `id = "pessoas"` e `displayName = "Pessoas"`
  - dependencia modulith de `atendimento` ajustada de `registration::api` para `pessoas::api`
- validacao apos promocao para `pessoas`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)
- ajuste de naming estrutural do modulo de atendimento para plural concluido:
  - pacote/raiz movidos de `aprimorar.atendimento` para `aprimorar.atendimentos`
  - imports e declaracoes de pacote atualizados em `main` e `test`
  - metadata modulith mantida e publicada no novo pacote (`aprimorar.atendimentos`)
- validacao apos rename para `atendimentos`:
  - `./mvnw compile -DskipTests` OK
  - `./mvnw test -Dtest=ModuleVerificationTest` OK (com warnings nao bloqueantes de JaCoCo/JDK)

### Falta Fazer

- validar e eventualmente refinar este dicionario durante a implementacao
- continuar a Fase 1 com normalizacao adicional de naming e contratos onde ainda houver ambiguidade
- reduzir o acoplamento interno `student -> parent.internal` na Fase 2 com a quebra estrutural de `registration`
- quebrar `registration` em ownership real sob `pessoas/`
- seguir a Fase 2 movendo contratos de `parent` para API publica consistente e reduzindo acoplamentos internos residuais
- remover acoplamentos internos restantes em `parent` e `student` que ainda nao representem contratos de modulo
- revisar e simplificar naming de contratos `api/contract/event` antes do split fisico para `pessoas/*`
- preparar o split fisico de `registration` para `pessoas/*` com baixo risco de quebra de fronteira
- consolidar `package-info.java` e `@NamedInterface` dos novos subdominios `pessoas/*`
- remover residuos de nomenclatura antiga (`Parent*`, `Student*`, `Employee*`) que ainda devem virar `Responsavel*`, `Aluno*`, `Colaborador*`
- revisar nomenclatura de metodos/parametros restantes em ingles no modulo `pessoas.colaborador` (ex.: `getEmployees`, `employeeId`) e decidir janela de traducao
- revisar padrao de traducao de endpoints legados em outros modulos (`appointment`, `aluno`, `responsavel`) antes da fase de renomeacao HTTP ampla
- traduzir modulos por ordem definida
- migrar schema do banco via Flyway
- revisar OpenAPI e testes finais

### Regra de Uso Deste Arquivo

- toda decisao nova desta migracao deve ser registrada aqui
- toda mudanca de nome relevante deve atualizar o dicionario oficial
- ao fim de cada fase, atualizar `Feito` e `Falta Fazer`

---

## Comandos de Verificacao

Executar em `server/`:

- `./mvnw clean compile`
- `./mvnw test`
- `./mvnw test -Dtest=ModuleVerificationTest`

Quando houver alteracao de schema que dependa de banco local:

- `docker compose up -d db`

---

## Meta Final

Ao final da migracao, o backend deve parecer um sistema desenhado originalmente assim:

- arquitetura modular clara
- fronteiras publicas pequenas e explicitas
- implementacoes privadas em `internal/`
- dominio central em portugues
- `auth` e `shared` preservados em ingles por decisao consciente
- schema do banco alinhado com o dominio final
- sem residuos importantes de nomenclatura antiga
