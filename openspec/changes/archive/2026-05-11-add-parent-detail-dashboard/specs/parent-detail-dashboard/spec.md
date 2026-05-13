## ADDED Requirements

### Requirement: Parent detail page mirrors student detail structure
The system SHALL present responsible parent details using the same composition pattern as the student detail page: parent information section, linked student table, and edit modal form.

#### Scenario: Viewing a parent detail page
- **WHEN** a user opens `/parents/{parentId}`
- **THEN** the page displays a parent detail layout with parent information before linked student records
- **AND** the page provides a way to open the parent edit form without leaving the detail page

### Requirement: Parent information section displays parent data and actions
The system SHALL provide a `ParentInfoSection` component that loads a responsible parent by ID and displays their core registration data, status, and available actions.

#### Scenario: Parent data loads successfully
- **WHEN** `ParentInfoSection` receives a valid parent ID
- **THEN** it displays the parent name, CPF, email, contact, creation date, and active/inactive status
- **AND** it displays edit, archive/unarchive, and delete actions consistent with the parent feature

#### Scenario: Parent data is loading or fails
- **WHEN** the parent lookup is pending
- **THEN** the section displays a loading state
- **WHEN** the parent lookup fails
- **THEN** the section displays an error state

### Requirement: Parent linked students table shows students for the responsible parent
The system SHALL provide a `ParentStudentsTable` component that loads and displays students linked to the selected responsible parent.

#### Scenario: Linked students load successfully
- **WHEN** `ParentStudentsTable` receives a valid parent ID
- **THEN** it loads paginated students for that parent
- **AND** it displays each linked student with key identifying information and navigation to the student detail page

#### Scenario: Linked students table state changes
- **WHEN** linked students are loading
- **THEN** the table displays a loading state
- **WHEN** linked students fail to load
- **THEN** the table displays an error state
- **WHEN** the user changes pages
- **THEN** the component requests linked students for the selected page

### Requirement: Parent edit form works from the detail modal
The system SHALL allow editing the responsible parent from the detail page modal using the existing parent form validation and mutation behavior.

#### Scenario: Editing a parent from details
- **WHEN** the user opens the edit modal from the parent detail page
- **THEN** the form is pre-filled with the current parent data
- **WHEN** the update succeeds
- **THEN** the modal closes and parent detail data is refreshed
