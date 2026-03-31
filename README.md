# Sistema de Gerenciamento de Cursinho

## 1. Visão Geral do Projeto

Este projeto é um sistema de gerenciamento para um cursinho preparatório para ENEM e aulas particulares. O sistema permite o controle completo de alunos, responsáveis e o agendamento de atendimentos. O objetivo foi criar uma aplicação robusta, performática e segura, seguindo as melhores práticas de desenvolvimento full-stack.

## 2. 📋 Funcionalidades Principais

* **Gerenciamento de Alunos:** CRUD completo de alunos, incluindo dados pessoais, escolares e endereço.
* **Gerenciamento de Responsáveis:** Cadastro e vínculo de responsáveis aos alunos.
* **Agendamento:** Fluxo de registro e agendamento de atendimentos (integração com Google Calendar).
* **Paginação e Filtros:** Busca de alunos e atendimentos com paginação e filtros realizados diretamente no banco de dados para alta performance.
* **Segurança:** Autenticação segura com JWT e controle de acesso baseado em cargos.

## 3. 🧰 Tecnologias Utilizadas

| Categoria | Tecnologia |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5, Hibernate, JUnit 5, Mockito |
| **Frontend** | React 19, TypeScript, Vite, Tailwind CSS, TanStack Query |
| **Banco de Dados** | PostgreSQL, Flyway Migrations |
| **Outros** | Docker, Docker Compose, OpenAPI/Kubb |

## 4. 🚀 Como Instalar e Rodar Localmente

Para iniciar o projeto, você precisará ter o **Docker** e o **Docker Compose** instalados.

### 4.1. Configuração

1. **Clone o Repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd aprimorar
   ```

2. **Crie o arquivo `.env`:**
   Na raiz do projeto, crie um arquivo `.env` com as variáveis necessárias (exemplo abaixo):
   ```env
   JWT_SECRET_KEY=sua_chave_secreta_segura
   DB_PASSWORD=sua_senha_do_banco
   # ... outras variáveis conforme necessário
   ```

### 4.2. Inicialização

1. **Inicie os Containers:**
   ```bash
   docker compose up -d db
   ```

2. **Instale e Sincronize as Dependências:**
   No diretório `client/`:
   ```bash
   npm install
   npm run sync
   ```
   *Nota: O comando `npm run sync` é obrigatório para gerar os tipos e hooks da API a partir da especificação OpenAPI.*

3. **Execute o Backend:**
   No diretório `server/api-aprimorar/`:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Execute o Frontend:**
   No diretório `client/`:
   ```bash
   npm run dev
   ```

Após a inicialização, o backend estará acessível em `http://localhost:8080` e o frontend em `http://localhost:5173`.
