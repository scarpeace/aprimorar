# Plano de Refatoracao do Cliente

## Objetivo
- Refatorar o frontend para acompanhar melhor a organizacao do backend.
- Usar `Responsavel` como primeira feature piloto, por ser o fluxo mais simples.
- Criar um padrao reaproveitavel para depois replicar em `alunos`, `colaboradores`, `atendimentos`, `financeiro` e `admin`.
- Atualizar traducoes/nomes de dominio para portugues onde o contrato novo do backend tambem esta em portugues.
- Melhorar organizacao e reutilizacao de componentes com mudancas pequenas, sem focar em otimizacao pesada de performance neste momento.

## Regras De Trabalho
- Nao editar manualmente arquivos em `client/src/kubb/`.
- Quando o backend estiver disponivel com o OpenAPI atualizado, rodar `npm run sync` em `client/` para regenerar hooks/tipos Kubb.
- Refatorar uma feature por vez.
- Comecar por `responsaveis` e tratar essa feature como blueprint.
- Manter o fluxo `Page -> hooks da feature -> components`.
- Componentes nao devem conhecer detalhes de endpoint ou invalidacao de cache.
- Mutacoes, toasts, invalidacoes e navegacao pos-sucesso devem ficar nos hooks da feature.
- Formularios novos ou reescritos devem usar React Hook Form + Zod.
- Erros de request devem passar por `getFriendlyErrorMessage`.
- Preferir mudancas pequenas e incrementais, com verificacao apos cada etapa relevante.

## Boas Praticas React Aplicaveis Agora
- Evitar componentes inline definidos dentro de outros componentes.
- Evitar estado derivado em `useEffect`; derivar durante render quando simples.
- Manter estado local apenas para UI efemera, como modal aberto, item selecionado e filtros da pagina.
- Extrair handlers e blocos repetidos quando isso reduzir duplicacao real entre features.
- Evitar `useMemo`/`useCallback` por padrao; usar apenas quando houver custo real ou padrao existente na area tocada.
- Usar imports estaticos e diretos quando possivel, evitando barrels novos desnecessarios.

## Estado Inicial Observado
- [x] Lido `client/AGENTS.md`.
- [x] Consultada skill `vercel-react-best-practices` para orientar organizacao simples.
- [x] Mapeada estrutura atual de `client/src/features`.
- [x] Identificado que `responsaveis` ainda esta incompleto: existe hook de mutacoes, mas as paginas/componentes esperados ainda nao apareceram no glob inicial.
- [x] Identificado que `App.tsx` ja referencia `ResponsaveisPage` e `ResponsavelDetailPage`.
- [x] Identificado que rotas ainda usam ingles (`/parents`, `/students`, `/employees`, `/appointments`, `/finance`).
- [x] Identificado que `responsaveis/hooks/use-responsavel-mutations.ts` ainda mistura nomes antigos em ingles (`Parent`) e endpoints antigos como archive/unarchive, que podem nao existir mais apos backend.
- [x] Identificado que `colaboradores` tem uma estrutura mais completa que pode servir de comparacao para a feature piloto.

## Decisoes Iniciais
- A feature piloto sera `responsaveis`.
- A nomenclatura dentro da feature piloto deve ser em portugues: `Responsavel`, `responsavel`, `ResponsaveisPage`, `ResponsavelForm`, `useResponsavelMutations`.
- O contrato HTTP gerado pelo Kubb deve ser tratado como fonte de verdade apos `npm run sync`.
- Enquanto o Kubb nao for regenerado, qualquer nome de hook/tipo gerado sera considerado provisoriamente inconsistente.
- Como o backend removeu arquivamento de `Responsavel`, a feature piloto nao deve ter UI ou mutacoes de arquivar/desarquivar responsavel.
- Rotas publicas podem continuar em ingles por uma etapa curta se isso reduzir risco, mas o alvo final deve ser discutir rotas em portugues para acompanhar o dominio (`/responsaveis`, `/alunos`, `/colaboradores`, `/atendimentos`, `/financeiro`).

## Blueprint Alvo Para Uma Feature

### Estrutura Recomendada
```text
client/src/features/responsaveis/
  pages/
    ResponsaveisPage.tsx
    ResponsavelDetailPage.tsx
  components/
    ResponsaveisTable.tsx
    ResponsavelForm.tsx
    ResponsavelInfoSection.tsx
    DeleteResponsavelButton.tsx
    ResponsavelSelectDropdown.tsx
  hooks/
    use-responsavel-mutations.ts
    use-responsaveis-query.ts
  lib/
    responsavel-form-schema.ts
    responsavel-labels.ts
    responsavel-formatters.ts
```

