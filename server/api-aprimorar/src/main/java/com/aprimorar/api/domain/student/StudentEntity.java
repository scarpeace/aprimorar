package com.aprimorar.api.domain.student;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.aprimorar.api.domain.address.AddressEntity;
import com.aprimorar.api.domain.parent.ParentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_student", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_student_cpf", columnNames = {"cpf"})
})

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String school;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "last_reactivated_at")
    private Instant lastReactivatedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "parent_id", nullable = false)
    private ParentEntity parentEntity;

    @Embedded
    private AddressEntity addressEntity;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

   public void archive() {
       if(this.archivedAt == null){
           this.archivedAt = Instant.now();
       }
   }

    public void unarchive() {
        if(this.archivedAt != null){
            this.archivedAt = null;
        }
    }

    public boolean isArchived() {
        return archivedAt != null;
    }

}
