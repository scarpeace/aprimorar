# AGENTS - Frontend Next.js

## Escopo

Este arquivo vale para o frontend atual do projeto em `client/`.

## Stack

- Next.js App Router
- React 19
- React Query
- React Hook Form
- Zod
- DaisyUI + Tailwind
- Kubb para código gerado do contrato OpenAPI

## Regras principais

- não editar manualmente nada em `src/lib/api/generated/`
- mudou endpoint/DTO/enum no backend: rodar `npm run sync`
- componente novo deve seguir padrão já existente na feature irmã
- preferir componente pequeno reutilizável a duplicação de bloco visual
- estado remoto fica com React Query
- estado local fica na tela/componente

## Estrutura preferida

- `src/app/` para rotas e composição de páginas
- `src/components/` para blocos de UI por feature
- `src/components/ui/` para componentes base reutilizáveis
- `src/components/ui/forms/` para inputs integrados com RHF
- `src/hooks/` para mutações e coordenação de cache/toast
- `src/lib/` para api, auth, constants, utils e validação

## Formulários

- usar React Hook Form com `FormProvider` quando o form ficar distribuído
- usar componentes de `src/components/ui/forms/`
- validação de UX fica no schema Zod
- sanitização e normalização pesada continuam no backend

## Dados e API

- usar hooks e tipos gerados pelo Kubb
- mutações manuais ficam em `src/hooks/`
- invalidar lista, detalhe e derivados quando a mutação alterar esses dados
- evitar `fetch` direto em componente

## Padrões já fechados

- telas de detalhe usam:
  - cabeçalho simples com nome/status/ações
  - bloco de dados
  - seletor de mês quando a tela depende de período
  - resumo financeiro
  - tabela de atendimentos vinculados
- ações:
  - editar: `primary`
  - arquivar: `warning`
  - excluir: `error`
- tabelas com detalhe geralmente usam linha clicável em vez de botão extra

## Comandos úteis

- desenvolvimento: `npm run dev`
- lint: `npm run lint`
- build: `npm run build`
- regenerar contrato: `npm run sync`

## Dependências externas do fluxo

- `npm run sync` depende do backend rodando em `http://localhost:8080/v3/api-docs`
- a API base default é `http://localhost:8080`

## O que evitar

- criar um segundo padrão de formulário para a mesma aplicação
- editar código gerado
- criar estado global sem necessidade real
- adicionar biblioteca para resolver coisa que já existe no projeto
