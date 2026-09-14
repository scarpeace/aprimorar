# Autenticação Base — Spring Boot

## 1. Fluxo

No login:

```
email + senha
     ↓
AuthenticationManager
     ↓
UserDetailsService
     ↓
User implements UserDetails
     ↓
PasswordEncoder
     ↓
autenticado
     ↓
┌─────────────────────┐
│ Access Token        │
│ JWT                 │
│ 5 minutos           │
└─────────────────────┘

┌─────────────────────┐
│ Refresh Token       │
│ string aleatória    │
│ 30 dias             │
└─────────────────────┘
```

O access token será retornado no JSON.

O refresh token será:

```
original
→ cookie HttpOnly

SHA-256(original)
→ banco
```

Nas requisições normais:

```
Authorization: Bearer <access-token>
              ↓
        Spring Security
              ↓
          JwtDecoder
              ↓
          Controller
```

Quando o access token expirar:

```
POST /auth/refresh
        ↓
refresh token vem no cookie
        ↓
Spring valida no banco
        ↓
gera novo access token
```

O refresh token permanece o mesmo.

---

# 2. Estrutura

```
src/main/java/com/example/app/

├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── JwtService.java
│   ├── RefreshTokenService.java
│   ├── RefreshToken.java
│   ├── RefreshTokenRepository.java
│   ├── AuthException.java
│   └── dto/
│       ├── LoginRequest.java
│       ├── LoginResponse.java
│       └── LoginResult.java
│
├── security/
│   ├── JwtConfig.java
│   └── SecurityConfig.java
│
└── user/
    ├── User.java
    └── UserRepository.java
```

---

# 3. Dependências

Além de JPA e seu banco:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

O `oauth2-resource-server` faz o Spring entender automaticamente:

```
Authorization: Bearer <jwt>
```

Não criaremos um `JwtFilter` manual.

---

# 4. Configuração

`application.yml`:

```
app:
  auth:
    issuer: my-app
    access-token-minutes: 5
    refresh-token-days: 30

    frontend-origin: ${FRONTEND_ORIGIN:http://localhost:3000}

    # false apenas durante desenvolvimento HTTP local
    cookie-secure: ${AUTH_COOKIE_SECURE:false}

security:
  jwt:
    secret: ${JWT_SECRET}
```

Para gerar a chave:

```
openssl rand -base64 32
```

Depois:

```
JWT_SECRET=sua-chave-aqui
```

Em produção:

```
AUTH_COOKIE_SECURE=true
```

---

# 5. User

Nossa própria entidade implementa `UserDetails`.

```
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private UUID id;

    @Column(
        nullable = false,
        unique = true
    )
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    protected User() {
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    /*
     * Ainda não temos roles/permissions.
     */
    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

A ideia agora é:

```
User
 ↓
UserDetails
 ↓
Spring Security
```

Não precisamos de `CustomUserDetails`.

---

# 6. UserRepository

```
public interface UserRepository
        extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(
        String email
    );
}
```

---

# 7. RefreshToken

O refresh token **não é JWT**.

Ele é uma string aleatória.

```
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(
        nullable = false,
        unique = true,
        length = 64
    )
    private String tokenHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked;

    protected RefreshToken() {
    }

    public RefreshToken(
        UUID id,
        UUID userId,
        String tokenHash,
        Instant expiresAt
    ) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isValid() {
        return !revoked
            && expiresAt.isAfter(
                Instant.now()
            );
    }

    public void revoke() {
        this.revoked = true;
    }
}
```

Teremos:

```
Browser:
refresh original

Banco:
SHA-256(refresh original)
```

---

# 8. Migration

Exemplo PostgreSQL:

```
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL
        REFERENCES users(id),

    token_hash VARCHAR(64)
        NOT NULL UNIQUE,

    expires_at TIMESTAMPTZ
        NOT NULL,

    revoked BOOLEAN
        NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_refresh_tokens_user_id
    ON refresh_tokens(user_id);
```

---

# 9. RefreshTokenRepository

```
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken>
    findByTokenHash(
        String tokenHash
    );
}
```

---

# 10. AuthException

```
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthException
        extends RuntimeException {

    public AuthException(
        String message
    ) {
        super(message);
    }
}
```

Usaremos para:

```
credenciais inválidas
refresh inválido
refresh expirado
```

# 11. JwtConfig

Aqui fica a configuração simples que vamos manter.

```
@Configuration
public class JwtConfig {

    @Bean
    SecretKey jwtSecretKey(
        @Value("${security.jwt.secret}")
        String secret
    ) {

        byte[] bytes =
            Base64
                .getDecoder()
                .decode(secret);

        return new SecretKeySpec(
            bytes,
            "HmacSHA256"
        );
    }

