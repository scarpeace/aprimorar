# Padrões de Arquitetura

**Domínio:** aplicativo de gestão de escola particular
**Pesquisado:** 17-04-2026

## Arquitetura Recomendada

Estender o **monolito modular** existente em vez de introduzir microsserviços. Manter o backend atual Spring Boot + PostgreSQL e o SPA React, mas formalizar o app em seis áreas delimitadas (bounded areas): **Identidade & Acesso**, **Registro Escolar**, **Operações**, **Financeiro**, **Dashboard/Relatórios** e **Plataforma Compartilhada**.

A base de código existente já se comporta como um monolito modular organizado por domínio: os pacotes do backend são divididos por domínio (`student`, `parent`, `employee`, `event`, `dashboard`), o frontend é dividido por pastas de funcionalidades (features) e os contratos fluem através de OpenAPI + Kubb. O caminho de extensão mais seguro é preservar esse formato e adicionar as capacidades ausentes como novos módulos, não como hacks transversais dentro de `event` ou `dashboard`.

```text
React SPA
├── Shell de Autenticação (login, bootstrap de sessão, rotas protegidas)
├── Páginas de Funcionalidades (dashboard, alunos, responsáveis, funcionários, eventos, financeiro)
└── Camada compartilhada de API/query (Kubb + TanStack Query)
        ↓
Monolito modular Spring Boot
├── identity/         -> login, logout, usuário atual, verificações de papel
├── student/          -> dados mestres de alunos
├── parent/           -> dados mestres de pais/responsáveis
├── employee/         -> dados mestres de funcionários
├── event/            -> operações de agendamento / atendimento
├── finance/          -> cobranças, pagamentos, regras de saldo
├── dashboard/        -> apenas modelos de leitura agregados
└── shared/           -> tratamento de erros, paginação, clock, configuração
        ↓
PostgreSQL
├── tabelas de registro
├── tabelas de operações
├── tabelas financeiras
└── tabelas de autenticação
```

### Limites dos Componentes

| Componente | Responsabilidade | Comunica-se com |
|-----------|---------------|-------------------|
| UI de Autenticação | Formulário de login, logout, bootstrap de sessão, guarda de rota, contexto de usuário atual | API de Identidade, rotas protegidas |
| Shell principal do app | Navegação, visibilidade de seção, layout, estados globais de erro/carregamento | UI de Autenticação, páginas de funcionalidades |
| Módulo de Aluno | CRUD de aluno, busca, arquivamento, vínculo com responsável | Módulo de Responsável, modelo de leitura do Dashboard |
| Módulo de Responsável | CRUD de responsável e busca para atribuição de aluno | Módulo de Aluno |
| Módulo de Funcionário | CRUD de funcionário e busca para agendamento/propriedade | Módulo de Evento, bootstrap de Identidade |
| Módulo de Evento | Agendamento de atendimentos/aulas, conflitos, vínculos com aluno + funcionário | Aluno, Funcionário, Dashboard |
| Módulo Financeiro | Livro-razão de recebíveis/pagamentos, rastreamento de atrasos, resumos financeiros | Aluno, Responsável, Dashboard |
| Módulo de Dashboard | KPIs e gráficos consolidados expostos apenas como endpoints de leitura | Evento, Financeiro, Aluno |
| Módulo de Identidade | Conta de usuário, hash de senha, ciclo de vida da sessão, claims de papel | Funcionário (referência opcional), todos os controllers protegidos |
| Plataforma compartilhada | ProblemDetail/formatação de erro, paginação, clock, config de CORS/segurança, auditoria | Todos os módulos do backend |

## Fluxo de Dados

### 1. Autenticação e bootstrap do app

1. O usuário abre o SPA.
2. O SPA chama `GET /v1/auth/me` (ou endpoint de bootstrap equivalente).
3. O backend resolve a sessão a partir do Spring Security e retorna o usuário atual + papel (role) + áreas de navegação permitidas.
4. O app React renderiza:
   - a rota de login para usuários anônimos, ou
   - o layout protegido para usuários autenticados.
5. O TanStack Query armazena em cache o payload da sessão; o logout limpa as consultas relacionadas à sessão.

### 2. Fluxo de negócio protegido

1. O usuário navega para uma página protegida.
2. O guarda de rota/layout verifica o estado da sessão em cache.
3. A página chama o hook Kubb gerado.
4. O controller do backend delega para o serviço.
5. O serviço aplica as regras de negócio e autorização.
6. O repositório persiste/lê os dados.
7. A resposta DTO retorna através do cliente gerado pelo OpenAPI.
8. A mutação invalida as chaves de consulta relevantes para que os dados de lista/detalhe/dashboard sejam atualizados.

