# Capability: Responsive Student Dashboard

## Purpose
Ensure the student dashboard maintains high usability and accessibility across all device types and screen sizes.

## Requirements

### Requirement: Responsive Dashboard Layout
The `StudentDetailsPage` SHALL adapt its layout structure based on the device's screen size to maintain usability and readability.

#### Scenario: Mobile view layout
- **WHEN** the viewport width is less than `768px` (md breakpoint)
- **THEN** major sections (Info, KPIs, Events) SHALL stack vertically with adjusted padding.

### Requirement: Mobile Card View for Events
The system SHALL display student events as interactive cards on mobile devices instead of a horizontal table.

#### Scenario: Viewing events on small screens
- **WHEN** the viewport width is less than `768px`
- **THEN** the `StudentEventsTable` component SHALL render each event as a card containing the collaborator name, date, time, content, price, and action buttons.

### Requirement: Sticky Table Header
The student events table SHALL have a sticky header on desktop to maintain context during vertical scrolling.

#### Scenario: Scrolling through many events
- **WHEN** the user scrolls down the `StudentEventsTable` on desktop
- **THEN** the table header row SHALL remain fixed at the top of the table container.

### Requirement: Status Visual Indicators
Student status (Active/Archived) SHALL be displayed using visually distinct badges rather than plain text.

#### Scenario: Viewing an active student
- **WHEN** a student is not archived
- **THEN** a green daisyUI "success" badge SHALL be displayed next to their name or in the summary.

#### Scenario: Viewing an archived student
- **WHEN** a student is archived
- **THEN** a gray/neutral daisyUI badge SHALL be displayed indicating the archived status.
