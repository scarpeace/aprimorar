# Plano — Emagrecendo o domínio de atendimentos individuais

## Objetivo

Transformar `atendimentos/individuais` em uma única fronteira funcional que
controle o atendimento, a cobrança do aluno e o repasse do colaborador na
mesma transação.

O módulo raiz continuará sendo `atendimentos`. `individuais`, cobrança e
repasse não serão módulos independentes do Spring Modulith e não terão
contratos ou eventos entre si.

A escrita usará diretamente as três tabelas reais. Todas as consultas HTTP de
atendimento, cobrança e repasse usarão uma única view:
`vw_atendimentos_individuais`.

## Decisões fechadas

- O atendimento individual nasce com uma cobrança e um repasse pendentes.
- `AtendimentoIndividualRequest` recebe `valorCobranca` e `valorRepasse`.
- Os valores não serão armazenados em `atendimentos_individuais`.
- `CobrancaIndividualEntity` será dona do valor cobrado do aluno.
- `RepasseIndividualEntity` será dono do valor devido ao colaborador.
- Cobranças e repasses terão estados `PENDENTE` e `PAGO`.
- Cobranças usarão `dataPagamento`; repasses usarão `dataRepasse`.
- Ambos poderão armazenar `formaPagamento` e `comprovanteUrl`.
- Pagamento e repasse poderão ser registrados ou cancelados em lote.
- Um lote de cobranças deverá pertencer ao mesmo aluno.
- Um lote de repasses deverá pertencer ao mesmo colaborador.
- Cobrança paga não poderá ter aluno ou valor alterado.
- Repasse pago não poderá ter colaborador ou valor alterado.
- Um atendimento com cobrança ou repasse pago não poderá ser excluído.
- Continuará válida a regra `valorCobranca >= valorRepasse`.
- Continuará válida a regra de valor mínimo da cobrança já existente.
- Será criada uma nova migration; as migrations existentes não serão editadas.
- O frontend está fora deste plano.

## Nomenclatura final

### Banco de dados

```text
atendimentos                 -> atendimentos_individuais
cobrancas_alunos             -> cobrancas_individuais
repasse_colaborador          -> removido de atendimentos_individuais
repasses_individuais         -> nova tabela
vw_consultas_atendimentos    -> vw_atendimentos_individuais
```

### Java

```text
AtendimentoEntity                    -> AtendimentoIndividualEntity
AtendimentoConsultaViewEntity        -> AtendimentoIndividualViewEntity
AtendimentoRepository                -> AtendimentoIndividualRepository
AtendimentoConsultaRepository        -> AtendimentoIndividualViewRepository
AtendimentoConsultaSpecifications    -> AtendimentoIndividualSpecifications
AtendimentoMutationService           -> AtendimentoIndividualService
AtendimentoQueryService              -> AtendimentoIndividualQueryService

CobrancaAlunoEntity                   -> CobrancaIndividualEntity
CobrancaAlunoRepository               -> CobrancaIndividualRepository
CobrancaAluno* DTOs/exceptions        -> CobrancaIndividual*

novo                                  -> RepasseIndividualEntity
novo                                  -> RepasseIndividualRepository
novo                                  -> RepasseIndividual* DTOs/exceptions
```

Não serão criados `CobrancaIndividualService` nem
`RepasseIndividualService`. O `AtendimentoIndividualService` será o único
orquestrador de escrita.

## Estrutura desejada

```text
aprimorar/atendimentos/
├── package-info.java
└── individuais/
    ├── config/
    │   └── AtendimentoIndividualExceptionHandler.java
    ├── domain/
    │   ├── AtendimentoIndividualEntity.java
    │   ├── AtendimentoIndividualViewEntity.java
    │   ├── CobrancaIndividualEntity.java
    │   ├── RepasseIndividualEntity.java
    │   ├── enums/
    │   │   ├── FormaPagamento.java
    │   │   ├── StatusCobrancaIndividual.java
    │   │   ├── StatusRepasseIndividual.java
    │   │   └── TipoAtendimento.java
    │   └── exception/
    ├── repository/
    │   ├── atendimento/
    │   │   └── AtendimentoIndividualRepository.java
    │   ├── cobranca/
    │   │   └── CobrancaIndividualRepository.java
    │   ├── repasse/
    │   │   └── RepasseIndividualRepository.java
    │   └── view/
    │       ├── AtendimentoIndividualViewRepository.java
    │       └── AtendimentoIndividualSpecifications.java
    ├── service/
    │   ├── AtendimentoIndividualService.java
    │   └── AtendimentoIndividualQueryService.java
    └── web/
        ├── controller/
        │   └── AtendimentoIndividualController.java
        └── dto/
            ├── atendimento/
            ├── cobranca/
            └── repasse/
```

