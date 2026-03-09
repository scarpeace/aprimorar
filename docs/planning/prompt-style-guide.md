# Prompt Style Guide

Use this guide when updating planning docs or asking an agent to do structured project work.

## Canonical files

- `AGENTS.md`: repo, product, workflow, and coding guidance.
- `docs/planning/roadmap.md`: phases and epic index.
- `docs/planning/epics/E-XXX-*.md`: epic execution docs.
- `docs/planning/epics/E-000-template.md`: template for new epics.
- `docs/planning/diagrams.md`: shared Mermaid diagrams.

## Typical command flow

1. `/feature` to clarify scope and constraints.
2. `/organize` to split approved work into epics, tasks, and dependencies.
3. `/docs` to update canonical docs for approved decisions.
4. `/do <T-XXX>` to implement one focused task.
5. `/check-backend` or `/check-frontend` to review quality.
6. `/tests` to identify missing coverage.

## Writing rules

- Keep docs factual, short, and current.
- Never invent completed work.
- Only document approved decisions.
- Keep IDs stable once created.
- Prefer exact commands over generic verification notes.
- Prefer links to canonical files over repeated context dumps.

## Doc update checklist

When a feature changes behavior, review these files:
- `AGENTS.md`
- `docs/planning/roadmap.md`
- relevant `docs/planning/epics/E-XXX-*.md`
- `docs/planning/diagrams.md` if flows or architecture changed

Update only what changed:
- commands
- API contracts
- architecture or workflow
- task/story status
- verification steps

## Example `/docs` prompt

`/docs "Update AGENTS.md, docs/planning/roadmap.md, docs/planning/diagrams.md, and the relevant epic files for this approved change. Keep updates concise and remove stale references."`
