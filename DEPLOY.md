# Plano de Deploy — Aprimorar (Local-First)

## 🗺️ Roteiro de aprendizado

```
FASE 0 ─── Conceitos (entender pra aplicar)
  │
FASE 1 ─── Docker: "PROD" local (containers rodando 24/7)
  │
FASE 2 ─── GitLab: repositório + CI básico
  │
FASE 3 ─── GitLab Runner local (pipeline roda no seu PC)
  │
FASE 4 ─── GitLab CI + SSH (deploy remoto simulado)
  │
FASE 5 ─── Apêndice: o que muda pra ir pra nuvem
```

---

## 📋 Checklist de progresso

### FASE 0 — Conceitos
- [ ] 0.1 — O que é Docker? image vs container
- [ ] 0.2 — Dockerfile, Compose, network, volume
- [ ] 0.3 — Dev vs "Prod" local: qual a diferença
- [ ] 0.4 — Rede local: como o app fica acessível na LAN
- [ ] 0.5 — O que é CI/CD? Pipeline, runner, stages

### FASE 1 — Docker: "PROD" local
- [ ] 1.1 — Criar perfil de produção (application-prod.yml)
- [ ] 1.2 — Criar Dockerfile do backend (multi-stage)
- [ ] 1.3 — Criar Dockerfile do frontend (Nginx)
- [ ] 1.4 — Criar nginx.conf
- [ ] 1.5 — Criar docker-compose.prod.yml
- [ ] 1.6 — Criar .env com variáveis de ambiente
- [ ] 1.7 — Testar: docker compose up
- [ ] 1.8 — Confirmar acesso via IP local
- [ ] 1.9 — Rodar 24/7 com restart always

### FASE 2 — GitLab: repositório + CI básico
- [ ] 2.1 — Criar projeto no GitLab
- [ ] 2.2 — Configurar chave SSH no GitLab
- [ ] 2.3 — Migrar repositório (git remote add gitlab)
- [ ] 2.4 — Criar .gitlab-ci.yml (build + teste)
- [ ] 2.5 — Primeira pipeline rodando
- [ ] 2.6 — Pipeline passa no GitLab

### FASE 3 — GitLab Runner local
- [ ] 3.1 — Instalar gitlab-runner no Ubuntu
- [ ] 3.2 — Registrar runner no projeto GitLab
- [ ] 3.3 — Runner executa pipeline localmente
- [ ] 3.4 — Pipeline faz docker compose build na máquina

### FASE 4 — GitLab CI + SSH
- [ ] 4.1 — Criar chave SSH dedicada pro deploy
- [ ] 4.2 — Configurar GitLab CI pra conectar via SSH
- [ ] 4.3 — Pipeline faz deploy via SSH na máquina local
- [ ] 4.4 — Comparar: runner vs SSH deploy

### FASE 5 — Apêndice: nuvem
- [ ] 5.1 — O que muda no Docker Compose
- [ ] 5.2 — HTTPS com Let's Encrypt
- [ ] 5.3 — Domínio e DNS
- [ ] 5.4 — Backup do banco

---

## FASE 0 — Conceitos

> Cada tópico abaixo tem uma explicação curta seguida de uma pergunta pra você.
> Leia, processe, e me responda antes de seguir.

---

### 0.1 — O que é Docker? Image vs Container

**Imagem (image):** é um "pacote" imutável com tudo que seu app precisa pra rodar — sistema operacional mínimo, runtime (Java, Node), bibliotecas, código compilado. É como um **ISO de instalação** ou um **.exe portátil**.

**Container:** é uma **instância rodando** de uma imagem. É como "executar o ISO" — ele vira um processo vivo, com seu próprio sistema de arquivos isolado, rede, etc.

```
Imagem:  ubuntu:22.04  (5 GB, parada)
Container:  ubuntu rodando com bash aberto (vivo, consumindo RAM/CPU)
```

**Por que isso importa?** Você vai criar imagens do seu backend (Java) e frontend (React + Nginx). Depois, o Docker vai instanciar containers a partir dessas imagens — e eles vão se comunicar via rede interna.

**📌 Pergunta:** Na sua opinião, quando você modificar o código fonte, você precisa reconstruir a imagem ou só reiniciar o container? Por quê?

---

### 0.2 — Dockerfile, Compose, network, volume

Quatro peças que você vai usar o tempo todo:

