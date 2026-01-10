package sti.project.template.base.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.entity.BaseEntity;

import java.util.List;

/**
 * Base mapper interface for entity-DTO conversion.
 * All MapStruct mappers should extend this interface with @Mapper(config =
 * BaseMapper.class)
 * 
 * @param <E>   Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
     * Convert request DTO to entity (for create).
     * BaseEntity fields (id, createdAt, updatedAt, createdBy, updatedBy, status)
     * are automatically ignored via unmappedTargetPolicy = IGNORE.
     */
    E toEntity(Req request);

    /**
     * Update entity from request DTO (for update).
     * BaseEntity fields are automatically ignored.
     */
    void updateEntity(Req request, @MappingTarget E entity);
}
