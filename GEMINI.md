# Gemini Instructions for Aprimorar

Este projeto é um **Sistema de Gerenciamento de Cursinho** (preparatório para ENEM e aulas particulares). É uma aplicação full-stack robusta que utiliza Java no backend e React no frontend.

## 🏗️ Arquitetura e Tecnologias

O projeto está dividido em dois diretórios principais:
-   `server/api-aprimorar/`: Backend Spring Boot (Java 21).
-   `client/`: Frontend React (Vite + TypeScript).

### ⚙️ Stack Tecnológica

| Camada | Tecnologia |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5, Hibernate (JPA), PostgreSQL, Flyway, JUnit 5, Mockito |
| **Frontend** | React 19, TypeScript, Vite, Tailwind CSS 4, TanStack Query (React Query), Kubb |
| **DevOps** | Docker, Docker Compose |

## 🚀 Comandos Principais

### Raiz do Projeto
-   `npm run dev`: Inicia o backend e o frontend simultaneamente (requer `concurrently`).
-   `npm run start:backend`: Inicia apenas o backend.
-   `npm run start:frontend`: Inicia o frontend após verificar que o backend está online.

### Backend (`server/api-aprimorar/`)
-   `./mvnw spring-boot:run`: Inicia a aplicação.
-   `./mvnw test`: Executa os testes unitários e de integração.
-   `./mvnw clean compile`: Limpa e compila o projeto.

### Frontend (`client/`)
-   `npm run dev`: Inicia o servidor de desenvolvimento do Vite.
-   `npm run build`: Gera o build de produção.
-   `npm run sync`: **Obrigatório** - Gera hooks, tipos e esquemas Zod a partir da API (OpenAPI) usando Kubb.
-   `npm run lint`: Executa o linting do código.

## 🛠️ Convenções de Desenvolvimento

### Backend
-   **Arquitetura:** Segue o padrão de camadas (Controller -> Service -> Domain/Entity).
-   **Entidades:** Utiliza o `BaseEntity` (em `com.aprimorar.api.shared`) como classe base para IDs e campos de auditoria.
-   **Migrações:** O banco de dados é gerenciado pelo Flyway (`src/main/resources/db/migration`).
-   **Documentação:** A API é documentada via Swagger/OpenAPI (`springdoc-openapi`).

### Frontend
-   **Sincronização com API:** Nunca crie tipos ou hooks de API manualmente. Use sempre o comando `npm run sync` para gerar código a partir da especificação do backend.
-   **Estilização:** Utiliza Tailwind CSS 4 com DaisyUI.
-   **Gerenciamento de Estado:** Prefere TanStack Query para dados assíncronos e React Hook Form para formulários (com validação Zod).
-   **Organização:** Features estão agrupadas em `src/features/`. Componentes comuns estão em `src/components/ui/`.

## 📌 Notas Importantes
-   O backend utiliza `spring-boot-docker-compose`, o que significa que ele pode tentar gerenciar os containers do banco de dados automaticamente se configurado.
-   Sempre verifique se o backend está rodando em `http://localhost:8080` antes de rodar o `npm run sync` no frontend.
