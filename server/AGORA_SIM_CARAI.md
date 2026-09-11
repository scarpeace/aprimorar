# Plano — Cobranças dentro de atendimentos

## Objetivo

Reorganizar as fronteiras do backend para refletir o ciclo de vida real do
domínio:

- `cobranca_aluno` passa a ser uma capacidade interna de `atendimentos`;
- `despesas` passa a ser um módulo independente na raiz da aplicação;
- o módulo intermediário `financeiro` deixa de existir;
- a sincronização entre atendimento e cobrança deixa de usar eventos internos
  e passa a acontecer por chamadas diretas dentro da mesma transação;
- `individuais`, `cobranca_aluno` e os futuros `turmas` e
  `repasse_colaborador` permanecem pacotes internos, não módulos aninhados do
  Spring Modulith.

O resultado desejado é:

```text
aprimorar/
├── atendimentos/                         # módulo Spring Modulith
│   ├── individuais/                      # capacidade interna
│   └── cobranca_aluno/                   # capacidade interna compartilhável
├── despesas/                             # módulo Spring Modulith
├── pessoas/                              # módulo Spring Modulith
├── auth/                                 # módulo Spring Modulith
├── common/                               # módulo compartilhado
└── config/
```

## Decisões já tomadas

- A cobrança não terá módulo Spring Modulith próprio.
- `atendimentos` continuará sendo a única fronteira modular desse contexto.
- `individuais` e `cobranca_aluno` não receberão `@ApplicationModule`.
- Não haverá `@NamedInterface` para comunicação entre essas duas capacidades.
- Cobrança de aluno será compartilhada futuramente por atendimentos individuais
  e turmas enquanto as regras de cobrança forem iguais.
- Despesas não pertence ao ciclo de vida do atendimento e será um módulo raiz.
- A tabela continuará se chamando `cobrancas_alunos`.
- As tabelas, colunas, constraints, view e dados do seed não serão renomeados
  nesta mudança.
- Os endpoints e seus payloads permanecerão iguais.
- A exclusão de atendimento com cobrança paga continuará proibida.
- Uma falha ao criar, atualizar ou excluir a cobrança deverá desfazer também a
  operação correspondente no atendimento.

## Estado atual

```text
atendimentos
└── individuais
    ├── api
    │   ├── AtendimentoCreatedEvent
    │   ├── AtendimentoUpdatedEvent
    │   ├── AtendimentoDeletedEvent
    │   └── package-info.java             # @NamedInterface("events")
    └── service
        └── AtendimentoMutationService

financeiro                               # @ApplicationModule
├── cobranca_aluno
│   ├── api                              # @NamedInterface("cobranca_aluno")
│   ├── config
│   ├── domain
│   ├── repository
│   ├── service
│   │   ├── CobrancaAlunoServiceImpl
│   │   └── CobrancaAlunoEventListener
│   └── web
└── despesas
```

O fluxo atual é:

```text
AtendimentoMutationService
        │
        └── publica evento
                  │
                  ▼
        CobrancaAlunoEventListener
                  │
                  ▼
        CobrancaAlunoRepository
```

Esse fluxo foi útil enquanto atendimento e cobrança estavam em módulos
diferentes. Depois da mudança de fronteira, o evento apenas adicionaria uma
indireção dentro do mesmo módulo.

## Estrutura final esperada

```text
aprimorar/
├── atendimentos/
│   ├── package-info.java                 # @ApplicationModule("atendimentos")
│   ├── individuais/
│   │   ├── config/
│   │   ├── domain/
│   │   ├── enums/
│   │   ├── repository/
│   │   ├── service/
│   │   └── web/
│   └── cobranca_aluno/
│       ├── config/
│       ├── domain/
│       ├── repository/
│       ├── service/
│       └── web/
├── despesas/
│   ├── package-info.java                 # @ApplicationModule("despesas")
│   ├── config/
│   ├── domain/
│   ├── enums/
│   ├── repository/
│   ├── service/
│   └── web/
└── ...
```

Não será criada uma pasta `api` em `atendimentos` apenas por antecipação. Ela
só deverá existir quando outro módulo precisar consumir um contrato público de
atendimentos.

## Fluxo final esperado

```text
AtendimentoMutationService
        │
        ├── AtendimentoRepository
        └── CobrancaAlunoServiceImpl
                  │
                  └── CobrancaAlunoRepository
```

