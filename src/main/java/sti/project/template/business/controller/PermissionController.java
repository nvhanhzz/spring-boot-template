package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.template.base.constant.ApiVersion;
import sti.project.template.base.controller.BaseController;
import sti.project.template.base.dto.ApiResponseFactory;
import sti.project.template.business.dto.request.PermissionRequest;
import sti.project.template.business.dto.response.PermissionResponse;
import sti.project.template.business.entity.Permission;
import sti.project.template.business.service.PermissionService;

@RestController
@RequestMapping(ApiVersion.V1 + "/permissions")
@Tag(name = "Permissions", description = "Permission management APIs")
public class PermissionController extends BaseController<Permission, PermissionResponse, PermissionRequest> {

    protected PermissionController(PermissionService service, ApiResponseFactory responseFactory) {
        super(service, responseFactory);
    }

    @Override
    protected String getResourceName() {
        return "permission";
    }
}