    @Bean
    JwtEncoder jwtEncoder(
        SecretKey key
    ) {

        return NimbusJwtEncoder
            .withSecretKey(key)
            .build();
    }

    @Bean
    JwtDecoder jwtDecoder(
        SecretKey key,

        @Value("${app.auth.issuer}")
        String issuer
    ) {

        NimbusJwtDecoder decoder =
            NimbusJwtDecoder
                .withSecretKey(key)
                .build();

        decoder.setJwtValidator(
            JwtValidators
                .createDefaultWithIssuer(
                    issuer
                )
        );

        return decoder;
    }
}
```

Só precisamos entender:

```
SecretKey
→ nossa chave secreta

JwtEncoder
→ cria JWT

JwtDecoder
→ valida JWT
```

Nada de JWK ou configuração de baixo nível.

---

# 12. JwtService

O `JwtService` cria nosso access token.

```
@Service
public class JwtService {

    private final JwtEncoder encoder;

    private final String issuer;

    private final long accessTokenMinutes;

    public JwtService(
        JwtEncoder encoder,

        @Value("${app.auth.issuer}")
        String issuer,

        @Value(
            "${app.auth.access-token-minutes}"
        )
        long accessTokenMinutes
    ) {

        this.encoder = encoder;
        this.issuer = issuer;

        this.accessTokenMinutes =
            accessTokenMinutes;
    }

    public String generateAccessToken(
        User user
    ) {

        Instant now =
            Instant.now();

        JwtClaimsSet claims =
            JwtClaimsSet
                .builder()

                .issuer(issuer)

                /*
                 * Identifica o usuário.
                 */
                .subject(
                    user.getId()
                        .toString()
                )

                .issuedAt(now)

                .expiresAt(
                    now.plus(
                        accessTokenMinutes,
                        ChronoUnit.MINUTES
                    )
                )

                .build();

        return encoder
            .encode(
                JwtEncoderParameters
                    .from(claims)
            )
            .getTokenValue();
    }
}
```

Nosso JWT terá essencialmente:

```
{
  "iss": "my-app",
  "sub": "uuid-do-usuario",
  "iat": 123456789,
  "exp": 123456999
}
```

Sem roles e sem permissions.

---

# 13. RefreshTokenService

Esta é a implementação **sem rotation**.

```
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository
        repository;

    private final SecureRandom random =
        new SecureRandom();

    private final long refreshTokenDays;

    public RefreshTokenService(
        RefreshTokenRepository repository,

        @Value(
            "${app.auth.refresh-token-days}"
        )
        long refreshTokenDays
    ) {

        this.repository = repository;

        this.refreshTokenDays =
            refreshTokenDays;
    }

    /*
     * Usado no login.
     */
    @Transactional
    public String create(
        UUID userId
    ) {

        String rawToken =
            generateRandomToken();

        RefreshToken refreshToken =
            new RefreshToken(
                UUID.randomUUID(),

                userId,

                /*
                 * Apenas o hash
                 * vai para o banco.
                 */
                hash(rawToken),

                Instant.now().plus(
                    refreshTokenDays,
                    ChronoUnit.DAYS
                )
            );

        repository.save(
            refreshToken
        );

        /*
         * O token original será
         * enviado ao navegador.
         */
        return rawToken;
    }

    /*
     * Usado em /auth/refresh.
     */
    @Transactional(readOnly = true)
    public UUID validate(
        String rawToken
    ) {

        RefreshToken token =
            repository
                .findByTokenHash(
                    hash(rawToken)
                )
                .orElseThrow(
                    () ->
                        new AuthException(
                            "Invalid refresh token"
                        )
                );

        if (!token.isValid()) {

            throw new AuthException(
                "Invalid refresh token"
            );
        }

        return token.getUserId();
    }

    /*
     * Usado no logout.
     */
    @Transactional
    public void revoke(
        String rawToken
    ) {

        repository
            .findByTokenHash(
                hash(rawToken)
            )
            .ifPresent(
                RefreshToken::revoke
            );
    }

    private String generateRandomToken() {

        byte[] bytes =
            new byte[32];

        random.nextBytes(bytes);

        return Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes);
    }

    private String hash(
        String token
    ) {

        try {

            MessageDigest digest =
                MessageDigest
                    .getInstance(
                        "SHA-256"
                    );

            byte[] hash =
                digest.digest(
                    token.getBytes(
                        StandardCharsets.UTF_8
                    )
                );

            return HexFormat
                .of()
                .formatHex(hash);

        } catch (
            NoSuchAlgorithmException e
        ) {

            throw new IllegalStateException(
                e
            );
        }
    }
}
```

Mentalmente:

```
create()
→ cria refresh

