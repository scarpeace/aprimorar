package com.aprimorar.api.domain.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<ParentEntity, UUID> {

    Page<ParentEntity> findAllByArchivedAtIsNull(Pageable pageable);

    Optional<ParentEntity> findByIdAndArchivedAtIsNull(UUID id);
}
