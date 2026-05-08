package com.aprimorar.api.domain.auth.internal;

import com.aprimorar.api.enums.Role;
import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User extends BaseEntity {

    @Column(name = "employee_id", nullable = false, unique = true)
    private UUID employeeId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    protected User() {}

    public User(UUID employeeId, String username, String passwordHash, boolean active, Role role) {
        this.employeeId = employeeId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
        this.role = role;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isActive() {
        return active;
    }

    public Role getRole() {
        return role;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void registerLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
