# Fase 2: Fortalecimento do Registro de Alunos & Responsáveis - Contexto

**Coletado:** 19-04-2026
**Status:** Pronto para planejamento

<domain>
## Limite da Fase

Fortalecer os fluxos de registro existentes de alunos e responsáveis para que a secretária possa criar, atualizar, arquivar, buscar e gerenciar registros vinculados de forma confiável, sem depender de planilhas. Esta fase melhora o modelo e as interações atuais de registro; ela não introduz um ciclo de vida de matrícula separado, um redesenho de muitos-para-muitos entre aluno/responsável, comportamento financeiro ou novos módulos mais amplos.

</domain>

<decisions>
## Decisões de Implementação

### Modelo de Registro de Aluno
- **D-01:** Manter um único fluxo de registro de aluno na Fase 2 em vez de introduzir um fluxo de trabalho de matrícula ou pré-matrícula separado.
- **D-02:** Um aluno não pode ser criado ou atualizado sem exatamente um responsável vinculado.
- **D-03:** Um responsável pode existir no sistema sem nenhum aluno vinculado.

### Relacionamento Aluno-Responsável
- **D-04:** Manter o formato de relacionamento atual: um aluno -> exatamente um responsável, enquanto um responsável pode estar vinculado a muitos alunos.
- **D-05:** Não introduzir uma migração de muitos-para-muitos, responsável principal ou conceito de responsável financeiro na Fase 2.
- **D-06:** Preservar a ligação existente estilo `parentId` nos fluxos de escrita, a menos que um ajuste de compatibilidade mais estreito seja necessário para a correção.

### Regras de Gerenciamento de Vínculos
- **D-07:** Gerenciar o responsável obrigatório diretamente no fluxo de criação/edição do aluno.
- **D-08:** Se uma ação deixar um aluno sem um responsável vinculado, bloqueie-a.
- **D-09:** As regras de arquivamento e edição devem preservar a integridade do registro em vez de permitir lacunas temporárias de "sem responsável".

### Terminologia e Apresentação
- **D-10:** Usar `Responsável` como o principal termo de negócio nos fluxos voltados para o usuário na Fase 2.
- **D-11:** Manter a apresentação da lista/detalhe do aluno compacta e centrada no responsável atual, sem inventar novas superfícies de gerenciamento de relacionamento.

### Campos e Metadados
- **D-12:** Não registrar o tipo específico de relacionamento (Pai, Mãe, Tutor, etc.) entre Aluno e Responsável na Fase 2; o vínculo em si é suficiente para as operações atuais da escola.
- **D-13:** Manter o conjunto atual de campos para registros de Aluno e Responsável sem adicionar novos campos específicos da escola, como Série, Turno ou metadados de contato extras nesta fase.

### Discrição do Agente
- Layout exato da página de detalhes para mostrar o resumo do responsável mantendo a tela organizada.
- Se o componente seletor atual deve permanecer como está ou receber um pequeno passe de fortalecimento de UX seguro para brownfield.
- Quanta informação de resumo do responsável deve ser incorporada nas respostas de leitura do aluno versus carregada separadamente, desde que o fluxo único de registro permaneça intacto.

</decisions>

<canonical_refs>
## Referências Canônicas

**Agentes a jusante DEVEM ler estas referências antes de planejar ou implementar.**

### Planejamento e Escopo
- `.planning/PROJECT.md` — Escopo do produto, restrições brownfield e o objetivo de substituição de planilhas para fluxos de trabalho da secretaria.
- `.planning/REQUIREMENTS.md` — IDs de requisitos da Fase 2 `STUD-01` a `STUD-04` e `PARN-01` a `PARN-04`.
- `.planning/ROADMAP.md` — Objetivo da Fase 2, dependência da Fase 1 e critérios de sucesso.
- `.planning/STATE.md` — Posição atual do projeto e decisões da Fase 1 levadas adiante.

### Fluxos de Alunos Existentes
- `client/src/features/students/pages/StudentsPage.tsx` — UX atual de lista/busca/arquivamento para alunos.
- `client/src/features/students/pages/StudentCreatePage.tsx` — Fluxo atual de criação de aluno com seleção de responsável único.
- `client/src/features/students/pages/StudentEditPage.tsx` — Fluxo atual de edição de aluno e conjunto de campos existente.
- `client/src/features/students/pages/StudentDetailsPage.tsx` — Premissas da página de detalhes do aluno atual sobre o responsável atual.
- `client/src/features/students/forms/studentFormSchema.ts` — Regras atuais do formulário de aluno, incluindo o `parentId` obrigatório.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java` — Modelo de persistência atual com `@ManyToOne Parent` obrigatório.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` — Regras de negócio atuais de criação/atualização/arquivamento/exclusão de alunos.