A dependência interna será unidirecional:

```text
individuais -> cobranca_aluno
```

`cobranca_aluno` não injetará nem chamará services ou repositories de
`individuais`.

## Fases de implementação

### Fase 1 — Extrair despesas para um módulo raiz

Objetivo: separar a mudança independente e manter o sistema compilando antes de
mexer na integração entre atendimento e cobrança.

Alterações:

1. Mover `aprimorar.financeiro.despesas` para `aprimorar.despesas`.
2. Atualizar os `package` e imports dos arquivos de:
   - configuração;
   - domínio e exceções;
   - enums;
   - repository e specifications;
   - service;
   - controller e DTOs.
3. Mover os testes de despesas para o novo pacote.
4. Criar `aprimorar/despesas/package-info.java` com:
   - identificador `despesas`;
   - nome de exibição `Despesas`;
   - somente as dependências externas realmente utilizadas, atualmente
     `common::*`.
5. Manter temporariamente `financeiro/package-info.java`, pois
   `cobranca_aluno` ainda estará nesse módulo durante esta fase.
6. Remover diretórios de despesas que ficarem vazios dentro de `financeiro`.

Validação da fase:

- pesquisar referências a `aprimorar.financeiro.despesas`;
- executar `./mvnw -q -DskipTests compile`;
- executar os testes de despesas ou `test-compile`.

Não há migration nem reinicialização obrigatória do banco nesta fase.

### Fase 2 — Mover cobrança para atendimentos

Objetivo: mudar a propriedade arquitetural sem alterar ainda o mecanismo de
eventos, reduzindo o tamanho de cada passo.

Alterações:

1. Mover `aprimorar.financeiro.cobranca_aluno` para
   `aprimorar.atendimentos.cobranca_aluno`.
2. Atualizar todos os `package` e imports de:
   - handler;
   - entidade, status e exceções;
   - repository e specifications;
   - service e listener;
   - controller e DTOs.
3. Atualizar o `basePackages` do `CobrancaAlunoExceptionHandler`.
4. Mover `FormaPagamento` da antiga pasta pública `api` para o domínio interno
   de cobrança e atualizar seus consumidores.
5. Remover `cobranca_aluno/api/package-info.java`, pois a capacidade não será
   mais exposta como named interface.
6. Preservar nesta fase:
   - `CobrancaAlunoEventListener`;
   - os três eventos de atendimento;
   - URLs, DTOs e respostas HTTP;
   - nomes de tabela e view.

Validação da fase:

- pesquisar referências a `aprimorar.financeiro.cobranca_aluno`;
- executar `./mvnw -q -DskipTests compile`;
- executar `test-compile`.

Não há migration nesta fase. O banco não precisa ser resetado.

### Fase 3 — Substituir eventos por colaboração interna direta

Objetivo: remover a comunicação entre módulos que deixou de existir e tornar o
fluxo explícito dentro de uma única transação.

#### 3.1 Operações internas de cobrança

Adicionar ao service de cobrança operações internas orientadas ao atendimento:

```java
void criarParaAtendimento(Long atendimentoId, UUID alunoId, BigDecimal valor);

void atualizarPorAtendimento(Long atendimentoId, UUID alunoId, BigDecimal valor);

void excluirPorAtendimento(Long atendimentoId);
```

Esses métodos permanecerão na implementação concreta interna. Não será criada
uma interface pública com uma única implementação.

Responsabilidades:

- `criarParaAtendimento` cria uma cobrança `PENDENTE`;
- `atualizarPorAtendimento` bloqueia a cobrança correspondente e chama o
  `update` da entidade;
- `excluirPorAtendimento` bloqueia a cobrança correspondente, rejeita cobrança
  `PAGO` e exclui somente cobranças que podem ser removidas;
- cobrança ausente continua sendo tratada como inconsistência do fluxo, e não
  como uma ausência silenciosa.

#### 3.2 Agendamento

O fluxo será:

```text
validar participantes
→ validar disponibilidade
→ salvar atendimento
→ criar cobrança pendente
→ concluir a mesma transação
```

O ID gerado pelo atendimento será passado diretamente para o service de
cobrança.

#### 3.3 Atualização

O fluxo será:

```text
buscar atendimento
→ validar participantes
→ validar disponibilidade ignorando o próprio atendimento
→ atualizar atendimento
→ atualizar cobrança com aluno e valor
→ concluir a mesma transação
```

