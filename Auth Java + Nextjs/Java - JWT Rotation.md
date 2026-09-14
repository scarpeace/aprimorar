# Caso queira adicionar Refresh Token Rotation

Na implementação base:

```
Login
→ Refresh A

/auth/refresh
→ valida Refresh A
→ cria Access B

/auth/refresh
→ valida o mesmo Refresh A
→ cria Access C
```

Com rotation:

```
Login
→ Refresh A

/auth/refresh
→ invalida Refresh A
→ cria Access B
→ cria Refresh B

/auth/refresh
→ invalida Refresh B
→ cria Access C
→ cria Refresh C
```

Isso **não adiciona uma request extra**. O novo access e o novo refresh são devolvidos na mesma chamada de `/auth/refresh`.

## 1. Trocar `validate()` por `consume()`

No `RefreshTokenService`, substitua:

```
public UUID validate(String rawToken)
```

por:

```
@Transactional
public UUID consume(
    String rawToken
) {

    RefreshToken token =
        repository
            .findByTokenHash(
                hash(rawToken)
            )
            .orElseThrow(
                () -> new AuthException(
                    "Invalid refresh token"
                )
            );

    if (!token.isValid()) {

        throw new AuthException(
            "Invalid refresh token"
        );
    }

    /*
     * Este refresh token acabou
     * de ser usado e não poderá
     * ser utilizado novamente.
     */
    token.revoke();

    return token.getUserId();
}
```

`consume` significa literalmente:

```
validar
+
usar
+
invalidar
```

---

## 2. Criar `RefreshResult`

```
public record RefreshResult(
    String accessToken,
    String refreshToken
) {
}
```

Antes o refresh retornava somente:

```
Access Token
```

Agora retorna internamente:

```
Access Token novo
+
Refresh Token novo
```

---

## 3. Alterar `AuthService.refresh()`

Antes:

```
public String refresh(
    String refreshToken
) {

    UUID userId =
        refreshTokenService
            .validate(refreshToken);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new AuthException(
                    "User not found"
                )
            );

    return jwtService
        .generateAccessToken(user);
}
```

Com rotation:

```
public RefreshResult refresh(
    String oldRefreshToken
) {

    /*
     * Valida e invalida
     * o refresh antigo.
     */
    UUID userId =
        refreshTokenService
            .consume(
                oldRefreshToken
            );

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new AuthException(
                    "User not found"
                )
            );

    String accessToken =
        jwtService
            .generateAccessToken(
                user
            );

    /*
     * Cria o próximo refresh.
     */
    String newRefreshToken =
        refreshTokenService
            .create(userId);

    return new RefreshResult(
        accessToken,
        newRefreshToken
    );
}
```

---

## 4. Alterar `/auth/refresh`

Antes o endpoint só devolvia o novo access.

Agora também envia um novo `Set-Cookie`:

```
@PostMapping("/refresh")
public ResponseEntity<LoginResponse>
refresh(

    @CookieValue("refresh_token")
    String oldRefreshToken

) {

    RefreshResult result =
        authService.refresh(
            oldRefreshToken
        );

    ResponseCookie cookie =
        createRefreshCookie(
            result.refreshToken()
        );

    return ResponseEntity
        .ok()

        .header(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
        )

        .body(
            new LoginResponse(
                result.accessToken()
            )
        );
}
```

O resultado é:

```
Request:

Cookie:
Refresh A

        ↓

POST /auth/refresh

        ↓

Response:

JSON:
Access B

Set-Cookie:
Refresh B
```

O browser substitui automaticamente o cookie antigo pelo novo.

O frontend não precisa conhecer o refresh token.