validate()
→ verifica refresh

revoke()
→ invalida refresh
```

Não existe `consume()` porque não temos rotation.

---

# 14. DTOs

`LoginRequest.java`:

```
public record LoginRequest(
    String email,
    String password
) {
}
```

`LoginResponse.java`:

```
public record LoginResponse(
    String accessToken
) {
}
```

O refresh token não aparece no JSON.

Para comunicação interna:

`LoginResult.java`:

```
public record LoginResult(
    String accessToken,
    String refreshToken
) {
}
```

---

# 15. AuthService

```
@Service
public class AuthService {

    private final AuthenticationManager
        authenticationManager;

    private final UserRepository
        userRepository;

    private final JwtService
        jwtService;

    private final RefreshTokenService
        refreshTokenService;

    public AuthService(
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        JwtService jwtService,
        RefreshTokenService refreshTokenService
    ) {

        this.authenticationManager =
            authenticationManager;

        this.userRepository =
            userRepository;

        this.jwtService =
            jwtService;

        this.refreshTokenService =
            refreshTokenService;
    }

    public LoginResult login(
        LoginRequest request
    ) {

        Authentication authentication;

        try {

            authentication =
                authenticationManager
                    .authenticate(
                        new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                        )
                    );

        } catch (
            AuthenticationException e
        ) {

            throw new AuthException(
                "Invalid credentials"
            );
        }

        /*
         * Como nosso User implementa
         * UserDetails, ele volta como
         * principal da autenticação.
         */
        User user =
            (User)
                authentication
                    .getPrincipal();

        String accessToken =
            jwtService
                .generateAccessToken(
                    user
                );

        String refreshToken =
            refreshTokenService
                .create(
                    user.getId()
                );

        return new LoginResult(
            accessToken,
            refreshToken
        );
    }

    public String refresh(
        String refreshToken
    ) {

        UUID userId =
            refreshTokenService
                .validate(
                    refreshToken
                );

        User user =
            userRepository
                .findById(userId)
                .orElseThrow(
                    () ->
                        new AuthException(
                            "User not found"
                        )
                );

        /*
         * Sem rotation:
         *
         * cria apenas um novo
         * access token.
         */
        return jwtService
            .generateAccessToken(
                user
            );
    }

    public void logout(
        String refreshToken
    ) {

        refreshTokenService
            .revoke(
                refreshToken
            );
    }
}
```

O login funciona assim:

```
UsernamePasswordAuthenticationToken
              ↓
AuthenticationManager
              ↓
UserDetailsService
              ↓
User
              ↓
PasswordEncoder
              ↓
Authentication
              ↓
getPrincipal()
              ↓
User
```

`UsernamePasswordAuthenticationToken` aqui **não é JWT**.

Ele só representa:

```
"tente autenticar
 este email e senha"
