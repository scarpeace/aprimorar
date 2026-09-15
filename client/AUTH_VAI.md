# Plano de autenticação do frontend

Este documento define a reconstrução da autenticação do frontend usando Next.js App Router, access token JWT em memória e refresh token HttpOnly gerenciado pelo Spring Boot.

## Objetivo

Implementar o fluxo:

```text
Access Token
→ memória JavaScript
→ enviado como Authorization: Bearer
→ validade curta

Refresh Token
→ cookie HttpOnly criado pelo Spring
→ inacessível ao JavaScript
→ enviado automaticamente pelo browser
```

Decisões atuais:

- não usar `localStorage`;
- não usar `sessionStorage`;
- não armazenar access token em cookie do Next;
- não criar proxy do Next para a API;
- não ler ou copiar o refresh token no frontend;
- manter o refresh token sob responsabilidade do Spring Boot e do browser;
- não implementar CSRF nesta etapa;
- não implementar roles/authorities nesta etapa;
- autorização real permanece no backend.

## Estado inicial

A implementação anterior de autenticação/autorização foi removida do frontend.

Já foram removidos:

```text
src/app/login/
src/app/api/session/
src/features/auth/
src/lib/auth/
middleware.ts
```

Também foram removidos:

- o guard do layout privado;
- o logout da navegação;
- a filtragem de links por role;
- a injeção de token no proxy do Next;
- o redirecionamento automático para `/login` no cliente antigo.

O grupo de rotas `src/app/(private)/` continua existindo apenas para preservar a estrutura das páginas e URLs. Neste momento ele não protege as páginas.

## Contrato do backend

O frontend deve respeitar o contrato atual do Spring Boot.

### Login

```http
POST /auth/login
Content-Type: application/json
```

Payload:

```json
{
  "email": "admin@aprimorar.com",
  "password": "senha"
}
```

Resposta:

```json
{
  "accessToken": "..."
}
```

Além do JSON, o backend envia:

```http
Set-Cookie: refresh_token=...; HttpOnly; Path=/auth
```

O frontend não deve tentar ler esse cookie.

### Refresh

```http
POST /auth/refresh
```

A requisição deve usar:

```typescript
credentials: "include"
```

Resposta:

```json
{
  "accessToken": "..."
}
```

### Logout

```http
POST /auth/logout
```

O backend revoga o refresh token e expira o cookie. O frontend deve apagar o access token da memória independentemente do resultado da chamada.

### Usuário atual

```http
GET /auth/me
Authorization: Bearer <access-token>
```

## Configuração local

Criar ou conferir `client/.env.local`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

O backend deve possuir, no ambiente de desenvolvimento:

```env
FRONTEND_URL=http://localhost:3000
AUTH_COOKIE_SECURE=false
```

Como o browser fará chamadas diretamente ao backend, o CORS e `credentials: "include"` são necessários.

## Estrutura final

```text
src/
├── app/
│   ├── login/
│   │   └── page.tsx
│   │
│   └── (private)/
│       ├── layout.tsx
│       ├── page.tsx
│       └── ...
│
├── components/
│   └── auth-provider.tsx
│
├── features/
│   └── auth/
│       ├── LoginForm.tsx
│       └── LogoutButton.tsx
│
└── lib/
    ├── api/
    │   └── api-error.ts
    │
    └── auth/
        ├── auth-client.ts
        └── token-store.ts
```

## Plano de implementação

### 1. Remover a camada `backend`

Remover:

```text
src/lib/backend/client.ts
src/lib/backend/server.ts
src/app/api/backend/[...path]/route.ts
```

O frontend passará a chamar diretamente:

```text
http://localhost:8080
```

Atenção: o PDF autenticado não poderá ser carregado por um `<a href>` simples, pois ele precisa do header Bearer. Esse fluxo será ajustado com `fetch` e `Blob` quando a funcionalidade for integrada.

### 2. Usar o cliente Fetch oficial do Kubb v4

