# Plano: separar o fluxo de responsáveis do formulário de aluno

## Objetivo

Separar a gestão de responsáveis em um fluxo próprio para simplificar:

- o formulário de criação e edição de aluno
- a API de aluno
- as regras de associação entre aluno e responsável

No estado desejado, o aluno deve trabalhar principalmente com `parentId`, enquanto criação, edição, arquivamento e listagem de responsáveis ficam em uma sessão própria.

## Motivação

Hoje o fluxo de aluno concentra responsabilidades demais:

- criar aluno
- selecionar responsável existente
- criar novo responsável
- editar responsável no contexto do aluno
- resolver duplicidade de CPF e email do responsável

Isso aumenta a complexidade no frontend e no backend.

## Resultado esperado

### Frontend

- nova área de responsáveis com:
  - listagem
  - detalhes
  - criação
  - edição
  - arquivar/desarquivar
- formulário de aluno com seleção de responsável existente
- opcional: atalho para criar responsável sem sair da tela do aluno

### Backend

- `StudentRequestDTO` recebendo `parentId` ou uma estrutura mínima de referência
- `StudentService` apenas validando e associando um responsável já existente
- `ParentService` concentrando criação, edição e validações de responsável

## Fases sugeridas

### Fase 1: preparar módulo de responsáveis

- criar as páginas:
  - `ParentsPage`
  - `ParentDetailPage`
  - `ParentCreatePage`
  - `ParentEditPage`
- adicionar rotas no `App.tsx`
- criar tabela e filtros no mesmo padrão de alunos e colaboradores
- adicionar botões de editar, arquivar/desarquivar e excluir

### Fase 2: consolidar contratos no frontend

- criar schemas de formulário e resposta para responsável
- adicionar `parentsApi.create`, `getById`, `update`, `archive`, `unarchive`, `delete`
- criar `queryKeys.parents.editDetail(id)` para não misturar cache de detalhe e edição

### Fase 3: simplificar formulário de aluno

- remover modo `novo responsável` de dentro de `StudentCreatePage`
- trocar o bloco de responsável por:
  - select de responsável
  - busca por nome/CPF
  - link ou botão para criar novo responsável
- em `StudentEditPage`, deixar a troca de responsável opcional e explícita

### Fase 4: ajustar backend

- revisar contrato de aluno para referenciar responsável por `id`
- remover do `StudentService` a lógica de criar ou atualizar `Parent`
- manter no `StudentService` apenas:
  - buscar responsável existente
  - validar associação obrigatória
- mover validações de duplicidade e atualização de responsável para `ParentService`

### Fase 5: limpeza e alinhamento

- remover código legado de criação/edição de responsável dentro do fluxo de aluno
- revisar textos da interface
- atualizar invalidações de cache e navegação
- revisar mensagens de erro

## Decisões em aberto

- o aluno pode ser criado sem responsável?
  - hoje parece que não
- o formulário de aluno deve abrir criação de responsável em modal ou em página separada?
- a troca de responsável no edit do aluno deve ser permitida livremente ou ter alguma regra?
- exclusão de responsável deve ser bloqueada quando houver alunos vinculados?

## Riscos

- quebrar o fluxo atual de criação de aluno se a transição for feita de uma vez
- duplicar cadastro de responsável durante a migração
- misturar shapes de resposta diferentes no cache do React Query

## Estratégia recomendada

Implementar primeiro o módulo de responsáveis sem remover o fluxo atual do aluno.

Depois:

1. publicar o fluxo novo de responsáveis
2. adaptar o aluno para selecionar responsável existente
3. remover a lógica antiga só quando o fluxo novo estiver estável

## Observações técnicas

- manter schemas de API separados de schemas formatados para UI
- evitar reutilizar a mesma `queryKey` para detalhe e edição
- preferir `ButtonLink` nas ações de navegação
- se o backend continuar com `PUT`, manter o schema de formulário compartilhado quando o payload for o mesmo
