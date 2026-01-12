package sti.project.template.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
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
        log.info("SearchableFieldsOperationCustomizer: Starting customization");

        // Get all BaseController beans
        Map<String, BaseController<?, ?, ?>> controllers = applicationContext.getBeansOfType(
                (Class<BaseController<?, ?, ?>>) (Class<?>) BaseController.class);

        log.info("Found {} BaseController beans", controllers.size());

        for (Map.Entry<String, BaseController<?, ?, ?>> entry : controllers.entrySet()) {
            BaseController<?, ?, ?> controller = entry.getValue();
            String beanName = entry.getKey();

            try {
                // Get the base path from @RequestMapping (using AnnotationUtils to find it
                // properly)
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(controller.getClass(),
                        RequestMapping.class);
                if (requestMapping == null || requestMapping.value().length == 0) {
                    log.debug("Skipping {} - no @RequestMapping found", beanName);
                    continue;
                }
                String basePath = requestMapping.value()[0];
                log.debug("Processing controller {} with basePath: {}", beanName, basePath);

                // Get service via reflection (handle Spring CGLIB proxies)
                BaseService<?, ?, ?> service = null;
                Class<?> currentClass = controller.getClass();
                while (currentClass != null && service == null) {
                    try {
                        Field serviceField = currentClass.getDeclaredField("service");
                        serviceField.setAccessible(true);
                        service = (BaseService<?, ?, ?>) serviceField.get(controller);
                        break;
                    } catch (NoSuchFieldException e) {
                        currentClass = currentClass.getSuperclass();
                    }
                }

                if (service == null) {
                    log.warn("Could not retrieve service from controller '{}'", beanName);
                    continue;
                }

                // Get searchable fields
                String[] searchableFields = service.getSearchableFields();
                log.info("Controller '{}' ({}): searchable fields = {}",
                        beanName, basePath, String.join(", ", searchableFields));

                if (searchableFields == null || searchableFields.length == 0) {
                    log.debug("No searchable fields for {}, skipping", basePath);
                    continue;
                }

                // Find the GET operation for the base path
                PathItem pathItem = openApi.getPaths().get(basePath);
                if (pathItem == null) {
                    log.warn("PathItem not found for basePath: {}", basePath);
                    continue;
                }

                if (pathItem.getGet() == null) {
                    log.warn("GET operation not found for basePath: {}", basePath);
                    continue;
                }

                for (String fieldName : searchableFields) {
                    Parameter param = new Parameter()
                            .name(fieldName)
                            .description("Filter by " + fieldName + " (LIKE search)")
                            .in("query")
                            .required(false)
                            .schema(new StringSchema());
                    pathItem.getGet().addParametersItem(param);
                    log.debug("Added parameter '{}' to {}", fieldName, basePath);
                }

                log.info("Successfully added {} searchable fields to {}", searchableFields.length, basePath);
            } catch (Exception e) {
                log.error("Failed to add searchable fields for '{}': {}", beanName, e.getMessage(), e);
            }
        }

        log.info("SearchableFieldsOperationCustomizer: Customization complete");
    }
}