Não haverá `package-info.java` dentro de `individuais`, cobrança ou repasse.
Para o Spring Modulith, tudo continuará pertencendo ao módulo
`atendimentos`.

## Modelo de dados final

### `atendimentos_individuais`

```text
id
aluno_id
colaborador_id
data_hora_inicio
data_hora_fim
tipo
created_at
updated_at
```

### `cobrancas_individuais`

```text
id
atendimento_id        unique, not null
aluno_id              not null
valor                 not null
status                PENDENTE | PAGO
data_pagamento        nullable
forma_pagamento       nullable
comprovante_url       nullable
created_at
updated_at
```

### `repasses_individuais`

```text
id
atendimento_id        unique, not null
colaborador_id        not null
valor                 not null
status                PENDENTE | PAGO
data_repasse          nullable
forma_pagamento       nullable
comprovante_url       nullable
created_at
updated_at
```

Cada atendimento possuirá exatamente uma cobrança e um repasse. Essa
cardinalidade será protegida por índices únicos e chaves estrangeiras no
banco.

## Responsabilidade da view

`vw_atendimentos_individuais` será um modelo somente de leitura. Ela não será
a fonte da verdade; apenas reunirá os dados das tabelas reais.

A view terá uma linha por atendimento e fará `JOIN` obrigatório com:

- `alunos`;
- `colaboradores`;
- `cobrancas_individuais`;
- `repasses_individuais`.

Ela fornecerá, no mínimo:

```text
atendimento_id, tipo, inicio, fim, created_at, updated_at
aluno_id, aluno_nome
colaborador_id, colaborador_nome
cobranca_id, cobranca_valor, cobranca_status,
cobranca_data_pagamento, cobranca_forma_pagamento,
cobranca_comprovante_url
repasse_id, repasse_valor, repasse_status,
repasse_data, repasse_forma_pagamento,
repasse_comprovante_url
```

## Fases

### Fase 1 — Migration estrutural (concluída)

Criar `V9__consolidate_atendimentos_individuais.sql`.

Ordem da migration:

1. Remover `vw_consultas_atendimentos`.
2. Renomear `atendimentos` para `atendimentos_individuais`.
3. Renomear sequence, índices e constraints associados ao atendimento.
4. Renomear `cobrancas_alunos` para `cobrancas_individuais`.
5. Renomear sequence, índices e constraints associados à cobrança.
6. Tornar `atendimento_id`, `aluno_id` e `valor` da cobrança obrigatórios.
7. Adicionar a FK da cobrança para `atendimentos_individuais` caso ainda não
   exista.
8. Criar `repasses_individuais` com constraints, FKs e índices.
9. Copiar `repasse_colaborador` de cada atendimento para um repasse pendente.
10. Remover `repasse_colaborador` de `atendimentos_individuais`.
11. Criar `vw_atendimentos_individuais` juntando atendimento, participantes,
    cobrança e repasse.
12. Ajustar sequences depois do backfill quando necessário.

Também atualizar `data.sql`:

- ordem dos `DELETE` respeitando as novas FKs;
- novos nomes de tabelas;
- atendimento sem valores financeiros;
- cobrança individual com o valor cobrado;
- repasse individual com o antigo `repasse_colaborador`;
- registros pagos e pendentes para testar os dois fluxos.

Não iniciar a aplicação entre esta fase e a adequação dos mapeamentos JPA,
pois a migration e o código antigo serão temporariamente incompatíveis.

### Fase 2 — Domínio consolidado (concluída)

- Renomear `AtendimentoEntity` para `AtendimentoIndividualEntity`.
- Mapear a entidade para `atendimentos_individuais`.
- Remover `repasseColaborador` da entidade, construtor e `update`.
- Manter na entidade apenas regras próprias de agenda e janela de edição.
- Mover e renomear `CobrancaAlunoEntity` para
  `CobrancaIndividualEntity` dentro de `individuais/domain`.
