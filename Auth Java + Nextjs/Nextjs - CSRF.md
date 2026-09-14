# Caso queira adicionar CSRF no Next.js

Com CSRF habilitado no Spring, nosso frontend passa a enviar um segundo token nas operações que alteram estado:

```
Access Token
→ autenticação
→ Authorization: Bearer

CSRF Token
→ proteção contra request forjado
→ X-CSRF-TOKEN
```

O refresh token continua completamente separado:

```
refresh_token
→ HttpOnly Cookie
→ browser controla
```

## 1. Criar `csrf-store.ts`

Assim como o access token, podemos manter o CSRF token em memória.

```
// src/lib/auth/csrf-store.ts

import 'client-only'

let csrfToken: string | null = null

export function getCsrfToken() {
  return csrfToken
}

export function setCsrfToken(
  token: string,
) {
  csrfToken = token
}

export function clearCsrfToken() {
  csrfToken = null
}
```

Diferentemente do refresh token, **não há problema de o JavaScript conhecer o CSRF token**.

---

## 2. Criar função para buscar o token

No `auth-client.ts`:

```
import {
  getCsrfToken,
  setCsrfToken,
} from './csrf-store'
```

Adicione:

```
export async function loadCsrfToken() {
  const response = await fetch(
    `${API_URL}/auth/csrf`,
    {
      credentials: 'include',
    },
  )

  if (!response.ok) {
    throw new Error(
      'Could not load CSRF token',
    )
  }

  const data = await response.json()

  setCsrfToken(data.token)

  return data.token as string
}
```

Nosso Spring possui:

```
GET /auth/csrf
```

e devolve algo como:

```
{
  "token": "abc123..."
}
```

---

## 3. Criar `getOrLoadCsrfToken()`

Para não ficar buscando toda hora:

```
export async function getOrLoadCsrfToken() {
  const token = getCsrfToken()

  if (token) {
    return token
  }

  return loadCsrfToken()
}
```

Então:

```
CSRF existe na memória?
       │
   sim │ não
       │  ↓
       │ /auth/csrf
       │  ↓
       └──token
```

---

# 4. Login passa a enviar CSRF

Como:

```
POST /auth/login
```

é um `POST`, o Spring também exigirá CSRF.

Antes:

```
export async function login(
  email: string,
  password: string,
) {
  const response = await fetch(
    `${API_URL}/auth/login`,
    {
      method: 'POST',

      headers: {
        'Content-Type':
          'application/json',
      },

      credentials: 'include',

      body: JSON.stringify({
        email,
        password,
      }),
    },
  )

  // ...
}
```

Agora:

```
export async function login(
  email: string,
  password: string,
) {
  const csrf =
    await getOrLoadCsrfToken()

  const response = await fetch(
    `${API_URL}/auth/login`,
    {
      method: 'POST',

      headers: {
        'Content-Type':
          'application/json',

        'X-CSRF-TOKEN':
          csrf,
      },

      credentials: 'include',

      body: JSON.stringify({
        email,
        password,
      }),
    },
  )

  if (!response.ok) {
    throw new Error(
      'Invalid credentials',
    )
  }

  const data: AuthResponse =
    await response.json()

  setAccessToken(
    data.accessToken,
  )

  /*
   * Depois do login buscamos
   * um CSRF token novo.
   */
  await loadCsrfToken()

  return data.accessToken
}
```

A parte nova é:

```
X-CSRF-TOKEN: abc123
```

---

# 5. Refresh também envia CSRF

Antes:

```
POST /auth/refresh

Cookie:
refresh_token=...
```

Agora:

```
Cookie:
refresh_token=...

+

X-CSRF-TOKEN:
abc123
```

Código:

```
export async function
refreshAccessToken() {

  const csrf =
    await getOrLoadCsrfToken()

  const response = await fetch(
    `${API_URL}/auth/refresh`,
    {
      method: 'POST',

      credentials: 'include',

      headers: {
        'X-CSRF-TOKEN':
          csrf,
      },
    },
  )

  if (!response.ok) {

    clearAccessToken()

    return null
  }

  const data: AuthResponse =
    await response.json()

  setAccessToken(
    data.accessToken,
  )

  return data.accessToken
}
```

`refreshOnce()` continua exatamente igual.

---

# 6. Logout também envia CSRF

```
export async function logout() {

  const csrf =
    await getOrLoadCsrfToken()

  try {

    await fetch(
      `${API_URL}/auth/logout`,
      {
        method: 'POST',

        credentials: 'include',

        headers: {
          'X-CSRF-TOKEN':
            csrf,
        },
      },
    )

  } finally {

    clearAccessToken()
    clearCsrfToken()
  }
}
```

---

# 7. `apiFetch` envia CSRF em operações de escrita

Não precisamos enviar CSRF em:

```
GET
HEAD
OPTIONS
```

Precisamos principalmente em:

```
POST
PUT
PATCH
DELETE
```

Podemos criar:

```
function requiresCsrf(
  method?: string,
) {
  const normalized =
    method?.toUpperCase()
      ?? 'GET'

  return [
    'POST',
    'PUT',
    'PATCH',
    'DELETE',
  ].includes(normalized)
}
```

E modificar nosso `request()`:

```
async function request(
  path: string,
  token: string,
  options: RequestInit,
) {

  const csrf =
    requiresCsrf(options.method)
      ? await getOrLoadCsrfToken()
      : null

  return fetch(
    `${API_URL}${path}`,
    {
      ...options,

      headers: {
        ...options.headers,

        Authorization:
          `Bearer ${token}`,

        ...(csrf && {
          'X-CSRF-TOKEN':
            csrf,
        }),
      },
    },
  )
}
```

O restante do `apiFetch()` não muda.

---

# 8. O fluxo de uma request passa a ser

GET:

```
GET /students

Authorization:
Bearer ACCESS_TOKEN
```

Só isso.

POST:

```
POST /students

Authorization:
Bearer ACCESS_TOKEN

X-CSRF-TOKEN:
abc123
```

---

# 9. E `/auth/refresh`?

É um caso especial porque a autenticação dele vem do cookie:

```
POST /auth/refresh

Cookie:
refresh_token=XYZ

X-CSRF-TOKEN:
ABC
```

O Spring verifica:

```
refresh válido?
+
CSRF válido?
        ↓
      sim
        ↓
novo access token
```

---

# 10. Arquivos novos/modificados

Nossa estrutura passa de:

```
lib/auth/
├── token-store.ts
└── auth-client.ts
```

para:

```
lib/auth/
├── token-store.ts
├── csrf-store.ts       ← novo
└── auth-client.ts      ← alterado
```

E:

```
lib/api/
└── api-client.ts       ← alterado
```

---

# 11. O que NÃO muda

Nada disso muda:

```
Access Token
→ memória

Refresh Token
→ HttpOnly Cookie

Bearer
→ requests normais

refreshOnce()
→ evita múltiplos refresh

AuthProvider
→ recupera sessão depois de F5
```

CSRF é só uma camada adicional:

```
POST / PUT / PATCH / DELETE

Bearer / Cookie
        +
CSRF Token
        ↓
Spring
```

A divisão fica bem clara:

```
Access Token
→ quem é você?

Refresh Token
→ pode criar outro access?

CSRF Token
→ essa request veio
  legitimamente da aplicação?
```

E, caso também esteja usando **Refresh Token Rotation**, nada muda nessa implementação: o browser continua recebendo o novo refresh via `Set-Cookie`, enquanto o frontend continua lidando apenas com access token + CSRF token.