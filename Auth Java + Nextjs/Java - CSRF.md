# Caso queira adicionar CSRF

CSRF resolve outro problema:

```
Rotation
→ refresh token roubado/reutilizado

CSRF
→ outro site tenta fazer o browser
  usar seus cookies automaticamente
```

Como nossa implementação usa `refresh_token` em cookie, podemos futuramente habilitar a proteção CSRF do próprio Spring.

Estamos usando **Spring Security 7**, que possui configuração específica para SPA através de:

```
.csrf(csrf -> csrf.spa())
```

## 1. Habilitar CSRF no `SecurityConfig`

Na implementação base temos:

```
.csrf(
    csrf ->
        csrf.disable()
)
```

Substitua por:

```
.csrf(
    csrf ->
        csrf.spa()
)
```

Só isso já ativa a infraestrutura de CSRF apropriada para uma SPA no Spring Security 7.

---

## 2. Criar endpoint para obter o CSRF Token

No `AuthController`:

```
@GetMapping("/csrf")
public CsrfToken csrf(
    CsrfToken csrfToken
) {

    return csrfToken;
}
```

Como já temos:

```
.requestMatchers("/auth/**")
.permitAll()
```

o endpoint:

```
GET /auth/csrf
```

também fica acessível sem autenticação.

A própria documentação do Spring recomenda que uma aplicação JavaScript obtenha o token na inicialização e novamente depois de login/logout quando necessário.

---

## 3. O frontend passa a enviar o token

Primeiro ele faz:

```
GET /auth/csrf
```

Recebe algo conceitualmente como:

```
{
  "headerName": "X-CSRF-TOKEN",
  "parameterName": "_csrf",
  "token": "abc123..."
}
```

Depois, em uma operação como:

```
POST /auth/refresh
```

envia:

```
X-CSRF-TOKEN: abc123...
```

junto do cookie:

```
Cookie: refresh_token=...
```

Ou seja:

```
POST /auth/refresh

Cookie:
refresh_token=XYZ
        +
X-CSRF-TOKEN:
ABC
        ↓
Spring
        ↓
ambos válidos?
        ↓
request aceita
```

O Spring aceita CSRF token através de header em aplicações JavaScript.

---

## 4. CORS

Como o frontend agora envia um header adicional, acrescente-o:

```
config.setAllowedHeaders(
    List.of(
        "Authorization",
        "Content-Type",
        "X-CSRF-TOKEN"
    )
);
```

---

## 5. O que muda conceitualmente

Antes:

```
POST /auth/refresh
        ↓
refresh cookie
        ↓
Spring
```

Depois:

```
POST /auth/refresh
        ↓
refresh cookie
+
CSRF token no header
        ↓
Spring
```

O refresh token continua:

```
HttpOnly
Secure
SameSite=Lax
```

O CSRF token **não é uma credencial de login** e pode ser conhecido pelo JavaScript. A proteção funciona justamente porque um site externo pode provocar o envio automático de cookies, mas não consegue simplesmente fornecer o header CSRF correto.

Uma consequência importante: habilitando `.csrf(csrf -> csrf.spa())` dessa forma, o Spring exigirá CSRF nos métodos HTTP considerados inseguros, como `POST`, `PUT`, `PATCH` e `DELETE`, não apenas no `/auth/refresh`. Portanto, quando adicionarmos isso ao frontend, nosso client HTTP deverá anexar o CSRF token automaticamente nessas requisições.