O projeto permanecerá na linha `4.36.1`.

Na versão v4, os clientes Fetch e Axios são fornecidos pelo pacote:

```text
@kubb/plugin-client
```

O cliente Fetch oficial já cuida de:

- montar a URL;
- serializar query params;
- serializar o body JSON;
- tratar `FormData`;
- interpretar as respostas;
- tratar `Blob` e respostas `204`.

Não devemos reimplementar essas responsabilidades em um cliente HTTP próprio.

Adicionar `@kubb/plugin-client@4.36.1` como dependência direta de desenvolvimento e configurar o preset Fetch no Kubb. O `plugin-react-query@4.36.1` já utiliza o plugin-client transitivamente, mas a dependência deve ficar explícita se a aplicação importar o cliente oficial diretamente.

O cliente deverá ser configurado com:

```typescript
client.setConfig({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  credentials: "include",
});
```

A autenticação dinâmica e o refresh continuarão sendo integrados pela nossa camada de auth, pois o cliente v4 não possui o `auth resolver` moderno documentado no Kubb v5.

### 3. Manter `api-error.ts`

A conversão de erros da API fica isolada em:

```text
src/lib/api/api-error.ts
```

O helper interpreta o `ProblemDetail` do Spring usando, nesta ordem:

```text
errors[0]
→ detail
→ title
→ mensagem genérica
```

Os hooks e componentes devem importar `getFriendlyErrorMessage` desse arquivo, e não do cliente HTTP.

### 4. Criar `token-store.ts`

O access token será mantido somente em memória:

```typescript
import "client-only";

let accessToken: string | null = null;

export function getAccessToken() {
  return accessToken;
}

export function setAccessToken(token: string) {
  accessToken = token;
}

export function clearAccessToken() {
  accessToken = null;
}
```

Comportamento esperado:

```text
navegação SPA → token permanece
F5 → token desaparece
fechar aba → token desaparece
```

Isso é intencional. O refresh cookie será usado para recuperar um novo access token.

### 5. Criar `auth-client.ts`

Responsável exclusivamente por:

```text
login()
refreshAccessToken()
refreshOnce()
logout()
```

Todas as chamadas deverão usar:

```typescript
credentials: "include"
```

`refreshOnce()` deverá compartilhar uma única Promise para impedir várias chamadas simultâneas a `/auth/refresh`.

Fluxo:

```text
request A ─┐
request B ─┼→ mesma Promise de refresh
request C ─┘
```

### 6. Integrar autenticação ao cliente Fetch do Kubb

O cliente Fetch oficial do Kubb será configurado com o access token atual antes de cada request, ou receberá um wrapper mínimo para resolver o token dinamicamente.

Fluxo esperado:

```text
operação gerada pelo Kubb
  ↓
cliente Fetch do Kubb
  ↓
getAccessToken()
  ↓
Authorization: Bearer <access-token>
  ↓
request
```

Se uma request retornar `401`:

```text
request com token antigo
  ↓
401
  ↓
refreshOnce()
  ↓
novo access token
  ↓
repetir request original uma única vez
```

Se o refresh falhar:

```text
clearAccessToken()
→ sessão não autenticada
```

O cliente de API não deve decidir sozinho a navegação da aplicação. O redirect para login ficará sob responsabilidade do `AuthProvider` ou da camada de interface.

### 7. Recriar `AuthProvider`

Criar:

```text
src/components/auth-provider.tsx
```

Responsabilidades:

- controlar o estado de inicialização da sessão;
- tentar `refreshOnce()` depois de um F5;
- evitar renderizar a área privada antes da recuperação da sessão;
- redirecionar para `/login` se o refresh falhar.

Fluxo:

```text
AuthProvider
├── access token existe → libera a aplicação
├── access token ausente + refresh válido → salva novo token e libera
└── refresh inválido → /login
```

### 8. Adaptar o layout privado

Atualizar:

```text
src/app/(private)/layout.tsx
```

O layout deverá envolver a área privada com `AuthProvider`.