- Mapear a cobrança para `cobrancas_individuais`.
- Criar `RepasseIndividualEntity` seguindo o ciclo da cobrança.
- Mover `FormaPagamento` para o domínio compartilhado de `individuais`.
- Criar enums separados `StatusCobrancaIndividual` e
  `StatusRepasseIndividual`.
- Renomear exceções para o vocabulário individual.

Operações de domínio esperadas:

```text
CobrancaIndividualEntity.registrarPagamento(...)
CobrancaIndividualEntity.cancelarPagamento()
CobrancaIndividualEntity.update(alunoId, valor)

RepasseIndividualEntity.registrarRepasse(...)
RepasseIndividualEntity.cancelarRepasse()
RepasseIndividualEntity.update(colaboradorId, valor)
```

### Fase 3 — Repositórios e locks (concluída, com leitura pendente)

- Renomear `AtendimentoRepository` para
  `AtendimentoIndividualRepository`.
- Renomear `AtendimentoConsultaRepository` para
  `AtendimentoIndividualViewRepository`.
- Renomear `CobrancaAlunoRepository` para
  `CobrancaIndividualRepository`.
- Criar `RepasseIndividualRepository`.
- O repositório de atendimento não expõe `JpaSpecificationExecutor`; o
  repositório de cobrança mantém essa extensão temporariamente para preservar
  as consultas HTTP atuais até a migração da leitura para a view na fase 5.
- Manter as queries de conflito no repositório de atendimento.
- Manter locks pessimistas nas operações em lote de cobrança.
- Criar locks equivalentes para operações em lote de repasse.
- Criar busca com lock por `atendimentoId` em cobrança e repasse para update e
  delete atômicos.
- Remover os repositórios antigos depois de migrar todos os consumidores.

### Fase 4 — Escrita unificada (concluída)

`AtendimentoIndividualService` agora concentra toda a escrita. Os services
antigos permanecem apenas como adaptadores temporários para os controllers
atuais e serão removidos na fase 7.

Dependências diretas:

```text
AtendimentoIndividualRepository
CobrancaIndividualRepository
RepasseIndividualRepository
AlunoService
ColaboradorService
```

Fluxo de criação:

1. Validar os campos e participantes já exigidos pelo fluxo atual.
2. Validar disponibilidade.
3. Validar `valorCobranca >= valorRepasse` e o valor mínimo da cobrança.
4. Salvar o atendimento.
5. Criar a cobrança pendente.
6. Criar o repasse pendente.
7. Retornar o ID do atendimento.

Tudo ocorrerá em uma única transação. Qualquer falha reverterá as três
gravações.

Fluxo de atualização:

1. Buscar o atendimento.
2. Validar participantes, disponibilidade e janela de edição.
3. Bloquear cobrança e repasse pelo `atendimentoId`.
4. Validar os estados financeiros.
5. Atualizar atendimento, cobrança e repasse na mesma transação.

Fluxo de exclusão:

1. Buscar o atendimento.
2. Bloquear cobrança e repasse.
3. Recusar exclusão se qualquer um estiver pago.
4. Remover cobrança e repasse pendentes.
5. Remover o atendimento.

Fluxos financeiros no mesmo service:

- `registrarPagamento(...)` e `cancelarPagamento(...)` para cobranças;
- `registrarRepasse(...)` e `cancelarRepasse(...)` para repasses;
- rejeitar IDs duplicados ou inexistentes;
- exigir o mesmo aluno no lote de cobranças;
- exigir o mesmo colaborador no lote de repasses;
- usar lock pessimista para impedir baixa concorrente do mesmo registro.

Os comandos de atualização, exclusão, pagamento e repasse retornarão `void`.
Dados atualizados serão obtidos pelas rotas de consulta da view.

### Fase 5 — Leitura única pela view (concluída, com repasse pendente)

- Renomear `AtendimentoConsultaViewEntity` para
  `AtendimentoIndividualViewEntity`.
- Mapear para `vw_atendimentos_individuais` com `@Immutable`.
- Renomear o query service para `AtendimentoIndividualQueryService`.
- Fazer as consultas existentes de atendimento e cobrança usarem
  exclusivamente `AtendimentoIndividualViewRepository`.
- Remover `CobrancaAlunoSpecifications`.
- Substituir as specifications atuais por uma única classe:
  `AtendimentoIndividualSpecifications`.

