# Sprint Inicial

Periodo: 2026-03-16 a 2026-03-29
Objetivo: iniciar o processo de planejamento e sincronizacao automatica de sprint no GitHub.

## Notas
- Este arquivo e o contrato unico para o sync local (`ops/sprint.py`).
- Ajuste `project.owner` e `project.number` se seu Project v2 for diferente.
- Modo estrito: labels e campos precisam existir antes de rodar `--apply`.

## Foco
- Consolidar um fluxo simples para criacao e acompanhamento de tickets.
- Priorizar base de organizacao (template, documentacao e correcao critica).

## Ticket Spec (machine-readable)
```json
{
  "version": "1.0",
  "repo": "scarpeace/aprimorar",
  "project": {
    "owner": "scarpeace",
    "number": 1,
    "field_map": {
      "status": "Status"
    }
  },
  "sprint": {
    "key": "S01",
    "name": "Sprint Inicial",
    "start": "2026-03-16",
    "end": "2026-03-29"
  },
  "defaults": {
    "status": "Todo",
    "labels": [],
    "assignees": []
  },
  "items": [
    {
      "ticket_key": "S01-001",
      "type": "feature",
      "title": "Padronizar templates de issue para bug e feature",
      "summary": "Definir um modelo enxuto para abertura de tickets com informacoes minimas e criterios de aceite.",
      "labels": ["enhancement"],
      "acceptance_criteria": [
        "Template com secoes obrigatorias para bug",
        "Template com secoes obrigatorias para feature",
        "Exemplo de preenchimento documentado"
      ]
    },
    {
      "ticket_key": "S01-002",
      "type": "bug",
      "title": "Corrigir validacao inconsistente no cadastro de alunos",
      "summary": "Mapear e corrigir validacoes que aceitam dados invalidos no fluxo de cadastro.",
      "labels": ["bug"],
      "acceptance_criteria": [
        "Reproducao documentada do problema atual",
        "Validacao bloqueia envio com dados invalidos",
        "Mensagem de erro amigavel em portugues"
      ]
    },
    {
      "ticket_key": "S01-003",
      "type": "chore",
      "title": "Documentar rotina local de planejamento e sync de sprint",
      "summary": "Registrar passo a passo para validar sprint.md, simular sync e aplicar alteracoes no GitHub.",
      "labels": ["documentation"],
      "acceptance_criteria": [
        "Comandos validate/dry-run/apply documentados",
        "Checklist de pre-requisitos do gh auth",
        "Guia rapido para ajustar owner e project number"
      ]
    }
  ]
}
```
