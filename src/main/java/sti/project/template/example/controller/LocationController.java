package sti.project.template.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sti.project.template.base.controller.BaseController;
import sti.project.template.example.dto.LocationRequest;
import sti.project.template.example.dto.LocationResponse;
import sti.project.template.example.entity.Location;
import sti.project.template.example.service.LocationService;

/**
 * Location REST controller - inherits all CRUD endpoints from BaseController.
 */
@RestController
@RequestMapping("/api/locations")
@Tag(name = "Locations", description = "Location management APIs")
public class LocationController extends BaseController<Location, LocationResponse, LocationRequest> {

    public LocationController(LocationService service) {
        super(service);
    }
}
