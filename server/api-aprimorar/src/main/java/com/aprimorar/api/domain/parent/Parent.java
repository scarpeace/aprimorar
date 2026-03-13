package com.aprimorar.api.domain.parent;


import com.aprimorar.api.shared.BaseEntity;
import jakarta.persistence.AttributeOverride;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor()
@Entity
@Table(name = "tb_parent")
public class Parent extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "contact",nullable = false)
    private String contact;

    @Column(name = "cpf",nullable = false, unique = true)
    private String cpf;

}
