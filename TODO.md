# TODO

Este arquivo centraliza pendencias tecnicas para implementacao futura.

## Estrutura

- ID: identificador curto e unico.
- Titulo: resumo objetivo do que precisa ser feito.
- Contexto: onde o problema aparece e por que importa.
- Objetivo: resultado esperado apos implementar.
- Escopo: o que entra e o que nao entra.
- Criterios de aceite: como validar que ficou correto.
- Riscos/observacoes: pontos de atencao.
- Status: `todo` | `doing` | `done`.

## Itens

### ID: ATD-001
- Titulo: Remover dependencia de UUID ghost hardcoded em atendimentos.
- Contexto: `AtendimentoMutationService` usa `GHOST_STUDENT_ID` e `GHOST_COLABORADOR_ID` fixos para reassign em exclusoes.
- Objetivo: resolver entidades ghost por regra de negocio/construto estavel, sem UUID magico hardcoded no codigo.
- Escopo:
  - Entram:
    - Resolver colaborador ghost por atributo de dominio (ex.: `duty=SYSTEM`) ou chave de negocio dedicada.
    - Resolver aluno ghost por atributo/chave sentinela de dominio.
    - Encapsular resolucao em componente unico (ex.: resolver/service/provider).
  - Nao entram:
    - Mudanca de contrato HTTP.
    - Refactor amplo fora de atendimentos/pessoas.
- Criterios de aceite:
  - Nenhum UUID ghost hardcoded em services de negocio.
  - Reassign de aluno e colaborador continua funcional em ambiente local e testes.
  - Falha com erro claro quando ghost esperado nao existir.
- Riscos/observacoes:
  - Dependencia de dados seed/migration para garantir existencia de ghost records.
  - Cuidado com performance se houver consulta repetida sem cache.
- Status: todo
