# Sprint Template

Use este template para planejar sprint e sincronizar tickets com GitHub.

## Como usar
- Edite as secoes humanas normalmente.
- Mantenha apenas um bloco `json` no arquivo.
- `ticket_key` deve ser unico e estavel (nao troque depois de criar issue).
- Em modo estrito, labels/campos/opcoes do Project precisam existir antes do sync.

## Comandos locais
- Validar contrato: `python3 docs/ops/sprint.py validate --file docs/sprint.md`
- Simular sync: `python3 docs/ops/sprint.py sync --file docs/sprint.md --dry-run`
- Aplicar sync: `python3 docs/ops/sprint.py sync --file docs/sprint.md --apply`

## Pre requisitos
- `gh` autenticado com escopos: `repo`, `read:project`, `project`
- Se faltar escopo: `gh auth refresh -s repo -s read:project -s project`

## Sprint
Periodo: 2026-03-16 a 2026-03-29
Objetivo: melhorar cadastro e reduzir bugs P1.

## Contexto
- Capacidade: 32 pontos
- Foco: bugs criticos + 1 feature principal

## Ticket Spec (machine-readable)
```json
{
  "version": "1.0",
  "repo": "scarpeace/aprimorar",
  "project": {
    "owner": "scarpeace",
    "number": 1,
    "field_map": {
      "status": "Status",
      "priority": "Priority",
      "iteration": "Iteration",
      "roadmap": "Roadmap",
      "estimate": "Estimate",
      "target_date": "Target date"
    }
  },
  "sprint": {
    "key": "S24",
    "name": "Sprint 24",
    "start": "2026-03-16",
    "end": "2026-03-29"
  },
  "defaults": {
    "status": "Todo",
    "priority": "Medium",
    "labels": [],
    "assignees": []
  },
  "items": [
    {
      "ticket_key": "S24-001",
      "type": "feature",
      "title": "Cadastro: validacao de CPF em tempo real",
      "summary": "Validar CPF no formulario com feedback imediato.",
      "acceptance_criteria": [
        "Mensagem clara para CPF invalido",
        "Bloquear envio com CPF invalido"
      ],
      "labels": ["type:feature", "priority:p1", "area:students"],
      "assignees": ["@me"],
      "status": "Todo",
      "priority": "High",
      "estimate": 5,
      "iteration": "Sprint 24",
      "roadmap": "Q2"
    },
    {
      "ticket_key": "S24-002",
      "type": "bug",
      "title": "Eventos: corrigir conflito de horario",
      "summary": "Conflitos de horario ainda permitem salvar eventos sobrepostos.",
      "labels": ["type:bug", "priority:p1", "area:events"],
      "assignees": [],
      "status": "Todo",
      "priority": "High",
      "estimate": 3,
      "target_date": "2026-03-22"
    }
  ]
}
```
