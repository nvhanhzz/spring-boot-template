package sti.project.template.business.service.impl;

import org.springframework.stereotype.Service;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.service.impl.BaseServiceImpl;
import sti.project.template.business.dto.request.RoleRequest;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.entity.Permission;
import sti.project.template.business.entity.Role;
import sti.project.template.business.mapper.RoleMapper;
import sti.project.template.business.repository.PermissionRepository;
import sti.project.template.business.repository.RoleRepository;
import sti.project.template.business.service.RoleService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleResponse, RoleRequest>
        implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper, PermissionRepository permissionRepository) {
        super(repository, mapper, Role.class);
        this.roleRepository = repository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name" };
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

    private void assignPermissions(Role role, Set<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            role.setPermissions(new HashSet<>());
            return;
        }

        Set<Permission> permissions = new HashSet<>();
        for (UUID permissionId : permissionIds) {
            Permission permission = permissionRepository.findByIdAndStatusNot(permissionId, EntityStatus.DELETED)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
            permissions.add(permission);
        }
        role.setPermissions(permissions);
    }
}
