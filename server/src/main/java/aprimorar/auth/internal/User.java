package aprimorar.auth.internal;

import aprimorar.shared.MapperUtils;
import aprimorar.shared.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
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
        this.username = normalizeUsername(username);
        this.password = validatePassword(password);
        this.role = validateRole(role);
        this.active = active;
    }

    @PrePersist
    @PreUpdate
    void normalize() {
        this.username = normalizeUsername(this.username);
        this.password = validatePassword(this.password);
        this.role = validateRole(this.role);
    }

    private String normalizeUsername(String username) {
        String normalized = MapperUtils.normalizeEmail(username);
        if (normalized == null) {
            throw new IllegalArgumentException("Username e obrigatorio");
        }
        return normalized;
    }

    private String validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password e obrigatorio");
        }
        return password;
    }

    private Role validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role e obrigatorio");
        }
        return role;
    }

    public void toggleActive() {
        this.active = !this.active;
    }
}
