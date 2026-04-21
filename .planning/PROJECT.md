# Aprimorar

## O que é isto

O Aprimorar é um aplicativo de gestão escolar para uma escola de aulas particulares que substitui o controle operacional e financeiro baseado em planilhas por um sistema centralizado. Atualmente, está orientado para fluxos de trabalho de secretaria e administradores, com a expectativa de que o produto seja expandido posteriormente para professores, pais, alunos e outros funcionários.

## Valor Central

A secretária deve ser capaz de gerenciar o dia a dia da escola a partir do app, sem depender de planilhas espalhadas.

## Requisitos

### Validados

- ✓ Endpoint de resumo do dashboard e UI do dashboard existem para visibilidade operacional de alto nível — existente
- ✓ Fluxos de gestão de alunos já existem na base de código atual — existente
- ✓ Fluxos de gestão de responsáveis já existem na base de código atual — existente
- ✓ Fluxos de gestão de funcionários já existem na base de código atual — existente
- ✓ Fluxos de gestão de eventos já existem na base de código atual — existente
- ✓ Autenticação simples para acesso de secretária/administrador está implementada com rotas internas protegidas — Fase 1

### Ativos

- [ ] Tornar o CRUD de funcionários, responsáveis, alunos e eventos totalmente funcional para a v1 com as regras de negócio exigidas para as operações escolares
- [ ] Introduzir o acompanhamento financeiro básico para cobranças, pagamentos, saldos vencidos e resumos simples
- [ ] Fornecer um resumo de dashboard pequeno, mas útil, que combine visibilidade operacional diária com visibilidade financeira
- [ ] Eliminar a necessidade de usar o Google Sheets para a gestão de alunos e eventos no trabalho do dia a dia

### Fora de Escopo

- Acesso de autoatendimento para professores, pais e alunos na v1 — adiado até que o fluxo de trabalho da secretária/administrador esteja sólido
- Relatórios avançados e análises mais ricas na v1 — o primeiro lançamento precisa apenas de visibilidade suficiente para decisões diárias
- Notificações e mensagens na v1 — não são necessárias para substituir o fluxo de trabalho de planilhas
- Integrações com gateways de pagamento na v1 — o acompanhamento financeiro interno básico é a prioridade inicial
- Modelos de permissões e papéis complexos na v1 — a autenticação simples é suficiente para o lançamento inicial

## Contexto

A escola atualmente gerencia alunos e eventos em planilhas do Google Drive, sem integração significativa entre esses fluxos. O controle financeiro ainda é impreciso, não há relatórios e as faturas de pagamento ou saldos vencidos não estão sendo acompanhados em um sistema estruturado.

Este repositório é um monorepo brownfield com um backend Spring Boot e um frontend SPA React. A base de código já contém áreas de domínio para dashboard, alunos, responsáveis, funcionários e eventos, além de clientes frontend gerados baseados em OpenAPI. A autenticação agora está presente para a equipe interna por meio de sessões gerenciadas pelo servidor e rotas protegidas de SPA/API, fornecendo a base segura para as próximas fases de endurecimento (hardening).

O usuário imediato do produto é a secretária/administrador da escola. Marcos (milestones) futuros devem abrir o acesso a professores, pais, alunos e outros funcionários da escola assim que o núcleo operacional estiver estável.

## Restrições

- **Escopo do produto**: a v1 deve focar nos fluxos de trabalho da secretária/administrador — portais de usuários posteriores são intencionalmente adiados para manter o primeiro lançamento operacionalmente útil
- **Brownfield**: as áreas existentes de alunos, responsáveis, funcionários, eventos e dashboard devem ser estendidas cuidadosamente em vez de substituídas — o código e os padrões atuais já existem
- **Stack tecnológica**: continuar trabalhando dentro da arquitetura atual React SPA + Spring Boot + PostgreSQL — esta é a direção estabelecida para o repositório
- **Limite de integração**: os tipos e hooks da API do frontend são gerados a partir do OpenAPI do backend — as mudanças de contrato do backend devem fluir através da geração de código (codegen)
- **Objetivo operacional**: o app deve ser bom o suficiente para parar de depender de planilhas para a administração diária da escola — caso contrário, a v1 não terá entregue o valor central

## Decisões Chave

| Decisão | Justificativa | Resultado |
|----------|-----------|---------|
| Priorizar os fluxos de trabalho da secretária/administrador primeiro | Este é o usuário real do dia a dia e o caminho mais rápido para substituir as operações por planilhas | - Pendente |
| Tratar as finanças na v1 como acompanhamento interno básico, não uma plataforma de faturamento completa | A dor imediata é a falta de visibilidade e controle, não a infraestrutura avançada de faturamento | - Pendente |
| Manter o primeiro dashboard focado em dados operacionais e financeiros instantâneos | A tomada de decisão diária precisa de visibilidade tanto da atividade escolar quanto de pagamentos/atrasos | - Pendente |
| Adiar o acesso de professores, pais e alunos | O acesso multi-papel expandiria o escopo antes que o fluxo de administração central esteja estável | - Pendente |
| Usar sessões gerenciadas pelo servidor do Spring Security em vez de JWT | O logout imediato e a persistência de atualização do navegador importam mais do que a portabilidade do token para este SPA interno | Implementado na Fase 1 |

## Evolução

Este documento evolui nas transições de fase e limites de marcos (milestones).

**Após cada transição de fase** (via `/gsd-transition`):
1. Requisitos invalidados? -> Mover para Fora de Escopo com o motivo
2. Requisitos validados? -> Mover para Validados com referência à fase
3. Novos requisitos surgiram? -> Adicionar aos Ativos
4. Decisões para registrar? -> Adicionar às Decisões Chave
5. "O que é isto" ainda está preciso? -> Atualizar se houver desvio

**Após cada marco (milestone)** (via `/gsd-complete-milestone`):
1. Revisão completa de todas as seções
2. Verificação do Valor Central - ainda é a prioridade certa?
3. Auditoria do Fora de Escopo - os motivos ainda são válidos?
4. Atualizar o Contexto com o estado atual

---
*Última atualização: 19-04-2026 após a Fase 1*
