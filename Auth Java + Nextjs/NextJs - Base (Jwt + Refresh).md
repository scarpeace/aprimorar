```
Next.js App Router

Access Token
→ memória do JavaScript
→ 5 minutos
→ Authorization: Bearer

Refresh Token
→ frontend NÃO acessa
→ cookie HttpOnly criado pelo Spring
→ browser envia automaticamente

Sem rotation
Sem CSRF token
Sem roles/authorities
```

Esta implementação assume que a área privada funciona como uma **SPA**, com os dados sendo buscados pelo client — React Query/Kubb encaixam muito bem depois.

# Frontend base — Next.js

## 1. Fluxo

No login:

```
Login Form
    ↓
POST /auth/login
    ↓
Spring
    ↓
┌─────────────────────────┐
│ JSON                    │
│ accessToken             │
│      ↓                  │
│ memória do frontend     │
└─────────────────────────┘

┌─────────────────────────┐
│ Set-Cookie              │
│ refresh_token           │
│      ↓                  │
│ browser                 │
└─────────────────────────┘
```

Request normal:

```
Frontend
   ↓
Authorization: Bearer ACCESS_TOKEN
   ↓
Spring
```

Depois de um F5:

```
F5
 ↓
accessToken desapareceu
 ↓
refresh cookie continua
 ↓
POST /auth/refresh
 ↓
browser manda cookie automaticamente
 ↓
Spring devolve novo accessToken
 ↓
frontend coloca em memória
```

Se o access token expirar durante o uso:

```
Request
 ↓
401
 ↓
POST /auth/refresh
 ↓
novo accessToken
 ↓
repete request original
```

---

# 2. Estrutura

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

`.env.local`:

```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

# 3. `token-store.ts`

O access token fica somente em uma variável JavaScript.

```
// src/lib/auth/token-store.ts

import 'client-only'

let accessToken: string | null = null

export function getAccessToken() {
  return accessToken
}

export function setAccessToken(
  token: string,
) {
  accessToken = token
}

export function clearAccessToken() {
  accessToken = null
}
```

Não usamos:

```
localStorage ❌
sessionStorage ❌
cookie JavaScript ❌
```

Consequência:

```
navegação normal SPA
→ token continua existindo

F5
→ token desaparece

fecha aba
→ token desaparece
```

Isso é intencional.

---

# 4. `auth-client.ts`

Esse arquivo cuida somente de:

```
login
refresh
logout
```

```
// src/lib/auth/auth-client.ts

import 'client-only'

import {
  clearAccessToken,
  setAccessToken,
} from './token-store'

const API_URL =
  process.env.NEXT_PUBLIC_API_URL!

type AuthResponse = {
  accessToken: string
}
```

## Login

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

      /*
       * Permite que o browser
       * receba/envie cookies
       * nessa comunicação.
       */
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

  /*
   * Apenas o access token
   * entra no JavaScript.
   */
  setAccessToken(
    data.accessToken,
  )

  return data.accessToken
}
```

O Spring responde:

```
JSON:
accessToken
→ nosso código recebe

Set-Cookie:
refresh_token
→ browser recebe
```

O frontend nunca recebe o refresh token no JSON.

---

# 5. Refresh

No mesmo `auth-client.ts`:

```
export async function
refreshAccessToken() {

  const response = await fetch(
    `${API_URL}/auth/refresh`,
    {
      method: 'POST',

      /*
       * Faz o navegador enviar:
       *
       * Cookie:
       * refresh_token=...
       */
      credentials: 'include',
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

Repare que não existe:

```
const refreshToken = ...
```

Isso é importante.

O refresh token é:

```
HttpOnly
↓
controlado pelo browser
↓
inacessível ao JavaScript
```

---

# 6. Evitar vários refresh ao mesmo tempo

Imagine que 5 requests ocorram ao mesmo tempo depois de um F5.

Não queremos:

```
5 × POST /auth/refresh
```

Então ainda em `auth-client.ts`:

```
let refreshPromise:
  Promise<string | null> | null =
    null

export function refreshOnce() {

  if (!refreshPromise) {

    refreshPromise =
      refreshAccessToken()
        .finally(() => {
          refreshPromise = null
        })
  }

  return refreshPromise
}
```

Agora:

```
request A ─┐
request B ─┼→ mesma Promise
request C ─┘
              ↓
        /auth/refresh