Se a cobrança já estiver paga e não aceitar a alteração, a exceção deverá
provocar rollback das alterações do atendimento.

#### 3.4 Exclusão

O fluxo será:

```text
buscar atendimento
→ validar e excluir cobrança pendente
→ excluir atendimento
→ concluir a mesma transação
```

Se a cobrança estiver paga:

- a exclusão da cobrança será recusada;
- o atendimento permanecerá no banco;
- a resposta continuará sendo um erro de negócio apropriado.

#### 3.5 Limpeza do mecanismo antigo

Depois das chamadas diretas estarem funcionando:

1. Remover `ApplicationEventPublisher` de `AtendimentoMutationService`.
2. Remover as publicações de eventos de criação, atualização e exclusão.
3. Excluir `CobrancaAlunoEventListener`.
4. Excluir:
   - `AtendimentoCreatedEvent`;
   - `AtendimentoUpdatedEvent`;
   - `AtendimentoDeletedEvent`.
5. Excluir `atendimentos/individuais/api/package-info.java`.
6. Remover a pasta `api` se ficar vazia.

Validação da fase:

- compilar;
- verificar que não existe `ApplicationEventPublisher` no fluxo de
  atendimentos;
- verificar que não existem listeners ou eventos residuais de cobrança;
- executar testes focados no fluxo de criação, atualização e exclusão.

Após esta fase será necessário reiniciar a aplicação para testar o fluxo HTTP.

### Fase 4 — Fechar as novas fronteiras do Modulith

Objetivo: fazer os metadados arquiteturais refletirem a nova estrutura.

Alterações:

1. Manter somente `atendimentos/package-info.java` como declaração modular do
   contexto de atendimentos.
2. Manter as dependências permitidas de `atendimentos`:
   - `pessoas::aluno`;
   - `pessoas::colaborador`;
   - `common::*`.
3. Não declarar `individuais` ou `cobranca_aluno` como módulos aninhados.
4. Não declarar named interfaces internas entre atendimento e cobrança.
5. Excluir `financeiro/package-info.java` depois que a pasta não contiver mais
   despesas nem cobranças.
6. Remover a pasta `financeiro` quando estiver vazia.
7. Confirmar que `despesas/package-info.java` declara o novo módulo raiz.
8. Atualizar o teste/documentação de módulos, se ele contiver expectativas
   explícitas sobre `financeiro` ou `atendimentos::events`.

Validação da fase:

```bash
./mvnw test -Dtest=ModuleVerificationTest
```

O resultado esperado é não haver:

- módulo `financeiro`;
- dependência `financeiro -> atendimentos::events`;
- named interface `atendimentos::events`;
- named interface `financeiro::cobranca_aluno`;
- ciclo entre atendimentos e cobrança.

### Fase 5 — Validação funcional e limpeza final

#### Verificações estáticas

Pesquisar e eliminar referências residuais a:

```text
aprimorar.financeiro
atendimentos::events
financeiro::cobranca_aluno
AtendimentoCreatedEvent
AtendimentoUpdatedEvent
AtendimentoDeletedEvent
CobrancaAlunoEventListener
```

Também remover diretórios vazios deixados pelos movimentos.

#### Verificações automatizadas

Executar, nesta ordem:

```bash
./mvnw -q -DskipTests compile
./mvnw test-compile
./mvnw test -Dtest=ModuleVerificationTest
./mvnw test
```

Testes quebrados por simples mudança de pacote deverão ser movidos e
atualizados. Testes que validavam exclusivamente publicação ou consumo dos
eventos removidos deverão ser substituídos por testes do resultado transacional,
e não mantidos artificialmente.

#### Verificações manuais após reiniciar a aplicação

1. Criar atendimento:
   - atendimento criado;
   - cobrança pendente criada com o mesmo `atendimento_id`;
   - nenhuma das duas linhas persiste se a outra operação falhar.
2. Atualizar atendimento com cobrança pendente:
   - atendimento atualizado;
   - aluno e valor da cobrança sincronizados.
3. Tentar atualizar atendimento com cobrança paga:
   - operação recusada quando os dados financeiros forem alterados;
   - atendimento não fica parcialmente atualizado.
4. Excluir atendimento com cobrança pendente:
   - cobrança removida;
   - atendimento removido.
