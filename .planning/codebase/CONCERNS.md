# Preocupações com a Base de Código

**Data da Análise:** 17-04-2026

## Dívida Técnica

**Auxiliares de erro de frontend duplicados e logs de depuração restantes:**
- Problema: A formatação de mensagens de erro existe tanto em `client/src/lib/shared/api.ts` quanto em `client/src/lib/shared/api-errors.ts`, com comportamentos diferentes de parsing do Axios (`error.message` vs `error.response?.data?.message`). A mesma área ainda registra logs diretamente no console, e `client/src/features/students/pages/StudentCreatePage.tsx` contém um `console.log` remanescente antes do envio.
- Arquivos: `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/features/students/pages/StudentCreatePage.tsx`
- Impacto: O tratamento de erros varia entre as telas, ruídos de depuração vazam para os consoles de produção e correções futuras precisam ser aplicadas em vários lugares.
- Abordagem de correção: Consolidar em um único auxiliar de erro compartilhado, remover o registro direto no console dos fluxos de UI e rotear falhas inesperadas através de um único caminho de log observável.

**Abstração de formulário de evento morta está fora de sincronia com o contrato atual:**
- Problema: `client/src/features/events/forms/EventForm.tsx` não é importado em nenhum lugar e ainda referencia `title`, enquanto o contrato de evento ativo em `client/src/features/events/forms/eventFormSchema.tsx` e `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java` não possui o campo `title`.
- Arquivos: `client/src/features/events/forms/EventForm.tsx`, `client/src/features/events/forms/eventFormSchema.tsx`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java`
- Impacto: A próxima pessoa que reviver ou reutilizar o `EventForm.tsx` reintroduzirá um formato de payload quebrado e criará um desvio evitável entre frontend/backend.
- Abordagem de correção: Excluir a abstração não utilizada ou realinhá-la com o `eventFormSchema` e o contrato Kubb gerado antes da reutilização.

**Placeholders de UI e TODOs não resolvidos permanecem em componentes reutilizáveis:**
- Problema: A UI compartilhada e os componentes de lista ainda carregam TODOs não resolvidos, incluindo `content: unknown[]` em `client/src/components/ui/pagination.tsx`, acompanhamento do modal de exclusão genérico em `client/src/features/parents/components/DeleteParentButton.tsx`, e TODOs de layout de tabela em `client/src/features/students/components/StudentsTable.tsx` e `client/src/features/employees/components/EmployeesTable.tsx`.
- Arquivos: `client/src/components/ui/pagination.tsx`, `client/src/features/parents/components/DeleteParentButton.tsx`, `client/src/features/students/components/StudentsTable.tsx`, `client/src/features/employees/components/EmployeesTable.tsx`
- Impacto: A UI reutilizável permanece com tipagem fraca, o comportamento duplicado de modal persiste e a camada de tabela é mais difícil de padronizar com segurança.
- Abordagem de correção: Refinar o tipo de paginação em torno do DTO de página real, extrair o comportamento genérico de confirmação para `client/src/components/ui/` e finalizar a refatoração de linha/ação nos wrappers de tabela.

**TODOs do backend marcam trabalhos de contrato e domínio inacabados:**
- Problema: Classes core do backend ainda carregam marcadores TODO para posicionamento de validação ausente, documentação de enum/codegen e futuros campos do Google Calendar.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/enums/BrazilianStates.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java`
- Impacto: Detalhes importantes da implementação permanecem implícitos, e o trabalho de contrato cross-stack depende de conhecimento tribal em vez de caminhos de código finalizados.
- Abordagem de correção: Resolver ou converter cada TODO em código explícito, testes ou trabalho rastreado antes de expandir os módulos afetados.

## Bugs Conhecidos

**Dashboard solicita o ano errado no SPA:**
- Sintomas: O dashboard consulta a API com o ano atual mais um.
- Arquivos: `client/src/features/dashboard/DashboardPage.tsx`
- Gatilho: Abrir o dashboard sem substituir o período padrão.
- Solução temporária (Workaround): Fixar o ano desejado na sessão do navegador ou aplicar um patch no `getCurrentYearMonth()` para remover o `+ 1`.

