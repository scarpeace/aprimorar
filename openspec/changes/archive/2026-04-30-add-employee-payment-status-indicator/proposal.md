## Why

Currently, the EmployeesTable shows only the archival status (Ativo/Arquivado) in the Status column. There's no visual indication of whether an employee has pending payments to receive, is fully paid, or has overdue payments. This makes it difficult for administrators to quickly identify employees who need payment attention at a glance.

## What Changes

- Replace the current status badge in EmployeesTable with a payment status indicator
- Add a new `PaymentStatusIndicator` component that displays a colored dot based on payment state
- The indicator shows three states: pending (yellow), paid (green), and late (red)
- The table row remains fully clickable for navigation to employee details

## Capabilities

### New Capabilities
- `employee-payment-status`: Add a payment status indicator to the employees table showing pending, paid, and late states based on the employee's payment summary

### Modified Capabilities
- (none)

## Impact

- `client/src/features/employees/components/EmployeesTable.tsx`: Add payment status indicator to the Status column
- `client/src/components/ui/payment-status-indicator.tsx`: New component for the status dot (if created as reusable)
- Uses existing `useGetEmployeeMonthlySummary` hook to get payment data per employee