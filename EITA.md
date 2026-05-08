# EITA: pendencias da verificacao Modulith estrita

Este documento registra o que apareceu quando tentamos fechar os modulos do servidor e rodar a verificacao estrita do Spring Modulith.

A ideia aqui nao e apontar "erro de codigo" no sentido tradicional. O sistema compila, e boa parte da migracao ja melhorou muito a estrutura. O ponto e que, quando o Modulith deixa de tratar os modulos como abertos, ele passa a cobrar uma arquitetura sem ciclos. Nesse modo, algumas regras de negocio atuais ainda fazem um modulo depender do outro nos dois sentidos.

## Resumo curto

Hoje o projeto ainda tem ciclos reais entre modulos:

- `auth -> employee -> auth`
- `employee -> event -> employee`
- `event -> finance -> employee`
- `event -> student -> event`
- `parent -> student -> parent`

Tambem existem dependencias de infraestrutura que precisam ser redesenhadas:

- `config -> auth`, via `SecurityConfig` usando `AuthService`
- `exception -> varios modulos`, via `GlobalExceptionHandler` importando excecoes de dominio
- `dashboard -> event.internal`, via acesso direto a `EventRepository`

Esses pontos impedem a verificacao estrita de passar.

## Por que isso importa

Em um modular monolith, um modulo pode chamar a API publica de outro modulo. Isso e normal.

O problema aparece quando dois modulos precisam um do outro diretamente. Exemplo:

```text
auth precisa de employee
employee precisa de auth
```

Mesmo que o Spring consiga iniciar com `ObjectProvider`, o Modulith entende isso como um ciclo arquitetural. O modulo deixa de ter uma direcao clara e fica dificil evoluir, testar e documentar a fronteira.

## 1. Ciclo auth <-> employee

### O que acontece hoje

`auth` depende de `employee` porque:

- login por email de colaborador usa `EmployeeService.findIdByEmail()`
- sessao atual monta `displayName`, `email` e `duty` usando `EmployeeService`
- criacao/listagem de usuario busca dados do colaborador com `EmployeeService`

`employee` depende de `auth` porque:

- ao deletar colaborador, `EmployeeServiceImpl.deleteEmployee()` procura e remove o usuario associado via `UserService`

### Perguntas de negocio

- Um usuario interno sempre precisa estar ligado a um colaborador?
- Login por email do colaborador ainda e obrigatorio, ou login por username basta?
- Ao deletar colaborador, o usuario deve ser deletado, desativado ou mantido como historico?
- O dono da regra "quando colaborador sai, o que acontece com o usuario?" e `employee` ou `auth`?

### Caminhos possiveis

1. `employee` publica evento `EmployeeDeletedEvent`, e `auth` escuta para desativar/deletar o usuario.
2. `auth` vira dono da associacao usuario-colaborador, e `employee` nao chama mais `auth`.
3. Remover login por email do colaborador, deixando `auth` independente de consultas a `employee`.
4. Denormalizar dados basicos no usuario, como `displayName`, para evitar consulta sincrona ao `employee` no login.

## 2. Ciclo employee <-> event

### O que acontece hoje

`event` depende de `employee` porque:

- cria/atualiza evento validando colaborador
- monta DTO de evento com nome do colaborador
- filtra eventos por nome do colaborador

`employee` depende de `event` porque:

- resumo financeiro/operacional do colaborador chama contagens e somas de eventos
- ao deletar colaborador, reatribui eventos para o colaborador fantasma

### Perguntas de negocio

- O resumo do colaborador pertence mesmo ao modulo `employee`, ou deveria morar em `finance`/`dashboard`?
- Ao deletar colaborador, eventos devem ser reatribuidos para fantasma, arquivados, bloqueados ou preservados com `employeeId` antigo?
- `event` precisa do nome atual do colaborador sempre, ou pode guardar um snapshot do nome no momento da aula?
- Se o colaborador for renomeado, eventos antigos devem mostrar o nome novo ou o nome da epoca?

### Caminhos possiveis