### 3. Fluxo do Dashboard

1. A UI do Dashboard chama um pequeno número de endpoints de leitura agregados.
2. O serviço de Dashboard compõe os dados de resumo a partir dos repositórios/serviços de Evento + Financeiro + Aluno.
3. A UI renderiza KPIs/gráficos sem reconstruir a lógica de negócio no lado do cliente.

### 4. Fluxo Financeiro

1. A secretária cria ou atualiza uma cobrança para um aluno/responsável.
2. O serviço financeiro calcula o status (`ABERTO`, `PAGO`, `VENCIDO`, `PARCIAL`, etc.).
3. Os lançamentos de pagamento atualizam o histórico de saldo.
4. O Dashboard lê resumos financeiros pré-definidos; as páginas financeiras leem endpoints estilo livro-razão.

## Padrões a Seguir

### Padrão 1: Manter a autenticação como um módulo de primeira classe, não apenas um guarda de frontend
**O quê:** Adicionar um limite de identidade no backend com autenticação baseada em sessão, endpoint de usuário atual e autorização baseada em papéis.
**Quando:** Imediatamente, antes das mudanças de visibilidade do financeiro e dashboard.
**Por quê:** Neste repositório, a autenticação ainda não existe e todas as rotas atuais são implicitamente públicas. Adicionar guardas de página sem imposição no servidor cria uma falsa sensação de segurança.

**Formato recomendado:**

```text
identity/
├── AuthController      -> login, logout, me, csrf se necessário
├── AuthService         -> verificação de credenciais, DTO de bootstrap
├── UserAccount         -> usuário/e-mail, hash de senha, flag ativa
├── UserRole            -> ADMIN, SECRETARIA (começar simples)
└── SecurityConfig      -> cadeia de filtros do Spring Security
```

**Recomendação:** Usar **autenticação de sessão no lado do servidor com cookies seguros**, não JWT, para a v1. Este é um par SPA + backend Spring do mesmo produto, não uma plataforma de API pública para múltiplos clientes. As sessões são mais simples de revogar, mais simples de entender e se alinham com os padrões do Spring Security. Se o SPA permanecer em uma origem diferente em dev/prod, configure o CORS e as requisições com credenciais explicitamente.

### Padrão 2: Separar o financeiro dos eventos, mesmo que o dashboard atualmente derive dinheiro dos eventos
**O quê:** Criar um módulo `finance` dedicado em vez de estender `Event` como a fonte da verdade para os recebíveis.
**Quando:** Assim que os saldos vencidos e o rastreamento de pagamentos forem introduzidos.
**Por quê:** A entidade `Event` existente já contém `price` e `payment`, e o dashboard soma esses valores hoje. Isso funciona para a economia das aulas, mas é o modelo core errado para os recebíveis da escola. As cobranças/pagamentos precisam de seu próprio ciclo de vida, datas de vencimento, status e trilha de auditoria.

**Núcleo financeiro recomendado:**

```text
finance/
├── Charge             -> valor devido, data de vencimento, referência de aluno/responsável, status
├── Payment            -> valor pago, pago em, método, observação
├── FinanceService     -> criar cobrança, registrar pagamento, calcular saldo
├── FinanceController  -> listar cobranças, resumo de atrasos, lançar pagamentos
└── FinanceSummaryService -> agregação orientada ao dashboard
```

**Regra de integração:** vincular registros financeiros ao `student` (primário) e opcionalmente ao `parent`, mas não torná-los filhos de `event`.

### Padrão 3: Dashboard como modelo de leitura, não dono da transação
**O quê:** Manter os endpoints do dashboard enxutos e apenas com agregações.
**Quando:** Sempre.
**Por quê:** Os dashboards mudam mais rápido que as regras transacionais. Se o código do dashboard se tornar o local onde a lógica financeira reside, cada ajuste de KPI corre o risco de corromper as regras de negócio.

**Boa divisão:**
- `finance` detém os cálculos de atraso e saldo.
- `event` detém o cronograma e a economia das aulas.
- `dashboard` apenas solicita a esses módulos os dados resumidos.

