## Purpose

Placeholder route shells keep finance, admin, and auth navigation targets available while those feature areas are rebuilt.

## Requirements

### Requirement: Finance renders a placeholder page
The system SHALL replace the active finance frontend implementation with a single finance placeholder page.

#### Scenario: User opens finance routes
- **WHEN** a user navigates to `/finance`, `/finance/expenses`, or `/finance/settlement`
- **THEN** the system renders the finance placeholder page
- **AND** the page displays the text `implementando`

### Requirement: Admin renders a placeholder page
The system SHALL replace the active admin frontend implementation with a single admin placeholder page.

#### Scenario: User opens admin routes
- **WHEN** a user navigates to `/admin` or `/admin/users`
- **THEN** the system renders the admin placeholder page
- **AND** the page displays the text `implementando`

### Requirement: Login renders an auth placeholder page
The system SHALL keep a login route as a placeholder for future authentication work.

#### Scenario: User opens login route
- **WHEN** a user navigates to `/login`
- **THEN** the system renders the login placeholder page
- **AND** the page displays the text `implementando`

### Requirement: Removed feature implementations do not remain imported
The system SHALL remove stale imports and route references to deleted finance, admin, and auth implementation files.

#### Scenario: Frontend compiles route imports
- **WHEN** the frontend imports route components from `App.tsx`
- **THEN** it imports only `FinancesPage`, `AdminPage`, and `LoginPage` for finance, admin, and auth areas
- **AND** no remaining source file imports deleted finance/admin/auth implementation modules
