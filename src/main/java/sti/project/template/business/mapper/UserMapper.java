package sti.project.template.business.mapper;

import org.mapstruct.*;
import sti.project.template.base.file.FileUrlMapper;
import sti.project.template.base.mapper.AuditUserMapper;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.UpdateProfileRequest;
import sti.project.template.business.dto.request.UserRequest;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.Role;
import sti.project.template.business.entity.User;

import java.util.List;
import java.util.Set;

@Mapper(config = BaseMapper.class, uses = { AuditUserMapper.class, RoleMapper.class, FileUrlMapper.class })
public abstract class UserMapper extends BaseMapper<User, UserResponse, UserRequest> {

    @Override
    @Mapping(target = "roles", qualifiedByName = "toSimpleRoles")
    @Mapping(target = "createdByUser", source = "createdBy")
    @Mapping(target = "updatedByUser", source = "updatedBy")
    @Mapping(target = "avatar", source = "avatar", qualifiedByName = "toFullUrl")
    public abstract UserResponse toResponse(User entity);

    @Named("toSimpleRoles")
    @IterableMapping(qualifiedByName = "toSimple")
    public abstract List<RoleResponse> mapRolesToSimple(Set<Role> roles);

    @Override
    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(UserRequest request);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract void updateEntity(UserRequest request, @MappingTarget User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateFromProfileRequest(UpdateProfileRequest request, @MappingTarget User entity);
}