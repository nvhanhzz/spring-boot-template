package sti.project.template.base.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import sti.project.scada.base.dto.BaseResponseDTO;
import sti.project.scada.base.entity.BaseEntity;

import java.util.List;

@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BaseMapper<E extends BaseEntity, Res extends BaseResponseDTO, Req> {

    public abstract Res toResponse(E entity);

    public abstract List<Res> toResponseList(List<E> entities);

    public abstract E toEntity(Req request);

    public abstract void updateEntity(Req request, @MappingTarget E entity);
}