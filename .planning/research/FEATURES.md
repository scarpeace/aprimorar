# Feature Landscape

**Domain:** Gestão Escolar (Aulas Particulares) / Financial Management for Employees
**Researched:** 2024-05
**Overall confidence:** HIGH (Based on domain patterns for gig/contractor management and explicitly stated project constraints).

## Table Stakes

Features users expect in any system managing contractor/teacher payments. Missing these makes the product feel incomplete or untrustworthy for financial tracking.

| Feature | Why Expected | Complexity | Notes |
|---------|--------------|------------|-------|
| **Tabela de Eventos do Colaborador** | A secretária precisa ver exatamente quais aulas o professor deu para poder pagá-lo. | Low | Listagem filtrada por `employeeId`, ordenada por data descrescente. |
| **Busca por Aluno e Filtros** | Para conciliação rápida. Quando um pai questiona uma aula, a secretária precisa encontrar o evento pelo nome do aluno. Ocultar itens "Pagos" reduz o ruído visual. | Low | Filtro de status (`PENDENTE`, `PAGO`) e busca textual no nome do aluno. |
| **Registro de Data de Pagamento** | Previne pagamentos duplicados e resolve disputas. Um simples status "Pago" não é auditável. | Low | Ao marcar como pago, o sistema deve registrar `paymentDate`. |
| **KPIs: Total a Pagar e Total Pago** | O resumo financeiro do mês (saldo pendente vs. saldo quitado). Vital para o controle de fluxo de caixa da escola. | Med | Requer queries de agregação no backend agrupando por status e mês. |

## Differentiators

Features that set the product apart or provide high workflow value, even if not strictly mandatory for a barebones MVP.

| Feature | Value Proposition | Complexity | Notes |
|---------|-------------------|------------|-------|
| **Alternador de Período (Mês vs. Histórico)** | Permite que a secretária faça uma auditoria rápida de todo o tempo de vida do professor na escola sem precisar gerar relatórios complexos em outras telas. | Low | Parametrização da query de KPIs e Tabela (ex: `?month=current` ou `?allTime=true`). |
| **Ações de Pagamento em Lote (Bulk Payment)** | Em vez de clicar "Pagar" em 20 aulas individuais, selecionar todas do mês e marcar como pagas de uma vez economiza tempo significativo. | Med | Otimização de fluxo de trabalho de alto valor. |
| **Geração de Recibo/Extrato (PDF)** | Para que a secretária possa enviar (via WhatsApp/Email) um comprovante claro ao professor do que está sendo pago naquele acerto. | High | Adia para V2 se o PDF for complexo, mas é um forte diferencial. |

## Anti-Features

Features to explicitly NOT build in this milestone to protect scope.

| Anti-Feature | Why Avoid | What to Do Instead |
|--------------|-----------|-------------------|
| **Portal de Autoatendimento do Professor** | Conforme `PROJECT.md`, o acesso de professores está fora do escopo. Adicionaria complexidade enorme de roteamento e permissões (RBAC). | Construir a interface **apenas** para o uso da secretária/administrador. |
| **Split de Pagamentos Automático / Gateway** | Integrações financeiras com bancos atrasariam o MVP operacional. | A secretária faz o PIX/Transferência por fora e apenas registra a transação no sistema (Controle Interno). |
| **Cálculo de Comissões Escalonadas Complexas** | Se a regra de negócio for "X% até 10 aulas, Y% depois", automatizar isso agora é arriscado. | Focar no valor nominal do evento primeiro. O professor recebe o valor atrelado àquele evento específico. |

## Feature Dependencies

```
CRUD de Eventos → Tabela do Colaborador (A tabela precisa ler os eventos já criados)
Relacionamento Evento/Professor → Tabela do Colaborador (Eventos precisam estar atrelados a um EmployeeID)
Status de Pagamento no Evento → Filtro de Tabela & KPIs (O modelo de Evento precisa ter 'paymentStatus' e 'paymentDate')
```

## MVP Recommendation

**Prioritize:**
1. Tabela de eventos filtrada por funcionário.
2. Filtro de "Ocultar Pagos" e busca por Aluno.
3. Ação de marcar evento como pago (gravando a data do pagamento).
4. Painel lateral com KPIs básicos (Total Pago, Total a Pagar, Qtd de Eventos) com alternador Mês/Histórico.

**Defer:**
- Geração de extrato em PDF (pode ser resolvido com um print da tela no início).
- Pagamento em lote (Bulk Payment) se a implementação do backend for complexa na primeira iteração (embora altamente recomendado se viável).

## Sources
- `PROJECT.md` - Definições de escopo, foco no fluxo da secretária e adiamento de acesso a professores.
- Padrões de mercado em sistemas de gestão de freelancers/prestadores de serviço (gig economy, clinicas, escolas): O padrão ouro inicial é rastreabilidade manual simples antes da automação financeira.