# Planning Project Hub

This file is the planning hub for the guided workflow.

## Canonical Planning Files

- Product and domain context: `docs/project.md`
- Roadmap and epic index: `docs/planning/roadmap.md`
- Epic plans: `docs/planning/epics/E-XXX-*.md`
- Epic template: `docs/planning/epics/E-000-template.md`
- Prompt quick reference: `docs/planning/prompt-style-guide.md`

## Workflow

Feature Discovery -> Planning -> Implementation -> Review -> Documentation

## Command Pipeline

1. `/feature` for discovery and requirement clarification.
2. `/organize` to break approved scope into epics, tasks, subtasks, order, and dependencies.
3. `/docs` to reflect approved decisions in docs.
4. `/do <T-XXX>` to implement one selected task.
5. `/check-backend` or `/check-frontend` for static quality review.
6. `/tests` to suggest test coverage improvements (implementation only with explicit approval).

## Rules

- Never invent completed work.
- Only document approved decisions.
- Keep epic structures aligned to the template.
- Keep IDs stable in epic files.
