package com.aprimorar.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_parent", uniqueConstraints = {
        @UniqueConstraint(name = "uk_parent_name", columnNames = {"name"}),
        @UniqueConstraint(name = "uk_parent_contact", columnNames = {"contact"}),
        @UniqueConstraint(name = "uk_parent_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_parent_cpf", columnNames = {"cpf"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parent {

    @Id
    @Column(name = "parent_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private String contact;

    private String cpf;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant creationTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

}