### Responsabilidades
- `pages/`: composicao da tela, modais, filtros locais e layout.
- `components/`: renderizacao pura e eventos de UI recebidos por props.
- `hooks/`: queries, mutacoes, invalidacao, toast e navegacao.
- `lib/`: schemas, labels, formatadores e helpers especificos da feature.

### Padrao De Page
- Usar `PageLayout`.
- Definir cabecalho da feature no topo da page.
- Manter estado local de modal e item selecionado na page.
- Renderizar KPIs/lista/formulario sem regra de API inline.
- Exemplo de estados locais permitidos:
  - `isFormOpen`
  - `selectedResponsavel`
  - filtros de busca/paginacao se nao vierem do componente de tabela.

### Padrao De Hook De Mutacoes
- Nome: `useResponsavelMutations`.
- Retornar mutacoes usando o padrao verbo em ingles + entidade em portugues:
  - `criarResponsavel`
  - `atualizarResponsavel`
  - `excluirResponsavel`
- Decisao posterior: manter nomes iguais aos hooks/conceito de mutation do projeto, com verbo em ingles e entidade em portugues:
  - `createResponsavel`
  - `updateResponsavel`
  - `deleteResponsavel`
  - `archiveResponsavel`, somente em features que realmente tenham arquivamento
- Centralizar:
  - `toast.success`
  - `toast.error(getFriendlyErrorMessage(error))`
  - `queryClient.invalidateQueries`
  - `navigate(...)` quando aplicavel
- Invalidar explicitamente:
  - lista de responsaveis
  - detalhe do responsavel alterado
  - listas relacionadas se existirem, como alunos por responsavel

### Padrao De Formulario
- `ResponsavelForm` deve receber:
  - `initialData?: ResponsavelResponseDTO | null`
  - `onSuccess: () => void`
  - `onCancel: () => void`
- Usar `responsavelFormSchema`.
- Campos esperados inicialmente:
  - `nome`
  - `cpf`
  - `dataNascimento`
  - `telefone`
  - `email`
- Usar mascaras para CPF, telefone e data quando fizer sentido.
- Converter `DD/MM/YYYY` para `YYYY-MM-DD` no schema ou num mapper pequeno da feature.
- Mostrar erros perto dos campos.

### Padrao De Tabela
- `ResponsaveisTable` deve receber dados/estado por props ou usar um hook de query especifico da feature.
- Evitar chamada direta a endpoint dentro de componente sem passar por hook da feature.
- Ter estados claros:
  - loading
  - erro
  - vazio
  - lista preenchida
- Linha deve navegar para detalhe ou expor callback `onRowClick`.

## Componentes Compartilhados A Avaliar
- [ ] `EntityPageHeader` ou padronizacao via `PageLayout` existente.
- [ ] `EntitySummarySection` para secoes de KPI simples.
- [ ] `EntityTableState` para loading/empty/error de listas.
- [ ] `FormField` para label/input/error repetidos em formularios.
- [ ] `MaskedTextField` para inputs com mascara.
- [ ] `DeleteEntityButton` generico ou confirmar se botoes especificos por feature sao melhores.
- [ ] `DetailInfoSection` para exibir pares label/valor em detalhes.
- [ ] Helpers de formatacao compartilhados para `cpf`, `telefone`, datas e dinheiro.

## Plano De Implementacao - Responsaveis

### Etapa 0 - Preparacao
- [x] Criar este documento.
- [x] Planejar `npm run sync` quando backend estiver disponivel.
- [x] Verificar disponibilidade do backend em `http://localhost:8080/v3/api-docs`.
- [x] Confirmar hooks/tipos Kubb atuais antes do sync.
- [x] Registrar que `archive/unarchive` de responsavel ainda existem no Kubb atual e devem sumir apos sync do backend novo.
- [x] Rodar `npm run sync` quando backend estiver disponivel.
- [x] Confirmar quais hooks/tipos Kubb existem apos sync.
- [x] Confirmar se `archive/unarchive` de responsavel sumiram do Kubb apos sync.

