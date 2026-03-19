# AGENTS.md

Operational guide for coding agents working in this repository.

## Scope and Context
- Project: educational operations system (students, parents, employees, events, dashboard)
- Architecture: React SPA in `client/` consuming Spring Boot API in `server/api-aprimorar/`
- Database: PostgreSQL (local Docker Compose for development)
- Default language for user-facing text and validation messages: Portuguese

## Repository Structure

```
aprimorar/
├── AGENTS.md                          # This file - root orchestrator
├── client/                            # React 19 + TypeScript + Vite frontend
│   └── AGENTS.md                      # Frontend-specific patterns
├── server/api-aprimorar/              # Java 21 + Spring Boot 3.5 backend
│   └── AGENTS.md                      # Backend-specific patterns
└── docs/                              # Planning and documentation
```

## Quick Reference

### Frontend Commands (`client/`)
```bash
npm install      # Install dependencies
npm run dev      # Start dev server
npm run lint     # Lint all code
npm run build    # TypeScript build + production bundle
npm run preview # Preview production build
```

### Backend Commands (`server/api-aprimorar/`)
```bash
docker compose up -d db          # Start Postgres
./mvnw spring-boot:run          # Run API
./mvnw test                      # Run tests
./mvnw -Dtest=ClassName test     # Single test class
./mvnw verify                    # Full verification lifecycle
```

## Sub-AGENTS Files

Read the sub-AGENTS.md file relevant to your task:
- **`client/AGENTS.md`** - React, TypeScript, Vite patterns
- **`server/api-aprimorar/AGENTS.md`** - Spring Boot, Java 21 patterns

## Cursor/Copilot Rules Status
Checked locations:
- `.cursorrules`: not present
- `.cursor/rules/`: not present
- `.github/copilot-instructions.md`: not present

If any of these files are added later, treat them as higher-priority constraints.

## General Guidelines

### Change Discipline
- Keep diffs scoped; avoid unrelated refactors
- For new persisted fields: update migration + entity + DTO + mapper + API consumer
- Do not rename DTO fields/endpoints/contracts without full-stack alignment
- Verify changes: run tests (`./mvnw test`) and build (`npm run build`)

### Portuguese First
All user-facing text, validation messages, and error messages should be in Portuguese unless explicitly requested otherwise.
