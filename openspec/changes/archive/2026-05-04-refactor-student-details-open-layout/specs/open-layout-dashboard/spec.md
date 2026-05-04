## ADDED Requirements

### Requirement: Semantic Section Grouping
The dashboard SHALL use semantic `<section>` tags to group related information instead of visual card containers.

#### Scenario: Page layout structure
- **WHEN** the `StudentDetailsPage` is rendered
- **THEN** major areas (Profile, KPIs, Events) are wrapped in `<section>` tags with vertical spacing between them.

### Requirement: Explicit Section Headers
Each section SHALL have an explicit header containing a title and description, rendered using typography instead of card headers.

#### Scenario: Section header visibility
- **WHEN** a section is rendered
- **THEN** it displays a bold title and a muted description at the top of the section area.
