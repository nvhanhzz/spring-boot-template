package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.template.base.constant.ApiVersion;
import sti.project.template.base.controller.BaseController;
import sti.project.template.base.dto.ApiResponseFactory;
import sti.project.template.business.dto.request.RoleRequest;
import sti.project.template.business.dto.response.RoleResponse;
import sti.project.template.business.entity.Role;
import sti.project.template.business.service.RoleService;

@RestController
@RequestMapping(ApiVersion.V1 + "/roles")
@Tag(name = "Roles", description = "Role management APIs")
public class RoleController extends BaseController<Role, RoleResponse, RoleRequest> {

    protected RoleController(RoleService service, ApiResponseFactory responseFactory) {
        super(service, responseFactory);
    }
}
