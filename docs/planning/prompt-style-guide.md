# Prompt Style Guide

Use this as a quick reference for planning and implementation prompts.

## Default Rules

- Start with the goal in one sentence.
- Reference real files and task IDs when they exist.
- Keep scope small enough to finish in one focused pass.
- Prefer approved decisions over speculation.
- If a task is too large, split it before implementation.

## Useful Inputs

- Product overview: `docs/project.md`
- AI context: `docs/project-ai-context.md`
- Roadmap: `docs/planning/roadmap.md`
- Epics: `docs/planning/epics/E-XXX-*.md`

## Command Patterns

### `/feature`

Use when discovering or clarifying scope.

```text
/feature "Goal: <short goal>. In scope: <...>. Out of scope: <...>. Constraints: keep it simple and beginner-friendly."
```

### `/organize`

Use when breaking approved work into epics, stories, tasks, and order.

```text
/organize "Approved scope: <description>. Break into small executable tasks with dependencies, risks, and recommended order."
```

### `/docs`

Use when reflecting approved decisions in canonical docs.

```text
/docs "Update docs/project.md, docs/project-ai-context.md, docs/planning/roadmap.md, and the relevant epic files for <approved change>."
```

### `/do`

Use when implementing one task.

```text
/do "T-XXX - <short task name>"
```

### `/check-backend`

Use for static backend review only.

```text
/check-backend "Review T-XXX changes for bugs, architecture drift, risky edge cases, and missing tests. Do not modify code."
```

### `/check-frontend`

Use for static frontend review only.

```text
/check-frontend "Review T-XXX changes for UX issues, architecture drift, accessibility gaps, and missing tests. Do not modify code."
```

### `/tests`

Use for test suggestions only.

```text
/tests "For T-XXX, suggest practical unit, integration, and manual checks. Do not implement tests unless asked."
```

## Recommended Flow

```text
/feature "..."
/organize "..."
/docs "..."
/do "T-XXX - ..."
/check-backend "..." or /check-frontend "..."
/tests "..."
```
