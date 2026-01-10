package sti.project.template.base.service;

import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.dto.SearchCriteria;
import sti.project.template.base.entity.BaseEntity;

import java.util.UUID;

/**
 * Base service interface defining common CRUD operations.
 * All services should implement this interface.
 *
 * @param <T>   Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
public interface BaseService<T extends BaseEntity, Res extends BaseResponseDTO, Req> {
    /**
     * Get entity by ID
     */
    Res getById(UUID id);

    /**
     * Search entities with dynamic field filters and ID list
     */
    PageDTO<Res> search(SearchCriteria criteria);

    /**
     * Create a new entity
     */
    Res create(Req request);

    /**
     * Update an existing entity
     */
    Res update(UUID id, Req request);

    /**
     * Soft delete an entity
     */
    void delete(UUID id);

    /**
     * Restore a deleted entity
     */
    Res restore(UUID id);

    /**
     * Toggle active/inactive status
     */
    Res toggleActive(UUID id);

    /**
     * Get list of searchable fields for this entity
     */
    String[] getSearchableFields();
}