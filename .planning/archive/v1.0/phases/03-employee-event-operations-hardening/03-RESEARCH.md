# Pesquisa Técnica: Fase 3 (Employee & Event Operations Hardening)

**Data:** 21 de Abril de 2026
**Domínio:** Backend (Java/Spring Boot) e Frontend (React/TypeScript)

## 1. Ciclo de Vida de Eventos (EventStatus)
- **Implementação:** Criar `com.aprimorar.api.enums.EventStatus` com os valores `SCHEDULED`, `COMPLETED`, `CANCELED`.
- **Entidade:** Adicionar o campo `status` na entidade `Event.java` com a anotação `@Enumerated(EnumType.STRING)`.
- **Valor Padrão:** O status inicial deve ser `SCHEDULED`.
- **Banco de Dados:** Necessária migração para adicionar a coluna `status` na tabela `tb_events`.

## 2. Refinamento da Lógica de Conflitos
- **Mudança:** Atualizar as queries no `EventRepository.java` para ignorar eventos com status `CANCELED`.
- **Justificativa:** Quando um evento é cancelado, o horário deve ser liberado para novos agendamentos tanto para o professor quanto para o aluno.
- **Validação:** Garantir que ao reativar um evento cancelado, os conflitos sejam revalidados.

## 3. Melhorias no Enum Duty
- **Backend:** 
    - Adicionar um campo `description` (ex: "Professor") ao enum `Duty.java`.
    - Usar `@Schema` do Swagger para documentar os valores no OpenAPI.
- **Frontend:** 
    - Criar um mapeamento de labels em `client/src/features/employees/utils/dutyLabels.ts` (seguindo o padrão de `eventContentLables.ts`).
    - Exibir a descrição amigável nas tabelas e detalhes.

## 4. Filtros Operacionais de Eventos
- **Backend:** 
    - Estender `EventSpecifications.java` para suportar filtros por `startDate` e `endDate`.
    - Atualizar o `EventController` para aceitar esses parâmetros opcionais.
- **Frontend:** 
    - Adicionar botões de filtro rápido na `EventsPage.tsx`:
        - **Hoje:** Filtra eventos do dia atual.
        - **Próximos 7 Dias:** Filtra eventos da próxima semana.
    - Implementar badges coloridos para os status (Azul: Agendado, Verde: Concluído, Vermelho: Cancelado).

## 5. Sincronização de Contratos (Kubb)
- A adição de novos enums e campos nos DTOs de resposta (`EventResponseDTO`) e requisição (`EventRequestDTO`) será automaticamente refletida no frontend após rodar o comando `npx kubb`.

---
*Pesquisa realizada para fundamentar o planejamento da Fase 3.*