```

É só uma proteção simples contra chamadas duplicadas.

---

# 7. Logout

Também no `auth-client.ts`:

```
export async function logout() {

  try {

    await fetch(
      `${API_URL}/auth/logout`,
      {
        method: 'POST',

        credentials:
          'include',
      },
    )

  } finally {

    /*
     * Independentemente da resposta,
     * removemos o access local.
     */
    clearAccessToken()
  }
}
```

O Spring:

```
revoga refresh no banco
+
expira refresh cookie
```

O Next:

```
remove access token
da memória
```

---

# 8. Arquivo completo `auth-client.ts`

Juntando:

```
// src/lib/auth/auth-client.ts

import 'client-only'

import {
  clearAccessToken,
  setAccessToken,
} from './token-store'

const API_URL =
  process.env.NEXT_PUBLIC_API_URL!

type AuthResponse = {
  accessToken: string
}

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

  return data.accessToken
}

export async function
refreshAccessToken() {

  const response = await fetch(
    `${API_URL}/auth/refresh`,
    {
      method: 'POST',

      credentials: 'include',
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

let refreshPromise:
  Promise<string | null> | null =
    null

export function refreshOnce() {

  if (!refreshPromise) {

    refreshPromise =
      refreshAccessToken()
        .finally(() => {
          refreshPromise = null
        })
  }

  return refreshPromise
}

export async function logout() {

  try {

    await fetch(
      `${API_URL}/auth/logout`,
      {
        method: 'POST',
        credentials: 'include',
      },
    )

  } finally {

    clearAccessToken()
  }
}
```

---

# 9. `api-client.ts`

Agora criamos um único lugar responsável por colocar o JWT nas requests.

```
// src/lib/api/api-client.ts

import 'client-only'

import {
  getAccessToken,
} from '@/lib/auth/token-store'

import {
  refreshOnce,
} from '@/lib/auth/auth-client'

const API_URL =
  process.env.NEXT_PUBLIC_API_URL!

export async function apiFetch(
  path: string,
  options: RequestInit = {},
) {

  let token =
    getAccessToken()

  /*
   * Não temos access token.
   *
   * O caso mais comum é F5.
   */
  if (!token) {

    token =
      await refreshOnce()

    if (!token) {

      redirectToLogin()

      throw new Error(
        'Unauthenticated',
      )
    }
  }

  let response =
    await request(
      path,
      token,
      options,
    )

  /*
   * O access token pode ter
   * expirado durante o uso.
   */
  if (response.status === 401) {

    token =
      await refreshOnce()

    if (!token) {

      redirectToLogin()

      return response
    }

    /*
     * Repete a mesma request
     * com o access token novo.
     */
    response =
      await request(
        path,
        token,
        options,
      )
  }

  return response
}

function request(
  path: string,
  token: string,
  options: RequestInit,
) {

  return fetch(
    `${API_URL}${path}`,
    {
      ...options,

      headers: {
        ...options.headers,

        Authorization:
          `Bearer ${token}`,
      },
    },
  )
}

function redirectToLogin() {

  window.location.replace(
    '/login',
  )
}
```

A aplicação agora pode simplesmente fazer:

```
const response =
  await apiFetch('/students')
```

E não precisa saber:

```
JWT
refresh
401
Bearer
cookie
```

---

# 10. Como `apiFetch` funciona

Se há access token:

```
apiFetch()
 ↓
access existe
 ↓
request com Bearer
 ↓
200
```

Depois de F5:

```
apiFetch()
 ↓
access = null
 ↓
refreshOnce()
 ↓
POST /auth/refresh
 ↓
novo access
 ↓
request original
```

Se ele expirou enquanto o usuário estava navegando:

```
apiFetch()
 ↓
request com access antigo
 ↓
401
 ↓
refreshOnce()
 ↓
novo access
 ↓
repete request
```

---

# 11. `AuthProvider`

Queremos evitar que uma página privada apareça antes de recuperarmos a sessão depois de um F5.

```
// src/components/auth-provider.tsx

'use client'

import {
  useEffect,
  useState,
} from 'react'

import {
  getAccessToken,
} from '@/lib/auth/token-store'

import {
  refreshOnce,
} from '@/lib/auth/auth-client'

export function AuthProvider({
  children,
}: {
  children: React.ReactNode
}) {

  const [ready, setReady] =
    useState(false)

  useEffect(() => {

    async function initialize() {

      /*
       * Usuário está navegando
       * normalmente pela SPA.
       */
      if (getAccessToken()) {

        setReady(true)

        return
      }

      /*
       * Access não existe.
       *
       * Ex.: usuário deu F5
       * ou abriu a aplicação novamente.
       */
      const token =
        await refreshOnce()

      if (!token) {

        window.location.replace(
          '/login',
        )

        return
      }

      setReady(true)
    }

    initialize()

  }, [])

  if (!ready) {
    return <div>Loading...</div>
  }

  return children
}
```

# 12. Layout privado

`app/(private)/layout.tsx`:

```
import {
  AuthProvider,
} from '@/components/auth-provider'

export default function PrivateLayout({
  children,
}: {
  children: React.ReactNode
}) {

  return (
    <AuthProvider>
      {children}
    </AuthProvider>
  )
}
```

Podemos organizar:

```
app/
└── (private)/
    ├── layout.tsx
    ├── dashboard/
    ├── students/
    ├── appointments/
    └── payments/
```

O `(private)` não aparece na URL.

Por exemplo:

```
app/(private)/students/page.tsx

→ /students
```

---

# 13. Login

Exemplo propositalmente simples:

```
// src/app/login/page.tsx

'use client'

import {
  FormEvent,
} from 'react'

import {
  useRouter,
} from 'next/navigation'

import {
  login,
} from '@/lib/auth/auth-client'

export default function LoginPage() {

  const router =
    useRouter()

  async function handleSubmit(
    event:
      FormEvent<HTMLFormElement>,
  ) {

    event.preventDefault()

    const form =
      new FormData(
        event.currentTarget,
      )

    const email =
      String(
        form.get('email'),
      )

    const password =
      String(
        form.get('password'),
      )

    try {

      await login(
        email,
        password,
      )

      router.replace(
        '/dashboard',
      )

    } catch {

      alert(
        'Email ou senha inválidos',
      )
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
    >

      <input
        name="email"
        type="email"
      />

      <input
        name="password"
        type="password"
      />

      <button type="submit">
        Entrar
      </button>

    </form>
  )
}
```

Depois você pode colocar Zod/TanStack Form/etc. sem mudar o fluxo de autenticação.

---

# 14. Logout em um componente

Exemplo:

```
'use client'

import {
  useRouter,
} from 'next/navigation'

import {
  logout,
} from '@/lib/auth/auth-client'

export function LogoutButton() {

  const router =
    useRouter()

  async function handleLogout() {

    await logout()

    router.replace(
      '/login',
    )
  }

  return (
    <button
      onClick={handleLogout}
    >
      Sair
    </button>
  )
}
```

---

# 15. Fluxo completo

### Login

```
login()
 ↓
POST /auth/login
 ↓
Spring
 ↓

JSON:
accessToken
 ↓
setAccessToken()
 ↓
memória

Set-Cookie:
refresh_token
 ↓
browser
```

### Request normal

```
apiFetch('/students')
 ↓
getAccessToken()
 ↓
Authorization:
Bearer <access>
 ↓
Spring
```

### JWT expirou

```
request
 ↓
401
 ↓
refreshOnce()
 ↓
POST /auth/refresh
 ↓
browser manda refresh cookie
 ↓
novo accessToken
 ↓
memória
 ↓
repete request
```

### F5

```
F5
 ↓
access = null

refresh cookie continua

 ↓

AuthProvider
 ↓
refreshOnce()
 ↓
novo access
 ↓
aplicação liberada
```

### Refresh expirou

```
POST /auth/refresh
 ↓
401
 ↓
access apagado
 ↓
/login
```

### Logout

```
POST /auth/logout
 ↓
Spring revoga refresh
 ↓
Spring remove cookie
 ↓
Next remove access
 ↓
/login
```

---

## 16. Responsabilidade de cada arquivo

Essa é provavelmente a parte mais útil para guardar:

```
token-store.ts
→ guarda access token em memória

auth-client.ts
→ login
→ refresh
→ logout

api-client.ts
→ coloca Bearer
→ trata 401
→ tenta refresh
→ repete request

AuthProvider
→ recupera sessão após F5

(private)/layout.tsx
→ protege a área privada
```

E o frontend **não gerencia o refresh cookie**:

```
Spring
→ cria cookie

Browser
→ guarda cookie
→ envia cookie
→ remove cookie quando Spring manda

Next
→ não lê refresh token
```

Essa é a base. Na próxima parte, **Refresh Token Rotation praticamente não exigirá mudança nenhuma no frontend**, justamente porque o novo refresh continua chegando via `Set-Cookie`.