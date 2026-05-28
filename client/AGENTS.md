# AGENTS - Client (tecnico-first)

## Objetivo
- Guiar implementacao de frontend com foco em fluxo previsivel de dados, UX consistente e baixo acoplamento entre features.
- Priorizar padrao unico para pagina, hooks, formularios e mutacoes.

## Arquitetura em uma frase
- UI por feature com fluxo `Page -> hooks da feature -> components`, usando React Query para estado remoto e estado local apenas para interacao da tela.
- Features atuais estao em ingles (`students`, `parents`, `employees`, `appointments`, `finance`, `auth`, `dashboard`, `admin`), enquanto contratos de API podem vir em portugues via Kubb; manter consistencia no contexto de cada camada.

## Fluxo de implementacao obrigatorio
- Page: compoe layout, roteamento, filtros locais e conexao entre blocos; sem regra de API inline.
- Hooks da feature: concentram leitura/mutacao, invalidacao de cache, toasts e navegacao pos-sucesso.
- Components: renderizacao e eventos de UI; recebem dados/acoes por props, sem conhecer detalhes de endpoint.
- API client: usar hooks/tipos gerados do `@/kubb`; nao chamar `fetch`/`axios` direto em componente.

## Padroes de escrita de codigo
- Imports internos via alias `@`.
- Nomear hooks e componentes por caso de uso (`useExpenseMutations`, `ArchiveStudentButton`).
- Componentes de acao devem expor estado de loading/desabilitado e feedback de erro/sucesso.
- Nao duplicar regra de negocio entre componentes; extrair para hook compartilhado da feature.

## Formularios e validacao (RHF + Zod)
- Formularios novos devem usar React Hook Form + schema Zod.
- Validacao de formato/regra simples fica no schema; regra de negocio final continua no backend.
- Erros de validacao devem aparecer perto do campo e manter mensagem amigavel.
- Ao submeter com sucesso, seguir padrao da feature (toast + navegacao + invalidacao de cache).

## Erros e excecoes
- Erro de request deve passar por `getFriendlyErrorMessage` para UX consistente.
- Interceptadores globais em `@/lib/shared/api` tratam autenticacao e 401; nao repetir isso por hook.
- Evitar `console.error` espalhado em componente de tela; tratar no hook da mutacao/query.

## Estado local vs remoto
- Estado remoto: sempre React Query (hooks gerados Kubb + query keys oficiais).
- Estado local: UI efemera (modal aberto, aba ativa, filtro local de pagina).
- Nao mover filtro local para contexto global sem necessidade real de compartilhamento.
- `usePageDateFilter()` segue local por pagina (nao compartilhado/persistido).

## Mutacao, invalidacao e consistencia
- Mutacoes devem ficar em hooks de feature (ex.: students, parents, employees, appointments, finance).
- Em `onSuccess`, invalidar explicitamente listas + detalhe afetado + relatorios derivados quando aplicavel.
- Padrao atual confirmado: hooks de mutacao centralizam `toast`, `navigate` e `invalidateQueries`.
- Manter `queryClient` global com defaults do projeto (`staleTime`, `retry`, `placeholderData`, `refetchOnWindowFocus`).

## Consultas e performance
- Reaproveitar query keys geradas pelo Kubb para evitar cache paralelo acidental.
- Evitar refetch manual desnecessario quando invalidacao cobre o fluxo.
- Para paginas com listas, preservar UX durante troca de filtro/pagina com comportamento de placeholder.

## Contratos e integracao
- Nao editar `client/src/kubb/` manualmente (codigo gerado).
- Mudou contrato backend: subir backend e rodar `npm run sync` para regenerar cliente.
- `npm run sync` depende de `http://localhost:8080/v3/api-docs` disponivel.
- Autenticacao e header Bearer sao centralizados no setup compartilhado de API.
- Se o contrato gerado mudar nomes/DTOs, ajuste somente codigo de feature fora de `client/src/kubb/`.

## Matriz de verificacao por tipo de mudanca
- Componente visual: validar estados `loading`, `erro` e `empty` na tela afetada.
- Formulario: validar schema, mensagens de erro e submit de sucesso.
- Mutacao: validar invalidacoes corretas (lista, detalhe e derivados) e toasts.
- Mudanca de contrato com backend: `npm run sync`, depois `npm run lint` e `npm run build`.
- Mudanca geral frontend: `npm run lint` e `npm run build`.

## Anti-padroes
- Chamar endpoint direto em component/page ignorando hooks da feature.
- Editar qualquer arquivo dentro de `client/src/kubb/`.
- Duplicar `queryKey` manualmente quando existe helper gerado.
- Formulario sem schema de validacao.
- Estado global para problema que e apenas local de uma pagina.

## Regras de negocio do projeto (resumo)
- Acoes de pagamento/cobranca de eventos devem refletir no relatorio financeiro apos mutacao.
- `ToggleExpensePaymentButton` ja encapsula mutacao via `useExpenseMutations`.
- Fluxo autenticado deve tratar expiracao de sessao com limpeza de auth e redirecionamento para login.

## Comandos de verificacao (executar em `client/`)
- `npm run dev`
- `npm run sync`
- `npm run lint`
- `npm run build`
