# Sprint Inicial

Periodo: 2026-03-16 a 2026-03-29
Objetivo: Implementar a paginação em páginas que usam tabelas

## Notas
- Este arquivo e o contrato unico para o sync local (`ops/sprint.py`).
- Ajuste `project.owner` e `project.number` se seu Project v2 for diferente.
- Modo estrito: labels e campos precisam existir antes de rodar `--apply`.

## Foco
- Melhorar a amostragem de dados para o cliente

## Ticket Spec (machine-readable)
```json
{
  "version": "1.0",
  "repo": "scarpeace/aprimorar",
  "project": {
    "owner": "scarpeace",
    "number": 3,
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
    "status": "Backlog",
    "labels": [],
    "assignees": []
  },
  "items": [
     {
    "ticket_key": "S01-008",
    "type": "feature",
    "title": "Implementar paginacao nos eventos vinculados em telas de detalhe",
    "summary": "Aplicar paginacao nas tabelas de eventos vinculados dentro dos detalhes de aluno e colaborador para evitar carregamento excessivo em uma unica resposta.",
    "labels": ["enhancement"],
    "status": "Backlog",
    "priority": "P1",
    "estimate": 3,
    "acceptance_criteria": [
      "A tela de detalhe do aluno pagina os eventos vinculados usando endpoint paginado",
      "A tela de detalhe do colaborador pagina os eventos vinculados usando endpoint paginado",
      "O total de eventos vinculados continua visivel e coerente com os metadados da API",
      "A troca de pagina da tabela vinculada nao afeta o carregamento do restante da tela"
    ]
  },
  {
    "ticket_key": "S01-009",
    "type": "chore",
    "title": "Endurecer contrato de paginacao e validar cenarios limite",
    "summary": "Garantir consistencia tecnica da paginacao com limites seguros de parametros, ordenacao suportada e cobertura dos cenarios de pagina invalida, erro e vazio.",
    "labels": ["enhancement"],
    "status": "Backlog",
    "priority": "P1",
    "estimate": 2,
    "acceptance_criteria": [
      "Os endpoints paginados possuem comportamento definido para page, size e sort invalidos ou fora do esperado",
      "Existe validacao para evitar tamanhos de pagina excessivos que prejudiquem desempenho",
      "Os cenarios de ultima pagina, pagina vazia e erro de consulta sao verificados nas telas impactadas",
      "O contrato de paginacao permanece estavel entre alunos, colaboradores e eventos"
    ]
  }
  ]
}
```
