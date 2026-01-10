package sti.project.template.base.controller;

import org.springframework.stereotype.Component;

/**
 * Helper bean for BaseController permission resolution.
 * Used in SpEL expressions within @PreAuthorize annotations.
 */
@Component("baseControllerHelper")
public class BaseControllerHelper {

    /**
     * Get permission string for a controller and action.
     *
     * @param controller the controller instance
     * @param action     the action (view, create, update, delete, restore)
     * @return permission string in format "resourceName.action"
     */
    public String getPermission(BaseController<?, ?, ?> controller, String action) {
        return controller.getResourceName() + "." + action;
    }
}
