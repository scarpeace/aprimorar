# Plano 01 — Módulo Instituição

## Objetivo

Criar `aprimorar.instituicao` como módulo proprietário de alunos, colaboradores e atendimentos, permitindo relações JPA internas e removendo a dependência da view agregada de atendimentos.

## Estado-alvo

```text
instituicao/
├── common/domain/Endereco.java
├── alunos/{domain,repository,service,web}
├── colaboradores/{domain,repository,service,web}
└── atendimentos/{domain,repository,service,web}
```

Não criar pacote de matrículas nesta refatoração. `Responsavel` permanece no domínio de alunos; `Endereco` continua como value object compartilhado dentro de instituição.

## 1. Mover pessoas para instituição

Mover o conteúdo funcional de `aprimorar.pessoas` para:

```text
aprimorar.instituicao.alunos
aprimorar.instituicao.colaboradores
```

Inclui entidades, value objects, enums, exceções, repositories, specifications, services, controllers e DTOs. Preservar inicialmente os nomes das classes para não misturar movimentação estrutural com renomeações cosméticas.

## 2. Mover atendimentos para instituição

Mover `aprimorar.atendimentos.individuais.atendimentos` para:

```text
aprimorar.instituicao.atendimentos
```

Preservar o sufixo `Individual`, que continua descrevendo o tipo de atendimento.

## 3. Mapear relações internas

Substituir os IDs escalares de aluno e colaborador na entidade de atendimento por relações JPA internas:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "aluno_id", nullable = false)
private AlunoEntity aluno;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "colaborador_id", nullable = false)
private ColaboradorEntity colaborador;
```

Regras:

- não usar cascade para remover aluno ou colaborador;
- não expor entidades nos DTOs;
- requests continuam recebendo `alunoId` e `colaboradorId`;
- service resolve as entidades antes de criar/atualizar o atendimento;
- usar `LAZY` e controlar o carregamento nas consultas;
- manter as foreign keys no banco.

Não criar relações JPA com cobrança, repasse ou despesa.

## 4. Status do atendimento

Criar:

```java
public enum StatusAtendimentoIndividual {
    AGENDADO,
    REALIZADO,
    CANCELADO
}
```

Adicionar o status à entidade com `@Enumerated(EnumType.STRING)` e coluna obrigatória.

Transições permitidas:

```text
AGENDADO -> REALIZADO
AGENDADO -> CANCELADO
```

Não permitir:

- `REALIZADO -> CANCELADO`;
- editar atendimento realizado;
- editar atendimento cancelado;
- realizar atendimento cancelado;
- cancelar atendimento com cobrança paga;
- cancelar atendimento com repasse pago.

Criar métodos de domínio explícitos:

```java
atendimento.realizar();
atendimento.cancelar();
```

`REALIZADO` depende de ação explícita, não da passagem do horário.

## 5. Criação de atendimento

O request continua recebendo:

- aluno ID;
- colaborador ID;
- data e horário;
- tipo;
- valor da cobrança;
- valor do repasse.

Os valores financeiros não são persistidos em `AtendimentoIndividualEntity`; instituição também não persiste `cobrancaId` nem `repasseId`.

Fluxo:

1. validar aluno e colaborador ativos;
2. validar conflito de agenda;
3. salvar atendimento como `AGENDADO`;
4. chamar `CobrancaApi.criar`;
5. chamar `RepasseApi.criar`;
6. confirmar a transação.

Se qualquer chamada financeira falhar, toda a operação sofre rollback.

## 6. Atualização de atendimento

Fluxo:

1. carregar atendimento;
2. exigir status `AGENDADO`;
3. resolver aluno e colaborador;
4. validar participantes ativos e disponibilidade;
5. chamar `CobrancaApi.atualizar`;
6. chamar `RepasseApi.atualizar`;
7. atualizar dados do atendimento;
8. confirmar a transação.

As APIs financeiras validam e atualizam atomicamente. Cobrança ou repasse pago/cancelado bloqueia a edição e causa rollback integral.

## 7. Realização e cancelamento

Criar ações explícitas:

```http
PATCH /instituicao/atendimentos/{id}/realizar
PATCH /instituicao/atendimentos/{id}/cancelar
```

### Realizar

- apenas atendimento `AGENDADO`;
- não depende de pagamento;
- não é automático pelo relógio.

### Cancelar

Fluxo transacional:

1. carregar atendimento;
2. exigir status `AGENDADO`;
3. chamar `CobrancaApi.cancelarPorAtendimento`;
4. chamar `RepasseApi.cancelarPorAtendimento`;
5. marcar atendimento como `CANCELADO`;
6. confirmar a transação.

Se cobrança ou repasse estiver pago, nada é alterado. Remover a exclusão física depois da migração do frontend.

## 8. Desativação de aluno

Antes de desativar:

```java
if (cobrancaApi.possuiPendenciaPorAlunoId(alunoId)) {
    throw new AlunoPossuiPendenciaFinanceiraException();
}
```

- `PENDENTE` bloqueia;
- `PAGO` e `CANCELADO` não bloqueiam;
- aluno não é excluído fisicamente;
- aluno inativo não pode receber novo atendimento.

## 9. Desativação de colaborador

Antes de desativar:

```java
if (repasseApi.possuiPendenciaPorColaboradorId(colaboradorId)) {
    throw new ColaboradorPossuiRepassePendenteException();
}
```

Aplicar regras equivalentes às de aluno. Colaborador não é excluído fisicamente e, quando inativo, não pode receber novo atendimento.

## 10. Specifications

Alterar `AtendimentoIndividualSpecifications` para operar sobre:

```java
Specification<AtendimentoIndividualEntity>
```

Permitir joins internos:

```java
Join<AtendimentoIndividualEntity, AlunoEntity> aluno =
    root.join("aluno", JoinType.INNER);

