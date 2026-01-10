package sti.project.template.business.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.template.base.controller.BaseController;
import sti.project.template.base.i18n.MessageHelper;
import sti.project.template.business.dto.request.ExampleRequest;
import sti.project.template.business.dto.response.ExampleResponse;
import sti.project.template.business.entity.Example;
import sti.project.template.business.service.ExampleService;

/**
 * Example REST controller.
 */
@RestController
@RequestMapping("/api/examples")
@Tag(name = "Examples", description = "Example management APIs")
public class ExampleController extends BaseController<Example, ExampleResponse, ExampleRequest> {

    public ExampleController(ExampleService service, MessageHelper messageHelper) {
        super(service, messageHelper);
    }
}
