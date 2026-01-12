package sti.project.template.business.mapper;

import org.mapstruct.*;
import sti.project.template.base.mapper.AuditUserMapper;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.PermissionRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.entity.Permission;

@Mapper(config = BaseMapper.class, uses = { AuditUserMapper.class })
public abstract class PermissionMapper extends BaseMapper<Permission, PermissionResponse, PermissionRequest> {

    @Override
    @Mapping(target = "createdByUser", source = "createdBy")
    @Mapping(target = "updatedByUser", source = "updatedBy")
    public abstract PermissionResponse toResponse(Permission entity);

    @Named("toSimple")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    public abstract PermissionResponse toSimpleResponse(Permission entity);

    @Override
    public abstract Permission toEntity(PermissionRequest request);

    @Override
    public abstract void updateEntity(PermissionRequest request, @MappingTarget Permission entity);
}