**Payloads de atualização aceitam CPF, mas as atualizações ignoram silenciosamente as alterações de CPF:**
- Sintomas: As requisições de atualização de responsável, aluno e funcionário aceitam o campo `cpf`, mas os caminhos de atualização de serviço/entidade não o persistem.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/dto/ParentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/Parent.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/dto/EmployeeRequestDTO.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/Employee.java`
- Gatilho: Enviar um formulário de edição com um CPF alterado para um responsável, aluno ou funcionário.
- Solução temporária: Tratar o CPF como imutável na UI até que o contrato do backend seja corrigido, ou implementar o suporte total à atualização de CPF, incluindo verificações de unicidade.

**Parâmetros de dashboard inválidos atualmente resultam em erros internos do servidor:**
- Sintomas: Valores inválidos de `year` (ano) ou `month` (mês) lançam `InvalidDashboardRequestException`, mas o `GlobalExceptionHandler` não trata essa exceção explicitamente e o tratador genérico retorna `500` com `VALIDATION_ERROR`.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/exception/InvalidDashboardRequestException.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`
- Gatilho: Chamar `GET /v1/dashboard/summary` com um ano ou mês nulo/fora de alcance.
- Solução temporária: Validar os valores de período no cliente e enviar apenas meses de `1..12` e anos de `2000..2100` até que o mapeamento de exceção seja corrigido.

## Considerações de Segurança

**Nenhum limite de autenticação ou autorização está presente:**
- Risco: Todos os endpoints de CRUD e dashboard estão efetivamente públicos para qualquer chamador que consiga alcançar a API.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventController.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardController.java`, `client/src/App.tsx`
- Mitigação atual: Não detectada no código. Não existem classes de Spring Security, filtros de autenticação, rotas protegidas ou caminhos de tratamento de token/sessão.
- Recomendações: Adicionar uma camada explícita de autenticação primeiro na API, depois proteger as rotas e mutações do frontend atrás do estado de usuário/sessão autenticado.

**CORS está fixado apenas para desenvolvimento local:**
- Risco: A API está fixada em `http://localhost:5173`, o que bloqueia ambientes não locais e empurra mudanças de origem em tempo de implantação para edições no código-fonte.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java`
- Mitigação atual: A origem de desenvolvimento local é permitida explicitamente.
- Recomendações: Externalizar as origens permitidas em configurações baseadas em variáveis de ambiente e separar as políticas de dev/prod.

## Gargalos de Desempenho

**O resumo do dashboard realiza múltiplas consultas agregadas sem cache por requisição:**
- Problema: Cada carregamento do dashboard dispara consultas separadas de count/sum/group.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventRepository.java`
- Causa: `getSummary()` emite cinco chamadas de repositório para a mesma janela mensal e não há camada de cache.
- Caminho de melhoria: Cachear os resumos mensais, colapsar agregados relacionados onde for prático e adicionar uma estratégia de invalidação no nível de serviço quando os eventos mudarem.

**Endpoints de busca dependem de varreduras `%termo%` + `lower(...)` sem índices correspondentes:**
- Problema: As buscas de alunos, responsáveis, funcionários e eventos usam filtros LIKE amplos e insensíveis a maiúsculas que não se alinham com os índices do esquema atual.
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/repository/ParentSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/repository/EmployeeSpecifications.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/repository/EventSpecifications.java`, `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- Causa: `V1__init.sql` adiciona índices apenas para `parent_id`, `student_id`, `employee_id` e horário de início do evento, não para os campos de texto pesquisados.
- Caminho de melhoria: Adicionar índices que correspondam aos filtros mais frequentes, ou migrar para busca de texto nativa do banco de dados/índices trigram para grandes conjuntos de dados.

## Áreas Frágeis

**O mapeamento global de exceções é inconsistente e fácil de regredir:**
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/ErrorCode.java`
- Por que é frágil: O tratador duplica algumas classes de exceção no ramo proibido, mapeia falhas internas para `VALIDATION_ERROR` e não cobre explicitamente todas as exceções de domínio.
- Modificação segura: Adicionar um ramo explícito por família de exceção de negócio, alinhar cada ramo com o par de código de erro/status HTTP pretendido e estender os testes antes de alterar mensagens ou status.
- Cobertura de teste: Existe apenas `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`; não existem testes no nível do controller validando a tradução de exceção de ponta a ponta.

