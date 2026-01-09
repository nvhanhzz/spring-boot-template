package sti.project.template.example.repository;

import sti.project.template.base.repository.BaseRepository;
import sti.project.template.example.entity.Location;

import java.util.Optional;

/**
 * Location repository.
 * Inherits all CRUD operations from BaseRepository.
 */
public interface LocationRepository extends BaseRepository<Location> {

    /**
     * Check if a location with the given code exists (excluding deleted).
     */
    boolean existsByCodeAndStatusNot(String code, sti.project.template.base.entity.EntityStatus status);

    /**
     * Find location by code.
     */
    Optional<Location> findByCode(String code);
}
