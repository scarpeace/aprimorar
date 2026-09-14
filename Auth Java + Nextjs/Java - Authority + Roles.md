# Caso queira adicionar Roles e Authorities

Na implementação base, nosso `User` já implementa `UserDetails`, mas ainda retorna:

```
@Override
public Collection<? extends GrantedAuthority>
getAuthorities() {
    return List.of();
}
```

A ideia agora é evoluir para:

```
User
 ↓
Role
 ↓
GrantedAuthority
 ↓
JWT
 ↓
Spring Security
 ↓
@PreAuthorize(...)
```

## 1. Criar `Role`

Começamos simples:

```
public enum Role {
    ADMIN,
    USER
}
```

Depois podemos adicionar outras:

```
public enum Role {
    ADMIN,
    SECRETARY,
    TEACHER,
    USER
}
```

---

## 2. Adicionar `role` ao `User`

```
@Enumerated(EnumType.STRING)
@Column(nullable = false)
private Role role;
```

A entidade fica, na parte relevante:

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    protected User() {
    }

    public UUID getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        return List.of(
            new SimpleGrantedAuthority(
                "ROLE_" + role.name()
            )
        );
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

Se o usuário for:

```
ADMIN
```

o Spring enxergará:

```
ROLE_ADMIN
```

Isso acontece porque `hasRole("ADMIN")` procura internamente pela authority:

```
ROLE_ADMIN
```

---

## 3. Migration

Adicione a coluna:

```
ALTER TABLE users
ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT 'USER';
```

---

# 4. Colocar authorities no JWT

Até agora nosso JWT só tinha:

```
{
  "sub": "user-id"
}
```

Agora adicionamos:

```
{
  "sub": "user-id",
  "authorities": [
    "ROLE_ADMIN"
  ]
}
```

No `JwtService`, antes de montar as claims:

```
List<String> authorities =
    user.getAuthorities()
        .stream()
        .map(
            GrantedAuthority::getAuthority
        )
        .toList();
```

E adicionamos:

```
.claim(
    "authorities",
    authorities
)
```

O método completo fica:

```
public String generateAccessToken(
    User user
) {

    Instant now =
        Instant.now();

    List<String> authorities =
        user.getAuthorities()
            .stream()
            .map(
                GrantedAuthority::getAuthority
            )
            .toList();

    JwtClaimsSet claims =
        JwtClaimsSet
            .builder()

            .issuer(issuer)

            .subject(
                user.getId()
                    .toString()
            )

            .claim(
                "authorities",
                authorities
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
```

O interessante é que reutilizamos:

```
user.getAuthorities()
```

Então `UserDetails` continua sendo a fonte das permissões.

---

# 5. Ensinar o Spring a ler `authorities`

Nas requests normais, o Spring não chama novamente o `UserDetailsService`.

Ele recebe:

```
JWT
```

Então precisamos dizer:

> leia a claim `authorities` do JWT.

No `SecurityConfig`:

```
@Bean
JwtAuthenticationConverter
jwtAuthenticationConverter() {

    JwtGrantedAuthoritiesConverter
        authoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    authoritiesConverter
        .setAuthoritiesClaimName(
            "authorities"
        );

    /*
     * Nossas authorities já possuem:
     *
     * ROLE_ADMIN
     *
     * então não queremos que
     * Spring adicione outro prefixo.
     */
    authoritiesConverter
        .setAuthorityPrefix("");

    JwtAuthenticationConverter converter =
        new JwtAuthenticationConverter();

    converter
        .setJwtGrantedAuthoritiesConverter(
            authoritiesConverter
        );

    return converter;
}
```

Os nomes parecem grandes, mas conceitualmente:

```
JwtGrantedAuthoritiesConverter
→ pega ["ROLE_ADMIN"] do JWT
→ transforma em GrantedAuthority

JwtAuthenticationConverter
→ cria a autenticação do Spring
  a partir do JWT
```

---

# 6. Usar o converter no Resource Server

Onde tínhamos:

```
.oauth2ResourceServer(
    resource ->
        resource.jwt(
            Customizer.withDefaults()
        )
)
```

troque por:

