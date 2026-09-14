## Caso queira adicionar Refresh Token Rotation no Next.js

No frontend, **quase nada muda**.

Isso acontece porque o refresh token continua sendo controlado pelo browser via `HttpOnly Cookie`.

Sem rotation:

```
POST /auth/refresh
 ↓
Browser envia Refresh A
 ↓
Spring devolve Access B
```

Com rotation:

```
POST /auth/refresh
 ↓
Browser envia Refresh A
 ↓
Spring invalida Refresh A
 ↓
Spring cria:
Access B
Refresh B
 ↓
JSON: Access B
Set-Cookie: Refresh B
```

O browser substitui automaticamente:

```
Refresh A
→ Refresh B
```

O JavaScript continua sem conhecer nenhum deles.

### 1. `refreshAccessToken()` não muda

Continua exatamente assim:

```
export async function refreshAccessToken() {
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
```

O Spring envia:

```
Set-Cookie: refresh_token=REFRESH_B
```

e o navegador atualiza o cookie sozinho.

---

## 2. `refreshOnce()` fica ainda mais importante

Com rotation não podemos deixar várias chamadas de `/auth/refresh` acontecerem simultaneamente.

Imagine:

```
Refresh A
```

e três requests fazem refresh juntas:

```
Request 1 → Refresh A
Request 2 → Refresh A
Request 3 → Refresh A
```

A primeira pode consumir `Refresh A`.

As outras duas encontrariam:

```
Refresh A
→ já revogado
```

Por isso mantemos:

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

Assim:

```
request A ─┐
request B ─┼──→ mesma chamada /auth/refresh
request C ─┘
```

E ocorre somente:

```
Refresh A
 ↓
Refresh B
```

uma vez.

---

## 3. `api-client.ts` não muda

Continua:

```
if (response.status === 401) {

  token =
    await refreshOnce()

  if (!token) {
    redirectToLogin()

    return response
  }

  response =
    await request(
      path,
      token,
      options,
    )
}
```

O frontend só conhece:

```
Access Token antigo
↓
401
↓
refreshOnce()
↓
Access Token novo
```

Ele não precisa saber que o backend também fez:

```
Refresh A
↓
Refresh B
```

---

## 4. Login e logout também não mudam

Login:

```
await fetch(
  `${API_URL}/auth/login`,
  {
    method: 'POST',
    credentials: 'include',
    // ...
  },
)
```

Spring cria:

```
Access A
Refresh A
```

Logout:

```
await fetch(
  `${API_URL}/auth/logout`,
  {
    method: 'POST',
    credentials: 'include',
  },
)
```

Spring revoga o refresh atual e remove o cookie.

---

## Resultado

No frontend, adicionar rotation significa essencialmente:

> **nenhuma mudança de arquitetura.**

O fluxo continua:

```
Next
 ↓
POST /auth/refresh
 ↓
Browser envia refresh cookie
 ↓
Spring
 ↓
JSON: novo access token
 ↓
Next salva access em memória
```

A diferença acontece escondida no backend/browser:

```
Refresh A
 ↓
Spring invalida A
 ↓
Set-Cookie Refresh B
 ↓
Browser substitui A por B
```

Por isso o `refreshOnce()` que já colocamos na implementação base é importante: ele deixa nosso frontend **já preparado para adicionar rotation depois**.