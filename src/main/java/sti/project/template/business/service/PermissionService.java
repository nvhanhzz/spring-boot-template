package sti.project.template.business.service;

import sti.project.template.base.service.BaseService;
import sti.project.template.business.dto.request.PermissionRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.entity.Permission;

public interface PermissionService extends BaseService<Permission, PermissionResponse, PermissionRequest> {
}