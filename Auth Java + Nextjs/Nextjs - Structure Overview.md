```
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

```
app/
├── login/
└── (private)/
```

É só a estrutura de páginas/rotas do Next.

`login/page.tsx`:

```
formulário de login
↓
login()
↓
redireciona para /dashboard
```

`(private)/layout.tsx`:

```
AuthProvider
↓
páginas autenticadas
```

E `(private)` não aparece na URL:

```
app/(private)/students/page.tsx

→ /students
```

---

### `components/auth-provider.tsx`

Responsável principalmente pelo caso:

```
F5
↓
access token sumiu da memória
↓
refreshOnce()
↓
recupera novo access token
```

Ele protege a árvore privada antes de renderizá-la.

```
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

```
Access Token
→ memória JavaScript
```

Contém:

```
getAccessToken()
setAccessToken()
clearAccessToken()
```

Não conhece refresh token.

---

## `auth-client.ts`

Implementação base.

Responsável por:

```
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

```
credentials: 'include'
```

para permitir que o browser trabalhe com o refresh cookie.

### Com Refresh Token Rotation

**Nenhum arquivo novo.**

```
auth-client.ts
```

continua igual.

O backend troca:

```
Refresh A
→ Refresh B
```

através de `Set-Cookie`, e o browser faz a substituição sozinho.

Nosso:

```
refreshOnce()
```

já deixa o frontend preparado para rotation.

---

## `csrf-store.ts` — somente com CSRF

Só aparece quando adicionarmos CSRF.

```
csrf-store.ts
→ CSRF token em memória
```

Contém:

```
getCsrfToken()
setCsrfToken()
clearCsrfToken()
```

Nesse caso também modificamos:

```
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

```
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

```
getAuthorities()

hasRole('ADMIN')

hasAuthority('student:create')
```

Exemplo:

```
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

```
apiFetch('/students')
```

sem conhecer JWT ou refresh.

### Com CSRF

Esse arquivo também passa a adicionar:

```
X-CSRF-TOKEN: ...
```

em:

```
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

```
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

```
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

```
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