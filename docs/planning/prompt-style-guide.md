# Prompt Style Guide (Quick)

Use this as a compact reference for the current command workflow.

## Core Rules

- Start with the goal in one sentence.
- Add scope and constraints.
- Reference concrete IDs and files when possible (`T-XXX`, `docs/planning/epics/E-XXX-*.md`).
- If anything is unclear, ask clarifying questions before final output.

## Commands

### `/feature`

Purpose: discover the feature and define requirements.

```text
/feature "Feature: <short description>. In scope: <...>. Out of scope: <...>. Constraints: keep it simple."
```

### `/organize`

Purpose: break approved scope into epics, tasks, subtasks, order, dependencies, and risks.

```text
/organize "Approved feature: <description>. Break into epics, tasks, subtasks, execution order, dependencies, and risk areas."
```

### `/docs`

Purpose: update planning documentation using approved decisions only.

```text
/docs "Update docs/project.md, docs/planning/project.md, docs/planning/roadmap.md, and relevant docs/planning/epics/E-XXX-*.md for this approved feature."
```

### `/do`

Purpose: implement one selected task ID.

```text
/do "T-XXX"
```

### `/check-backend`

Purpose: static backend review only (no code changes).

```text
/check-backend "Review T-XXX backend changes. Detect bugs, architecture issues, code smells, and suggest tests only."
```

### `/check-frontend`

Purpose: static frontend review only (no code changes).

```text
/check-frontend "Review T-XXX frontend changes. Detect UI/UX issues, architecture issues, code smells, and suggest tests only."
```

### `/tests`

Purpose: suggest test coverage improvements only.

```text
/tests "For T-XXX, suggest unit tests, integration tests, and edge cases. Do not implement without explicit approval."
```

## Suggested Flow

```text
/feature "..."
/organize "..."
/docs "..."
/do "T-XXX"
/check-backend "..."  or  /check-frontend "..."
/tests "..."
```