### Fluxos de Responsáveis Existentes
- `client/src/features/parents/pages/ParentsPage.tsx` — UX atual de lista/busca/arquivamento de responsáveis.
- `client/src/features/parents/pages/ParentCreatePage.tsx` — Fluxo atual de criação de responsável.
- `client/src/features/parents/pages/ParentEditPage.tsx` — Fluxo atual de edição de responsável.
- `client/src/features/parents/pages/ParentDetailPage.tsx` — Visualização de detalhes do responsável atual e exibição de alunos vinculados.
- `client/src/features/parents/components/ParentSelectDropdown.tsx` — Seletor de responsável reutilizável existente usado nos formulários de aluno.
- `client/src/features/parents/forms/parentFormSchema.ts` — Regras atuais de validação de responsável.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/Parent.java` — Entidade de responsável atual.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java` — Regras atuais de arquivamento/exclusão de responsável e salvaguardas de vínculo com o aluno.

</canonical_refs>

<code_context>
## Insights do Código Existente

### Ativos Reutilizáveis
- `client/src/features/parents/components/ParentSelectDropdown.tsx`: Seletor de responsável existente já usado em formulários de alunos; ele deve permanecer o ponto de partida porque a Fase 2 mantém um único responsável obrigatório.
- `client/src/features/students/components/StudentsTable.tsx` e `client/src/features/parents/components/ParentsTable.tsx`: Wrappers de lista paginada existentes que devem ser fortalecidos em vez de substituídos.
- `client/src/components/ui/list-search-input.tsx`, `toggle-switch.tsx` e `pagination.tsx`: Controles compartilhados de lista/busca/arquivamento já usados em todas as telas de registro.
- `client/src/components/ui/section-card.tsx` e `summary-item.tsx`: Primitivos de layout de detalhe e formulário existentes que devem continuar moldando as páginas de registro.

### Padrões Estabelecidos
- As páginas de registro já usam busca + paginação + alternância de arquivado a partir do componente da página pai.
- Os formulários do frontend usam `react-hook-form` + `zodResolver` com mensagens de validação em português.
- Os serviços do backend normalizam CPF/e-mail/contato e impõem unicidade em métodos auxiliares da camada de serviço.
- O arquivamento é preferido em relação à exclusão permanente para operações normais, enquanto os caminhos de exclusão preservam a integridade do domínio através de regras de serviço explícitas.

### Pontos de Integração
- Os fluxos de criação/edição/detalhe de alunos são o principal ponto de integração para fortalecer a experiência do responsável obrigatório sem alterar o formato do relacionamento.
- O detalhe do responsável já mostra os alunos vinculados, portanto deve permanecer o principal local para revisar um responsável -> muitos alunos.
- Os serviços/controllers de aluno e responsável do backend, além dos contratos OpenAPI/Kubb gerados, ainda podem precisar evoluir juntos para que as leituras de alunos possam expor o responsável atual de forma limpa, sem forçar buscas extras no SPA.

</code_context>

<specifics>
## Ideias Específicas

- O CRUD atual existe, mas a Fase 2 deve torná-lo confiável o suficiente para o trabalho diário de registro, em vez de adicionar um processo de matrícula separado.
- O usuário deseja que o conceito de `Responsável` seja mais central do que um enquadramento estrito de `Pai/Mãe`.
- O usuário deseja explicitamente manter a relação atual de um-aluno-para-um-responsável na Fase 2 e remover o escopo planejado de migração de muitos-para-muitos.

</specifics>

<deferred>
## Ideias Adiadas

- Ciclo de vida de matrícula ou pré-matrícula separado — adie, a menos que o produto introduza posteriormente uma semântica real de status/ciclo de vida.
- Vínculos muitos-para-muitos entre aluno/responsável — explicitamente fora da Fase 2 após a correção do escopo.
- Designação de responsável principal ou financeiro — provável decisão futura da fase financeira, não da Fase 2.

</deferred>

---

*Fase: 02-student-parent-registry-hardening*
*Contexto coletado: 19-04-2026*