Resultado pos-sync:
- Hooks de responsavel disponiveis: `useCreateResponsavel`, `useUpdateResponsavel`, `useDeleteResponsavel`, `useGetResponsaveis`, `useGetResponsavelById`, `useListResponsaveis`.
- Hooks de responsavel removidos do contrato: `useArchiveResponsavel`, `useUnarchiveResponsavel`.
- DTO de request: `ResponsavelRequestDTO` com `nome`, `email`, `telefone`, `dataNascimento`, `cpf`.
- DTO de response: `ResponsavelResponseDTO` com `id`, `nome`, `dataNascimento`, `cpf`, `telefone`, `email`, `createdAt`, `updatedAt`.
- Query params de listagem: `nome`, `email`, `cpf`, `page`, `size`, `sort`.

### Etapa 1 - Inventario Da Feature Atual
- [x] Verificar se existem arquivos de `ResponsaveisPage`, `ResponsavelDetailPage`, tabela e formulario que o glob inicial nao mostrou.
- [x] Mapear todos os imports de `features/responsaveis`.
- [x] Mapear todos os usos das rotas `/parents` e `/parents/:id`.
- [x] Mapear todos os usos dos tipos gerados de responsavel.
- [x] Registrar gaps reais antes de implementar.

Resultado do inventario:
- A feature `responsaveis` existe completa em disco: pages, components, hook e lib.
- `App.tsx` ainda usa rotas `/parents` e `/parents/:id`.
- `AlunosPage` reutiliza `ResponsaveisTable` e `ResponsavelForm`.
- A feature ainda usa nomes antigos do contrato anterior em varios pontos: `parentId`, `name`, `contact`, `birthdate`, `pix`, `active`.
- `ResponsaveisTable` ainda envia filtros antigos para `useGetResponsaveis`: `search` e `archived`; o contrato atual espera `nome`, `email`, `cpf`, `page`, `size`, `sort`.
- `ResponsaveisTable` ainda mostra filtro de arquivados, mas responsavel nao tem mais arquivamento.
- `ResponsavelInfoSection` ainda importa e renderiza `ArchiveResponsavelButton`; esse componente ficou obsoleto.
- `ArchiveResponsavelButton` depende de `archiveParent`/`unarchiveParent`, que nao existem mais apos sync.
- `use-responsavel-mutations.ts` ainda importa `useArchiveResponsavel` e `useUnarchiveResponsavel`, que nao existem mais apos sync.
- `DeleteResponsavelButton` ainda usa `deleteParent` e tenta enviar `params: { cascade: true }`; o contrato atual precisa ser confirmado antes de manter esse parametro.
- `ResponsavelForm` usa schema antigo `parentFormSchema.ts` e campos `name`, `contact`, `birthdate`, `pix`; o contrato atual usa `nome`, `telefone`, `dataNascimento` e nao possui `pix`.
- `ResponsavelDetailPage` usa `parentId`, backLink incorreto `/responsavels` e textos mencionando historico arquivado.
- `ResponsavelAlunosTable` ainda usa campos antigos de aluno (`name`, `age`, `contact`, `school`, `active`). Essa tabela depende da refatoracao posterior de `alunos`, mas precisa de ajuste minimo se quebrar build agora.
- Teste/helper `getActiveLinkedStudentsCount` ainda fala de students/active; deve ser reavaliado porque responsavel nao tem mais arquivamento.

### Etapa 2 - Contrato Kubb E Tipos
- [x] Regenerar cliente com `npm run sync`, se backend estiver executando.
- [x] Verificar hooks gerados para responsavel.
- [x] Verificar nomes dos DTOs gerados:
  - `ResponsavelRequestDTO`
  - `ResponsavelResponseDTO`
  - filtros/listagem se existirem.
- [x] Atualizar imports fora de `client/src/kubb/`.
- [x] Remover referencias a hooks antigos de arquivar/desarquivar responsavel.