O `Nav` não deverá receber usuário ou role de um cookie. Informações do usuário poderão ser carregadas posteriormente por `GET /auth/me` usando o cliente autenticado.

### 9. Recriar a tela de login

Recriar:

```text
src/app/login/page.tsx
src/features/auth/LoginForm.tsx
```

O formulário deverá:

1. coletar email e senha;
2. chamar `login(email, password)`;
3. guardar o access token em memória;
4. redirecionar para `/` ou para a URL originalmente solicitada;
5. exibir erro amigável quando o login falhar.

O payload deve usar `email`, nunca `username`:

```json
{
  "email": "...",
  "password": "..."
}
```

### 10. Recriar o logout

Recriar:

```text
src/features/auth/LogoutButton.tsx
```

Fluxo:

```text
POST /auth/logout
  ↓
backend revoga refresh token
  ↓
frontend limpa access token da memória
  ↓
router.replace("/login")
  ↓
router.refresh()
```

O access token deve ser limpo mesmo que a chamada ao backend falhe.

### 11. Integrar e regenerar o Kubb

Depois de configurar o `@kubb/plugin-client` com Fetch:

```bash
npm run sync
```

Confirmar que os hooks gerados usam o cliente Fetch oficial do Kubb e que não existe mais dependência de:

```text
@/lib/backend/client
@/lib/api/api-client
```

As páginas e hooks da aplicação não devem montar headers JWT manualmente.

### 12. Ajustar arquivos autenticados especiais

Arquivos como relatórios PDF não devem usar links simples para endpoints protegidos.

O fluxo correto será:

```text
api-client
  ↓
fetch autenticado
  ↓
Blob
  ↓
URL.createObjectURL()
  ↓
abrir ou baixar arquivo
```

## Validação por etapa

### Infraestrutura

```bash
npm run lint
npm run sync
```

### Fluxo de login

- login com credenciais válidas;
- resposta contém `accessToken`;
- refresh cookie existe no browser;
- access token não aparece em `localStorage` ou `sessionStorage`;
- primeira chamada autenticada envia Bearer.

### Recuperação de sessão

- fazer F5 em uma página privada;
- confirmar que `/auth/refresh` é chamado;
- confirmar que a página não aparece antes da recuperação;
- confirmar que a aplicação volta a funcionar com o novo access token.

### Expiração

- aguardar ou simular expiração do access token;
- confirmar resposta `401`;
- confirmar chamada única a `/auth/refresh`;
- confirmar repetição da request original.

### Concorrência

- disparar várias requests simultâneas sem access token;
- confirmar que somente um refresh é executado.

### Logout

- chamar `/auth/logout`;
- confirmar revogação do refresh no backend;
- confirmar limpeza do access token em memória;
- confirmar redirect para `/login`;
- confirmar que nova tentativa de refresh falha.

## Evoluções futuras

### Refresh Token Rotation

Não exige novos arquivos no frontend. O browser substituirá o cookie ao receber novo `Set-Cookie` do backend.

### CSRF

Se for adicionado posteriormente:

```text
src/lib/auth/csrf-store.ts
```

Também será necessário alterar `auth-client.ts` e a configuração/interceptor do cliente Fetch do Kubb para enviar o header CSRF nas operações mutáveis.

### Roles e authorities

Quando necessário, adicionar:

```text
src/lib/auth/auth.types.ts
src/lib/auth/authorities.ts
```

Esses dados servem apenas para UX. A autorização real continua sendo responsabilidade do Spring Boot.

## Regras importantes

- não editar manualmente arquivos gerados pelo Kubb;
- não salvar access token em armazenamento persistente;
- não tentar ler refresh token no JavaScript;
- não duplicar a lógica de refresh em componentes;
- não adicionar headers JWT manualmente nas páginas;
- não proteger apenas pela visibilidade de links;
- não confiar em roles do frontend para autorização real;
- não adicionar um proxy Next sem uma necessidade arquitetural clara.
