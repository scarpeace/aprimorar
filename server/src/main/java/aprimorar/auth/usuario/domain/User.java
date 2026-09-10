package aprimorar.auth.usuario.domain;

import aprimorar.auth.Role;
import aprimorar.auth.usuario.domain.exception.UsuarioEstadoInvalidoException;
import aprimorar.common.utils.MapperUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected User() {
    }

    public User(String username, String password, Role role, boolean active) {
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.role = validateRole(role);
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    private String validateUsername(String username) {
        var normalized = MapperUtils.normalizeEmail(username);

        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }
        return normalized;
    }

    private String validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        return password;
    }

    private Role validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Função é obrigatória");
        }
        return role;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void promoteToAdmin(String encodedPassword) {
        if(this.role == Role.ALUNO || this.role == Role.COLABORADOR){
            throw new UsuarioEstadoInvalidoException("Não é permitido promover este usuário para ADMIN");
        }
        this.password = validatePassword(encodedPassword);
        this.role = Role.ADMIN;
        this.active = true;
    }


}
