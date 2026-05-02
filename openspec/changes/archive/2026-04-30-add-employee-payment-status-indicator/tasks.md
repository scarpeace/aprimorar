## 1. Create Payment Status Indicator Component

- [ ] 1.1 Create `src/components/ui/payment-status-indicator.tsx` component
- [ ] 1.2 Define types for payment status (pending, paid, late, archived)
- [ ] 1.3 Implement indicator logic using DaisyUI indicator classes
- [ ] 1.4 Add tooltips for accessibility showing full status text

## 2. Integrate Indicator into EmployeesTable

- [ ] 2.1 Import `useGetEmployeeMonthlySummary` hook in EmployeesTable
- [ ] 2.2 Fetch payment summary for each employee in the table
- [ ] 2.3 Replace existing badge with PaymentStatusIndicator component
- [ ] 2.4 Handle archived employees (show archived badge, not payment status)

## 3. Verify Implementation

- [ ] 3.1 Test yellow indicator shows when employee has unpaid amount
- [ ] 3.2 Test green indicator shows when employee is fully paid and current
- [ ] 3.3 Test red indicator shows when last payment is >30 days ago
- [ ] 3.4 Test archived employees show archived badge