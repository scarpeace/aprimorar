# Phase 01: Employee Events Tracking - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-28
**Phase:** 01-Employee Events Tracking
**Areas discussed:** Search Scope, Search Behavior, Search Bar Placement, Empty States

---

## Search Scope

| Option | Description | Selected |
|--------|-------------|----------|
| Student name only | Matches requirement TAB-02 strictly | ✓ |
| Student name AND event content | Broader search, might need backend changes | |

**User's choice:** Student name only

---

## Search Behavior

| Option | Description | Selected |
|--------|-------------|----------|
| Auto-search (debounced) | Filters as you type (matches EmployeesPage.tsx pattern) | ✓ |
| Submit button | Requires pressing Enter or clicking a button | |

**User's choice:** Auto-search (debounced)

---

## Search Bar Placement

| Option | Description | Selected |
|--------|-------------|----------|
| Inside SectionCard header | Keeps the UI compact and aligns with other table headers | |
| Above the table | More prominent, matches standalone search pages | ✓ |

**User's choice:** Above the table

---

## Empty States

| Option | Description | Selected |
|--------|-------------|----------|
| Show an EmptyCard illustration | More prominent, reusable component | |
| Simple text inside table body | Keeps table headers visible, simple text | ✓ |

**User's choice:** Simple text inside table body
