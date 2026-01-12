package sti.project.template.base.mapper;

import org.mapstruct.*;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.entity.BaseEntity;

import java.util.List;

@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BaseMapper<E extends BaseEntity, Res extends BaseResponseDTO, Req> {

    public abstract Res toResponse(E entity);

    public abstract List<Res> toResponseList(List<E> entities);

    public abstract E toEntity(Req request);

    public abstract void updateEntity(Req request, @MappingTarget E entity);
}