### Padrão 4: Shell de autenticação no frontend acima das rotas de funcionalidades
**O quê:** Introduzir um pequeno shell de app que resolva o estado da sessão antes de renderizar o layout protegido.
**Quando:** Primeiro incremento de autenticação no frontend.
**Por quê:** O app atual monta o `MainLayout` diretamente para todas as rotas. Isso torna difícil adicionar autenticação, visibilidade de navegação e redirecionamentos anônimos de forma limpa.

**Fluxo recomendado:**

```text
<BrowserRouter>
  /login -> público
  /app/* -> layout protegido
            ├── dashboard
            ├── students
            ├── parents
            ├── employees
            ├── events
            └── finance
```

Se a equipe adotar posteriormente os data routers do React Router, mova as verificações de autenticação para os loaders/middleware de rota. Por enquanto, um layout protegido + consulta de bootstrap é a adaptação de menor risco.

### Padrão 5: Preservar a integração baseada em contrato (contract-first)
**O quê:** Todos os novos DTOs de autenticação, financeiro e dashboard fluem através da geração OpenAPI do backend e sincronização Kubb no frontend.
**Quando:** Em cada mudança de contrato.
**Por quê:** Este repositório já depende de clientes gerados. Ignorar esse padrão para autenticação ou financeiro criará o maior risco de integração no marco (milestone).

## Ordem de Construção Sugerida

### Passo 1: Fundação de identidade no backend
Adicionar Spring Security, contas de usuário, hashing de senha, endpoints de login/logout/usuário atual e verificações básicas de papel.

**Por que primeiro:** é a única capacidade transversal totalmente nova. A visibilidade do financeiro e do dashboard depende de saber quem está logado.

### Passo 2: Shell de autenticação no frontend e roteamento protegido
Adicionar página de login, consulta de bootstrap, layout protegido, logout e visibilidade de navegação.

**Por que segundo:** isso expõe a autenticação com segurança sem forçar reescritas imediatas de domínio. As páginas existentes de aluno/responsável/funcionário/evento podem permanecer praticamente inalteradas atrás do shell.

### Passo 3: Reforço da autorização nos módulos existentes
Proteger os endpoints atuais do backend e garantir que o SPA trate erros `401/403` de forma limpa.

**Por que terceiro:** minimiza o risco em brownfield. Primeiro faça a autenticação funcionar, depois faça o app antigo respeitá-la.

### Passo 4: Domínio financeiro como um módulo separado
Adicionar tabelas de cobrança/pagamento, serviços, endpoints de CRUD/lista, regras de atraso e endpoints de resumo.

**Por que quarto:** o financeiro tem o maior risco de domínio e deve ser implementado após a autenticação, mas antes do redesenho do dashboard. Isso evita construir métricas de dashboard sobre uma lógica financeira temporária.

### Passo 5: Reescrita do dashboard para compor resumos operacionais + financeiros
Refatorar o serviço de dashboard para que ele leia os resumos financeiros mais as métricas operacionais/de eventos existentes.

**Por que quinto:** o dashboard deve ser o consumidor de módulos finalizados, não a superfície de protótipo onde a lógica financeira inacabada vaza.

### Passo 6: Refinamento opcional de visibilidade
Apenas após o acima, adicionar visibilidade mais granular baseada em papéis, flags de funcionalidades (feature flags) ou futuros portais para professores/pais/alunos.

**Por que por último:** a v1 explicitamente não precisa de permissões complexas.

## Orientação de Integração para Autenticação e Financeiro nesta Stack Existente

### Orientação de integração no backend

- Adicionar `spring-boot-starter-security`; não está presente hoje no `pom.xml`.
- Manter a configuração de segurança centralizada em `config/` mais um pacote de domínio `identity/`.
- Reutilizar o formato de erro global existente adicionando tratadores/pontos de entrada explícitos para `401` e `403`.
- Como o SPA roda em `http://localhost:5173`, atualize o CORS para requisições com credenciais quando a autenticação de sessão for introduzida.
- Adicionar migrações Flyway relacionadas à autenticação antes de expor o login.
- Manter apenas `ADMIN` e `SECRETARIA` para a v1; não modele toda a futura matriz de papéis ainda.

### Orientação de integração no frontend

- Adicionar uma pasta de funcionalidade de autenticação dedicada em vez de espalhar a lógica de autenticação pelo `MainLayout`.
- Manter o estado da sessão no TanStack Query, não em um estado global ad hoc.
- Após o login/logout bem-sucedido, invalide ou limpe o `me`, o dashboard e quaisquer consultas escopadas ao usuário.
- Derive a visibilidade da navegação do payload `me` em vez de fixar cada item do menu para sempre no código.
- Manter os hooks Kubb gerados como a fonte da verdade; não trate a autenticação como caso especial com código de fetch escrito manualmente, a menos que seja inevitável.

