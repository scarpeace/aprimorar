# Plano 03 — Integração e migração

## Objetivo

Executar a transição para `instituicao` e `financeiro` incrementalmente, preservando compilação, contratos e regras em cada etapa, até remover a view agregada e atualizar o frontend.

## Princípio de execução

Não mover todos os packages de uma vez. Cada fase deve terminar com diagnostics sem erros novos, compilação, testes específicos, verificação modular quando aplicável e OpenAPI coerente.

O usuário autorizou descartar registros locais existentes. Isso não autoriza apagar produção ou editar migrations já aplicadas.

## Fase 0 — Baseline

1. Registrar o estado atual sem alterar trabalho não relacionado.
2. Executar, mediante autorização no momento:
   - `./mvnw clean compile`;
   - testes específicos;
   - `ModuleVerificationTest`.
3. Registrar falhas preexistentes separadamente.

## Fase 1 — Contratos financeiros

Criar primeiro:

```text
aprimorar.financeiro.api.cobrancas
aprimorar.financeiro.api.repasses
```

Adicionar APIs, commands, resumos e `package-info.java` com `NamedInterface`. Fazer os services atuais implementarem os contratos antes da movimentação completa. Validar Spring Modulith.

## Fase 2 — Completar regras financeiras

1. Adicionar `CANCELADO` aos status.
2. Implementar cancelamento por atendimento.
3. Implementar consultas de pendência.
4. Implementar resumos em lote.
5. Adicionar buscas por `atendimentoId`.
6. Adicionar constraints únicas para `atendimento_id` por nova migration.
7. Cobrir com testes.

Ainda não remover a view.

## Fase 3 — Mover financeiro

Mover em blocos:

1. common financeiro;
2. cobranças;
3. repasses;
4. despesas;
5. handlers/configurações.

Destino: `aprimorar.financeiro`.

A cada bloco, atualizar packages/imports e `basePackages`, compilar, testar e verificar ausência de import de instituição. Manter comportamento HTTP até a fase de rotas.

## Fase 4 — Mover alunos e colaboradores

Mover:

1. `Endereco` para common institucional;
2. alunos;
3. colaboradores;
4. handlers/configurações.

Destino: `aprimorar.instituicao`.

Adaptar desativação:

- aluno consulta `CobrancaApi`;
- colaborador consulta `RepasseApi`.

Adicionar testes de bloqueio por pendência.

## Fase 5 — Mover atendimentos

1. Mover packages para instituição.
2. Adicionar relações JPA com aluno e colaborador.
3. Remover contratos antigos de existência de pessoas.
4. Alterar specifications para `AtendimentoIndividualEntity`.
5. Adicionar status e métodos de domínio.
6. Implementar realizar e cancelar.

Não criar relações JPA com financeiro.

## Fase 6 — Substituir acesso financeiro direto

Substituir imports de entities/repositories financeiros por `CobrancaApi` e `RepasseApi`.

### Criar

```text
salvar atendimento
criar cobrança
criar repasse
commit
```

### Atualizar

```text
carregar atendimento
atualizar cobrança
atualizar repasse
atualizar atendimento
commit
```

### Cancelar

```text
carregar atendimento
cancelar cobrança pendente
cancelar repasse pendente
cancelar atendimento
commit
```

Testar rollback quando a segunda API recusar a operação.

## Fase 7 — Composição de leitura

### Listagem

1. Buscar página de atendimentos com aluno e colaborador.
2. Coletar IDs.
3. Consultar `CobrancaApi` em lote.
4. Consultar `RepasseApi` em lote.
5. Compor o response.

### Detalhe

Reutilizar contratos em lote com conjunto de um ID.

### Calendário

Retornar o mínimo necessário. Consultar financeiro em lote somente se o calendário realmente usar status financeiros.

Garantias:

- nenhuma consulta por item;
- coleção vazia não consulta repository;
- ausência de resumo financeiro é erro de consistência;
- responses não expõem entidades.

## Fase 8 — Remover a view

Após migrar todos os consumidores:

