# Aprimorar Agent Notes (global)

## Escopo deste arquivo
- Este AGENTS raiz define apenas regras globais do repositorio.
- Regras tecnicas de implementacao ficam nos arquivos locais: `server/AGENTS.md` e `client/AGENTS.md`.

## Layout e execucao
- As raizes reais da aplicacao sao `server/` e `client/`.
- Scripts de dev no `package.json` da raiz estao defasados para backend/frontend (ex.: `start:backend` aponta para `server/api-aprimorar`); execute comandos dentro de `server/` e `client/`.

## Baseline tecnico rapido
- Backend: Java 21, Spring Boot 3.5.x, Spring Modulith, PostgreSQL, Flyway.
- Frontend: React 19, Vite 7, TypeScript, Tailwind 4, DaisyUI, TanStack Query, Kubb.
- Modulos backend atuais: `auth`, `pessoas`, `atendimentos`, `financeiro`, `shared`.

## Regras globais obrigatorias
- Nao editar manualmente codigo gerado em `client/src/kubb/`.
- Mudanca de contrato backend que afeta frontend exige regeneracao via `npm run sync` (backend com `/v3/api-docs` disponivel).
- Dependencias entre modulos backend devem respeitar `package-info.java` e contratos `...api`.
- Se alterar fronteira de modulo backend, rodar `./mvnw test -Dtest=ModuleVerificationTest` em `server/`.

## Comandos de verificacao padrao
- Backend (em `server/`): `./mvnw clean compile`, `./mvnw test`.
- Frontend (em `client/`): `npm run lint`, `npm run build`.

## Sonar (quando aplicavel)
- Scripts Sonar usam `SONAR_TOKEN` do `.env.local` na raiz.
- Infra local Sonar sobe a partir de `server/` com `docker compose up -d db sonar_db sonarqube`.
