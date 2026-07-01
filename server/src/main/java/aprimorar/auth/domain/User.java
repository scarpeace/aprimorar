package aprimorar.auth.domain;

import aprimorar.auth.Role;
import aprimorar.utils.MapperUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(name = "users")
public class User {

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

    public void toggleActive() {
        this.active = !this.active;
    }

    public void syncAdminAccess(String encodedPassword) {
        this.password = validatePassword(encodedPassword);
        this.role = Role.ADMIN;
        this.active = true;
    }
}
