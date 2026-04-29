# Phase 02 Research: Event Payment Workflow

## Objectives
Analyze the technical requirements for recording payment dates and implementing status-based filtering for employee events.

## Current State Analysis

### Backend
- **Entity**: `Event.java` currently has `employeePaid` and `studentCharged` booleans, but lacks fields for recording *when* these actions occurred.
- **Service**: `EventService.settleEmployeePayment` and `settleStudentCharge` only update the boolean flags.
- **Specifications**: `EventSpecifications.java` does not have a filter for payment status.
- **Controller**: `getEventsByEmployeeId` only filters by `studentName`.

### Frontend
- **EmployeeDetailPage**: Modularized.
- **EmployeeEventsTable**: Currently displays a read-only list with a "Repasse" column and a status badge. No interactive payment actions or status filters.

## Gap Analysis & Technical Decisions

### 1. Recording Payment Dates (Requirement TAB-04)
- **Decision**: Add `employeePaymentDate` and `studentChargeDate` (Instant) to the `Event` entity.
- **Implementation**: The `EventService` will use the injected `Clock` to set these dates when the settlement methods are called with `true`. If called with `false` (unsettling), the dates will be cleared (set to `null`).

### 2. Status Filtering (Requirement TAB-03)
- **Decision**: Introduce a `hidePaid` boolean parameter in the API.
- **Implementation**: 
    - Add `withEmployeePaid(Boolean paid)` to `EventSpecifications`.
    - Update `EventController` to accept `hidePaid`. If true, the specification `withEmployeePaid(false)` will be applied.

### 3. Integrated Filtering (Requirement TAB-05)
- **Implementation**: The existing `Specification` chain in `EventService` will combine `withEmployeeId`, `withStudentNameIgnoreCase`, and the new `withEmployeePaid` (conditional on `hidePaid`).

### 4. UI/UX for Payment
- **Component**: `EmployeeEventsTable.tsx`.
- **Additions**:
    - **Toggle**: A `ToggleSwitch` or simple checkbox in the header titled "Ocultar Pagos".
    - **Actions**: A button in the table row (e.g., a "Money" or "Check" icon) that triggers the `settleEmployeePayment` mutation.
    - **Visual Feedback**: The status badge should remain, but we might show the payment date if available for paid events.

## Risks & Mitigations
- **Data Consistency**: Ensure that `employeePaymentDate` is only set when `employeePaid` is true.
- **Kubb Synchronization**: Any backend change to DTOs or Controller signatures requires running `npm run sync` on the client.