1. Mover resumo de colaborador para `finance` ou `dashboard`, removendo `employee -> event`.
2. `employee` publica `EmployeeDeletedEvent`; `event` escuta e faz a reatribuicao para fantasma.
3. `event` guarda snapshot de `employeeName` no evento para reduzir lookups.
4. Criar uma API de leitura mais neutra, tipo `EventAnalyticsService`, mas ainda precisa cuidar para nao criar ciclo.

## 3. Ciclo event <-> finance

### O que acontece hoje

`event` depende de `finance` porque:

- ao criar evento, cria transacoes
- ao atualizar evento, sincroniza transacoes
- ao deletar evento, remove transacoes
- ao marcar cobranca/pagamento, sincroniza status financeiro

`finance` depende de `event` porque:

- calcula resumos usando contagem de eventos
- `TransactionRepository` consulta `Event` em JPQL para somar transacoes por aluno/colaborador

### Perguntas de negocio

- O evento deve comandar diretamente a criacao de transacoes?
- Ou `event` deve publicar eventos de dominio, e `finance` deve reagir?
- As transacoes devem ser a fonte da verdade financeira, ou `Event` ainda participa do calculo financeiro?
- Para resumos, `finance` precisa consultar `event`, ou as transacoes deveriam carregar dados suficientes (`studentId`, `employeeId`, datas, origem)?

### Caminhos possiveis

1. `event` publica `EventCreated`, `EventUpdated`, `EventDeleted`, `StudentChargeChanged`, `EmployeePaymentChanged`; `finance` escuta e atualiza transacoes.
2. `Transaction` passa a guardar explicitamente `studentId`, `employeeId` e data de referencia quando a origem for evento.
3. Remover JPQL de `finance` que faz join com `Event`.
4. Definir `Transaction` como fonte de verdade financeira e `Event` como fonte de verdade operacional.

## 4. Ciclo student <-> event

### O que acontece hoje

`event` depende de `student` porque:

- cria/atualiza evento validando aluno
- monta DTO de evento com nome do aluno
- filtra eventos por nome do aluno

`student` depende de `event` porque:

- resumo do aluno chama contagens e somas de eventos
- ao deletar aluno, reatribui eventos para aluno fantasma

### Perguntas de negocio

- O resumo do aluno pertence mesmo ao modulo `student`, ou deveria ser `finance`/`dashboard`?
- Ao deletar aluno, eventos devem ir para aluno fantasma, permanecer com `studentId`, ou impedir delecao se houver historico?
- Eventos antigos devem mostrar nome atual do aluno ou snapshot do nome da epoca?
- O aluno pode ser removido fisicamente, ou deveria sempre ser arquivado?

### Caminhos possiveis

1. Mover resumo do aluno para `finance` ou `dashboard`.
2. `student` publica `StudentDeletedEvent`; `event` escuta e decide reatribuicao.
3. Trocar delecao fisica por arquivamento, preservando historico sem reatribuir.
4. `event` guardar snapshot de `studentName` para evitar lookups em listagens.

## 5. Ciclo parent <-> student

### O que acontece hoje

`student` depende de `parent` porque:

- cria/atualiza aluno validando responsavel
- monta DTO do aluno com resumo do responsavel

`parent` depende de `student` porque:

- antes de arquivar/deletar responsavel, verifica se existem alunos ativos vinculados

### Perguntas de negocio

- Um responsavel pode ser arquivado se tiver alunos ativos?
- Se nao pode, essa validacao pertence a `parent` ou a um caso de uso de `student`?
- O aluno deve carregar snapshot dos dados do responsavel?
- A exclusao de responsavel deveria ser proibida sempre que existir qualquer aluno historico, nao apenas ativo?

### Caminhos possiveis

1. Tratar `Parent` como parte do agregado de `Student`, reduzindo a separacao entre os modulos.
2. Mover a regra "pode arquivar responsavel?" para `student`, porque quem sabe o vinculo ativo e o modulo de alunos.
3. `parent` publica `ParentArchiveRequestedEvent` e `student` valida/reage, mas isso complica o fluxo sincrono.
4. Manter `parent -> student` e remover `student -> parent` com snapshots/queries alternativas, se aceitavel.

