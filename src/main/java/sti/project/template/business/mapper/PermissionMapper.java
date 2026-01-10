package sti.project.template.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.PermissionRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.entity.Permission;

@Mapper(config = BaseMapper.class)
public interface PermissionMapper extends BaseMapper<Permission, PermissionResponse, PermissionRequest> {

    @Override
    PermissionResponse toResponse(Permission entity);

    @Override
    Permission toEntity(PermissionRequest request);

    @Override
    void updateEntity(PermissionRequest request, @MappingTarget Permission entity);
}