Resultado da etapa:
- `use-responsavel-mutations.ts` agora usa apenas `useCreateResponsavel`, `useUpdateResponsavel`, `useDeleteResponsavel`.
- Nomes retornados pelo hook: `createResponsavel`, `updateResponsavel`, `deleteResponsavel`.
- Removido `ArchiveResponsavelButton.tsx`.
- `ResponsaveisTable` usa `nome` como filtro principal e removeu filtro de arquivados.
- `ResponsavelInfoSection` removeu status/arquivamento e usa campos atuais (`id`, `nome`, `telefone`, `dataNascimento`).
- `ResponsavelForm` e `parentFormSchema.ts` foram alinhados ao request atual (`nome`, `email`, `telefone`, `dataNascimento`, `cpf`).
- `DeleteResponsavelButton` usa `deleteResponsavel` e manteve `cascade: true`, pois o contrato atual ainda aceita esse query param.
- `ResponsavelAlunosTable` foi ajustada minimamente para os campos atuais de aluno (`nome`, `idade`, `telefone`, `escola`).
- `ResponsavelSelectDropdown` exibe `responsavel.nome`.
- `parentId` permanece apenas no schema/fluxo antigo de `alunos`, que sera tratado quando a feature `alunos` for refatorada.

### Etapa 3 - Hook De Mutacoes
- [x] Refatorar `use-responsavel-mutations.ts` para nomes definidos no plano.
- [x] Remover `archiveParent` e `unarchiveParent` se o backend nao suportar mais arquivamento.
- [x] Corrigir navegacao pos-criacao para usar `createdResponsavel.id` ou campo real do DTO.
- [x] Corrigir rotas alvo conforme decisao de rota (`/parents` ou `/responsaveis`).
- [x] Padronizar invalidacoes com query keys oficiais geradas pelo Kubb.

Resultado da etapa:
- Hook retorna `createResponsavel`, `updateResponsavel`, `deleteResponsavel`.
- `toast.success`, `toast.error`, navegacao e invalidacao ficam centralizados no hook.
- Componentes consumidores nao repetem `toast.error(getFriendlyErrorMessage(error))`.
- Invalidacoes cobrem lista paginada, lista de dropdown, detalhe e alunos vinculados quando aplicavel.
- Rotas da feature foram normalizadas na etapa de rotas/navegacao.

### Etapa 4 - Schema E Formulario
- [x] Criar `lib/responsavel-form-schema.ts`.
- [x] Criar `components/ResponsavelForm.tsx`.
- [x] Reutilizar padroes de endereco apenas se responsavel tiver endereco; pelo backend atual, responsavel nao parece ter endereco.
- [x] Garantir validacao e mascaras basicas.
- [x] Garantir submit de criacao/edicao via `useResponsavelMutations`.

Resultado da etapa:
- `parentFormSchema.ts` foi renomeado para `responsavel-form-schema.ts`.
- Schema usa os campos do contrato atual: `nome`, `email`, `telefone`, `dataNascimento`, `cpf`.
- `ResponsavelForm` usa `useResponsavelMutations` para criar/editar.
- `ResponsavelForm` manteve mascaras para CPF, telefone e data de nascimento.
- Criado `FieldErrorMessage` local no formulario para reduzir repeticao sem introduzir componente compartilhado prematuro.
- Responsavel nao possui endereco no contrato atual, entao nenhum componente de endereco foi usado.

### Etapa 5 - Lista
- [x] Criar ou refatorar `ResponsaveisTable.tsx`.
- [x] Usar filtros do backend em portugues, se disponiveis:
  - `nome`
  - `email`
  - `cpf`
- [x] Definir estado de loading/erro/vazio.
- [x] Navegar para detalhe ao clicar numa linha.

Resultado da etapa:
- `ResponsaveisTable` usa `useGetResponsaveis` com `nome`, `page` e ordenacao `nome,asc`.
- Tabela foi alinhada ao modelo paginado atual (`data.content` + `data.page`).
- Estados `loading`, `erro` e `empty` ficam claros e preservam a barra de busca na tela.
- Colunas exibidas: nome, contato, e-mail e CPF.
- Navegacao de linha usa `responsavel.id`.
- Removidos comentarios mortos e restos de status/arquivamento.

### Etapa 6 - Pagina Principal
- [x] Criar ou refatorar `ResponsaveisPage.tsx`.
- [x] Usar `PageLayout`.
- [x] Adicionar botao `Novo Responsavel`.
- [x] Abrir `ResponsavelForm` em modal.
- [x] Renderizar `ResponsaveisTable`.
- [x] Remover mencoes a `Parent`/`parents` dentro da feature, salvo rota temporaria se decidida.

Resultado da etapa:
- `ResponsaveisPage` esta como composicao simples: header, secao de lista, botao de criacao e modal de formulario.
- Estado local restrito a modal aberto e `selectedResponsavel`.
- Textos visiveis foram ajustados com acentos.
- Removido `backLink` de `headerProps`, pois `PageLayout` nao consome essa prop.
- Rotas antigas foram removidas na etapa 8.

