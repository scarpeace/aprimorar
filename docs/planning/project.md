# Planning Hub

This folder is the canonical planning area for incremental delivery.

## Canonical Files

- Product overview: `docs/project.md`
- AI scan context: `docs/project-ai-context.md`
- Planning hub: `docs/planning/project.md`
- Roadmap and epic index: `docs/planning/roadmap.md`
- Epic template: `docs/planning/epics/E-000-template.md`
- Active epics: `docs/planning/epics/E-XXX-*.md`
- Prompt quick reference: `docs/planning/prompt-style-guide.md`
- Frontend conventions: `docs/refactor/frontend-conventions.md`

## Working Rules

- Keep docs beginner-friendly and concrete.
- Prefer small executable tasks over broad vague tasks.
- Keep `docs/project.md` concise and human-readable.
- Put high-context implementation details in `docs/project-ai-context.md`.
- Only mark work done when code or docs actually changed.
- Keep IDs stable once published.

## Delivery Flow

1. Clarify the feature or issue.
2. Update planning docs if scope or priorities changed.
3. Implement one task at a time.
4. Review and verify the slice.
5. Reflect any approved decisions back into docs.

## Practical Rule of Thumb

If a task feels too big to finish confidently in one focused implementation pass, split it again.
