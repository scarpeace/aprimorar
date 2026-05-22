# AGENTS - Client

## Objetivo
- Este arquivo orienta agentes a manter os padroes de arquitetura e codigo do frontend.
- Priorize consistencia com os hooks gerados, React Query e componentes orientados a feature.

## Stack e baseline tecnico
- React 19 + Vite 7 + TypeScript.
- Tailwind CSS 4 + DaisyUI para UI.
- TanStack Query para estado remoto.
- Kubb para geracao de cliente, hooks e tipos da API.

## Principios de arquitetura
- Organize por feature de negocio e mantenha coesao local.
- Separe estado remoto (React Query) de estado local de tela.
- Evite logica de negocio duplicada em varios componentes.
- Prefira componentes pequenos e composicao ao inves de componentes gigantes.

## Consumo de API e React Query
- Use os hooks gerados em `client/src/kubb/` como caminho padrao para chamadas HTTP.
- Centralize feedback de sucesso/erro em hooks de mutacao da feature.
- Invalide queries por `queryKey` de forma explicita apos mutacoes.
- Preserve os defaults do projeto no QueryClient (staleTime, retry e refetch).

## Regras do codigo gerado (Kubb)
- Nao edite manualmente arquivos em `client/src/kubb/`.
- Ao mudar contrato no backend, regenere com `npm run sync` (backend rodando).
- Trate qualquer ajuste em codigo de feature, nunca dentro do codigo gerado.

## Padrões de código
- Use alias `@` para imports internos (`client/src`).
- Use Zod e schemas locais para validacao de formularios.
- Mantenha mascaras, formatadores e utilitarios em modulos reutilizaveis.
- Para acoes de tabela/lista, siga o padrao de botoes de acao com loading e feedback.
- Evite estado global sem necessidade; prefira estado local por pagina quando o filtro nao e compartilhado.

## Autenticacao e interceptadores
- Reaproveite configuracao central de API e interceptadores de autenticacao.
- Nao replique logica de token em cada chamada.
- Em fluxos autenticados, sempre considerar expiracao de sessao e redirecionamento.

## Fluxo de validacao antes de concluir
- Lint: `npm run lint`.
- Build (inclui typecheck): `npm run build`.

## Comandos usuais (executar em `client/`)
- Dev server: `npm run dev`.
- Regenerar cliente API: `npm run sync`.

## Alertas importantes deste projeto
- O frontend depende de `http://localhost:8080/v3/api-docs` para `npm run sync`.
- O root `package.json` tem scripts antigos para dev; prefira comandos direto em `client/`.
