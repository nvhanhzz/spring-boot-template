package sti.project.template.business.service.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.service.impl.BaseServiceImpl;
import sti.project.template.business.dto.request.PermissionRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.entity.Permission;
import sti.project.template.business.mapper.PermissionMapper;
import sti.project.template.business.repository.PermissionRepository;
import sti.project.template.business.service.PermissionService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl extends BaseServiceImpl<Permission, PermissionResponse, PermissionRequest>
        implements PermissionService {

    PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
        super(repository, mapper, Permission.class);
        this.permissionRepository = repository;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name" };
    }

    @Override
    protected void beforeCreate(Permission entity, PermissionRequest request) {
        validateNameUnique(request.getName(), null);
    }

    @Override
    protected void beforeUpdate(Permission entity, PermissionRequest request) {
        if (!entity.getName().equals(request.getName())) {
            validateNameUnique(request.getName(), entity.getName());
        }
    }

    private void validateNameUnique(String name, String currentName) {
        if (permissionRepository.existsByNameAndStatusNot(name, EntityStatus.DELETED)) {
            if (currentName == null || !currentName.equals(name)) {
                throw new AppException(ErrorCode.NAME_EXISTS);
            }
        }
    }
}