```
.oauth2ResourceServer(
    resource ->
        resource.jwt(
            jwt ->
                jwt.jwtAuthenticationConverter(
                    jwtAuthenticationConverter
                )
        )
)
```

Para isso, receba o converter:

```
@Bean
SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    JwtAuthenticationConverter
        jwtAuthenticationConverter
) throws Exception {
```

---

# 7. Habilitar segurança nos métodos

Adicione:

```
@EnableMethodSecurity
```

Então:

```
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
```

Isso permite:

```
@PreAuthorize(...)
```

---

# 8. Proteger operações por Role

Agora podemos fazer:

```
@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/users")
public void createUser() {
}
```

Ou:

```
@PreAuthorize(
    "hasAnyRole('ADMIN', 'SECRETARY')"
)
@PostMapping("/students")
public void createStudent() {
}
```

O JWT:

```
ROLE_ADMIN
```

permite:

```
hasRole("ADMIN")
```

---

# 9. E `Authority`?

Aqui está uma distinção importante:

```
Role
→ grupo de permissões

Authority
→ permissão que o Spring verifica
```

Hoje estamos transformando:

```
Role.ADMIN
```

em:

```
ROLE_ADMIN
```

que também é uma `GrantedAuthority`.

Mais tarde podemos adicionar permissões específicas:

```
ROLE_ADMIN

student:read
student:create
student:update

payment:read
payment:create
```

Então poderíamos proteger assim:

```
@PreAuthorize(
    "hasAuthority('student:create')"
)
```

em vez de:

```
@PreAuthorize(
    "hasRole('ADMIN')"
)
```

---

# 10. Evoluindo Role para permissions

Quando precisar, podemos criar:

```
public enum Permission {

    STUDENT_READ("student:read"),
    STUDENT_CREATE("student:create"),
    STUDENT_UPDATE("student:update"),

    PAYMENT_READ("payment:read"),
    PAYMENT_CREATE("payment:create");

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
```

E o `Role`:

```
public enum Role {

    ADMIN(
        Set.of(
            Permission.STUDENT_READ,
            Permission.STUDENT_CREATE,
            Permission.STUDENT_UPDATE,
            Permission.PAYMENT_READ,
            Permission.PAYMENT_CREATE
        )
    ),

    USER(
        Set.of(
            Permission.STUDENT_READ
        )
    );

    private final Set<Permission>
        permissions;

    Role(
        Set<Permission> permissions
    ) {
        this.permissions =
            permissions;
    }

    public Set<Permission>
    getPermissions() {
        return permissions;
    }
}
```

---

# 11. Atualizar `getAuthorities()`

A partir daí:

```
@Override
public Collection<? extends GrantedAuthority>
getAuthorities() {

    List<GrantedAuthority> authorities =
        new ArrayList<>();

    /*
     * Role
     */
    authorities.add(
        new SimpleGrantedAuthority(
            "ROLE_" + role.name()
        )
    );

    /*
     * Permissions
     */
    role.getPermissions()
        .stream()
        .map(Permission::getValue)
        .map(SimpleGrantedAuthority::new)
        .forEach(
            authorities::add
        );

    return authorities;
}
```

Um `ADMIN` poderia então produzir:

```
ROLE_ADMIN
student:read
student:create
student:update
payment:read
payment:create
```

O `JwtService` **não muda**, porque já faz:

```
user.getAuthorities()
```

Isso é uma vantagem importante do desenho.

---

# 12. Uso final

Role:

```
@PreAuthorize(
    "hasRole('ADMIN')"
)
```

Permission:

```
@PreAuthorize(
    "hasAuthority('student:create')"
)
```

Você pode usar ambos:

```
@PreAuthorize(
    "hasRole('ADMIN') or hasAuthority('student:create')"
)
```

Mas, conforme o sistema cresce, eu tenderia a deixar:

```
Role
→ define grupos de acesso

Permission / Authority
→ protege ações específicas
```

Então o fluxo fica:

```
User
 ↓
Role
 ↓
Permissions
 ↓
getAuthorities()
 ↓
JWT
{
  authorities: [...]
}
 ↓
Spring Security
 ↓
@PreAuthorize
```

E isso encaixa diretamente na implementação base sem mudar login, refresh token, cookies ou o restante da autenticação.