**A estratégia de exclusão de registros fantasma depende de UUIDs fixos semeados em uma única migração:**
- Arquivos: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/EmployeeService.java`, `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- Por que é frágil: A lógica de exclusão de alunos e funcionários pressupõe que as linhas fantasmas do `V1__init.sql` sempre existam e sempre mantenham os mesmos UUIDs.
- Modificação segura: Preservar essas linhas de semente em todos os ambientes, adicionar verificação de integridade de dados na inicialização e cobrir a exclusão/reatribuição com testes de integração antes de alterar o histórico de migração.
- Cobertura de teste: Existem testes unitários no nível de serviço, mas não há testes de repositório/banco de dados provando que a reatribuição fantasma funciona contra um esquema real.

## Limites de Escala

**Endpoints de lista e busca estão ajustados para pequenos conjuntos de dados:**
- Capacidade atual: Adequada para o tamanho atual do domínio estilo CRUD e fluxos de UI pagináveis em `client/src/features/students/pages/StudentsPage.tsx`, `client/src/features/parents/pages/ParentsPage.tsx`, `client/src/features/employees/pages/EmployeesPage.tsx`, e `client/src/features/events/pages/EventsPage.tsx`.
- Limite: O tempo de resposta degradará conforme as linhas pesquisáveis por texto crescerem, porque os filtros são executados com predicados LIKE amplos e sem índices de busca dedicados.
- Caminho de escala: Adicionar colunas de filtro indexadas, introduzir parâmetros de consulta mais seletivos e realizar benchmarks das consultas de repositório antes que o crescimento dos dados torne as páginas de lista lentas.

## Dependências em Risco

**O cliente frontend gerado está fortemente acoplado a um backend ativo durante o desenvolvimento:**
- Risco: A regeneração do contrato requer uma API em execução e sequenciamento manual, portanto, o desvio de DTO pode sobreviver localmente até que alguém se lembre de regenerar.
- Impacto: O `client/src/kubb/` pode se tornar obsoleto em relação aos DTOs do backend e quebrar formulários ou hooks de consulta após alterações na API.
- Plano de migração: Manter o fluxo atual do Kubb, mas automatizar a regeneração de OpenAPI + Kubb no CI ou na verificação pré-merge para que o desvio de contrato falhe rapidamente.

## Funcionalidades Críticas Ausentes

**A exclusão de eventos está implementada no serviço, mas não exposta via HTTP ou no SPA:**
- Problema: `EventService` contém `deleteEvent(UUID eventId)`, mas o `EventController` tem o endpoint de exclusão comentado e o frontend não tem nenhuma ação de exclusão para eventos.
- Bloqueio: Os operadores não conseguem remover um evento através da superfície de produto suportada.

## Lacunas de Cobertura de Teste

**O comportamento do frontend não é testado:**
- O que não é testado: Renderização de rotas, validação de formulários, fluxos de mutação, comportamento de invalidação otimista e estados de erro em `client/src/features/**`.
- Arquivos: `client/package.json`, `client/src/App.tsx`, `client/src/features/students/pages/StudentCreatePage.tsx`, `client/src/features/events/pages/EventCreatePage.tsx`, `client/src/features/employees/hooks/emlpoyee-mutations.ts`
- Risco: Regressões de UI, incompatibilidades de contrato e bugs de data/validação podem ser entregues sem detecção automatizada.
- Prioridade: Alta

**As camadas web e de persistência do backend são pouco exercitadas:**
- O que não é testado: Comportamento de requisição/resposta do controller, consultas/specifications do repositório, comportamento do serviço de dashboard e fluxos de exclusão baseados em migração.
- Arquivos: `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/employee/EmployeeServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/event/EventServiceTest.java`, `server/api-aprimorar/src/test/java/com/aprimorar/api/exception/GlobalExceptionHandlerTest.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/dashboard/DashboardService.java`
- Risco: Regressões de contrato HTTP, bugs de consulta e falhas exclusivas do banco de dados podem escapar da cobertura unitária do serviço.
- Prioridade: Alta

---

*Auditoria de preocupações: 17-04-2026*