### Etapa 7 - Detalhe
- [x] Criar ou refatorar `ResponsavelDetailPage.tsx`.
- [x] Exibir dados principais do responsavel.
- [x] Exibir alunos vinculados, se endpoint/hook existir.
- [x] Adicionar edicao e exclusao.
- [x] Nao adicionar arquivamento para responsavel.

Resultado da etapa:
- `ResponsavelDetailPage` usa `ResponsavelInfoSection`, `ResponsavelAlunosTable` e modal de edicao.
- Textos do modal foram corrigidos com acentos.
- `backLink` removido do header porque `PageLayout` nao consome essa prop.
- `ResponsavelInfoSection` exibe dados principais e acoes de editar/excluir, sem arquivamento.
- `ResponsavelAlunosTable` exibe alunos vinculados usando campos atuais do contrato de aluno.
- `DeleteResponsavelButton` foi limpo para usar nomenclatura `alunosVinculados`.

### Etapa 8 - Rotas E Navegacao
- [x] Decidir se troca imediata de `/parents` para `/responsaveis`.
- [x] Se trocar, atualizar `App.tsx` e links/menu.
- [x] Remover redirects temporarios sem necessidade concreta.

Resultado da etapa:
- Rotas canonicas da feature mudaram para `/responsaveis` e `/responsaveis/:id`.
- Rotas antigas `/parents` e `/parents/:id` foram removidas; nao ha compatibilidade legada para responsaveis.
- Navegacoes internas de criar, editar, excluir e abrir detalhe usam `/responsaveis`.
- Link antigo `/parents/new` foi removido do dropdown porque nao havia rota correspondente; cadastro permanece no modal da pagina de responsaveis.

### Etapa 9 - Verificacao
- [x] Rodar `npm run lint`.
- [ ] Rodar `npm run build`.
- [ ] Testar manualmente:
  - listar responsaveis
  - criar responsavel
  - editar responsavel
  - abrir detalhe
  - excluir responsavel
  - ver alunos vinculados, se implementado.

Resultado parcial da etapa:
- `npm run lint` passou sem erros, com 5 warnings preexistentes fora da feature `responsaveis`.
- `npm run build` ainda falha porque outras features (`alunos`, `colaboradores`, `atendimentos`, `financeiro`, `employees`, dashboard e helpers compartilhados) continuam usando campos/hooks antigos apos o sync do Kubb.
- O unico erro apontado dentro de `responsaveis` foi ajustado: `ResponsavelSelectDropdown` usa `ResponsaveisListDTO.name`, que ainda e o campo gerado para a lista compacta.
- A nova execucao de `npm run build` nao aponta mais erros em `features/responsaveis`.

## Pendencias Para Replicar Em Outras Features
- [ ] Definir padrao final de nomes de rotas.
- [ ] Definir se features antigas em ingles serao renomeadas em disco ou apenas no dominio interno.
- [ ] Definir componentes compartilhados extraidos a partir de duplicacao real da feature `responsaveis`.
- [x] Atualizar `alunos` seguindo o blueprint validado.
- [x] Atualizar `colaboradores` seguindo o blueprint validado.
- [x] Atualizar `atendimentos` seguindo o blueprint validado e o novo contrato em portugues.
- [x] Remover `financeiro` do cliente, pois o contrato atual nao expoe mais o modulo de despesas usado pela feature antiga.

## Aplicacao Do Blueprint - Alunos
- [x] Contrato Kubb atual mapeado: `AlunoRequestDTO`, `AlunoResponseDTO`, `AlunosListDTO`, `PagedModelAlunoResponseDTO` e hooks gerados.
- [x] Schema antigo `alunoFormSchema.ts` removido e substituido por `aluno-form-schema.ts`.
- [x] `AlunoForm` alinhado aos campos atuais: `nome`, `dataNascimento`, `telefone`, `escola`, `responsavelId`, `endereco`, `cpf`, `email`.
- [x] `ResponsavelSelectDropdown` atualizado para usar `responsavelId`; a lista compacta de responsaveis ainda usa `name` conforme Kubb.
- [x] `useAlunoMutations` padronizado com `createAluno`, `updateAluno`, `archiveAluno`, `unarchiveAluno`, `deleteAluno`.
- [x] Arquivamento preservado porque o contrato de aluno ainda expoe `active`, `useArchiveAluno` e `useUnarchiveAluno`.
- [x] `AlunosTable` alinhada a `content/page`, filtro `nome`, `ativos` e rota `/alunos/:id`.
- [x] `AlunoInfoSection` alinhada aos campos atuais e ao endereco em portugues.
- [x] `AlunoSelectDropdown` ajustado para `AlunosListDTO.nome`.
- [x] `AlunoEventsTable` e `AlunoAtendimentoMobileCard` ajustados minimamente ao contrato atual de atendimento (`colaboradorNome`, `inicio`, `fim`, `tipo`, `valor`, `dataCobrancaAluno`) e sem toggle de cobranca ate a refatoracao de `atendimentos`.
- [x] Rotas canonicas mudaram para `/alunos` e `/alunos/:id`, sem redirect legado de `/students`.
- [x] Menu principal aponta para `/alunos`.

