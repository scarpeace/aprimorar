## Why

The current `StudentDetailsPage` uses nested `SectionCard` components for every section, leading to "box fatigue" and a cramped visual layout, especially on mobile. This refactor aims to transition to a cleaner "Open Layout" structure that uses semantic HTML sections and better integrates filters and headers.

## What Changes

- **Layout Refactor**: Replace nested `SectionCard` usage in `StudentDetailsPage` with semantic `<section>` tags and explicit headers.
- **Component Update**: Modify `StudentInfoSection`, `StudentKPIs`, and `StudentEventsTable` to remove internal `SectionCard` wrappers, allowing the parent page to control the layout container.
- **Header Integration**: Move titles and descriptions from card headers to section headers in the main page.
- **Filter Integration**: Integrate date filters more naturally into the section header area.

## Capabilities

### New Capabilities
- `open-layout-dashboard`: A layout pattern that prioritizes semantic sections and typography over boxed containers.

### Modified Capabilities
- `responsive-student-dashboard`: Update requirements to move away from card-based grouping to semantic section-based grouping.
- `student-financial-summary`: Update requirements to allow KPIs to be rendered without a card wrapper.

## Impact

- `client/src/features/students/pages/StudentDetailsPage.tsx`
- `client/src/features/students/components/StudentInfoSection.tsx`
- `client/src/features/students/components/StudentKPIs.tsx`
- `client/src/features/students/components/StudentEventsTable.tsx`
- `client/src/components/ui/section-card.tsx` (potential cleanup if unused)
