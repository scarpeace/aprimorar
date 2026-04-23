# Fase 3: Employee & Event Operations Hardening - Contexto

**Data:** 21 de Abril de 2026
**Status:** Pronto para Planejamento

<domain>
## Fronteira da Fase

Fortalecer os fluxos de registros de funcionários e eventos para que a secretária possa gerenciar o dia a dia da escola com confiabilidade. Esta fase foca na integridade do agendamento, clareza dos papéis de funcionários e visibilidade operacional imediata, substituindo o uso de planilhas para essas tarefas.

</domain>

<decisions>
## Decisões de Implementação

### Cadastro de Funcionários (Employees)
- **D-01:** Utilizar o Enum `Duty` existente para definir papéis (`TEACHER`, `ADM`, `THERAPIST`, `MENTOR`, `SYSTEM`).
- **D-02:** (Recomendação Técnica) Adicionar descrições amigáveis no Enum `Duty` para exibição na UI (ex: "Professor", "Administrativo").
- **D-03:** Manter o arquivamento (`archived_at`) como padrão para funcionários, preservando o histórico de eventos vinculados.

### Ciclo de Vida do Evento (Events)
- **D-04:** Implementar um Enum `EventStatus` para gerenciar o estado dos eventos: `SCHEDULED` (Agendado), `COMPLETED` (Concluído), `CANCELED` (Cancelado).
- **D-05:** Eventos cancelados devem liberar o horário no sistema de conflitos, mas permanecer no banco para histórico.
- **D-06:** Manter a janela de edição de 20 dias (regra existente no `Event.java`) para ajustes de valor/pagamento após o término do evento.

### Regras de Agendamento e Conflitos
- **D-07:** Regra Rígida (1:1): Um professor não pode ter mais de um evento no mesmo horário, e um aluno não pode ter mais de um evento no mesmo horário.
- **D-08:** Validar disponibilidade de ambos os participantes (Professor e Aluno) tanto na criação quanto na atualização de eventos.
- **D-09:** Bloquear criação/edição de eventos com participantes (estudantes ou funcionários) que estejam arquivados.

### Visibilidade Operacional
- **D-10:** Implementar uma lista simples de "Próximos Eventos" com filtros rápidos (Hoje, Próximos 7 dias) na página principal de Eventos.
- **D-11:** (Estratégia) Focar em listagens tabulares eficientes antes de investir em componentes complexos de calendário, priorizando a utilidade para a secretária.

</decisions>

<canonical_refs>
## Referências Canônicas

### Planejamento e Escopo
- `.planning/PROJECT.md` — Escopo do produto e metas de substituição de planilhas.
- `.planning/REQUIREMENTS.md` — Requisitos `EMPL-01` a `EMPL-04` e `EVNT-01` a `EVNT-05`.
- `.planning/ROADMAP.md` — Objetivos e critérios de sucesso da Fase 3.

### Fluxos Existentes - Funcionários
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/employee/Employee.java` — Entidade atual com campo `duty`.
- `client/src/features/employees/forms/employeeFormSchema.ts` — Regras de validação do formulário de funcionários.

### Fluxos Existentes - Eventos
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/Event.java` — Regras de negócio de datas e participantes.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/EventService.java` — Lógica de verificação de conflitos de horário.
- `client/src/features/events/forms/eventFormSchema.tsx` — Validação de campos de evento no frontend.

</canonical_refs>

<code_context>
## Insights do Código

### Ativos Reutilizáveis
- `EmployeeSpecifications.java` e `EventSpecifications.java`: Devem ser estendidos para suportar novos filtros operacionais (status, range de datas).
- `EmployeeSelectDropdown.tsx` e `ContentSelectDropdown.tsx`: Componentes base para seleção de participantes e conteúdos em formulários.

### Padrões Estabelecidos
- Uso de `Instant` para datas/horas no backend, garantindo consistência de fuso horário.
- Validações em cascata: Entity (regras de integridade) -> Service (regras de negócio/conflitos) -> DTO (regras de entrada).
- Mensagens de erro em Português centralizadas em exceções customizadas.

</code_context>

<specifics>
## Ideias Específicas
- O campo `title` do evento deve ser gerado automaticamente combinando `Conteúdo - Professor - Aluno` para facilitar a busca rápida pela secretária.
- Incluir indicadores visuais de "Status" (badges coloridos) nas tabelas de eventos para facilitar a leitura rápida.

</specifics>

---

*Fase: 03-employee-event-operations-hardening*
*Contexto gerado: 21 de Abril de 2026*
