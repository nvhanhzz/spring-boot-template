package sti.project.template.base.mapper;

import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.entity.BaseEntity;

import java.util.List;

/**
 * Base mapper interface for entity-DTO conversion.
 * All MapStruct mappers should extend this interface.
 * @param <E> Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
public interface BaseMapper<E extends BaseEntity, Res extends BaseResponseDTO, Req> {

    /**
     * Convert entity to response DTO
     */
    Res toResponse(E entity);

    /**
     * Convert list of entities to list of response DTOs
     */
    List<Res> toResponseList(List<E> entities);

    /**
     * Convert request DTO to entity (for create)
     */
    E toEntity(Req request);

    /**
     * Update entity from request DTO (for update)
     */
    void updateEntity(Req request, @org.mapstruct.MappingTarget E entity);
}
