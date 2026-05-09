# EITA: pendencias da verificacao Modulith estrita (Atualizado em 2026-05-08)

Este documento registra o progresso da migração para Modulith e os problemas detectados na verificação estrita.

## Status da Migração

### ✅ Resolvido
- **Módulo Auth**: Removido completamente para migração futuro para JWT. Ciclos `auth <-> employee` e `config -> auth` eliminados.
- **Consolidação Registration**: Os módulos `student`, `parent`, `employee` e `address` foram fundidos no macro-módulo `registration`.
- **Ciclo Parent-Student**: Eliminado. Agora é uma relação JPA `@ManyToOne` interna ao módulo.
- **Rich Domain**: Mappers removidos. Entidades agora cuidam de sua própria normalização e conversão para DTO.
- **Zip Normalization**: Movido para dentro da entidade `Address`.

### 🚧 Pendências Atuais (Prioritárias)

#### 1. Rename da Entidade `Event`
A palavra `Event` colide semanticamente com os mecanismos de eventos do Java/Spring (`ApplicationEvent`). Precisamos renomear a entidade e o módulo para evitar confusão.
- **Sugestão**: `Occurrence` (Ocorrência) ou `Activity` (Atividade).

#### 2. Ciclo `registration <-> [Occurrence]`
- **O que acontece**: O `StudentService` e `EmployeeService` (dentro de `registration`) chamam o service de eventos para calcular resumos financeiros/operacionais.
- **Caminho**: Mover o cálculo de resumos para o módulo de eventos ou para um novo módulo de `analytics`.

#### 3. Ciclo `[Occurrence] <-> finance`
- **O que acontece**: O cadastro de eventos cria transações síncronas em `finance`, mas `finance` consulta eventos via JPQL para somas.
- **Caminho**: 
    1. `registration` publica `StudentDeletedEvent`.
    2. `[Occurrence]` escuta e reatribui para fantasma.
    3. `[Occurrence]` publica `OccurrenceCreatedEvent`.
    4. `finance` escuta e gera `Transaction`.

#### 4. Dashboard e Repositórios Internos
- **O que acontece**: `DashboardServiceImpl` ainda acessa repositórios internos de outros módulos.
- **Caminho**: Criar APIs públicas de métricas em cada módulo.

---

## Próximos Passos (Plano de Ação)

1. **Change: `event-to-occurrence-rename`**
   - Renomear entidade, classes, pacotes e tabelas de `Event` para `Occurrence`.
   - Garantir que não haja conflitos com `ApplicationEventPublisher`.

2. **Change: `modulith-event-driven-decoupling`**
   - Implementar os listeners de eventos disparados pelo `registration` (já criamos o `RegistrationEventListener`, falta plugar).
   - Remover as chamadas síncronas de "volta" nos Services.

3. **Change: `modulith-finance-autonomy`**
   - Enriquecer a entidade `Transaction` para que ela não dependa de JOINS com a tabela de ocorrências para gerar resumos.

4. **Change: `modulith-strict-verification`**
   - Ativar o `verify()` e garantir o build verde.