Resultado de verificacao da etapa:
- `npm run lint` passou sem erros; resta 1 warning antigo em `MainLayout` por import nao usado.
- `npm run build` nao aponta mais erros em `features/alunos`; segue falhando em features ainda nao refatoradas (`atendimentos`, `financeiro`, dashboard e helper antigo de endereco usado por codigo legado).

## Aplicacao Do Blueprint - Colaboradores
- [x] Contrato Kubb atual mapeado: `ColaboradorRequestDTO`, `ColaboradorResponseDTO`, `ColaboradoresListDTO`, `PagedModelColaboradorResponseDTO` e hooks gerados.
- [x] Schema antigo `employeeFormSchema.ts` removido e substituido por `colaborador-form-schema.ts`.
- [x] `ColaboradorForm` alinhado aos campos atuais: `nome`, `dataNascimento`, `telefone`, `funcao`, `endereco`, `pix`, `cpf`, `email`.
- [x] `useColaboradorMutations` padronizado com `createColaborador`, `updateColaborador`, `archiveColaborador`, `unarchiveColaborador`, `deleteColaborador`.
- [x] Arquivamento preservado porque o contrato de colaborador ainda expoe `active`, `useArquivarColaborador` e `useDesarquivarColaborador`.
- [x] `ColaboradoresTable` alinhada a `content/page`, filtro `nome`, `ativos` e rota `/colaboradores/:id`.
- [x] `ColaboradorInfoSection` alinhada aos campos atuais e ao endereco em portugues.
- [x] `ColaboradorSelectDropdown` trocado para `useGetColaboradoresList`; `ColaboradoresListDTO` ainda usa `name` na lista compacta.
- [x] `ColaboradorEventsTable` ajustada minimamente ao contrato atual de atendimento (`alunoNome`, `inicio`, `fim`, `tipo`, `repasse`, `dataPagamentoColaborador`) e sem toggle de pagamento ate a refatoracao de `atendimentos`.
- [x] Rotas canonicas mudaram para `/colaboradores` e `/colaboradores/:id`, sem redirect legado de `/employees`.
- [x] Menu principal aponta para `/colaboradores`.

Resultado de verificacao da etapa:
- `npm run lint` passou sem erros; warnings restantes estao fora de `features/colaboradores`.
- `npm run build` nao aponta mais erros em `features/colaboradores`; segue falhando em features ainda nao refatoradas (`alunos`, `atendimentos`, `financeiro`, `employees`, dashboard e helpers usados por `alunos`).

## Aplicacao Do Blueprint - Atendimentos
- [x] Contrato Kubb atual mapeado: `AtendimentoRequestDTO`, `AtendimentoResponseDTO`, `PagedModelAtendimentoResponseDTO`, `AtendimentosContentReportDTO`, `AtendimentosKpisDTO` e hooks gerados.
- [x] Schema antigo `appointmentFormSchema.tsx` removido e substituido por `atendimento-form-schema.ts`.
- [x] `AtendimentoForm` alinhado aos campos atuais: `alunoId`, `colaboradorId`, `tipo`, `descricao`, `inicio`, `duracao`, `valor`, `repasse`.
- [x] `useAtendimentoMutations` padronizado com `createAtendimento`, `updateAtendimento`, `deleteAtendimento`, `alternarCobrancaAluno`, `alternarPagamentoColaborador`.
- [x] Tabela, card mobile, detalhe e secao de informacoes alinhados aos campos atuais: `alunoNome`, `colaboradorNome`, `inicio`, `fim`, `tipo`, `valor`, `repasse`, `dataCobrancaAluno`, `dataPagamentoColaborador`.
- [x] Filtros de listagem usam query params atuais: `busca`, `inicio`, `fim`, `ocultarCobrados`, `ocultarPagos`.
- [x] Dashboard de atendimentos ajustado para `alunoNome`, `inicio`, `fim`, indicadores por `inicio`/`fim` e labels de `tipo`.
- [x] Rotas canonicas mudaram para `/atendimentos` e `/atendimentos/:id`, sem redirect legado de `/appointments`.
- [x] Menu principal e links internos de alunos, colaboradores e dashboard apontam para `/atendimentos`.

