package sti.project.template.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import sti.project.template.base.controller.BaseController;
import sti.project.template.base.service.BaseService;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
@Component
public class SearchableFieldsOperationCustomizer implements GlobalOpenApiCustomizer {

    private final ApplicationContext applicationContext;

    public SearchableFieldsOperationCustomizer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void customise(OpenAPI openApi) {
        // Get all BaseController beans
        Map<String, BaseController<?, ?, ?>> controllers = applicationContext.getBeansOfType(
                (Class<BaseController<?, ?, ?>>) (Class<?>) BaseController.class);

        for (Map.Entry<String, BaseController<?, ?, ?>> entry : controllers.entrySet()) {
            BaseController<?, ?, ?> controller = entry.getValue();

            try {
                // Get the base path from @RequestMapping
                RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
                if (requestMapping == null || requestMapping.value().length == 0) {
                    continue;
                }
                String basePath = requestMapping.value()[0];

                // Get service via reflection
                Field serviceField = BaseController.class.getDeclaredField("service");
                serviceField.setAccessible(true);
                BaseService<?, ?, ?> service = (BaseService<?, ?, ?>) serviceField.get(controller);

                // Get searchable fields
                String[] searchableFields = service.getSearchableFields();
                log.info("Adding searchable fields to Swagger for {}: {}", basePath,
                        String.join(", ", searchableFields));

                // Find the GET operation for the base path
                PathItem pathItem = openApi.getPaths().get(basePath);
                if (pathItem != null && pathItem.getGet() != null) {
                    for (String fieldName : searchableFields) {
                        Parameter param = new Parameter()
                                .name(fieldName)
                                .description("Filter by " + fieldName + " (LIKE search)")
                                .in("query")
                                .required(false)
                                .schema(new StringSchema());
                        pathItem.getGet().addParametersItem(param);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to add searchable fields for {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }
}
