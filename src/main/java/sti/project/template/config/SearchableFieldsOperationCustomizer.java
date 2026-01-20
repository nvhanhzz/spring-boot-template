package sti.project.template.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import sti.project.scada.base.controller.BaseController;
import sti.project.scada.base.service.BaseService;

import java.lang.reflect.Field;
import java.util.Map;

@Component
public class SearchableFieldsOperationCustomizer implements GlobalOpenApiCustomizer {

    private final ApplicationContext applicationContext;

    public SearchableFieldsOperationCustomizer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void customise(OpenAPI openApi) {
        if (openApi.getPaths() == null || openApi.getPaths().isEmpty()) {
            return;
        }

        Map<String, BaseController<?, ?, ?>> controllers = applicationContext.getBeansOfType(
                (Class<BaseController<?, ?, ?>>) (Class<?>) BaseController.class);

        for (Map.Entry<String, BaseController<?, ?, ?>> entry : controllers.entrySet()) {
            BaseController<?, ?, ?> controller = entry.getValue();

            try {
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(controller.getClass(),
                        RequestMapping.class);
                if (requestMapping == null || requestMapping.value().length == 0) {
                    continue;
                }
                String basePath = requestMapping.value()[0];

                Object targetController = controller;
                if (AopUtils.isAopProxy(controller)) {
                    try {
                        targetController = AopProxyUtils.getSingletonTarget(controller);
                        if (targetController == null) {
                            targetController = controller;
                        }
                    } catch (Exception ignored) {
                    }
                }

                BaseService<?, ?, ?> service = null;
                Class<?> currentClass = targetController.getClass();
                while (currentClass != null && service == null) {
                    try {
                        Field serviceField = currentClass.getDeclaredField("service");
                        serviceField.setAccessible(true);
                        service = (BaseService<?, ?, ?>) serviceField.get(targetController);
                        break;
                    } catch (NoSuchFieldException e) {
                        currentClass = currentClass.getSuperclass();
                    }
                }

                if (service == null) {
                    continue;
                }

                String[] searchableFields = service.getSearchableFields();
                if (searchableFields == null || searchableFields.length == 0) {
                    continue;
                }

                PathItem pathItem = openApi.getPaths().get(basePath);
                if (pathItem == null || pathItem.getGet() == null) {
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
                }
            } catch (Exception ignored) {
            }
        }
    }
}
