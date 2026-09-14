
```
src/main/java/com/example/app/

└── auth/
    │
    ├── AuthController.java
    ├── AuthService.java
    │
    ├── config/
    │   ├── SecurityConfig.java
    │   └── JwtConfig.java
    │
    ├── jwt/
    │   └── JwtService.java
    │
    ├── refresh/
    │   ├── RefreshToken.java
    │   ├── RefreshTokenRepository.java
    │   ├── RefreshTokenService.java
    │   └── RefreshResult.java              # somente com Rotation
    │
    ├── user/
    │   ├── User.java
    │   ├── UserRepository.java
    │   ├── Role.java                       # com Roles/Authorities
    │   └── Permission.java                 # com Permissions/Authorities
    │
    ├── dto/
    │   ├── LoginRequest.java
    │   ├── LoginResponse.java
    │   └── LoginResult.java
    │
    └── exception/
        └── AuthException.java
```

### Responsabilidade de cada parte

```
auth/
```

É o módulo de autenticação inteiro.

`AuthController.java`

```
POST /auth/login
POST /auth/refresh
POST /auth/logout

GET /auth/csrf        # somente com CSRF
```

`AuthService.java`

```
login()
refresh()
logout()
```

É quem orquestra autenticação. Ele usa `AuthenticationManager`, `JwtService` e `RefreshTokenService`.

---

### `config/`

```
config/
├── SecurityConfig.java
└── JwtConfig.java
```

`SecurityConfig`

```
Spring Security
CORS
STATELESS
UserDetailsService
AuthenticationManager
PasswordEncoder
endpoints públicos/protegidos
```

E futuramente:

```
.csrf(...)                    # com CSRF
@EnableMethodSecurity         # com Roles/Authorities
JwtAuthenticationConverter   # com Roles/Authorities
```

`JwtConfig`

```
SecretKey
JwtEncoder
JwtDecoder
```

Nada além disso.

---

### `jwt/`

```
jwt/
└── JwtService.java
```

Só cuida do **Access Token**:

```
User
 ↓
JWT
```

Hoje:

```
sub
iat
exp
issuer
```

Com Roles/Authorities:

```
sub
iat
exp
issuer
authorities      # com Roles/Authorities
```

---

### `refresh/`

```
refresh/
├── RefreshToken.java
├── RefreshTokenRepository.java
├── RefreshTokenService.java
└── RefreshResult.java       # somente com Rotation
```

Essa parte cuida exclusivamente do refresh token.

Na implementação base:

```
RefreshToken
→ entidade

RefreshTokenRepository
→ persistência

RefreshTokenService
→ create()
→ validate()
→ revoke()
```

Com Rotation:

```
validate()
↓
consume()
```

e aparece:

```
RefreshResult
```

porque `/refresh` passa a gerar:

```
Access Token novo
+
Refresh Token novo
```

---

### `user/`

```
user/
├── User.java
├── UserRepository.java
├── Role.java          # com Roles/Authorities
└── Permission.java    # com Permissions/Authorities
```

Nossa implementação base já tem:

```
User implements UserDetails
```

Então:

```
User
↓
UserDetailsService
↓
AuthenticationManager
```

Na base:

```
getAuthorities() {
    return List.of();
}
```

Quando adicionarmos Role:

```
Role.java
```

entra nessa pasta.

Quando adicionarmos permissões específicas:

```
Permission.java
```

entra também.

---

### `dto/`

```
dto/
├── LoginRequest.java
├── LoginResponse.java
└── LoginResult.java
```

`LoginRequest`

```
email
password
```

`LoginResponse`

```
accessToken
```

`LoginResult` é interno:

```
accessToken
refreshToken
```

Porque o controller precisa:

```
accessToken
→ JSON

refreshToken
→ Set-Cookie
```

O refresh **nunca vai no JSON para o frontend**.

---

### `exception/`

```
exception/
└── AuthException.java
```

Centraliza erros como:

```
credenciais inválidas
refresh inválido
refresh expirado
```

Se futuramente você já tiver um sistema global de exceptions/Problem Details no projeto, `AuthException` pode continuar aqui e o handler global tratá-la.

---

## Como a estrutura evolui

### Base

```
auth/
├── AuthController.java
├── AuthService.java
├── config/
│   ├── SecurityConfig.java
│   └── JwtConfig.java
├── jwt/
│   └── JwtService.java
├── refresh/
│   ├── RefreshToken.java
│   ├── RefreshTokenRepository.java
│   └── RefreshTokenService.java
├── user/
│   ├── User.java
│   └── UserRepository.java
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── LoginResult.java
└── exception/
    └── AuthException.java
```

### Com tudo que discutimos

```
auth/
├── AuthController.java
│   └── + GET /auth/csrf                     # CSRF
│
├── AuthService.java
│
├── config/
│   ├── SecurityConfig.java
│   │   ├── + CSRF config                    # CSRF
│   │   ├── + @EnableMethodSecurity          # Roles/Authorities
│   │   └── + JwtAuthenticationConverter     # Roles/Authorities
│   │
│   └── JwtConfig.java
│
├── jwt/
│   └── JwtService.java
│       └── + authorities claim              # Roles/Authorities
│
├── refresh/
│   ├── RefreshToken.java
│   ├── RefreshTokenRepository.java
│   ├── RefreshTokenService.java
│   │   └── validate → consume               # Rotation
│   │
│   └── RefreshResult.java                   # Rotation
│
├── user/
│   ├── User.java
│   │   └── getAuthorities()                 # Roles/Authorities
│   │
│   ├── UserRepository.java
│   ├── Role.java                            # Roles/Authorities
│   └── Permission.java                      # Permissions/Authorities
│
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── LoginResult.java
│
└── exception/
    └── AuthException.java
```

Eu **não criaria** coisas como:

```
domain/
application/
infrastructure/
ports/
adapters/
use-cases/
handlers/
```

dentro desse módulo agora. Para esse caso, só aumentaria a navegação sem nos dar benefício real.

A regra que eu usaria é simples:

```
auth/
→ fluxo geral

config/
→ Spring Security

jwt/
→ access token

refresh/
→ refresh token

user/
→ identidade usada pela autenticação

dto/
→ entrada/saída

exception/
→ erros do módulo
```

Essa estrutura suporta a implementação base e todas as três evoluções que definimos sem precisar reorganizar o módulo depois.