```

---

# 16. AuthController

Teremos:

```
POST /auth/login
POST /auth/refresh
POST /auth/logout
```

Código:

```
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService
        authService;

    private final boolean
        cookieSecure;

    private final long
        refreshTokenDays;

    public AuthController(
        AuthService authService,

        @Value(
            "${app.auth.cookie-secure}"
        )
        boolean cookieSecure,

        @Value(
            "${app.auth.refresh-token-days}"
        )
        long refreshTokenDays
    ) {

        this.authService =
            authService;

        this.cookieSecure =
            cookieSecure;

        this.refreshTokenDays =
            refreshTokenDays;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse>
    login(
        @RequestBody
        LoginRequest request
    ) {

        LoginResult result =
            authService.login(
                request
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

    @PostMapping("/refresh")
    public LoginResponse refresh(

        @CookieValue(
            "refresh_token"
        )
        String refreshToken

    ) {

        String accessToken =
            authService.refresh(
                refreshToken
            );

        return new LoginResponse(
            accessToken
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void>
    logout(

        @CookieValue(
            value = "refresh_token",
            required = false
        )
        String refreshToken

    ) {

        if (refreshToken != null) {

            authService.logout(
                refreshToken
            );
        }

        return ResponseEntity
            .noContent()

            .header(
                HttpHeaders.SET_COOKIE,

                deleteRefreshCookie()
                    .toString()
            )

            .build();
    }

    private ResponseCookie
    createRefreshCookie(
        String refreshToken
    ) {

        return ResponseCookie
            .from(
                "refresh_token",
                refreshToken
            )

            /*
             * JavaScript não consegue
             * ler o cookie.
             */
            .httpOnly(true)

            /*
             * HTTPS apenas em produção.
             */
            .secure(cookieSecure)

            /*
             * Limita envio cross-site.
             */
            .sameSite("Lax")

            /*
             * Cookie só acompanha
             * endpoints /auth/*
             */
            .path("/auth")

            .maxAge(
                Duration.ofDays(
                    refreshTokenDays
                )
            )

            .build();
    }

    private ResponseCookie
    deleteRefreshCookie() {

        return ResponseCookie
            .from(
                "refresh_token",
                ""
            )

            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Lax")
            .path("/auth")

            /*
             * Expira imediatamente.
             */
            .maxAge(
                Duration.ZERO
            )

            .build();
    }
}
```

No login o servidor responde conceitualmente:

```
HTTP/1.1 200 OK

Set-Cookie:
refresh_token=abc123;
HttpOnly;
SameSite=Lax;
Path=/auth
```

e:

```
{
  "accessToken": "eyJ..."
}
```

---

# 17. SecurityConfig

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain
    securityFilterChain(
        HttpSecurity http
    ) throws Exception {

        return http

            /*
             * Ainda não estamos
             * implementando CSRF token.
             */
            .csrf(
                csrf ->
                    csrf.disable()
            )

            .cors(
                Customizer
                    .withDefaults()
            )

            /*
             * Não usamos HttpSession.
             */
            .sessionManagement(
                session ->
                    session
                        .sessionCreationPolicy(
                            SessionCreationPolicy
                                .STATELESS
                        )
            )

            .authorizeHttpRequests(
                auth ->
                    auth

                        /*
                         * Login, refresh
                         * e logout não exigem
                         * access token.
                         */
                        .requestMatchers(
                            "/auth/**"
                        )
                        .permitAll()

                        /*
                         * Todo o restante
                         * exige JWT válido.
                         */
                        .anyRequest()
                        .authenticated()
            )

            /*
             * Ativa autenticação
             * Authorization: Bearer JWT.
             */
            .oauth2ResourceServer(
                resource ->
                    resource.jwt(
                        Customizer
                            .withDefaults()
                    )
            )

            .build();
    }

    @Bean
    PasswordEncoder
    passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService
    userDetailsService(
        UserRepository repository
    ) {

        return email ->
            repository
                .findByEmail(email)
                .orElseThrow(
                    () ->
                        new UsernameNotFoundException(
                            "User not found"
                        )
                );
    }

    @Bean
    AuthenticationManager
    authenticationManager(
        AuthenticationConfiguration
            configuration
    ) throws Exception {

        return configuration
            .getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource
    corsConfigurationSource(

        @Value(
            "${app.auth.frontend-origin}"
        )
        String frontendOrigin

    ) {

        CorsConfiguration config =
            new CorsConfiguration();

        config.setAllowedOrigins(
            List.of(
                frontendOrigin
            )
        );

        config.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
            )
        );

        config.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type"
            )
        );

        /*
         * Necessário porque teremos
         * cookie de refresh.
         */
        config.setAllowCredentials(
            true
        );

        UrlBasedCorsConfigurationSource
            source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            config
        );

        return source;
    }
}
```

As três peças principais daqui são:

```
UserDetailsService
→ busca o usuário

AuthenticationManager
→ autentica email + senha

PasswordEncoder
→ verifica BCrypt
```

---

# 18. Fluxo completo da implementação

Login:

```
POST /auth/login

email + senha
     ↓
AuthenticationManager
     ↓
UserDetailsService
     ↓
User
     ↓
BCrypt
     ↓
autenticado
     ↓
Access Token
+
Refresh Token
```

O access:

```
JWT
5 minutos
retornado no JSON
```

O refresh:

```
string aleatória
30 dias

original
→ cookie HttpOnly

hash
→ banco
```

Request normal:

```
Authorization:
Bearer <access-token>
        ↓
Spring Security
        ↓
JwtDecoder
        ↓
JWT válido?
        ↓
Controller
```

Refresh:

```
POST /auth/refresh
        ↓
cookie refresh_token
        ↓
SHA-256(cookie)
        ↓
busca banco
        ↓
válido?
        ↓
User
        ↓
novo Access Token
```

O refresh **não muda**.

Logout:

```
POST /auth/logout
        ↓
refresh revogado no banco
        ↓
Set-Cookie Max-Age=0
        ↓
cookie removido
```

E essa é a versão base que eu manteria congelada no documento:

```
UserDetails
→ login

JWT
→ requests normais

Refresh Token
→ renovar JWT

HttpOnly
→ JS não lê o refresh

Secure
→ HTTPS em produção

SameSite=Lax
→ limita envio cross-site

Path=/auth
→ refresh cookie só participa
  das rotas de autenticação
```

Sem rotation, sem CSRF token, sem authorities e sem frontend.