### Orientação de migração em brownfield

- **Não** reescreva alunos/responsáveis/funcionários/eventos para "caber na autenticação". Envolva-os com a autenticação primeiro.
- **Não** adapte o financeiro na tabela `event` existente apenas porque a receita/custo já existe lá.
- **Não** expanda as permissões além do que o marco atual necessita.
- Altere o dashboard por último entre as superfícies voltadas para o usuário; é o lugar mais fácil para recompor dados uma vez que os módulos subjacentes estejam estáveis.

## Antipadrões a Evitar

### Antipadrão 1: Usar `Employee` como o modelo de conta de login
**O quê:** Tratar registros de funcionários como registros de autenticação.
**Por que é ruim:** Dados de RH/pessoa e credenciais de login evoluem de forma diferente. Nem todo funcionário deve se tornar um usuário, e as preocupações com senha/bloqueio de conta não pertencem ao agregado de funcionário.
**Em vez disso:** Crie um `UserAccount` e opcionalmente vincule-o ao `Employee`.

### Antipadrão 2: Manter o financeiro dentro das consultas do dashboard
**O quê:** Calcular saldos vencidos diretamente dentro dos serviços/controllers de dashboard.
**Por que é ruim:** As regras de negócio tornam-se duplicadas e impossíveis de testar.
**Em vez disso:** Coloque as regras de saldo em `finance`, exponha métodos de resumo amigáveis ao dashboard.

### Antipadrão 3: Complexidade de JWT primeiro para um SPA administrativo interno
**O quê:** Adicionar emissão de token, rotação de refresh token, estratégia de armazenamento e interceptores customizados para a v1.
**Por que é ruim:** Mais partes móveis do que o produto precisa agora.
**Em vez disso:** Use cookies de sessão seguros primeiro; revisite o JWT apenas se surgirem clientes externos/mobile/públicos.

### Antipadrão 4: Autorização apenas no frontend
**O quê:** Esconder rotas/itens de menu sem proteção no lado do servidor.
**Por que é ruim:** Qualquer pessoa ainda pode chamar a API.
**Em vez disso:** Imponha a autenticação no Spring Security e use a visibilidade do frontend apenas como UX.

## Considerações de Escalabilidade

| Preocupação | Com 100 usuários | Com 10 mil usuários | Com 1 milhão de usuários |
|---------|--------------|--------------|-------------|
| Autenticação | Sessão em memória/local ok para dev; contas no DB | Considere Spring Session se houver múltiplas instâncias | Provavelmente necessária plataforma de identidade dedicada |
| Agregação do dashboard | Agregação direta no repositório está ok | Adicionar consultas de resumo/views materializadas para KPIs financeiros | Pipeline de leitura/analytics separado |
| Livro-razão financeiro | Tabelas simples de cobrança/pagamento são suficientes | Adicionar índices em data de vencimento, aluno, status | Provavelmente necessária estratégia de particionamento/arquivamento |
| Composição da API | Chamadas de serviço de módulo para módulo dentro do monolito | Manter limites claros, evitar dependências circulares de serviço | Considere a extração apenas se a organização/escala exigir |
| Carregamento de dados no frontend | Consultas no nível da página estão ok | Consolidar endpoints de resumo para reduzir cascatas (waterfalls) | Caching em BFF/edge pode ser justificado |

## Fontes

- Inspeção do repositório: pacotes de domínio existentes, estrutura de roteamento, acoplamento dashboard/evento, config de CORS e fluxo OpenAPI/Kubb nesta base de código. **Confiança: ALTA**
- Referência de gerenciamento de sessão do Spring Security: https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html **Confiança: ALTA**
- Referência de CSRF do Spring Security para SPAs: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html **Confiança: ALTA**
- Referência de integração CORS do Spring Security: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html **Confiança: ALTA**
- Documentação de objeto de rota e middleware do React Router: https://reactrouter.com/start/data/route-object **Confiança: MÉDIA** (útil para a evolução futura do roteador; o app atual ainda usa rotas declarativas)
- Orientação de invalidação do TanStack Query: https://tanstack.com/query/latest/docs/framework/react/guides/invalidations-from-mutations **Confiança: ALTA**
