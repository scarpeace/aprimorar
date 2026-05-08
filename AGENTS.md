# Aprimorar — AI Agents Context

Sistema de Gerenciamento de Cursinho (preparatório ENEM + aulas particulares). Full-stack: Java backend + React frontend.

## Stack

| Camada | Tecnologia |
|--------|-----------|
| **Backend** | Java 21, Spring Boot 3.5, Hibernate (JPA), PostgreSQL, Flyway, JUnit 5, Mockito |
| **Frontend** | React 19, TypeScript, Vite, Tailwind CSS 4, DaisyUI, TanStack Query, React Hook Form, Zod, Kubb |
| **DevOps** | Docker, Docker Compose |

## Convenções

- **Nunca crie tipos/hooks de API manualmente no frontend.** Use `npm run sync` (Kubb gera a partir do OpenAPI do backend).
- **Backend:** Controller → Service → Repository. Entidades extendem `BaseEntity`. Migrações com Flyway.
- **Frontend:** Features em `src/features/`. UI comum em `src/components/ui/`. Tailwind + DaisyUI.

## Comandos

### Raiz
- `npm run dev` — backend + frontend simultâneo
- `npm run start:backend` — só backend
- `npm run start:frontend` — frontend (espera backend)

### server/
- `./mvnw spring-boot:run` — inicia
- `./mvnw test` — testes
- `./mvnw clean compile` — compila

### client/
- `npm run dev` — Vite dev server
- `npm run build` — produção
- `npm run sync` — gera hooks/tipos/Zod da API (obrigatório após mudar backend)
- `npm run lint` — lint

## OpenSpec — Fonte da Verdade do Sistema

Toda definição do sistema está em `openspec/specs/`. **Sempre leia a spec relevante antes de implementar qualquer mudança.**

### Server
- `server-architecture` — decomposição de módulos, dependências, regras estruturais. **Leia antes de qualquer mudança no backend.**

### Frontend (capabilities existentes)
- `brazilian-states-metadata`
- `employee-details-parallel-loading`
- `employee-financial-summary`
- `event-date-range-filtering`
- `events-operations-dashboard`
- `finance-transactions`
- `open-layout-dashboard`
- `responsive-student-dashboard`
- `student-details-parallel-loading`
- `student-events-filtering`
- `student-financial-summary`

### Mudanças em andamento
Se houver um change ativo em `openspec/changes/<nome>/`, leia `proposal.md` e `design.md` antes de codar. O change contém o escopo exato da mudança atual.

### Workflow
1. Leia a spec da capability que vai modificar (`openspec/specs/<capability>/spec.md`)
2. Se há change ativo, leia `proposal.md` e `design.md`
3. Implemente
4. Ao final, sincronize delta specs e arquive o change

## Arquitetura — Modulith Migration

O servidor está em migração para Spring Modulith. Consulte `openspec/specs/server-architecture/spec.md` para o estado atual e os changes em `openspec/changes/archive/` para o histórico de cada fase.
