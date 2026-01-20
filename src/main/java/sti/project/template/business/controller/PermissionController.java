package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.scada.base.constant.ApiVersion;
import sti.project.scada.base.controller.BaseController;
import sti.project.scada.base.dto.ApiResponseFactory;
import sti.project.scada.business.dto.request.PermissionRequest;
import sti.project.scada.business.dto.response.PermissionResponse;
import sti.project.scada.business.entity.Permission;
import sti.project.scada.business.service.PermissionService;

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