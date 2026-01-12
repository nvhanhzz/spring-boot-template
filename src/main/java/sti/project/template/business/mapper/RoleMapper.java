package sti.project.template.business.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.RoleRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.entity.Permission;
import sti.project.template.business.entity.Role;

import java.util.Set;

@Mapper(config = BaseMapper.class, uses = { PermissionMapper.class })
public abstract class RoleMapper extends BaseMapper<Role, RoleResponse, RoleRequest> {

    @Override
    @Mapping(target = "permissions", qualifiedByName = "toSimplePermissions")
    public abstract RoleResponse toResponse(Role entity);

    @Named("toSimplePermissions")
    @IterableMapping(qualifiedByName = "toSimple")
    public abstract Set<PermissionResponse> mapPermissionsToSimple(Set<Permission> permissions);

    @Named("toSimple")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "permissions", qualifiedByName = "toSimplePermissions")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract RoleResponse toSimpleResponse(Role entity);

    @Override
    @Mapping(target = "permissions", ignore = true)
    public abstract Role toEntity(RoleRequest request);

    @Override
    @Mapping(target = "permissions", ignore = true)
    public abstract void updateEntity(RoleRequest request, @MappingTarget Role entity);
}
