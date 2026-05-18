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

3. **Crie o arquivo `.env.local` para integrações locais opcionais:**
   Use esse arquivo para variáveis que nao devem ir para o repositório, como o token do SonarQube:
   ```env
   SONAR_TOKEN=seu_token_do_sonar
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
   No diretório `server/`:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Execute o Frontend:**
   No diretório `client/`:
   ```bash
   npm run dev
   ```

Após a inicialização, o backend estará acessível em `http://localhost:8080` e o frontend em `http://localhost:5173`.

## 5. Documentação de Módulos

O backend gera documentação arquitetural do Spring Modulith em `server/src/main/resources/docs`, incluindo diagramas `.puml` e arquivos `.adoc` por módulo.

Para regenerar esses arquivos, execute em `server/`:

```bash
./mvnw test -Dtest=ModuleVerificationTest
```

## 6. Qualidade de Código e Scripts

O projeto possui scripts utilitários na raiz para análise com SonarQube e consulta de issues abertas.

Antes de usar os scripts do SonarQube, suba os containers necessários em `server/`:

```bash
docker compose up -d db sonar_db sonarqube
```

O token de autenticação deve estar em `.env.local`:

```env
SONAR_TOKEN=seu_token_do_sonar
```

Você pode gerar esse token em `http://localhost:9000/account/security`.

### 6.1. Scripts disponíveis

| Script | Descrição |
| :--- | :--- |
| `npm run sonar:backend` | Executa testes, gera cobertura JaCoCo e envia a análise do backend para o SonarQube. |
| `npm run sonar:frontend` | Executa a análise do frontend no SonarQube. |
| `npm run sonar:all` | Executa a análise de backend e frontend em sequência. |
| `npm run sonar:issues` | Consulta e lista no terminal as issues abertas do projeto `aprimorar-server`. |
| `npm run sonar:issues:frontend` | Consulta e lista no terminal as issues abertas do projeto `aprimorar-client`. |

### 6.2. Observações

* Os scripts `sonar:issues` usam a API do SonarQube e dependem de `jq` instalado no ambiente.
* O backend gera o relatório de cobertura JaCoCo em `server/target/site/jacoco/jacoco.xml`.
* O dashboard do SonarQube fica disponível em `http://localhost:9000`.
* O arquivo `server/src/main/resources/data.sql` nao roda no profile `dev` enquanto `spring.sql.init.mode` estiver como `never` em `server/src/main/resources/application-dev.yml`.
* Para reexecutar o seed localmente, troque temporariamente para `spring.sql.init.mode: always` e reinicie o backend.
