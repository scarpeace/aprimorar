# Sprint Automation Quickstart

Guia rapido (1 pagina) para planejar sprint com MCP e sincronizar no GitHub.

## O que voce ganha

- Planejamento padronizado por `sprint.md`
- Criacao/atualizacao de issues sem trabalho manual repetitivo
- Backlog/board atualizados no Project com rastreabilidade

## Fluxo oficial

1. Agente Scrum (MCP) gera/atualiza `sprint.md`
2. Script local valida e sincroniza:
   - `validate`
   - `dry-run`
   - `apply`

Regra: o MCP planeja; o `ops/sprint.py` escreve no GitHub.

## Setup inicial (uma vez)

1. Garantir `gh` autenticado com escopos de projeto:

```bash
gh auth refresh --hostname github.com -s repo -s project
gh auth status
```

2. Confirmar Project alvo:

```bash
gh project list --owner "<owner>" --format json
gh project field-list <project-number> --owner "<owner>" --format json
```

3. Ajustar `sprint.md`:
- `repo` correto (`owner/repo`)
- `project.owner` e `project.number` corretos
- `defaults.status` com opcao real do campo `Status`

## Execucao diaria (3 comandos)

```bash
python3 ops/sprint.py validate --file sprint.md
python3 ops/sprint.py sync --file sprint.md --dry-run
python3 ops/sprint.py sync --file sprint.md --apply
```

## Boas praticas essenciais

- Sempre rodar `dry-run` antes de `apply`
- Manter `ticket_key` estavel (`S02-001`, `S02-002`, ...)
- Nao editar manualmente issue sincronizada sem atualizar `sprint.md`
- Usar labels existentes (modo estrito)
- Incluir `acceptance_criteria` em todo item

## Modo estrito (importante)

O sync falha se:

- label nao existe
- campo/opcao do Project nao existe
- `ticket_key` esta duplicado

Isso evita inconsistencias no backlog.

## Erros comuns e correcao rapida

- Erro de escopo do token:
  - rode `gh auth refresh --hostname github.com -s repo -s project`
- Erro de status invalido:
  - verifique opcoes com `gh project field-list ...`
  - ajuste `defaults.status` no `sprint.md`
- Label inexistente:
  - crie a label no repo ou troque por uma label existente

## Referencias

- Guia completo: `docs/planning/sprint-automation-guide.md`
- Template de sprint: `docs/planning/sprint-template.md`
- Prompt Scrum MCP: `.opencode/prompts/scrum-agent-mcp.md`
- Exemplo JSON: `ops/sprint_schema_example.json`
