# Aprimorar

Sistema de gestão para operação do Aprimorar: cadastro de alunos, responsáveis e colaboradores, agendamento de atendimentos, autenticação e acompanhamento operacional.

## Stack

- Backend: Java 21, Spring Boot 3.5, Spring Security, Spring Data JPA, Flyway, Spring Modulith
- Frontend: Next.js 16, React 19, React Query, React Hook Form, Zod, DaisyUI
- Banco: PostgreSQL 15
- Contrato: OpenAPI + Kubb

## Estrutura

```text
.
├── server/    # backend Spring Boot
├── client/    # frontend Next.js
└── AGENTS.md  # documentação operacional viva
```

## Pré-requisitos

- Java 21
- Node.js 20+
- Docker + Docker Compose

## Variáveis de ambiente

O backend lê variáveis destes arquivos, nessa ordem:

- `./.env`
- `./.env.local`
- `./server/.env`
- `./server/.env.local`

### Backend

Variáveis mínimas:

```env
APP_ADMIN_USERNAME=admin@aprimorar.com
APP_ADMIN_PASSWORD=admin123
APRIMORAR_ADMIN_ID=b3a092e0-fc48-43ff-8b35-149eb81a033f
APRIMORAR_GHOST_STUDENT_ID=00000000-0000-4000-8000-000000000002
APRIMORAR_GHOST_COLABORADOR_ID=00000000-0000-4000-8000-000000000001
```

Notas:

- `APP_ADMIN_PASSWORD` é obrigatória
- os IDs ghost devem bater com a migration inicial
- no profile `dev`, o banco local já aponta para `localhost:5432/aprimorar` com `myuser/mypassword`

Arquivos de exemplo:

- [/.env.example](/home/scarpellini/Documents/Projetos/aprimorar/.env.example)
- [server/.env.example](/home/scarpellini/Documents/Projetos/aprimorar/server/.env.example)

### Frontend

Se precisar sobrescrever a URL da API:

```env
API_URL=http://localhost:8080
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Arquivo de exemplo:

- [client/.env.example](/home/scarpellini/Documents/Projetos/aprimorar/client/.env.example)

### SonarQube local

Opcional:

```env
SONAR_TOKEN=seu_token
```

## Como rodar localmente

### 1. Subir o banco

Dentro de `server/`:

```bash
docker compose up -d db
```

Banco local:

- host: `localhost`
- porta: `5432`
- database: `aprimorar`
- user: `myuser`
- password: `mypassword`

### 2. Rodar o backend

Dentro de `server/`:

```bash
./mvnw spring-boot:run
```

Pontos úteis:

- profile padrão: `dev`
- Flyway roda automaticamente
- `data.sql` roda no profile `dev`
- o usuário admin é criado/sincronizado na inicialização

Endpoints úteis:

- API: `http://localhost:8080`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3. Rodar o frontend

Dentro de `client/`:

```bash
npm install
npm run dev
```

Frontend local:

- `http://localhost:3000`

## Acesso inicial

Se `APP_ADMIN_PASSWORD` estiver configurada, o backend garante a existência do usuário admin:

- login: `admin@aprimorar.com`
- senha: valor de `APP_ADMIN_PASSWORD`

## Contrato backend -> frontend

Quando um endpoint, DTO ou enum mudar no backend:

1. suba o backend
2. rode em `client/`:

```bash
npm run sync
```

O Kubb lê:

- `http://localhost:8080/v3/api-docs`

E gera código em:

- `src/lib/api/generated/`

Regra:

- não editar manualmente código gerado

## Comandos úteis

### Backend

Dentro de `server/`:

```bash
./mvnw clean compile
./mvnw test
./mvnw test -Dtest=ModuleVerificationTest
```

O `ModuleVerificationTest` também regenera a documentação do Spring Modulith em:

- `server/src/main/resources/docs`

### Frontend

Dentro de `client/`:

```bash
npm run lint
npm run build
```

## Qualidade local

Referência prática:

- backend: compile + testes
- frontend: lint + build
- contrato: `npm run sync` após mudanças de API

SonarQube local:

Dentro de `server/`:

```bash
docker compose up -d db sonar_db sonarqube
```

URLs/portas:

- SonarQube: `http://localhost:9000`
- banco do Sonar: `localhost:5433`

## Convenções importantes

- backend sanitiza e normaliza os dados; o frontend pode ser mais simples
- migrations são a fonte de verdade do schema
- atendimento usa relações JPA com aluno e colaborador
- ações destrutivas de aluno/colaborador respeitam validações de negócio antes de excluir
- componentes de formulário são padronizados em `src/components/ui/forms/`
- detalhes de aluno e colaborador seguem o mesmo layout base

## Problemas comuns

### O frontend quebrou depois de mudar o backend

Provavelmente o contrato está desatualizado. Rode `npm run sync`.

### O admin não foi criado

Verifique principalmente:

- `APP_ADMIN_PASSWORD`
- `APP_ADMIN_USERNAME`

### O backend não sobe por erro de usuário ghost

Verifique:

- `APRIMORAR_GHOST_STUDENT_ID`
- `APRIMORAR_GHOST_COLABORADOR_ID`

### Não sei onde está o contexto técnico mais completo

Veja [AGENTS.md](/home/scarpellini/Documents/Projetos/aprimorar/AGENTS.md).
