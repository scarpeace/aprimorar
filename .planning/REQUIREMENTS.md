# Requirements: v1.1 Gestão Financeira e Detalhes do Colaborador

## Active Requirements

### Tabela e Filtros
- [ ] **TAB-03**: Alternador (switch) para ocultar eventos já marcados como pagos na tabela de eventos do colaborador.
- [ ] **TAB-04**: Registrar a data exata do pagamento ao marcar um evento como pago.
- [ ] **TAB-05**: Garantir que o filtro de busca por aluno funcione em conjunto com o filtro de status de pagamento.

### KPIs e Visão Financeira
- [ ] **KPI-01**: Painel de KPIs na lateral: Total pago, Total a pagar, Total de eventos no mês.
- [ ] **KPI-02**: Alternador no painel de KPIs para alternar a visualização entre "Mensal" (mês atual) e "Histórico" (todos os tempos).

## Validados
- ✓ **TAB-01**: Visualizar tabela exclusiva de eventos do funcionário, ordenada por data decrescente.
- ✓ **TAB-02**: Barra de busca para filtrar eventos pelo nome do aluno.
- ✓ **MOD-01**: Refatorar EmployeeDetailPage em componentes modulares (`EmployeeKPIs`, `EmployeeEventsTable`).

## Future Requirements (Deferred)
- **Pagamento em Lote (Bulk Payment)**: Selecionar vários eventos e marcá-los como pagos de uma só vez.
- **Geração de Extrato em PDF**: Exportar um relatório formal dos pagamentos.

## Out of Scope
- **Portal do Professor**: Foco inicial no fluxo da secretária/administrador.
- **Integração com Gateways de Pagamento**: Registro de transações permanece manual no sistema.

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| TAB-01 | Phase 01 | ✅ Green |
| TAB-02 | Phase 01 | ✅ Green |
| MOD-01 | Phase 01 | ✅ Green |
| TAB-03 | Phase 02 | ⬜ Pending |
| TAB-04 | Phase 02 | ⬜ Pending |
| TAB-05 | Phase 02 | ⬜ Pending |
| KPI-01 | Phase 03 | ⬜ Pending |
| KPI-02 | Phase 03 | ⬜ Pending |
