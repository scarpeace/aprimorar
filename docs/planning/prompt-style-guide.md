# OpenCode Prompt Style Guide

Use this guide to write clearer prompts for local OpenCode commands.

## Core Principles

- Start with intent and scope in one sentence.
- State constraints explicitly: avoid overengineering, keep beginner-friendly, keep changes small.
- Reference concrete artifacts when possible: task IDs (`T-xxx`), file paths, endpoints, screens.
- Ask for recommended defaults when unsure.
- Request a short explanation when complexity increases.

## Prompt Patterns by Command

### `/scan`

Use when you want read-only understanding before planning or coding.

Template:

```text
/scan "Goal: <what I want>. Focus on current implementation patterns. No edits. Show relevant file paths and where a beginner should start reading."
```

### `/plan`

Use when work is not trivial.

Template:

```text
/plan "Feature: T-015. MVP only. Avoid overengineering. Keep tasks small, verifiable, and scoped to one focused session. Always ask 3-8 objective questions with recommended defaults. If complexity increases, explain what will be implemented and why in simple terms."
```

### `/do`

Use to implement one task by ID.

Template:

```text
/do "T-xxx. Implement only this task. Keep code clean and minimal. If task is too large or unclear, stop, raise a complexity alert, and propose a split with next commands."
```

### `/review`

Use after implementation before updating plan/committing.

Template:

```text
/review "Review current diff for code quality, security basics, and performance basics. Return prioritized fixes (P0/P1/P2) with beginner-friendly rationale."
```

### `/refactor-review`

Use to review one or more file/folder paths and get refactoring suggestions.

Template:

```text
/refactor-review "<path1>, <path2>"
```

Examples:

```text
/refactor-review "server/api-aprimorar/src/main/java/com/aprimorar/api/service"
/refactor-review "client/src/pages/EventsPage.tsx"
/refactor-review "client/src/pages/EventsPage.tsx, client/src/components/EventForm.tsx"
```

Notes:
- The agent may ask numbered clarification questions before reviewing.
- It always creates a report under `docs/refactor/`.
- The report contains an ordered (numbered) list of suggestions grouped by priority.

### `/refactor-review-quick`

Use to review exactly one file or folder path, but keep chat output compact.

Template:

```text
/refactor-review-quick "<path>"
```

Examples:

```text
/refactor-review-quick "server/api-aprimorar/src/main/java/com/aprimorar/api/service"
/refactor-review-quick "client/src/pages/EventsPage.tsx"
```

Notes:
- Returns top 5 improvements at a time.
- If more exist, it asks: `Do you want 5 more?`
- This quick mode never creates files.

### `/update-plan`

Use to keep planning docs synchronized.

Template:

```text
/update-plan "T-xxx. Update task status and append notes. If a complexity alert exists, mark task BLOCKED and register split follow-up tasks under the same story."
```

### `/next`

Use to choose the next task after finishing one.

Template:

```text
/next "Recommend the next best task based on roadmap, dependencies, and recent progress."
```

## Prompt Checklist

Before sending a prompt, quickly include:

- Goal (what outcome you want)
- In scope / out of scope
- Constraints (clean code, no overengineering)
- Verification expectation (what proves done)
- Learning preference (explain complex parts simply)

## Example

```text
/plan "Add expense categories to the expenses module.
In scope: backend CRUD + dropdown in expense form.
Out of scope: reports, exports, and permissions.
Constraints: keep it simple, beginner-friendly, and no overengineering.
Ask 3-8 questions with defaults and explain non-trivial choices."
```

## Flow Example (End-to-End)

Use this when starting a new feature from scratch.

```text
# 1) Understand current code (optional, recommended)
/scan "Goal: add expense categories. Show current backend and frontend patterns only."

# 2) Create a plan (always asks 3-8 questions)
/plan "Feature: expense categories MVP.
In scope: backend CRUD + frontend select field.
Out of scope: reports and permissions.
Constraints: avoid overengineering, keep code beginner-friendly."

# 3) Create branch from main
git checkout -b feat/expense-categories main

# 4) Implement one task
/do "T-021"

# 5) Run checks on demand
/check-backend
/check-frontend

# 6) Review and update planning docs
/review
/update-plan "T-021"

# Optional: path-based refactor review for one area
/refactor-review "server/api-aprimorar/src/main/java/com/aprimorar/api/service"

# Optional: quick refactor review in batches of 5
/refactor-review-quick "server/api-aprimorar/src/main/java/com/aprimorar/api/service"

# 7) Commit with plain message
git status
git add <paths>
git commit -m "Implement T-021: add expense category entity and endpoint"

# 8) Pick next task
/next
```

If `/do` returns a complexity alert, use:

```text
/update-plan "T-021"
/plan "Refine task T-021 into smaller tasks"
```