**Dockerfile:** É a "receita de bolo" pra criar uma imagem. Cada linha é uma instrução:
```dockerfile
FROM node:22-alpine       # começa de uma imagem base
WORKDIR /app              # define diretório de trabalho
COPY . .                  # copia arquivos do seu PC pra imagem
RUN npm run build         # executa comando durante a construção
CMD ["node", "server.js"] # comando que roda quando o container inicia
```

**Docker Compose (docker-compose.yml):** É o "maestro" que orquestra múltiplos containers. Você define:
```yaml
services:
  backend:    # um container
    build: ./server
  frontend:   # outro container
    build: ./client
  db:         # mais um
    image: postgres
```

**Network:** Rede virtual do Docker. Containers no mesmo `docker-compose.yml` se enxergam pelo **nome do serviço**. Ex: o frontend chama `http://backend:8080` em vez de `localhost:8080`.

**Volume:** Diretório persistente que sobrevive à destruição do container. Usado principalmente pro banco de dados, pra não perder dados quando reiniciar.

**📌 Pergunta:** Se você subir um container do PostgreSQL sem volume e der `docker compose down`, o que acontece com os dados do banco?

---

### 0.3 — Dev vs "Prod" local: qual a diferença

No seu **ambiente dev atual**, você roda:
- Backend: `./mvnw spring-boot:run` (terminal 1)
- Frontend: `npm run dev` (terminal 2)
- PostgreSQL: via `docker compose up -d db` (container)

Problemas desse modelo:
- Depende de você manter os terminais abertos
- Ferramentas de dev (Vite hot reload, dev profile) consomem mais recursos
- Banco precisa estar rodando separado

No **ambiente "prod" local** que vamos construir:
- Tudo em containers: Java, Nginx, PostgreSQL
- Backend compilado (JAR otimizado, sem devtools)
- Frontend buildado (arquivos estáticos servidos pelo Nginx)
- Um único comando: `docker compose up`
- Roda 24/7 mesmo com terminal fechado

**📌 Pergunta:** Por que faria sentido ter um ambiente "prod" local se você já tem o dev funcionando? Que problema isso resolve?

---

### 0.4 — Rede local: como o app fica acessível na LAN

O Docker, por padrão, expõe portas no `0.0.0.0` do seu computador. Isso significa que qualquer dispositivo na sua rede local consegue acessar.

**Seu IP local** (na rede da sua casa/escritório):
```bash
ip a | grep "inet "
# Exemplo: 192.168.1.100
```

**Acessar o app:** outro dispositivo na mesma rede abre o navegador e vai em:
```
http://192.168.1.100:80
```

**Isso funciona sem configuração extra** porque:
1. Docker faz `ports: "80:80"` (liga porta 80 do container à porta 80 do seu PC)
2. Seu PC está na rede local com IP 192.168.x.x
3. Qualquer dispositivo na mesma rede reacha esse IP

**Roteador:** você só precisaria mexer no roteador (port forwarding) se quisesse acesso **da internet** (alguém de fora da sua casa). Pra acesso local, não precisa.

**📌 Pergunta:** Seu celular conectado no mesmo Wi-Fi consegue acessar `http://192.168.x.x`? O que precisaria ser verdade pra isso funcionar? (responda com o que você imagina, sem testar)

---

### 0.5 — O que é CI/CD? Pipeline, runner, stages

**CI (Continuous Integration):** Cada vez que você faz `git push`, o código é automaticamente testado e buildado. Ninguém esquece de rodar os testes.

**CD (Continuous Delivery/Deploy):** Após os testes passarem, o código é automaticamente levado pra produção.

**Pipeline:** É o "script" que define esse fluxo. Exemplo:
```yaml
stages:
  - test       # roda os testes
  - build      # compila o frontend
  - deploy     # sobe os containers
```

**Runner:** É o programa que **executa** a pipeline. Pode rodar:
- Nos servidores do GitLab (gitlab.com) — "shared runners"
- Na sua máquina — "self-hosted runner" (é o que vamos fazer)

**Stages:** São as "fases" da pipeline. Cada stage tem um ou mais `jobs`. Um job que falha para a pipeline.

**📌 Pergunta:** Pra que serve um runner na sua máquina se o GitLab já tem runners próprios? Quando você usaria um vs o outro?

---

## FASE 1 — Docker: "PROD" local

