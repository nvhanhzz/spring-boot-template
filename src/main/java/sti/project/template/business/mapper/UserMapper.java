package sti.project.template.business.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.UserRequest;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.Role;
import sti.project.template.business.entity.User;

import java.util.Set;

@Mapper(config = BaseMapper.class, uses = { RoleMapper.class })
public interface UserMapper extends BaseMapper<User, UserResponse, UserRequest> {

    @Override
    @Mapping(target = "roles", qualifiedByName = "toSimpleRoles")
    UserResponse toResponse(User entity);

    @Named("toSimpleRoles")
    @IterableMapping(qualifiedByName = "toSimple")
    Set<RoleResponse> mapRolesToSimple(Set<Role> roles);

    @Override
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User entity);
}
