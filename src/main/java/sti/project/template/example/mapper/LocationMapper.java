package sti.project.template.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.example.dto.LocationRequest;
import sti.project.template.example.dto.LocationResponse;
import sti.project.template.example.entity.Location;

/**
 * MapStruct mapper for Location entity.
 * Implementation is auto-generated at compile time.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocationMapper extends BaseMapper<Location, LocationResponse, LocationRequest> {

    @Override
    LocationResponse toResponse(Location entity);

    @Override
    Location toEntity(LocationRequest request);

    @Override
    void updateEntity(LocationRequest request, @MappingTarget Location entity);
}
