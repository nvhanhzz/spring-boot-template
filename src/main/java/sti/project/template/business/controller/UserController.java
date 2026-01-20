package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.scada.base.constant.ApiVersion;
import sti.project.scada.base.controller.BaseController;
import sti.project.scada.base.dto.ApiResponseFactory;
import sti.project.scada.business.dto.request.UserRequest;
import sti.project.scada.business.dto.response.UserResponse;
import sti.project.scada.business.entity.User;
import sti.project.scada.business.service.UserService;

@RestController
@RequestMapping(ApiVersion.V1 + "/users")
@Tag(name = "Users", description = "User management APIs")
public class UserController extends BaseController<User, UserResponse, UserRequest> {

    protected UserController(UserService service, ApiResponseFactory responseFactory) {
        super(service, responseFactory);
    }

    @Override
    protected String getResourceName() {
        return "user";
    }
}