## Context

The current `StudentDetailsPage` layout is heavily boxed, using `SectionCard` components that wrap every major section. This creates visual clutter and "box fatigue." We want to move to an "Open Layout" that uses typography and semantic sections to group information.

## Goals / Non-Goals

**Goals:**
- Transition `StudentDetailsPage` to a semantic `<section>` based layout.
- Remove internal `SectionCard` usage from `StudentInfoSection`, `StudentKPIs`, and `StudentEventsTable`.
- Implement a consistent typography-based section header pattern.
- Integrate filters into the section headers area.

**Non-Goals:**
- Changing the data fetching or business logic.
- Adding new features to the page.
- Redesigning the underlying UI components (beyond removing their card wrappers).

## Decisions

### 1. Typography-based Headers
- **Rationale**: Using `h2` and muted `p` tags for section headers provides better visual hierarchy than card headers. It makes the page feel more like a document/dashboard and less like a collection of disconnected boxes.
- **Alternative**: Keeping card headers but making them borderless. This still maintains a "boxed" feel due to the card's background/shadow.

### 2. Semantic `<section>` Tags
- **Rationale**: Using semantic HTML is better for accessibility and maintenance. It clearly defines the regions of the page.
- **Alternative**: Using generic `div` tags. Less semantic and harder to read in the code.

### 3. Integrated Filter Bar
- **Rationale**: Placing the filters in the section header area (aligned to the right or bottom) integrates them better into the page flow than a separate boxed bar.
- **Alternative**: A sticky filter bar. Possibly useful for long pages, but overkill here.

## Risks / Trade-offs

- **[Risk]** Visual regression (looks "unfinished") → **Mitigation**: Use strong typography and consistent vertical spacing (e.g., `space-y-10`) to ensure the page feels intentional.
- **[Risk]** Breaking existing components → **Mitigation**: Carefully remove the `SectionCard` wrapper and ensure any required props (like title/description) are moved to the parent page.
