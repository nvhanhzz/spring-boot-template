package sti.project.template.business.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sti.project.scada.base.dto.PageDTO;
import sti.project.scada.base.dto.SearchCriteria;
import sti.project.scada.base.entity.EntityStatus;
import sti.project.scada.base.exception.AppException;
import sti.project.scada.base.exception.ErrorCode;
import sti.project.scada.base.service.impl.BaseServiceImpl;
import sti.project.scada.business.dto.request.RoleRequest;
import sti.project.scada.business.dto.response.RoleResponse;
import sti.project.scada.business.entity.Permission;
import sti.project.scada.business.entity.Role;
import sti.project.scada.business.mapper.RoleMapper;
import sti.project.scada.business.repository.PermissionRepository;
import sti.project.scada.business.repository.RoleRepository;
import sti.project.scada.business.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleResponse, RoleRequest>
        implements RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper, PermissionRepository permissionRepository) {
        super(repository, mapper, Role.class);
        this.roleRepository = repository;
        this.roleMapper = mapper;
        this.permissionRepository = permissionRepository;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name" };
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<RoleResponse> search(SearchCriteria criteria) {
        Sort sort = Sort.by(Sort.Direction.fromString(criteria.getSortDir()), criteria.getSortBy());
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        Specification<Role> spec = buildSearchSpecification(criteria);
        Page<Role> pageResult = roleRepository.findAll(spec, pageable);

        if (pageResult.isEmpty()) {
            return PageDTO.of(List.of(), 0L);
        }
        List<UUID> ids = pageResult.getContent().stream()
                .map(Role::getId)
                .toList();
        List<Role> roles = roleRepository.findByIdsWithPermissions(ids);
        return PageDTO.of(roleMapper.toResponseList(roles), pageResult.getTotalElements());
    }

    @Override
    protected void beforeCreate(Role entity, RoleRequest request) {
        validateNameUnique(request.getName(), null);
        assignPermissions(entity, request.getPermissionIds());
    }

    @Override
    protected void beforeUpdate(Role entity, RoleRequest request) {
        if (!entity.getName().equals(request.getName())) {
            validateNameUnique(request.getName(), entity.getName());
        }
        assignPermissions(entity, request.getPermissionIds());
    }

    private void validateNameUnique(String name, String currentName) {
        if (roleRepository.existsByNameAndStatusNot(name, EntityStatus.DELETED)) {
            if (currentName == null || !currentName.equals(name)) {
                throw new AppException(ErrorCode.NAME_EXISTS);
            }
        }
    }

    private void assignPermissions(Role role, List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            role.setPermissions(new HashSet<>());
            return;
        }

        Set<Permission> permissions = new HashSet<>();
        for (UUID permissionId : permissionIds) {
            Permission permission = permissionRepository.findByIdAndStatusNot(permissionId, EntityStatus.DELETED)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Permission"));
            permissions.add(permission);
        }
        role.setPermissions(permissions);
    }
}
