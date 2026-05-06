## Why

The `StudentDetailsPage` and its components (`StudentKPIs`, `StudentEventsTable`, `StudentInfoSection`) currently lack optimal responsiveness and visual hierarchy. Tables are difficult to read on mobile, and the layout does not adapt gracefully to different screen sizes. This change aims to modernize the UI using daisyUI and Tailwind CSS to ensure a professional and accessible experience across all devices.

## What Changes

- **Responsive KPIs**: Refactor `StudentKPIs` to use a responsive grid that stacks on mobile.
- **Mobile-Optimized Events Table**: Implement a card-based view for `StudentEventsTable` on small screens and a sticky header for the desktop table.
- **Improved Information Layout**: Reorganize `StudentInfoSection` into logical groups (Student vs. Responsible) and add visual status indicators.
- **Header Refinement**: Adjust `PageLayout` to scale icon and navigation elements for mobile devices.
- **Visual Polish**: Apply staggered animations and consistent styling using daisyUI components.

## Capabilities

### New Capabilities
- `responsive-student-dashboard`: Enhanced responsive layout and component behavior for student-related information displays.

### Modified Capabilities
- `student-details-parallel-loading`: Update UI requirements to include responsive behavior and improved information grouping.
- `student-financial-summary`: Update KPI display requirements for responsive layouts.

## Impact

- `client/src/features/students/pages/StudentDetailsPage.tsx`
- `client/src/features/students/components/StudentKPIs.tsx`
- `client/src/features/students/components/StudentEventsTable.tsx`
- `client/src/features/students/components/StudentInfoSection.tsx`
- `client/src/components/layout/PageLayout.tsx`
