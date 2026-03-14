# Guia de Automacao de Sprint

Este documento explica, em passo a passo, como usar o fluxo de sprint automatizado deste repositorio.

## Objetivo

Padronizar planejamento e execucao de sprint com dois componentes:

- Agente Scrum/PM (MCP GitHub) para planejar e gerar `sprint.md`
- Script local `ops/sprint.py` para sincronizar issues e Project no GitHub

Essa separacao deixa o processo rapido, auditavel e previsivel.

## Arquitetura recomendada

- Planejamento assistido: MCP so le contexto e propoe o plano
- Escrita oficial: apenas `ops/sprint.py` cria/atualiza issue e campos do Project
- Fonte unica da verdade: `sprint.md`
- Idempotencia: cada item usa `ticket_key` estavel (`S01-001`, `S01-002`, etc.)

## Arquivos importantes

- `sprint.md`: contrato da sprint atual
- `ops/sprint.py`: CLI de validacao e sync
- `docs/planning/sprint-template.md`: template pronto de sprint
- `.opencode/prompts/scrum-agent-mcp.md`: prompt base para o agente Scrum
- `ops/sprint_schema_example.json`: exemplo de payload JSON

## Pre requisitos

1. Python 3 instalado
2. GitHub CLI (`gh`) instalado e autenticado
3. Escopos do token:
   - `repo`
   - `project`
4. Um GitHub Project v2 existente
5. Labels que serao usadas no `sprint.md` ja criadas no repositorio

Comando para atualizar escopos:

```bash
gh auth refresh --hostname github.com -s repo -s project
```

Comandos uteis de descoberta:

```bash
gh auth status
gh project list --owner "<owner>" --format json
gh project field-list <project-number> --owner "<owner>" --format json
```

## Modo estrito (como funciona)

O sync usa modo estrito por padrao:

- Falha se label nao existir
- Falha se campo do Project nao existir
- Falha se opcao de single-select nao existir
- Falha se `ticket_key` estiver duplicado

Beneficio: evita criar dados inconsistentes no backlog.

## Passo a passo (primeira execucao)

### 1) Preparar `sprint.md`

Use como base `docs/planning/sprint-template.md`.

Checklist rapido:

- `repo` esta correto (`owner/repo`)
- `project.owner` e `project.number` apontam para o Project certo
- `defaults.status` usa uma opcao valida de `Status` no Project
- labels do JSON existem no repo
- cada item tem `ticket_key` unico

### 2) Validar contrato

```bash
python3 ops/sprint.py validate --file sprint.md
```

Se falhar, ajuste o JSON antes de continuar.

### 3) Simular sincronizacao (obrigatorio)

```bash
python3 ops/sprint.py sync --file sprint.md --dry-run
```

Revise:

- se cada item esta como `create` ou `update`
- labels e assignees esperados
- campos de Project que serao atualizados

### 4) Aplicar no GitHub

```bash
python3 ops/sprint.py sync --file sprint.md --apply
```

Resultado esperado:

- issues criadas/atualizadas
- items adicionados ao Project
- campos aplicados (ex.: `Status`)

## Passo a passo (rotina semanal)

1. Gerar nova proposta de sprint via MCP usando `.opencode/prompts/scrum-agent-mcp.md`
2. Salvar em `sprint.md`
3. Rodar `validate`
4. Rodar `dry-run`
5. Ajustar erros de modo estrito
6. Rodar `--apply`
7. Compartilhar links das issues criadas na daily/planning

## Boas praticas

### Planejamento

- Mantenha sprint com escopo pequeno e claro
- Use titulos objetivos, sem "ajustes gerais"
- Inclua `acceptance_criteria` em todos os itens
- Prefira de 5 a 10 itens por sprint

### Ticket key

- Nunca reciclar `ticket_key`
- Nunca renomear `ticket_key` depois que a issue existe
- Use prefixo de sprint (`S02-001`, `S02-002`)

### Sincronizacao

- Sempre rodar `--dry-run` antes de `--apply`
- Tratar erro de schema antes de erro de integracao
- Fazer sync em pequenos lotes quando houver muitas mudancas
- Nao editar manualmente titulo/corpo/labels de issue sincronizada sem atualizar `sprint.md`

### Project

- Verificar opcoes de `Status` e `Priority` antes de planejar
- Padronizar nomes de campo no Project (evita mapeamento extra)
- Se mudar nome de campo, atualizar `field_map` no `sprint.md`

## Troubleshooting rapido

### Erro de escopo do token

Sintoma:

- mensagem sobre scope ausente (`repo`/`project`)

Acao:

```bash
gh auth refresh --hostname github.com -s repo -s project
gh auth status
```

### Opcao de status invalida

Sintoma:

- erro como "Opcao inexistente em 'Status'"

Acao:

1. Rodar `gh project field-list ...`
2. Usar valor existente (ex.: `Backlog`, `In progress`, `Done`)
3. Ajustar `defaults.status` ou item especifico

### Label inexistente

Sintoma:

- erro de label faltando

Acao:

1. Criar label no repositorio
2. Ou trocar por label existente no `sprint.md`

### Project incorreto

Sintoma:

- item foi para board errada

Acao:

1. Confirmar `project.owner` e `project.number`
2. Validar com `gh project list --owner ...`
3. Reaplicar sync

## Exemplo minimo de execucao

```bash
python3 ops/sprint.py validate --file sprint.md
python3 ops/sprint.py sync --file sprint.md --dry-run
python3 ops/sprint.py sync --file sprint.md --apply
```

## Checklist de pronto para usar em time

- [ ] Escopos `gh` corretos
- [ ] Project e campos confirmados
- [ ] `sprint.md` validado
- [ ] Dry-run revisado por alguem do time
- [ ] Apply executado
- [ ] Links das issues compartilhados

## Evolucao futura sugerida

- Adicionar comando de resumo (`report`) no `ops/sprint.py`
- Adicionar comando para validar labels/campos antes do dry-run
- Criar automacao opcional em GitHub Actions (apenas depois do fluxo local estar estavel)
