# Proximo passo

Migrar as operacoes restantes de integracao entre `appointment` e `finance` para eventos do Spring Modulith, removendo as chamadas diretas que ainda ficaram em `AppointmentServiceImpl`.

## Objetivo

Substituir estas chamadas diretas por eventos publicados pelo modulo `appointment` e consumidos pelo modulo `finance`:

- `transactionService.createAppointmentTransactions(...)`
- `transactionService.syncAppointmentTransactions(...)`
- `transactionService.deleteAppointmentTransactions(...)`

## Escopo

1. Criar eventos publicos no pacote `aprimorar.appointment.api.event`:
   - `AppointmentCreatedEvent`
   - `AppointmentUpdatedEvent`
   - `AppointmentDeletedEvent`
2. Publicar esses eventos em `AppointmentServiceImpl` nos fluxos:
   - `createAppointment`
   - `updateAppointment`
   - `deleteAppointment`
3. Expandir `FinanceEventReactor` para reagir aos novos eventos.
4. Manter `TransactionService` como detalhe interno do modulo `finance` para sincronizacao das transacoes.
5. Validar com `./mvnw clean compile`.

## Decisoes

- Os eventos devem carregar apenas os dados necessarios para o `finance` reagir, sem expor entidades.
- `deleteAppointment` deve publicar o evento antes de remover definitivamente o aggregate, preservando o `appointmentId` para o listener.
- A migracao deve ser incremental: primeiro `appointment -> finance`, sem alterar contrato HTTP nem frontend.

## Criterios de pronto

- `AppointmentServiceImpl` nao chama mais `transactionService` diretamente.
- `FinanceEventReactor` cobre create, update, delete, student charge e employee payment.
- O backend compila sem erros.

## Risco principal

Em `deleteAppointment`, o evento precisa ser publicado em um ponto em que o `appointmentId` ainda esteja disponivel e o listener consiga remover as transacoes sem depender da entidade apagada.