Join<AtendimentoIndividualEntity, ColaboradorEntity> colaborador =
    root.join("colaborador", JoinType.INNER);
```

Filtros:

- busca por nome do aluno;
- busca por nome do colaborador;
- busca por tipo;
- intervalo de data/hora;
- tipo;
- aluno ID;
- colaborador ID;
- status do atendimento.

Filtros financeiros deixam de pertencer à specification institucional.

## 11. Composição do response

1. Buscar `Page<AtendimentoIndividualEntity>` com aluno e colaborador via `EntityGraph`, projection ou fetch controlado.
2. Coletar IDs dos atendimentos.
3. Consultar cobranças em lote:
   ```java
   Map<Long, CobrancaResumo> cobrancas =
       cobrancaApi.buscarResumosPorAtendimentoIds(ids);
   ```
4. Consultar repasses em lote:
   ```java
   Map<Long, RepasseResumo> repasses =
       repasseApi.buscarResumosPorAtendimentoIds(ids);
   ```
5. Compor `AtendimentoIndividualResponse` sem expor entidades.

Não realizar chamadas financeiras por linha. Ausência de resumo é erro de consistência, pois todo atendimento deve ter cobrança e repasse.

Aplicar o mesmo padrão ao detalhe. Para o calendário, retornar somente os dados realmente usados e consultar financeiro em lote apenas se o calendário precisar desses status.

## 12. Rotas

Estado-alvo:

```text
/instituicao/alunos
/instituicao/colaboradores
/instituicao/atendimentos
/instituicao/atendimentos/calendario
/instituicao/atendimentos/{id}/realizar
/instituicao/atendimentos/{id}/cancelar
```

## 13. Testes

Cobrir:

- criação atômica de atendimento, cobrança e repasse;
- rollback quando criação financeira falha;
- edição bloqueada por cobrança paga;
- edição bloqueada por repasse pago;
- realização explícita;
- cancelamento de agendado;
- cancelamento bloqueado por lançamento pago;
- realizado não pode ser cancelado;
- desativação de aluno bloqueada por pendência;
- desativação de colaborador bloqueada por pendência;
- specifications com joins internos;
- composição financeira em lote.

## Critérios de aceite

- Instituição não importa entity, repository, service ou DTO web do financeiro.
- Instituição importa somente contratos permitidos de `financeiro.api`.
- Aluno, colaborador e atendimento não são excluídos fisicamente.
- Listagem, detalhe e calendário não dependem da view antiga.
- Composição financeira usa uma chamada em lote por capacidade.
- Rotas novas aparecem no OpenAPI.
