package com.aprimorar.api.repository;

import com.aprimorar.api.entity.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {

    Page<Parent> findAllByActiveTrue(Pageable pageable);

    Optional<Parent> findByIdAndActiveTrue(UUID id);
}