> Aqui começamos a criar arquivos de verdade no projeto.

---

### 1.1 — Criar perfil de produção (application-prod.yml)

O Spring Boot usa **perfis** pra separar configurações. O perfil `dev` (já existente) aponta pro banco local. O perfil `prod` vai apontar pro banco dentro do Docker.

**Arquivo:** `server/src/main/resources/application-prod.yml`

Conteúdo previsto:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://db:5432/aprimorar
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    locations: classpath:db/migration

jwt:
  public:
    key: classpath:jwt/app.pub
  private:
    key: classpath:jwt/app.key

logging:
  level:
    com.aprimorar: INFO

app:
  security:
    cors:
      allowed-origins:
        - "http://localhost:5173"
        - "http://localhost"
```

Diferenças pro `dev`:
- `datasource.url` → aponta pra `db` (nome do serviço no Docker)
- `show-sql: false` → sem poluição no log
- Sem SQL init (seed) → só Flyway de migração
- CORS aceita `localhost` (do Nginx) além do `localhost:5173` (do Vite dev)

**📌 Pergunta:** Por que o `datasource.url` muda de `localhost:5432` pra `db:5432`? O que é esse `db`?

---

### 1.2 — Dockerfile do backend (multi-stage)

O backend precisa de:

**Stage 1 (builder):**
- Imagem com JDK 21 e Maven Wrapper
- Copia `pom.xml` e baixa dependências (cache)
- Copia código fonte
- Roda `./mvnw package -DskipTests` (gera o .jar)

**Stage 2 (runtime):**
- Imagem menor, só com JRE 21 (sem compilador)
- Copia o .jar do stage anterior
- Expõe porta 8080
- Define `ENTRYPOINT` com `java -jar app.jar --spring.profiles.active=prod`

**Por que multi-stage?**
- Imagem final é **muito menor** (JRE ~50MB vs JDK ~200MB)
- Código fonte não vai pra imagem final (segurança)
- Dependências de build não poluem o runtime

**📌 Pergunta:** O que acontece se você esquecer de copiar `application-prod.yml` pra imagem final? O Spring Boot vai usar qual perfil?

---

### 1.3 — Dockerfile do frontend (Nginx)

O frontend React precisa ser **buildado** em arquivos estáticos (HTML, JS, CSS) e servidos por um servidor HTTP. O Vite dev server não é pra produção.

**Stage 1 (builder):**
- Node 22 + npm ci (instala exatamente o que está no lockfile)
- `npm run build` (gera pasta `dist/`)

**Stage 2 (runtime):**
- `nginx:alpine` (imagem mínima, ~5MB)
- Copia `dist/` pro diretório padrão do Nginx
- Copia config personalizada (`nginx.conf`)

**Por que Nginx?**
- Servir arquivos estáticos é o que ele faz de melhor
- Faz proxy reverso: `/api` → backend Java
- Lida com cache, compressão, SSL

**📌 Pergunta:** Se o frontend React roda no navegador do usuário, por que precisamos de um servidor (Nginx) rodando no backend? Não bastaria subir os arquivos .html/.js em qualquer lugar?

---

### 1.4 — Nginx.conf

O Nginx precisa de duas regras:

```nginx
server {
    listen 80;

    root /usr/share/nginx/html;
    index index.html;

    location / {
        # SPA: qualquer rota que não seja arquivo físico → index.html
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        # Requisições pra /api/* vão pro backend
        proxy_pass http://backend:8080/;
    }
}
```

**Regra 1 — SPA:** O React Router gerencia as rotas (`/alunos`, `/financeiro`, etc.). Se o navegador recarregar a página em `/alunos`, o servidor precisa servir `index.html` (e não tentar achar um arquivo `/alunos`).

**Regra 2 — Proxy reverso:** Tudo que chega em `/api/*` é redirecionado pro container `backend:8080`. O navegador nunca sabe que o backend Java existe — ele só vê o Nginx.

**📌 Pergunta:** O que acontece se você esquecer a regra `try_files` e o usuário der F5 na página `/alunos`?

---

### 1.5 — Docker Compose de produção

Arquivo que junta tudo: `docker-compose.prod.yml`

```yaml
services:
  db:
    image: postgres:15-alpine
    restart: always
    environment:
      POSTGRES_DB: aprimorar
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - pg_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USERNAME} -d aprimorar"]

  backend:
    build: ./server
    restart: always
    depends_on:
      db:
        condition: service_healthy
    environment:
      DB_USERNAME: ${DB_USERNAME}
      DB_PASSWORD: ${DB_PASSWORD}

  frontend:
    build: ./client
    restart: always
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  pg_data:
```

**Três serviços:**
| Serviço | Função | Portas | Depende de |
|---------|--------|--------|------------|
| `db` | PostgreSQL 15 | interna | — |
| `backend` | API Java (Spring Boot) | interna | `db` saudável |
| `frontend` | Nginx (arquivos + proxy) | **80** | `backend` |

**Rede:** Por padrão, todos os serviços compartilham a rede `aprimorar_default`. O backend acessa o banco como `db:5432`. O Nginx acessa o backend como `backend:8080`.

**📌 Pergunta:** Por que o `frontend` expõe a porta 80 pro host, mas o `backend` não expõe nenhuma porta? Como o navegador vai falar com o backend então?

---

### 1.6 — .env com variáveis de ambiente

Arquivo `.env` na raiz do projeto (NÃO versionado — já está no `.gitignore`):

```bash
DB_USERNAME=aprimorar
DB_PASSWORD=uma_senha_forte_aqui
```

O Docker Compose lê automaticamente o `.env` da mesma pasta e substitui `${VARIAVEL}` nos `environment:` dos serviços.

**📌 Pergunta:** Por que não colocar a senha diretamente no `docker-compose.prod.yml` ao invés de usar `.env`?

---

### 1.7 — Testar: docker compose up

Comando mágico:

```bash
docker compose -f docker-compose.prod.yml up --build -d
```

Explicando cada parte:
- `-f docker-compose.prod.yml` → usa esse arquivo (e não o `docker-compose.yml` padrão)
- `--build` → reconstrói as imagens antes de subir
- `-d` → detached (roda em segundo plano)

Depois de subir:

```bash
docker compose -f docker-compose.prod.yml ps
# STATUS: Up (todas verdes)

docker compose -f docker-compose.prod.yml logs -f backend
# Ver logs do backend em tempo real
```

**📌 Pergunta:** O que você espera ver quando acessar `http://localhost` no navegador depois de subir os containers? E `http://192.168.x.x` do celular?

---

### 1.8 — Confirmar acesso via IP local

```bash
ip a | grep "inet " | grep -v 127.0.0.1
# Exemplo: inet 192.168.1.100/24
```

Pegue esse IP e teste de **outro dispositivo na mesma rede** (celular, notebook):
```
http://192.168.1.100
```

Se funcionar, seu "prod" local já está acessível pra equipe na mesma rede.

**📌 Pergunta:** Por que aparece `192.168.x.x` e não o IP que você vê no site "meuip.com.br"? Qual a diferença entre IP local e IP público?

---

### 1.9 — Rodar 24/7 com restart always

O `restart: always` no Compose já garante que os containers sobem quando o Docker iniciar. Mas você precisa garantir que o **Docker Engine** inicie com o sistema:

```bash
sudo systemctl enable docker
```

A partir daí, se você reiniciar o Ubuntu, o Docker sobe sozinho e os containers também.

**📌 Pergunta:** Se você reiniciar o PC, os containers sobem automaticamente? Em qual ordem eles sobem, considerando que o `backend` depende do `db`?

---

## FASE 2 — GitLab: repositório + CI básico

> Aqui vamos sair da máquina local e interagir com o GitLab.

---

### 2.1 — Criar projeto no GitLab

1. Acesse [gitlab.com](https://gitlab.com)
2. Crie conta (se não tiver)
3. New project → Create blank project
4. Nome: `aprimorar`
5. Visibility: **Private** (já que tem dados de alunos)
6. **Não** marcar "Initialize repository with a README" (já temos repo)

No final, o GitLab mostra um URL como:
```
git@gitlab.com:<seu-username>/aprimorar.git
```

**📌 Pergunta:** O que significa o projeto ser "Private" vs "Public"? Qual a implicação prática de deixar privado?

---

### 2.2 — Configurar chave SSH no GitLab

O GitLab precisa de uma chave SSH pra autenticar você.

```bash
# Ver se já tem chave
ls -la ~/.ssh/

# Se não tiver, criar:
ssh-keygen -t ed25519 -C "seu-email@exemplo.com"

# Ver chave pública:
cat ~/.ssh/id_ed25519.pub
```

No GitLab: Settings → SSH Keys → colar a chave pública.

**📌 Pergunta:** Por que usamos chave SSH em vez de usuário/senha? Qual a diferença entre a chave pública e a privada?

---

### 2.3 — Migrar repositório

```bash
# Ver remotos atuais
git remote -v
# Deve mostrar: origin  https://github.com/...

# Adicionar GitLab como remoto
git remote add gitlab git@gitlab.com:<seu-username>/aprimorar.git

# Enviar tudo pro GitLab
git push gitlab main
```

Agora você tem dois remotos:
- `origin` → GitHub
- `gitlab` → GitLab

Pra enviar código pros dois:
```bash
git push origin main && git push gitlab main
```

**📌 Pergunta:** Por que mantivemos o GitHub como `origin`? Qual a vantagem de ter dois remotos?

---

### 2.4 — Criar .gitlab-ci.yml

Pipeline básica que só testa o backend:

```yaml
image: docker:27

services:
  - docker:dind

variables:
  DOCKER_TLS_CERTDIR: ""

stages:
  - test

backend-test:
  stage: test
  image: eclipse-temurin:21-jdk-alpine
  script:
    - cd server
    - ./mvnw test
  only:
    - main
```

Esse pipeline:
- Usa **shared runners** do GitLab (gratuitos)
- Roda apenas quando fizer push na `main`
- Executa `./mvnw test` num container com JDK 21

**📌 Pergunta:** Onde esse pipeline está executando? Quem provê a máquina que roda o `./mvnw test`?

---

### 2.5 — Primeira pipeline rodando

```bash
git add .gitlab-ci.yml
git commit -m "ci: add basic pipeline with backend tests"
git push gitlab main
```

No GitLab: CI/CD → Pipelines → ver a pipeline em execução.

**O que esperar:**
- Pipeline demora ~2-3 minutos (primeira vez baixa dependências Maven)
- Se passar: ✅ verde
- Se falhar: 🔴 vermelho + logs de erro

**📌 Pergunta:** Se a pipeline falhar, o GitLab impede o merge/commit? Ou só avisa?

---

### 2.6 — Pipeline passa no GitLab

Após confirmar que a pipeline passou no GitLab usando shared runners, você já tem CI funcionando. Agora vamos trazer o CI pra rodar **na sua máquina**.

---

## FASE 3 — GitLab Runner local

> O runner local faz a pipeline rodar no SEU computador, não nos servidores do GitLab.
> Isso é útil pra: usar sua própria infra, ter mais recurso (RAM/CPU), rodar coisas que precisam de Docker local.

---

### 3.1 — Instalar gitlab-runner no Ubuntu

```bash
# Adicionar repositório oficial do GitLab Runner
curl -L "https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh" | sudo bash

# Instalar
sudo apt install gitlab-runner
```

**📌 Pergunta:** O gitlab-runner é um processo que roda em background. O que você acha que ele faz enquanto está ocioso? (dica: ele fica "escutando" o GitLab)

---

### 3.2 — Registrar runner no projeto GitLab

```bash
sudo gitlab-runner register
```

O registro pede:
1. **URL do GitLab:** `https://gitlab.com`
2. **Token de registro:** Settings → CI/CD → Runners → Specific runners → token
3. **Descrição:** `aprimorar-local-runner`
4. **Tags:** `local` (importante: vamos usar essa tag no .gitlab-ci.yml)
5. **Executor:** `docker`
6. **Default image:** `docker:27`

Depois de registrar, o runner aparece como disponível no GitLab.

**Agora precisamos atualizar o `.gitlab-ci.yml` pra usar esse runner:**

```yaml
image: docker:27

services:
  - docker:dind

variables:
  DOCKER_TLS_CERTDIR: ""

stages:
  - test
  - build
  - deploy

backend-test:
  stage: test
  image: eclipse-temurin:21-jdk-alpine
  tags:
    - local
  script:
    - cd server && ./mvnw test

backend-build:
  stage: build
  tags:
    - local
  script:
    - cd server && ./mvnw package -DskipTests
  artifacts:
    paths:
      - server/target/*.jar

frontend-build:
  stage: build
  image: node:22-alpine
  tags:
    - local
  script:
    - cd client && npm ci && npm run build
  artifacts:
    paths:
      - client/dist/

deploy-local:
  stage: deploy
  tags:
    - local
  script:
    - docker compose -f docker-compose.prod.yml up --build -d
  only:
    - main
```

**📌 Pergunta:** O que é uma `tag` no contexto do runner? Por que adicionamos `tags: [local]` nos jobs?

---

### 3.3 — Runner executa pipeline localmente

```bash
git add .gitlab-ci.yml
git commit -m "ci: add local runner pipeline with build and deploy"
git push gitlab main
```

Agora:
1. GitLab recebe o push
2. GitLab procura um runner com a tag `local`
3. Encontra o runner registrado no seu PC
4. Runner executa os jobs **dentro de containers Docker** na sua máquina
5. O job `deploy-local` vai executar `docker compose up` na sua máquina

**📌 Pergunta:** Quando o runner executa `docker compose up`, onde os containers sobem? Na máquina host (seu Ubuntu) ou dentro do container do runner?

---

### 3.4 — Pipeline faz deploy na máquina

Uma consideração importante: o `docker compose` precisa estar disponível dentro do container do runner. Por padrão, a imagem `docker:27` já inclui o Docker CLI.

**Problema:** o socket do Docker precisa ser montado dentro do container do runner pra ele se comunicar com o Docker daemon do host.

**Solução:** configurar o `config.toml` do runner:

```toml
# /etc/gitlab-runner/config.toml
[[runners]]
  name = "aprimorar-local-runner"
  url = "https://gitlab.com"
  token = "seu-token"
  executor = "docker"
  [runners.docker]
    image = "docker:27"
    volumes = ["/var/run/docker.sock:/var/run/docker.sock", "/cache"]
    privileged = true
```

Isso monta o socket do Docker do host dentro do container do runner. Assim, `docker compose` dentro do runner mexe nos containers do host.

**⚠️ Segurança:** `privileged = true` dá acesso total ao Docker do host. É o padrão pra runners que precisam buildar imagens. Em produção, você isolaria isso num runner dedicado.

**📌 Pergunta:** O que significa montar `/var/run/docker.sock` dentro do container? Por que sem isso o `docker compose` dentro do runner não funcionaria?

---

## FASE 4 — GitLab CI + SSH (deploy remoto simulado)

> Agora vamos fazer o pipeline rodar nos **shared runners do GitLab** (não no seu PC),
> mas o deploy ainda é feito na sua máquina local via SSH.

Essa abordagem simula o que empresas fazem: CI/CD na nuvem deployando em servidores remotos.

---

### 4.1 — Criar chave SSH dedicada pro deploy

Vamos criar um par de chaves **específico pro deploy** (separado da sua chave pessoal):

```bash
ssh-keygen -t ed25519 -f ~/.ssh/deploy_key -C "deploy@aprimorar"
```

Isso gera dois arquivos:
- `~/.ssh/deploy_key` → **chave privada** (nunca compartilhar)
- `~/.ssh/deploy_key.pub` → **chave pública** (colocar no servidor)

Adicione a chave pública ao `~/.ssh/authorized_keys` do seu usuário:

```bash
cat ~/.ssh/deploy_key.pub >> ~/.ssh/authorized_keys
```

**📌 Pergunta:** Por que criar uma chave separada pra deploy em vez de usar sua chave pessoal? Qual o risco se a chave de deploy vazar?

---

### 4.2 — Configurar GitLab CI pra conectar via SSH

No GitLab: Settings → CI/CD → Variables, adicione:

| Variável | Valor | Tipo |
|----------|-------|------|
| `SSH_PRIVATE_KEY` | conteúdo de `~/.ssh/deploy_key` | File |
| `VPS_HOST` | seu IP local (ex: `192.168.1.100`) | Variable |
| `VPS_USER` | seu usuário do Ubuntu | Variable |

**Agora o `.gitlab-ci.yml` com deploy via SSH:**

```yaml
stages:
  - test
  - build
  - deploy

backend-test:
  stage: test
  image: eclipse-temurin:21-jdk-alpine
  script:
    - cd server && ./mvnw test

backend-build:
  stage: build
  script:
    - cd server && ./mvnw package -DskipTests
  artifacts:
    paths:
      - server/target/*.jar

frontend-build:
  stage: build
  image: node:22-alpine
  script:
    - cd client && npm ci && npm run build
  artifacts:
    paths:
      - client/dist/

deploy-via-ssh:
  stage: deploy
  image: alpine:latest
  before_script:
    - apk add --no-cache openssh-client
    - eval $(ssh-agent -s)
    - echo "$SSH_PRIVATE_KEY" | tr -d '\r' | ssh-add -
    - mkdir -p ~/.ssh
    - chmod 700 ~/.ssh
  script:
    - ssh -o StrictHostKeyChecking=no "$VPS_USER@$VPS_HOST" "
        cd ~/aprimorar &&
        git pull &&
        docker compose -f docker-compose.prod.yml up --build -d
      "
  only:
    - main
```

**📌 Pergunta:** Por que o SSH precisa de um `ssh-agent` e de adicionar a chave com `ssh-add`? Não bastaria passar a chave diretamente no comando `ssh`?

---

### 4.3 — Pipeline faz deploy via SSH

```bash
git add .gitlab-ci.yml
git commit -m "ci: add SSH deploy pipeline"
git push gitlab main
```

O fluxo completo agora:
1. Shared runner do GitLab baixa a imagem `alpine`
2. Configura SSH com a chave privada (vinda das variáveis)
3. Conecta no **seu IP local** via SSH
4. Executa `git pull && docker compose up` na sua máquina
5. Seu "prod" local é atualizado automaticamente

**Mas tem um problema:** seu PC precisa estar acessível pelo GitLab. Seu IP local (`192.168.x.x`) **não é visível** da internet. O shared runner do GitLab não consegue alcançar `192.168.1.100`.

**Solução real:** você usaria um VPS na nuvem com IP público. Mas pra aprendizado local:
- Usamos o **runner local** (Fase 3) que já roda na mesma rede
- OU usamos um **túnel** (ngrok, bore) que expõe seu PC na internet

**📌 Pergunta:** Por que o shared runner do GitLab não consegue acessar `192.168.1.100`? O que é NAT/roteador e como ele bloqueia esse acesso?

---

### 4.4 — Comparar: runner vs SSH deploy

Duas abordagens que aprendemos:

| Aspecto | Runner Local | SSH Deploy |
|---------|-------------|------------|
| Onde o CI roda | Seu PC | Servidores GitLab |
| Precisa seu PC ligado | Sim (é o runner) | Só pro deploy |
| Pipeline gratuita | Sim (infra sua) | Sim (shared runners) |
| Acesso à máquina local | Imediato (mesmo host) | Precisa de IP público/túnel |
| Cenário real | Empresas com servidor próprio | Empresas com VPS na nuvem |

**Conclusão:** você entendeu os **dois modelos**. O runner local é mais imediato pro seu cenário atual. O SSH é o padrão da indústria quando o servidor é remoto (VPS).

---

## FASE 5 — Apêndice: nuvem

> Aqui só um resumo do que muda quando você for pra nuvem.
> Não vamos executar agora.

### 5.1 — O que muda no Docker Compose

Quase **nada**. O mesmo `docker-compose.prod.yml` funciona num VPS. Só precisa:
- Remover `build:` e usar imagens pré-buildadas (push pro GitLab Container Registry)
- Ou manter o build no próprio VPS (mais simples, igual você faz local)

### 5.2 — HTTPS com Let's Encrypt

```bash
apt install certbot python3-certbot-nginx
certbot --nginx -d aprimorar.com.br
```

**Gratuito**, renovação automática a cada 90 dias.

### 5.3 — Domínio e DNS

- Comprar domínio (ex: `aprimorar.com.br`)
- No painel DNS: apontar `A` record pro IP do VPS
- Esperar propagação (~minutos a horas)

### 5.4 — Backup do banco

```bash
# Script simples
docker compose exec db pg_dump -U aprimorar aprimorar > backup-$(date +%Y%m%d).sql
```

Rodar via cron diariamente.

---

## Notas finais

- **O que você aprendeu:** Docker, multi-stage builds, Docker Compose, rede local, GitLab CI, runner local, deploy via SSH.
- **O que vem naturalmente depois:** VPS, HTTPS, domínio — tudo usa as mesmas ferramentas que você já domina.
- **Foco do projeto:** Você tem um "prod" local funcional, com CI/CD rodando, e sabe exatamente como cada peça funciona antes de colocar na nuvem.

> "Primeiro faça funcionar localmente. Depois coloque na nuvem." — esse é o espírito.
