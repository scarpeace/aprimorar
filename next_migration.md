# Migração gradual do client para Next.js

## Objetivo

Migrar o frontend atual para Next.js de forma gradual, mantendo o backend atual e sem tentar converter o projeto Vite em cima do que já existe.

## Estratégia

Criar um projeto novo em paralelo e portar em camadas:

1. casca da aplicação
2. autenticação
3. telas, uma a uma

O `client/` atual continua funcionando até o `web-next/` ficar pronto para assumir.

## O que não fazer

- não migrar tudo de uma vez
- não reescrever backend para isso
- não introduzir Auth.js/NextAuth agora
- não portar telas antes da autenticação estar fechada

## Estrutura proposta

```txt
/client       -> app atual em Vite/React
/web-next     -> app nova em Next.js
/server       -> backend atual Spring Boot
```

## Mapeamento do que já existe hoje

Hoje a autenticação já está relativamente centralizada:

- `client/src/auth/authContext.tsx`
- `client/src/services/api.ts`
- `client/src/auth/components/ProtectedRoute.tsx`
- `client/src/components/Layout/MainLayout.tsx`
- `client/src/auth/pages/Login.tsx`

Isso é bom porque a migração pode reaproveitar a regra, trocando só a casca do framework.

## Etapa 0 — Shell mínimo do Next

Objetivo: subir o projeto novo com layout base e rotas vazias.

### Entregáveis

- criar `web-next/`
- configurar Tailwind, DaisyUI, React Query
- criar layout público
- criar layout privado
- criar sidebar/header básicos
- criar páginas placeholder:
  - `/login`
  - `/`
  - `/alunos`
  - `/colaboradores`
  - `/atendimentos`
  - `/responsaveis`
  - `/financeiro`
  - `/admin`

### Observação

Aqui ainda não precisa portar lógica de tela. Só estrutura, navegação e layout.

## Etapa 1 — Autenticação funcionando

Objetivo: conseguir logar, manter sessão, proteger rotas e sair.

### Decisão de arquitetura

Usar o backend atual de auth e guardar o token em cookie no Next.

Motivo: isso encaixa melhor no Next do que manter auth só em `localStorage`, porque permite proteger rota no servidor e usar `middleware.ts`.

### Fluxo desejado

1. usuário acessa `/login`
2. envia usuário/senha
3. Next chama o backend Spring
4. Next salva o token em cookie
5. usuário é redirecionado para `/`
6. rotas privadas leem o cookie
7. logout limpa o cookie

## Arquivos da etapa 1

### 1. Rota de login no Next

Criar:

- `src/app/api/auth/login/route.ts`

Responsabilidade:

- receber `username` e `password`
- chamar o endpoint de login do Spring
- salvar `accessToken` em cookie
- opcionalmente salvar dados mínimos do usuário em cookie separado ou retornar no body

### 2. Rota de logout no Next

Criar:

- `src/app/api/auth/logout/route.ts`

Responsabilidade:

- remover cookie de sessão
- redirecionar ou responder `200`

### 3. Leitura de sessão

Criar:

- `src/lib/auth/session.ts`

Responsabilidade:

- ler cookie atual
- expor helpers simples:
  - `getSession()`
  - `isAuthenticated()`
  - `getCurrentUser()` se houver user persistido

### 4. Proteção de rotas

Criar:

- `middleware.ts`

Responsabilidade:

- bloquear acesso a rotas privadas sem cookie
- redirecionar para `/login`
- bloquear `/admin` se a role não permitir

### 5. Página de login

Criar:

- `src/app/login/page.tsx`

Responsabilidade:

- portar visual e validação do login atual
- fazer submit para `/api/auth/login`
- redirecionar ao sucesso

### 6. Layout privado

Criar:

- `src/app/(private)/layout.tsx`

Responsabilidade:

- validar sessão
- renderizar `MainLayout`
- envolver páginas privadas

### 7. Cliente HTTP

Criar:

- `src/lib/api/client.ts`
- `src/lib/api/server.ts`

Responsabilidade:

- `client.ts`: chamadas no browser
- `server.ts`: chamadas server-side usando cookie atual

### 8. Provider de React Query

Criar:

- `src/app/providers.tsx`

Responsabilidade:

- centralizar `QueryClientProvider`
- manter padrão semelhante ao atual

## Ordem exata da etapa 1

### Passo 1

Subir o `web-next/` vazio com:

- `app/layout.tsx`
- `app/login/page.tsx`
- `app/page.tsx`

### Passo 2

Portar o visual do `MainLayout` atual para o layout privado.

Ainda sem dados reais se necessário.

### Passo 3

Implementar `/api/auth/login` e `/api/auth/logout`.

### Passo 4

Implementar cookie de sessão.

### Passo 5

Implementar `middleware.ts`.

### Passo 6

Portar o formulário de login atual.

Reaproveitar:

- schema do login
- mensagens
- comportamento de loading

### Passo 7

Criar uma home privada simples que prova que a auth funciona.

Exemplo:

- mostra nome do usuário
- mostra role
- botão de logout

### Passo 8

Testar cenários mínimos.

## Critérios de aceite da etapa 1

- usuário consegue logar com o backend atual
- refresh da página mantém a sessão
- rota privada sem login redireciona para `/login`
- logout remove sessão
- `/admin` respeita role
- layout privado aparece após login
- nenhuma tela de negócio foi portada ainda

## Etapa 2 — Portar contratos e infraestrutura de tela

Objetivo: preparar a base para começar a migrar páginas.

### Entregáveis

- decidir onde o código gerado pelo OpenAPI/Kubb vai ficar no Next
- gerar tipos e hooks da API no projeto novo
- portar helpers compartilhados:
  - formatadores
  - validators
  - constantes
  - componentes UI reaproveitáveis

### Regra

Ainda não sair migrando página inteira. Primeiro deixar o terreno pronto.

## Etapa 3 — Migrar telas por feature

Ordem sugerida:

1. dashboard
2. alunos
3. responsaveis
4. colaboradores
5. atendimentos
6. financeiro
7. admin

### Regra de migração

Cada feature só entra quando tiver:

- listagem
- detalhe
- navegação
- loading
- erro
- mutações principais

## Etapa 4 — Corte final

Quando o `web-next/` já cobrir tudo:

- apontar uso principal para o Next
- congelar o `client/`
- remover o Vite depois do período de validação

## Decisões práticas

### Sobre autenticação

Não usar `localStorage` como solução principal no Next.

Usar cookie desde o começo evita retrabalho.

### Sobre telas

Não portar componente por componente sem rota real.

Migrar por feature fecha melhor o fluxo.

### Sobre geração de client

Continuar usando contrato gerado da API. Não reescrever DTO na mão.

### Sobre coexistência

Manter os dois frontends em paralelo durante a transição.

Isso reduz risco.

## Primeira entrega real

A primeira entrega não é "migrar o sistema".

A primeira entrega é esta:

- `web-next/` sobe
- layout privado existe
- login funciona contra o backend atual
- logout funciona
- rotas privadas estão protegidas
- home autenticada aparece

Se isso estiver estável, o resto vira trabalho de portabilidade, não de arquitetura.
