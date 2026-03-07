# Frontend Conventions

Keep the frontend easy to follow for a React beginner.

## Feature Structure

- Use `pages/` for route-level screens.
- Use `components/` for feature-local reusable UI.
- Use `utils/` for pure helpers, constants, and mappers.
- Keep imports feature-local when the code is only reused inside that feature.

## CSS Ownership

- Every page gets its own `.module.css` file beside the page.
- Every extracted component gets its own `.module.css` file beside the component.
- Page CSS should only own page layout.
- Component CSS should only own that component's presentation.
- Avoid importing page CSS inside child components.

## Extraction Rules

- Extract a shared component only when duplication is obvious in 2 or more places.
- Keep data fetching and mutation logic inside the page unless moving it clearly reduces confusion.
- Prefer small primitives like `FormField` or `DetailField` over highly generic frameworks.
- If a refactor makes a file harder to understand, it is too abstract.

## Loading, Error, and Empty States

- Use shared page-state components for repeated loading and error patterns.
- Prefer explicit retry actions over full page reloads when practical.
- Keep empty-state copy actionable and route the primary button correctly.

## Forms

- Reuse shared primitives for label, error, actions, and select styling.
- Keep form-specific validation and submit logic inside the feature form component.
- Avoid building a configuration-driven form system.

## Lists and Detail Pages

- Keep list query state local to the page.
- Reuse shared UI pieces for pagination, headers, and repeated detail fields.
- Prefer clear composition over generic table abstractions.