1. remover conversores que recebem `AtendimentoIndividualViewEntity`;
2. excluir `AtendimentoIndividualViewEntity`;
3. excluir `AtendimentoIndividualViewRepository`;
4. remover specifications da view;
5. remover queries obsoletas;
6. criar nova migration para remover `vw_atendimentos_individuais`.

Não editar a migration original. Buscar ao final por:

```text
AtendimentoIndividualViewEntity
AtendimentoIndividualViewRepository
vw_atendimentos_individuais
```

## Fase 9 — Banco de dados

Criar migrations novas para:

- status do atendimento;
- status financeiros cancelados, quando houver constraints;
- unicidade de `atendimento_id` em cobrança e repasse;
- foreign keys necessárias;
- remoção da view.

Não há necessidade de preservar dados locais atuais. Antes de resetar:

1. confirmar PostgreSQL local/dev;
2. nunca executar contra produção;
3. não imprimir credenciais;
4. recriar schema via Flyway;
5. não adicionar `data.sql` apenas para compensar o reset.

## Fase 10 — Rotas

Migrar para:

```text
/instituicao/alunos
/instituicao/colaboradores
/instituicao/atendimentos
/financeiro/cobrancas
/financeiro/repasses
/financeiro/despesas
```

Adicionar ações de realizar e cancelar. Remover exclusão física após o frontend migrar. Evitar manter contratos duplicados indefinidamente.

## Fase 11 — OpenAPI e frontend

Depois de estabilizar o backend:

1. iniciar backend;
2. executar `npm run sync`;
3. não editar gerados;
4. corrigir imports de hooks/tipos;
5. atualizar navegação;
6. mover despesas para `/financeiro/despesas`;
7. criar páginas de lote;
8. adaptar status de atendimento;
9. substituir exclusão por cancelamento;
10. adicionar ação de realizar;
11. atualizar query invalidations.

## Fase 12 — Documentação

Atualizar `AGENTS.md` apenas depois da implementação, incluindo módulos, direção de dependência, rotas, status, migrations e fluxo backend/frontend. Atualizar README se houver referências antigas.

## Estratégia de commits sugerida

O agente não cria commits sem solicitação. Divisão recomendada:

1. contratos financeiros;
2. regras/consultas financeiras;
3. movimentação financeira;
4. movimentação de alunos/colaboradores;
5. status e movimentação de atendimentos;
6. integração via APIs;
7. composição e remoção da view;
8. migrations;
9. rotas/OpenAPI;
10. frontend;
11. documentação.

## Riscos

### Ciclo de módulos

Mitigação: financeiro não importa instituição; executar `ModuleVerificationTest` em cada fase estrutural.

### Transação parcial

Mitigação: instituição orquestra APIs em transação `REQUIRED`; testar rollback.

### N+1

Mitigação: contratos em lote e testes de consulta quando viável.

### Perda de regras

Mitigação: separar movimentação física, contrato e regra em fases distintas.

### Contrato gerado desatualizado

Mitigação: sincronizar Kubb somente após estabilizar backend.

### Reset incorreto

Mitigação: confirmar alvo exato e limitar ao profile dev.

## Checklist final

- [ ] `aprimorar.pessoas` removido ou vazio.
- [ ] `aprimorar.atendimentos.individuais` removido ou vazio.
- [ ] `aprimorar.despesas` movido.
- [ ] Instituição depende somente de `financeiro.api`.
- [ ] Financeiro não depende de instituição.
- [ ] `ModuleVerificationTest` aprovado.
- [ ] Atendimento relaciona aluno e colaborador internamente.
- [ ] Financeiro usa IDs escalares externos.
- [ ] Atendimento possui status e não é excluído.
- [ ] Lançamentos cancelados são preservados.
- [ ] View removida.
- [ ] Consultas financeiras em lote.
- [ ] Migrations aplicadas desde banco limpo.
- [ ] OpenAPI regenerado.
- [ ] Frontend migrado.
- [ ] Testes aprovados.
- [ ] `AGENTS.md` atualizado.

## Critério de conclusão

A refatoração termina quando instituição gerencia alunos, colaboradores e atendimentos usando apenas contratos públicos do financeiro; financeiro opera sem importar instituição; a view não existe; e backend e frontend funcionam com as novas rotas e regras.