Resultado de verificacao da etapa:
- `npm run lint` passou sem erros.
- `npm run build` nao aponta mais erros em `features/atendimentos`, `features/dashboard`, `features/alunos` ou `features/colaboradores`; segue falhando em `features/financeiro` e `lib/shared/address/forms/AddressFormFields.tsx`, que ainda usam contrato/campos antigos.

## Refatoracao Compartilhada - Endereco
- [x] `AddressFormFields` alinhado ao schema compartilhado `addressFormSchema` e aos DTOs atuais de endereco.
- [x] Campos registrados agora usam nomes em portugues: `rua`, `numero`, `complemento`, `bairro`, `cidade`, `estado`, `cep`.
- [x] Campo `numero`, que existia no schema mas nao era renderizado pelo helper, foi incluido.
- [x] Removidos acessos antigos de erro para `street`, `complement`, `district`, `city`, `state`, `zip`.

Resultado de verificacao da etapa:
- `npm run lint` passou sem erros.
- `npm run build` nao aponta mais erros em `lib/shared/address/forms/AddressFormFields.tsx`; os erros restantes estao restritos a `features/financeiro`.

## Remocao - Financeiro
- [x] Removida completamente a feature `client/src/features/financeiro`.
- [x] Removidos imports lazy de `FinanceiroPage` e `DespesaDetailPage` em `App.tsx`.
- [x] Removidas rotas `/finance`, `/finance/expenses`, `/finance/expenses/:id` e `/finance/settlement`.
- [x] Removido item `Financeiro` do menu principal em `MainLayout`.
- [x] Confirmado que nao restaram referencias a `features/financeiro`, rotas `/finance` ou componentes/hooks de despesa em `client/src`.

Resultado de verificacao da etapa:
- `npm run lint` passou sem erros.
- `npm run build` passou com sucesso. Resta apenas warning de chunk grande do Vite/Rollup.

## Registro De Decisoes
- 2026-06-11: `responsaveis` sera a feature piloto.
- 2026-06-11: O plano deve ser mantido vivo e atualizado conforme etapas forem concluidas.
- 2026-06-11: Otimizacoes React pesadas ficam fora de escopo inicial; foco em organizacao, padronizacao e reutilizacao simples.
- 2026-06-11: Backend removeu arquivamento de `Responsavel`; frontend nao deve manter esse comportamento.
- 2026-06-11: Nomes de mutations devem seguir verbo em ingles + entidade em portugues, como `createResponsavel`, `updateResponsavel`, `deleteResponsavel`; `archiveResponsavel` apenas para entidades com arquivamento.

## Bloqueios / Observacoes Operacionais
- 2026-06-11: `curl http://localhost:8080/v3/api-docs` falhou porque o backend nao esta rodando em `localhost:8080`; `npm run sync` ficou pendente.
- 2026-06-11: Kubb atual ainda contem `useArchiveResponsavel` e `useUnarchiveResponsavel`, refletindo contrato antigo. A feature piloto deve considerar esses hooks obsoletos ate o sync do backend novo.
- 2026-06-11: Usuario rodou `npm run sync`; Kubb de responsavel foi atualizado e removeu archive/unarchive.
- 2026-06-12: Seed idempotente de admin adicionado no bootstrap da aplicacao backend via `CommandLineRunner`, usando `PasswordEncoder` real do projeto para criar `admin@aprimorar.com` com a senha `Freerider` somente quando o usuario nao existir.

## Notas Para A Proxima Sessao
- Comecar pela Etapa 1: inventario real da feature `responsaveis`.
- Se paginas referenciadas por `App.tsx` nao existirem, criar a feature piloto do zero.
- Antes de mexer em Kubb, confirmar se backend compila e disponibiliza `/v3/api-docs`.
