```text
src/
├── app/
│   ├── login/
│   │   └── page.tsx
│   │
│   └── (private)/
│       ├── layout.tsx
│       ├── dashboard/
│       │   └── page.tsx
│       ├── students/
│       │   └── page.tsx
│       └── ...
│
├── components/
│   └── auth-provider.tsx
│
└── lib/
    ├── auth/
    │   ├── token-store.ts
    │   ├── auth-client.ts
    │   ├── csrf-store.ts          # com CSRF
    │   ├── auth.types.ts          # com Roles/Authorities
    │   └── authorities.ts         # com Roles/Authorities
    │
    └── api/
        └── api-client.ts
```

## Responsabilidade de cada parte

### `app/`

```text
app/
├── login/
└── (private)/
```

É só a estrutura de páginas/rotas do Next.

`login/page.tsx`:

```text
formulário de login
↓
login()
↓
redireciona para /dashboard
```

`(private)/layout.tsx`:

```text
AuthProvider
↓
páginas autenticadas
```

E `(private)` não aparece na URL:

```text
app/(private)/students/page.tsx

→ /students
```

---

### `components/auth-provider.tsx`

Responsável principalmente pelo caso:

```text
F5
↓
access token sumiu da memória
↓
refreshOnce()
↓
recupera novo access token
```

Ele protege a árvore privada antes de renderizá-la.

```text
AuthProvider
├── access existe → libera
├── refresh válido → libera
└── refresh inválido → /login
```

---

# `lib/auth/`

Aqui fica toda a lógica de autenticação do frontend.

## `token-store.ts`

Implementação base.

```text
Access Token
→ memória JavaScript
```

Contém:

```text
getAccessToken()
setAccessToken()
clearAccessToken()
```

Não conhece refresh token.

---

## `auth-client.ts`

Implementação base.

Responsável por:

```text
login()
refreshAccessToken()
refreshOnce()
logout()
```

É onde estão as chamadas:

```
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

E onde usamos:

```text
credentials: 'include'
```

para permitir que o browser trabalhe com o refresh cookie.

### Com Refresh Token Rotation

**Nenhum arquivo novo.**

```text
auth-client.ts
```

continua igual.

O backend troca:

```text
Refresh A
→ Refresh B
```

através de `Set-Cookie`, e o browser faz a substituição sozinho.

Nosso:

```text
refreshOnce()
```

já deixa o frontend preparado para rotation.

---

## `csrf-store.ts` — somente com CSRF

Só aparece quando adicionarmos CSRF.

```text
csrf-store.ts
→ CSRF token em memória
```

Contém:

```text
getCsrfToken()
setCsrfToken()
clearCsrfToken()
```

Nesse caso também modificamos:

```text
auth-client.ts
api-client.ts
```

para enviar:

```
X-CSRF-TOKEN: ...
```

---

## `auth.types.ts` — com Roles/Authorities

Contém o formato das claims que esperamos no JWT:

```typescript
export type AccessTokenClaims = {
  sub: string
  authorities?: string[]
  iat: number
  exp: number
}
```

Só é necessário quando começarmos a ler authorities do access token.

---

## `authorities.ts` — com Roles/Authorities

Responsável por ler as authorities do JWT.

Contém:

```text
getAuthorities()

hasRole('ADMIN')

hasAuthority('student:create')
```

Exemplo:

```text
JWT

authorities:
[
  ROLE_ADMIN,
  student:read,
  student:create
]

       ↓

authorities.ts

       ↓

hasRole('ADMIN')
hasAuthority('student:create')
```

Essa informação serve apenas para **UX**.

A autorização real continua no Spring.

---

# `lib/api/`

## `api-client.ts`

É o ponto central para chamadas à API autenticada.

Ele:

```
pega access token
↓
adiciona Authorization: Bearer
↓
faz request
↓
recebeu 401?
↓
refreshOnce()
↓
repete request
```

Então o resto da aplicação pode fazer:

```text
apiFetch('/students')
```

sem conhecer JWT ou refresh.

### Com CSRF

Esse arquivo também passa a adicionar:

```
X-CSRF-TOKEN: ...
```

em:

```text
POST
PUT
PATCH
DELETE
```

### Com Rotation

Não muda.

### Com Roles/Authorities

Não muda.

---

# Estrutura base

Sem nenhuma evolução:

```text
src/
├── app/
│   ├── login/
│   │   └── page.tsx
│   │
│   └── (private)/
│       ├── layout.tsx
│       └── dashboard/
│           └── page.tsx
│
├── components/
│   └── auth-provider.tsx
│
└── lib/
    ├── auth/
    │   ├── token-store.ts
    │   └── auth-client.ts
    │
    └── api/
        └── api-client.ts
```

Com tudo que discutimos:

```text
src/
├── app/
│   ├── login/
│   │   └── page.tsx
│   │
│   └── (private)/
│       ├── layout.tsx
│       ├── dashboard/
│       │   └── page.tsx
│       └── ...
│
├── components/
│   └── auth-provider.tsx
│
└── lib/
    ├── auth/
    │   ├── token-store.ts
    │   ├── auth-client.ts
    │   │   └── + CSRF header logic           # CSRF
    │   │
    │   ├── csrf-store.ts                     # CSRF
    │   │
    │   ├── auth.types.ts                     # Roles/Authorities
    │   └── authorities.ts                    # Roles/Authorities
    │
    └── api/
        └── api-client.ts
            └── + CSRF header logic           # CSRF
```

E vale destacar:

```text
Refresh Token Rotation
→ nenhum arquivo novo

CSRF
→ csrf-store.ts
→ altera auth-client.ts
→ altera api-client.ts

Roles/Authorities
→ auth.types.ts
→ authorities.ts
```

Eu manteria exatamente essa estrutura. Ela deixa a autenticação isolada em `lib/auth`, as chamadas autenticadas em `lib/api` e não espalha lógica de JWT/cookies pelas páginas.