A classe de specifications terá composições específicas para cada caso de
uso, sem criar novas classes:

```text
paraAtendimentos(AtendimentoIndividualFiltroRequest)
paraCobrancas(CobrancaIndividualFiltroRequest)
paraRepasses(RepasseIndividualFiltroRequest)
```

Consultas previstas (repasses serão expostos junto dos DTOs/controllers na
fase 6):

```text
buscarAtendimentos(...)
buscarAtendimentoPorId(...)
buscarCobrancas(...)
buscarCobrancaPorId(...)
buscarRepasses(...)
buscarRepassePorId(...)
```

As respostas serão projeções próprias de cada rota, todas montadas a partir
da mesma entidade da view.

### Fase 6 — DTOs e controllers (concluída)

Renomear os DTOs de atendimento para `AtendimentoIndividual*` e trocar os
campos financeiros do request:

```text
pagamentoAluno       -> valorCobranca
repasseColaborador   -> valorRepasse
```

Organizar os DTOs em:

```text
web/dto/atendimento
web/dto/cobranca
web/dto/repasse
```

Controllers e prefixos:

```text
AtendimentoIndividualController
  /v1/atendimentos-individuais

CobrancaIndividualController
  /v1/atendimentos-individuais/cobrancas

RepasseIndividualController
  /v1/atendimentos-individuais/repasses
```

Cada controller será fino e poderá injetar:

- `AtendimentoIndividualService` para comandos;
- `AtendimentoIndividualQueryService` para consultas.

Contratos HTTP desejados:

- criação do atendimento: `201` com `Location`;
- atualização e exclusão: `204`;
- registrar ou cancelar pagamento: `204`;
- registrar ou cancelar repasse: `204`;
- consultas: `200` com DTO ou `Page<DTO>`.

O response completo do atendimento conterá:

- resumo do aluno;
- resumo do colaborador;
- resumo da cobrança;
- resumo do repasse.

### Fase 7 — Erros e limpeza estrutural

- Consolidar o tratamento em `AtendimentoIndividualExceptionHandler`.
- Incluir erros de atendimento, cobrança e repasse.
- Remover `CobrancaAlunoExceptionHandler`.
- Remover `CobrancaAlunoServiceImpl`.
- Remover todo o diretório `atendimentos/cobranca_aluno` após a migração.
- Remover imports e nomes antigos.
- Remover diretórios vazios.
- Confirmar que não há `package-info.java` criando módulos internos.
- Manter apenas `atendimentos/package-info.java` como fronteira Modulith.

### Fase 8 — Validação

- Compilar depois de cada fase Java compatível.
- Reiniciar a aplicação após migration e mapeamentos estarem alinhados.
- Verificar a execução da `V9` em banco limpo e em banco no estado da `V8`.
- Conferir que cada atendimento possui exatamente uma cobrança e um repasse.
- Testar criação, atualização e exclusão transacionais.
- Testar bloqueio de atualização e exclusão para valores pagos.
- Testar registro e cancelamento em lote de cobrança e repasse.
- Testar paginação e filtros das três consultas pela view.
- Executar `./mvnw -q -DskipTests compile`.
- Executar `./mvnw test` quando os testes afetados estiverem alinhados.
- Atualizar `AGENTS.md` ao final com a estrutura efetivamente implementada.

## Critérios de conclusão

- Não existe mais tabela `atendimentos`.
- Não existe mais tabela `cobrancas_alunos`.
- Não existe mais coluna `repasse_colaborador` no atendimento.
- Existem `atendimentos_individuais`, `cobrancas_individuais` e
  `repasses_individuais`.
- Existe apenas a view `vw_atendimentos_individuais` para as consultas desse
  domínio.
- Nenhuma consulta HTTP de cobrança ou repasse usa seus repositórios de
  escrita.
- Existe apenas um service de escrita para o fluxo individual.
- Existe apenas um query service, um view repository e uma classe de
  specifications.
- Não há eventos ou contratos internos entre atendimento, cobrança e repasse.
- O Spring Modulith continua enxergando apenas o módulo raiz `atendimentos`.

## Fora deste plano

- Atendimentos em turma.
- Cobranças ou repasses de turma.
- Compartilhamento de tabelas financeiras entre individual e turma.
- Relatórios institucionais e contabilidade geral.
- Materialização da view.
- Eventos para integrações externas.
- Alterações no frontend.
