# Aprimorar — AGENTS.md

Documento operacional do repositório. Mantenha este arquivo atualizado quando a estrutura, o fluxo de trabalho ou os padrões principais mudarem.

## Estrutura atual

- `server/`: backend Spring Boot
- `client/`: frontend Next.js

## Regra básica

- backend: trabalhar em `server/`
- frontend: trabalhar em `client/`
- contrato da API: backend primeiro, frontend depois

## Backend

### Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway
- Spring Modulith

### Convenções

- controller recebe e delega
- service concentra regra de negócio
- repository faz acesso a dados
- DTO na borda HTTP
- entidade JPA não vaza para API
- mudança de schema sempre via Flyway
- dependência entre módulos só pelos contratos permitidos

### Comandos úteis

Dentro de `server/`:

```bash
./mvnw spring-boot:run
./mvnw clean compile
./mvnw test
./mvnw test -Dtest=ModuleVerificationTest
```

### Observações do domínio

- atendimento usa relações JPA com aluno e colaborador
- transações/financeiro antigo saíram do fluxo principal
- deleções de aluno e colaborador respeitam validações de negócio antes de excluir
- existem registros ghost/sistema; regras de exclusão e contagem precisam respeitá-los

## Frontend

### Stack

- Next.js App Router
- React Query
- React Hook Form
- Zod
- DaisyUI
- Kubb

### Convenções

- `page.tsx` compõe a página
- componentes cuidam da UI e interação local
- hooks de mutação concentram invalidation/toast/efeitos
- usar tipos e hooks gerados sempre que possível
- não editar manualmente `src/lib/api/generated/`
- formulários usam RHF + `FormProvider` quando distribuídos
- inputs reutilizáveis ficam em `src/components/ui/forms/`
- backend continua responsável por sanitização e normalização final

### Padrões visuais já consolidados

- detalhes de aluno e colaborador:
  - cabeçalho simples
  - bloco de dados
  - seletor de mês quando necessário
  - resumo financeiro
  - tabela de atendimentos vinculados
- ações:
  - editar: `primary`
  - arquivar: `warning`
  - excluir: `error`
- tabelas de detalhe preferem linha clicável em vez de botão extra

### Comandos úteis

Dentro de `client/`:

```bash
npm run dev
npm run lint
npm run build
npm run sync
```

### Observações

- `npm run sync` depende do backend em `http://localhost:8080/v3/api-docs`
- helpers de data devem ficar centralizados em utilitários, não espalhados nos componentes
- evitar estado global sem necessidade real

## Fluxo backend -> frontend

Quando o backend mudar contrato:

1. subir o backend
2. rodar `npm run sync` em `client/`
3. ajustar a feature no frontend

## Infra local

Dentro de `server/`:

```bash
docker compose up -d db
```

Banco local esperado no profile `dev`:

- host: `localhost`
- porta: `5432`
- database: `aprimorar`
- user: `myuser`
- password: `mypassword`

## O que costuma quebrar

- contrato gerado desatualizado
- import antigo de tipo gerado depois de mudança no OpenAPI
- lógica demais em um único componente de tela
- script antigo da raiz sendo usado como fonte de verdade

## O que evitar

- editar código gerado
- criar abstração sem uso real
- criar estado global para problema local
- mudar migration quando o pedido era só ajustar entidade
- quebrar padrão visual já fechado em telas irmãs
