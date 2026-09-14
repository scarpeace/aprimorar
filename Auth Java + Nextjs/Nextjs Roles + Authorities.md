# Caso queira adicionar Roles e Authorities no Next.js

No backend, passamos a colocar no JWT:

```
{
  "sub": "user-id",
  "authorities": [
    "ROLE_ADMIN",
    "student:read",
    "student:create"
  ]
}
```

No frontend podemos ler essas informações para controlar a **interface**:

```
ROLE_ADMIN
→ mostrar área administrativa

student:create
→ mostrar botão "Novo aluno"
```

Mas existe uma regra importante:

> **Frontend nunca é responsável pela segurança.**

Esconder um botão não impede ninguém de chamar a API manualmente.

A segurança continua sendo:

```
@PreAuthorize(
    "hasAuthority('student:create')"
)
```

no Spring.

O Next usa authorities apenas para **UX**.

---

## 1. Instalar `jwt-decode`

Para ler as claims do access token:

```
npm install jwt-decode
```

Não estamos validando o JWT no frontend.

Só estamos lendo seu conteúdo.

```
Spring
→ valida JWT

Next
→ apenas lê informações do JWT
```

---

# 2. Criar o tipo das claims

`src/lib/auth/auth.types.ts`

```
export type AccessTokenClaims = {
  sub: string

  authorities?: string[]

  iat: number
  exp: number
}
```

---

# 3. Criar helpers

`src/lib/auth/authorities.ts`

```
import 'client-only'

import {
  jwtDecode,
} from 'jwt-decode'

import {
  getAccessToken,
} from './token-store'

import type {
  AccessTokenClaims,
} from './auth.types'

export function getAuthorities() {

  const token =
    getAccessToken()

  if (!token) {
    return []
  }

  const claims =
    jwtDecode<AccessTokenClaims>(
      token,
    )

  return claims.authorities ?? []
}
```

Agora podemos fazer:

```
getAuthorities()
```

e receber:

```
[
  'ROLE_ADMIN',
  'student:read',
  'student:create',
]
```

---

# 4. Criar `hasAuthority`

No mesmo arquivo:

```
export function hasAuthority(
  authority: string,
) {

  return getAuthorities()
    .includes(authority)
}
```

Uso:

```
hasAuthority(
  'student:create',
)
```

---

# 5. Criar `hasRole`

Também podemos facilitar roles:

```
export function hasRole(
  role: string,
) {

  return hasAuthority(
    `ROLE_${role}`,
  )
}
```

Então:

```
hasRole('ADMIN')
```

procura:

```
ROLE_ADMIN
```

Exatamente como o Spring.

---

# 6. Arquivo completo

`src/lib/auth/authorities.ts`

```
import 'client-only'

import {
  jwtDecode,
} from 'jwt-decode'

import {
  getAccessToken,
} from './token-store'

import type {
  AccessTokenClaims,
} from './auth.types'

export function getAuthorities() {

  const token =
    getAccessToken()

  if (!token) {
    return []
  }

  const claims =
    jwtDecode<AccessTokenClaims>(
      token,
    )

  return claims.authorities ?? []
}

export function hasAuthority(
  authority: string,
) {

  return getAuthorities()
    .includes(authority)
}

export function hasRole(
  role: string,
) {

  return hasAuthority(
    `ROLE_${role}`,
  )
}
```

---

# 7. Mostrar componente por Role

Por exemplo:

```
'use client'

import {
  hasRole,
} from '@/lib/auth/authorities'

export function AdminMenu() {

  if (!hasRole('ADMIN')) {
    return null
  }

  return (
    <a href="/admin">
      Administração
    </a>
  )
}
```

Então:

```
ROLE_ADMIN
→ mostra menu

sem ROLE_ADMIN
→ não mostra
```

---

# 8. Mostrar ação por Authority

Isso é mais interessante conforme o sistema cresce.

```
'use client'

import {
  hasAuthority,
} from '@/lib/auth/authorities'

export function CreateStudentButton() {

  if (
    !hasAuthority(
      'student:create',
    )
  ) {
    return null
  }

  return (
    <button>
      Novo aluno
    </button>
  )
}
```

Backend:

```
@PreAuthorize(
    "hasAuthority('student:create')"
)
@PostMapping("/students")
public void createStudent() {
}
```

Frontend:

```
hasAuthority(
  'student:create',
)
```

Assim ambos usam exatamente o mesmo nome:

```
student:create
```

---

# 9. Role vs Authority no frontend

Eu manteria a mesma linguagem do Spring:

```
Role
→ grupo

Authority
→ permissão efetiva
```

Exemplo:

```
ADMIN
        ↓
ROLE_ADMIN
student:read
student:create
student:update
payment:create
```

Então:

```
hasRole('ADMIN')
```

é útil para coisas maiores:

```
menu administrativo
dashboard administrativo
área exclusiva
```

E:

```
hasAuthority(
  'student:create',
)
```

para ações específicas:

```
criar aluno
cancelar atendimento
registrar pagamento
```

---

# 10. O refresh continua funcionando normalmente

Quando acontece:

```
Access A
 ↓
expirou
 ↓
/auth/refresh
 ↓
Access B
```

o novo JWT já contém as authorities atuais:

```
{
  "authorities": [
    "ROLE_ADMIN",
    "student:create"
  ]
}
```

Nosso código já faz:

```
setAccessToken(
  data.accessToken,
)
```

Então a próxima vez que:

```
getAuthorities()
```

for chamada, ela lê o **novo token**.

Não precisamos armazenar roles separadamente.

---

# 11. Não salvar authorities separadamente

Eu evitaria:

```
localStorage.setItem(
  'role',
  'ADMIN',
)
```

ou:

```
let permissions = [...]
```

porque começaríamos a duplicar informação.

Já temos:

```
Access Token
      ↓
authorities
```

Então:

```
JWT
→ única fonte no frontend
```

---

# 12. Estrutura

Nossa pasta passa a ter:

```
lib/
└── auth/
    ├── token-store.ts
    ├── auth-client.ts
    ├── auth.types.ts       ← novo
    └── authorities.ts      ← novo
```

O restante não precisa mudar.

---

# 13. Fluxo final

Backend cria:

```
User
 ↓
getAuthorities()
 ↓
JWT

[
  ROLE_ADMIN,
  student:read,
  student:create
]
```

Next recebe:

```
JWT
 ↓
jwtDecode()
 ↓
authorities
```

E usa para UX:

```
hasRole('ADMIN')

hasAuthority('student:create')
```

Enquanto o Spring continua fazendo a segurança real:

```
JWT
 ↓
Spring Security
 ↓
@PreAuthorize(...)
```

A regra para guardar é:

```
Next
→ "devo mostrar esse botão?"

Spring
→ "o usuário realmente pode executar essa ação?"
```

Essa separação é a mais importante.