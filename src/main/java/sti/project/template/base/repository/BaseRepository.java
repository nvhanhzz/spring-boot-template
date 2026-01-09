package sti.project.template.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import sti.project.template.base.entity.BaseEntity;
import sti.project.template.base.entity.EntityStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base repository interface with common query methods.
 * All repositories should extend this interface.
 * 
 * @param <T> The entity type
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {

    /**
     * Find entity by ID excluding deleted status
     */
    Optional<T> findByIdAndStatusNot(UUID id, EntityStatus status);

    /**
     * Find all entities excluding a specific status
     */
    List<T> findAllByStatusNot(EntityStatus status);

    /**
     * Find all entities excluding a specific status with pagination
     */
    Page<T> findAllByStatusNot(EntityStatus status, Pageable pageable);

    /**
     * Find all active entities
     */
    default List<T> findAllActive() {
        return findAllByStatusNot(EntityStatus.DELETED);
    }

    /**
     * Find active entity by ID (not deleted)
     */
    default Optional<T> findActiveById(UUID id) {
        return findByIdAndStatusNot(id, EntityStatus.DELETED);
    }

    /**
     * Find all active entities with pagination
     */
    default Page<T> findAllActive(Pageable pageable) {
        return findAllByStatusNot(EntityStatus.DELETED, pageable);
    }
}