## 6. Dashboard acessando EventRepository interno

### O que acontece hoje

`DashboardServiceImpl` injeta `EventRepository`, que fica em `event.internal.repository`.

Isso viola a regra mais importante do `api/internal`: outro modulo nao deve importar `internal` de um modulo vizinho.

### Perguntas de negocio

- Dashboard e apenas uma tela de agregacao, ou e um modulo de dominio de verdade?
- Queries de dashboard sobre eventos deveriam morar no proprio modulo `event`?
- O modulo `event` pode expor uma API publica de metricas, por exemplo `EventAnalyticsService`?

### Caminhos possiveis

1. Criar `EventAnalyticsService` em `event.api` e mover as queries agregadas para `event`.
2. Transformar dashboard em camada de aplicacao/read-model fora dos modulos de dominio.
3. Expor DTOs de metricas no `event.api` e remover `EventRepository.EventContentCount` da API consumida por dashboard.

## 7. SecurityConfig dependendo de AuthService

### O que acontece hoje

`SecurityConfig` injeta `AuthService` para configurar o `DaoAuthenticationProvider`.

Como `config` foi tratado como shared/infrastructure, o Modulith reclama que infraestrutura esta dependendo de um modulo de dominio.

### Perguntas de negocio/arquitetura

- `AuthService` e parte do dominio `auth` ou e infraestrutura de seguranca?
- A configuracao de security deveria ficar dentro do modulo `auth`?
- O `UserDetailsService` deveria ser um bean exposto por `auth.api` e consumido pela config global?

### Caminhos possiveis

1. Mover a configuracao de authentication provider para o modulo `auth`.
2. Criar uma interface de infraestrutura mais neutra fora dos modulos de dominio.
3. Aceitar `config` como pacote especial que pode depender de modulos, mas isso enfraquece a verificacao.

## 8. GlobalExceptionHandler dependendo de excecoes dos modulos

### O que acontece hoje

`GlobalExceptionHandler` importa excecoes especificas de varios modulos.

Como `exception` foi marcado como shared/infrastructure, o Modulith reclama de referencias para submodulos.

### Perguntas de arquitetura

- Queremos manter excecoes especificas por modulo?
- Queremos uma base comum, tipo `DomainException`, com status/errorCode?
- O handler global precisa conhecer cada excecao, ou pode tratar por contrato comum?

### Caminhos possiveis

1. Criar `DomainException` em `shared` ou `exception`, com `HttpStatus`/`ErrorCode`.
2. Fazer excecoes de modulo herdarem dessa base e o handler tratar apenas `DomainException`.
3. Manter handlers especificos dentro de cada modulo, se quisermos isolamento total.

## Minha leitura

A verificacao estrita nao esta falhando por detalhe pequeno. Ela esta mostrando que ainda falta uma fase de decisao arquitetural.

O caminho mais saudavel parece ser:

1. Definir ownership das regras de negocio principais.
2. Remover chamadas sincrona de "volta" usando eventos de dominio onde fizer sentido.
3. Tirar resumos financeiros/operacionais de `student` e `employee`, ou aceitar que esses modulos dependem de `event`.
4. Fazer `finance` ser a fonte de verdade financeira, sem join direto com `Event`.
5. Transformar `dashboard` em consumidor de APIs publicas, nunca de repositories internos.

## Ordem sugerida para proximos changes

1. `modulith-break-lifecycle-cycles`
   - tratar delete/archive de student/employee/parent via eventos ou nova regra de negocio

2. `modulith-finance-event-decoupling`
   - transformar chamadas `event -> finance` em eventos de dominio
   - enriquecer `Transaction` para nao precisar consultar `Event`

3. `modulith-dashboard-api`
   - criar API publica de analytics em `event`
   - remover acesso a `EventRepository` de `dashboard`

4. `modulith-exception-contract`
   - criar contrato comum de excecao
   - remover imports de excecoes de dominio no handler global

5. `modulith-strict-verification`
   - fechar modulos de novo
   - rodar `ApplicationModules.verify()`
   - criar docs e CI com a arquitetura ja aciclica

