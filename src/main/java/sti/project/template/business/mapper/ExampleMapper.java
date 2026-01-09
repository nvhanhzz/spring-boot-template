package sti.project.template.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.ExampleRequest;
import sti.project.template.business.dto.response.ExampleResponse;
import sti.project.template.business.entity.Example;

/**
 * MapStruct mapper for Example entity.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExampleMapper extends BaseMapper<Example, ExampleResponse, ExampleRequest> {

    @Override
    ExampleResponse toResponse(Example entity);

    @Override
    Example toEntity(ExampleRequest request);

    @Override
    void updateEntity(ExampleRequest request, @MappingTarget Example entity);
}
