# Plano 02 — Módulo Financeiro

## Objetivo

Criar `aprimorar.financeiro` como módulo proprietário de cobranças, repasses e despesas, expondo APIs internas pequenas para instituição sem importar entidades ou serviços institucionais.

## Estado-alvo

```text
financeiro/
├── api/
│   ├── cobrancas/
│   └── repasses/
├── common/
│   └── FormaPagamentoEnum.java
├── cobrancas/{domain,repository,service,web}
├── repasses/{domain,repository,service,web}
└── despesas/{domain,repository,service,web}
```

Projections ficam em `repository/projections` e specifications em `repository/specifications`.

## 1. Mover capacidades

```text
aprimorar.atendimentos.individuais.cobrancas -> aprimorar.financeiro.cobrancas
aprimorar.atendimentos.individuais.repasses  -> aprimorar.financeiro.repasses
aprimorar.despesas                           -> aprimorar.financeiro.despesas
```

Mover `FormaPagamentoEnum` para `aprimorar.financeiro.common`. Preservar inicialmente os nomes das classes.

## 2. Contratos públicos

Criar interfaces em `financeiro.api`, declaradas como `NamedInterface`.

### CobrancaApi

```java
public interface CobrancaApi {
    void criar(CriarCobrancaCommand command);
    void atualizar(AtualizarCobrancaCommand command);
    void cancelarPorAtendimento(Long atendimentoId);
    boolean possuiPendenciaPorAlunoId(UUID alunoId);
    Map<Long, CobrancaResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds);
}
```

### RepasseApi

```java
public interface RepasseApi {
    void criar(CriarRepasseCommand command);
    void atualizar(AtualizarRepasseCommand command);
    void cancelarPorAtendimento(Long atendimentoId);
    boolean possuiPendenciaPorColaboradorId(UUID colaboradorId);
    Map<Long, RepasseResumo> buscarResumosPorAtendimentoIds(Set<Long> atendimentoIds);
}
```

Os contratos não expõem entidades JPA, repositories, DTOs HTTP, `Page` de persistência nem classes de instituição.

## 3. Commands

```java
public record CriarCobrancaCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {}

public record AtualizarCobrancaCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {}

public record CriarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {}

public record AtualizarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {}
```

Commands são contratos Java internos. Não reutilizar DTOs web.

## 4. Resumos financeiros

```java
public record CobrancaResumo(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    StatusCobrancaIndividual status,
    LocalDateTime dataPagamento,
    FormaPagamentoEnum formaPagamento,
    UUID loteId
) {}
```

```java
public record RepasseResumo(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    StatusRepasseIndividual status,
    LocalDateTime dataRepasse,
    FormaPagamentoEnum formaPagamento,
    UUID loteId
) {}
```

Campos condicionais permanecem nullable em lançamentos pendentes ou cancelados.

## 5. Status financeiros

Usar nos dois enums:

```java
PENDENTE,
PAGO,
CANCELADO
```

### Reverter pagamento/repasse

Operação financeira existente:

```text
PAGO -> PENDENTE
```

Limpa data, forma de pagamento, lote e comprovante.

### Cancelar por atendimento

Nova operação institucional:

```text
PENDENTE -> CANCELADO
```

Não permitir:

```text
PAGO -> CANCELADO
CANCELADO -> PENDENTE
CANCELADO -> PAGO
```

Cancelamento preserva o lançamento.

## 6. Implementação das APIs

`CobrancaIndividualService` implementa `CobrancaApi`. `RepasseIndividualService` implementa `RepasseApi`. Não criar adapters sem uma segunda implementação real.

### Criar

- exigir IDs e valor;
- impedir mais de uma cobrança ou repasse por atendimento;
- iniciar como `PENDENTE`.

### Atualizar

- buscar por `atendimentoId`, com lock quando necessário;
- permitir somente `PENDENTE`;
- atualizar participante e valor;
- rejeitar `PAGO` ou `CANCELADO`.

### Cancelar por atendimento

- buscar por `atendimentoId`;
- permitir somente `PENDENTE`;
- marcar `CANCELADO`;
- preservar registro;
- rejeitar lançamento pago.

## 7. Repositories

Adicionar:

```java
Optional<CobrancaIndividual> findByAtendimentoId(Long atendimentoId);
Optional<RepasseIndividual> findByAtendimentoId(Long atendimentoId);

boolean existsByAlunoIdAndStatus(UUID alunoId, StatusCobrancaIndividual status);
boolean existsByColaboradorIdAndStatus(UUID colaboradorId, StatusRepasseIndividual status);

List<CobrancaIndividual> findAllByAtendimentoIdIn(Collection<Long> ids);
List<RepasseIndividual> findAllByAtendimentoIdIn(Collection<Long> ids);
```

Adicionar constraints únicas para `atendimento_id` nas duas tabelas.

Os services convertem resultados em mapas por atendimento. Coleção vazia retorna mapa vazio sem consultar o banco.

## 8. Specifications

Manter:

```java
Specification<CobrancaIndividual>
Specification<RepasseIndividual>
```

Usar somente campos próprios:

- participante ID;
- status;
- forma de pagamento;
- intervalo de pagamento/repasse;
- lote ID, quando necessário.

Não fazer join com instituição e não depender da view antiga.

## 9. Lotes

Manter projections e consultas de lote dentro das capacidades financeiras.

Detalhe do lote e listagem de transações são responsabilidades financeiras. Cancelamento de lote, quando implementado, reverte pagamentos ou repasses para `PENDENTE`; não cancela atendimentos e não consulta instituição.

## 10. Despesas

Mover despesas sem relacioná-las a atendimento. Manter entidade, service, controller e specifications próprios. Compartilhar o módulo financeiro não obriga despesas a importar cobrança ou repasse.

## 11. Rotas

```text
/financeiro/cobrancas
/financeiro/cobrancas/{id}
/financeiro/cobrancas/lotes
/financeiro/cobrancas/lotes/{loteId}
/financeiro/cobrancas/pagar
/financeiro/cobrancas/cancelar

/financeiro/repasses
/financeiro/repasses/{id}
/financeiro/repasses/lotes
/financeiro/repasses/lotes/{loteId}
/financeiro/repasses/pagar
/financeiro/repasses/cancelar

/financeiro/despesas
```

Distinguir cancelamento/reversão de pagamento do cancelamento interno de lançamento por atendimento. A segunda operação não precisa de endpoint HTTP.

## 12. Eventos

Não criar eventos por antecipação. Se houver consumidor concreto, eventos financeiros ficam em `financeiro.api`, e o financeiro publica seus próprios eventos sem importar instituição.

Possíveis eventos futuros:

```text
PagamentoRegistradoEvent
PagamentoCanceladoEvent
RepasseRegistradoEvent
RepasseCanceladoEvent
```

## 13. Testes

Cobrir:

- criação pendente;
- duplicidade por atendimento;
- atualização pendente;
- atualização bloqueada para pago/cancelado;
- cancelamento por atendimento;
- cancelamento pago bloqueado;
- pendência por aluno/colaborador;
- resumo em lote;
- coleção vazia;
- registro e reversão de pagamento/repasse;
- lotes e projections;
- independência de despesas.

## Critérios de aceite

- Financeiro não importa packages de instituição.
- Instituição acessa financeiro somente por `financeiro.api`.
- Não existem relações JPA para entidades institucionais.
- Cobrança e repasse possuem `atendimentoId` único e escalar.
- Cancelamento institucional preserva lançamentos como `CANCELADO`.
- Consultas em lote não geram N+1.
- DTOs HTTP não são contratos internos.
- Rotas aparecem corretamente no OpenAPI.
