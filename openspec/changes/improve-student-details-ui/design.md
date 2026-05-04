## Context

The current `StudentDetailsPage` and its sub-components are functional but have several UI/UX shortcomings, particularly on mobile. The layout is static, the events table overflows on small screens, and the information hierarchy is flat. We want to leverage daisyUI (which is already present in the project via classes like `card`, `alert`, `table-zebra`) and Tailwind CSS to improve responsiveness and visual polish.

## Goals / Non-Goals

**Goals:**
- Implement a responsive grid for KPIs and information sections.
- Create a mobile-first "Card View" for student events.
- Improve visual hierarchy using daisyUI components (badges, cards, accents).
- Add staggered animations for a premium feel.
- Ensure the header (`PageLayout`) scales correctly on small screens.

**Non-Goals:**
- Changing the underlying data fetching logic or API endpoints.
- Modifying the edit student form logic (except for styling).
- Adding new features or data fields to the student profile.

## Decisions

### 1. Priority: daisyUI over Tailwind Utility Classes
- **Rationale**: The project already uses daisyUI. Using daisyUI components (e.g., `badge-success`, `stats`, `collapse`) ensures visual consistency and reduces the amount of custom Tailwind boilerplate.
- **Alternative**: Pure Tailwind. While flexible, it leads to longer class strings and inconsistent component styles across the app.

### 2. Dual-View for Events (Table vs. Card)
- **Rationale**: Tables are inherently bad on mobile. Hiding the table and showing a list of cards on small screens (`max-md`) provides a much better touch-friendly experience.
- **Alternative**: Responsive table (horizontal scroll). It's a poor UX as users lose context of the columns.

### 3. Staggered Animations using Tailwind/CSS
- **Rationale**: Using simple `animate-fade-up` with varying `animation-delay` (via Tailwind) adds a sense of flow without the overhead of heavy animation libraries.
- **Alternative**: Framer Motion. Powerful but possibly overkill for these simple entry effects.

### 4. Grouped Information in `StudentInfoSection`
- **Rationale**: Splitting the 11+ fields into "Dados do Aluno" and "Dados do Responsável" headers within the grid makes the data much easier to scan.
- **Alternative**: Tabs. Adds extra clicks to see information that can fit on one screen.

## Risks / Trade-offs

- **[Risk]** CSS utility conflict → **Mitigation**: Stick to standard daisyUI classes and use Tailwind for layout/spacing only.
- **[Risk]** Increased code complexity in `StudentEventsTable` → **Mitigation**: Extract the mobile card view into a small sub-component within the same file or a sibling file.
- **[Risk]** Performance impact of animations → **Mitigation**: Use CSS-only animations which are GPU-accelerated.
