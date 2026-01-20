package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.scada.base.constant.ApiVersion;
import sti.project.scada.base.controller.BaseController;
import sti.project.scada.base.dto.ApiResponseFactory;
import sti.project.scada.business.dto.request.RoleRequest;
import sti.project.scada.business.dto.response.RoleResponse;
import sti.project.scada.business.entity.Role;
import sti.project.scada.business.service.RoleService;

@RestController
@RequestMapping(ApiVersion.V1 + "/roles")
@Tag(name = "Roles", description = "Role management APIs")
public class RoleController extends BaseController<Role, RoleResponse, RoleRequest> {

    protected RoleController(RoleService service, ApiResponseFactory responseFactory) {
        super(service, responseFactory);
    }

    @Override
    protected String getResourceName() {
        return "role";
    }
}
