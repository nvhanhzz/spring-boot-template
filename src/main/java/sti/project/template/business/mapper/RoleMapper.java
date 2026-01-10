package sti.project.template.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.RoleRequest;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.entity.Role;

@Mapper(config = BaseMapper.class, uses = { PermissionMapper.class })
public interface RoleMapper extends BaseMapper<Role, RoleResponse, RoleRequest> {

    @Override
    RoleResponse toResponse(Role entity);

    @Override
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest request);

    @Override
    @Mapping(target = "permissions", ignore = true)
    void updateEntity(RoleRequest request, @MappingTarget Role entity);
}
