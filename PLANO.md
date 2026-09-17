# Plano de refatoração arquitetural

Este documento é o índice da refatoração que dividirá o sistema nos módulos `instituicao` e `financeiro`. Os detalhes executáveis estão separados em três planos para reduzir o risco de mudanças incompletas ou perda de contexto.

## Planos

1. [Instituição](docs/planos/01-instituicao.md)
2. [Financeiro](docs/planos/02-financeiro.md)
3. [Integração e migração](docs/planos/03-integracao-e-migracao.md)

O terceiro plano define a ordem real de execução. Os planos de instituição e financeiro descrevem o estado-alvo de cada módulo e não devem ser executados isoladamente sem observar essa ordem.

## Decisões fechadas

### Fronteiras

```text
aprimorar.instituicao
├── alunos
├── colaboradores
└── atendimentos

aprimorar.financeiro
├── api
├── cobrancas
├── repasses
└── despesas
```

- Matrículas não serão criadas nesta refatoração.
- Endereço continua como value object compartilhado por aluno e colaborador dentro de instituição.
- Responsável continua como value object pertencente ao aluno.
- Cobranças, repasses e despesas pertencem ao financeiro.
- Alunos, colaboradores e atendimentos pertencem à instituição.

### Direção das dependências

```text
instituicao -> financeiro.api
```

- O financeiro não importa classes de instituição.
- O financeiro armazena `atendimentoId`, `alunoId` e `colaboradorId` como referências escalares.
- As foreign keys entre tabelas dos módulos serão mantidas.
- Não serão criados relacionamentos JPA entre instituição e financeiro.
- A instituição usa `CobrancaApi` e `RepasseApi` separadamente.
- Não será criada uma `FinanceiroApi` genérica.

### Transações

- Criar atendimento e criar cobrança/repasse é uma operação síncrona e transacional.
- Atualizar atendimento e atualizar os lançamentos é uma operação síncrona e transacional.
- Cancelar atendimento e cancelar os lançamentos pendentes é uma operação síncrona e transacional.
- As APIs internas usam a mesma transação e o mesmo datasource.
- Eventos não serão usados para invariantes obrigatórias.

### Atendimento

- Status: `AGENDADO`, `REALIZADO`, `CANCELADO`.
- `REALIZADO` depende de ação explícita.
- Transições permitidas:
  - `AGENDADO -> REALIZADO`;
  - `AGENDADO -> CANCELADO`.
- Atendimento realizado não pode ser cancelado.
- Atendimento com cobrança paga ou repasse pago não pode ser editado.
- Atendimento com cobrança paga ou repasse pago não pode ser cancelado.
- Exclusão física será substituída por cancelamento.
- Valores de cobrança e repasse continuam entrando no request de atendimento, mas não são persistidos na entidade de atendimento.
- A instituição não persiste `cobrancaId` nem `repasseId`.

### Financeiro

- Status de cobrança e repasse: `PENDENTE`, `PAGO`, `CANCELADO`.
- Cancelar atendimento preserva cobrança e repasse com status `CANCELADO`.
- Cancelar um pagamento registrado continua sendo uma reversão financeira para `PENDENTE`; não é o mesmo caso de uso que cancelar o lançamento por cancelamento do atendimento.
- Cobranças e repasses pagos não podem ser cancelados pelo fluxo de cancelamento do atendimento.
- Despesas permanecem independentes de atendimentos.

### Alunos e colaboradores

- Não haverá exclusão física.
- Cobrança pendente impede desativar aluno.
- Repasse pendente impede desativar colaborador.
- Lançamentos pagos ou cancelados não impedem desativação.

### Consultas

- A instituição compõe os responses de atendimento.
- Aluno e colaborador podem ser relações JPA de atendimento porque estão no mesmo módulo.
- Dados financeiros são obtidos em lote pelas APIs internas de cobrança e repasse.
- Não haverá consulta financeira por linha.
- `vw_atendimentos_individuais`, sua entidade e seu repository serão removidos após a migração dos consumidores.

### Rotas

Estado-alvo:

```text
/instituicao/alunos
/instituicao/colaboradores
/instituicao/atendimentos

/financeiro/cobrancas
/financeiro/repasses
/financeiro/despesas
```

As classes podem manter o sufixo `Individual` quando ele descreve corretamente o tipo de atendimento.

## Regras de execução

- Backend primeiro; frontend depois da atualização do OpenAPI/Kubb.
- Não editar arquivos gerados em `client/src/lib/api/generated/`.
- Não editar migrations Flyway já aplicadas; criar novas migrations.
- O reset de dados autorizado deve ocorrer somente no ambiente local/dev e com o alvo confirmado antes do comando destrutivo.
- Executar mudanças incrementalmente e validar cada etapa antes da próxima.
- Manter as rotas antigas apenas enquanto necessário para uma transição controlada; não manter contratos duplicados indefinidamente.
