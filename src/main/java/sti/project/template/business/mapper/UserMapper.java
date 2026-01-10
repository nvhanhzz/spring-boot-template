package sti.project.template.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.business.dto.request.UserRequest;
import sti.project.template.business.dto.response.UserResponse;
import sti.project.template.business.entity.User;

@Mapper(config = BaseMapper.class, uses = { RoleMapper.class })
public interface UserMapper extends BaseMapper<User, UserResponse, UserRequest> {

    @Override
    UserResponse toResponse(User entity);

    @Override
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);

    @Override
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User entity);
}