5. Excluir atendimento com cobrança paga:
   - operação recusada;
   - cobrança e atendimento permanecem.
6. Consultar, registrar e cancelar pagamento:
   - rotas atuais continuam funcionando com os mesmos contratos.
7. Executar operações de despesas:
   - endpoints continuam funcionando após a mudança de pacote.

### Fase 6 — Atualizar documentação operacional

Depois de todas as validações:

1. Atualizar `AGENTS.md` com a nova árvore de módulos.
2. Registrar que:
   - cobrança é parte interna de atendimentos;
   - despesas é módulo raiz;
   - eventos internos de sincronização foram removidos;
   - atendimento e cobrança são alterados na mesma transação.
3. Remover menções ao módulo `financeiro` e à named interface
   `atendimentos::events`.
4. Excluir este plano somente após confirmação do usuário de que a mudança foi
   concluída e validada.

## Arquivos e áreas afetadas

### Movimento de despesas

```text
src/main/java/aprimorar/financeiro/despesas/**
    -> src/main/java/aprimorar/despesas/**

src/test/java/aprimorar/financeiro/despesas/**
    -> src/test/java/aprimorar/despesas/**
```

### Movimento de cobrança

```text
src/main/java/aprimorar/financeiro/cobranca_aluno/**
    -> src/main/java/aprimorar/atendimentos/cobranca_aluno/**
```

### Atendimento individual

```text
src/main/java/aprimorar/atendimentos/individuais/service/AtendimentoMutationService.java
src/main/java/aprimorar/atendimentos/individuais/api/**
```

### Metadados e documentação

```text
src/main/java/aprimorar/atendimentos/package-info.java
src/main/java/aprimorar/financeiro/package-info.java
src/main/java/aprimorar/despesas/package-info.java
../AGENTS.md
```

Arquivos SQL só deverão ser alterados se uma referência de comentário ou seed
estiver incorreta. A estrutura física `cobrancas_alunos` e a view
`vw_consultas_atendimentos` permanecem válidas e não exigem migration.

## Riscos e controles

### Persistência parcial

Risco: atendimento ser salvo sem cobrança, ou cobrança ser alterada sem o
atendimento correspondente.

Controle: manter a orquestração em um método `@Transactional` do
`AtendimentoMutationService`; as chamadas ao service de cobrança participarão
da mesma transação.

### Exclusão de cobrança paga

Risco: remover o atendimento antes de descobrir que a cobrança não pode ser
excluída.

Controle: validar e remover a cobrança antes de solicitar a exclusão do
atendimento. Qualquer exceção causa rollback da transação completa.

### Movimento grande escondendo erro funcional

Risco: misturar renomeação de pacotes com alteração do fluxo e dificultar o
diagnóstico.

Controle: executar primeiro os movimentos de pacote, mantendo o listener; só na
fase seguinte substituir os eventos.

### Exposição acidental de código interno

Risco: manter named interfaces que não representam mais contratos entre
módulos.

Controle: remover as named interfaces antigas e validar a arquitetura com
`ModuleVerificationTest`.

## Fora do escopo

- implementar atendimentos em turma;
- implementar turmas online;
- implementar repasses de colaboradores;
- criar módulos aninhados dentro de `atendimentos`;
- criar APIs públicas antecipadas para capacidades internas;
- alterar endpoints, DTOs ou tipos gerados do frontend;
- alterar o schema ou renomear `cobrancas_alunos`;
- alterar a view de consultas sem necessidade causada por esta mudança;
- preparar integração HTTP, mensageria ou extração para microserviços.

## Critérios de conclusão

A mudança estará concluída quando:

- `despesas` for reconhecido como módulo raiz;
- `financeiro` não existir mais como módulo ou pacote;
- `cobranca_aluno` estiver dentro de `atendimentos`;
- atendimento e cobrança colaborarem diretamente na mesma transação;
- não existirem os eventos e listener antigos;
- não existirem named interfaces internas desnecessárias;
- os endpoints atuais mantiverem seus contratos;
- criação, atualização e exclusão forem atômicas;
- exclusão de atendimento com cobrança paga continuar bloqueada;
- compilação, testes e verificação modular passarem;
- diretórios vazios e referências antigas tiverem sido removidos;
- `AGENTS.md` refletir a arquitetura final.

Ao final de cada fase implementada, deverão ser informados explicitamente os
arquivos criados, movidos, alterados e removidos.
