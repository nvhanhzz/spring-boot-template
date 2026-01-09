package sti.project.template.example.service;

import org.springframework.stereotype.Service;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.service.BaseService;
import sti.project.template.example.dto.LocationRequest;
import sti.project.template.example.dto.LocationResponse;
import sti.project.template.example.entity.Location;
import sti.project.template.example.mapper.LocationMapper;
import sti.project.template.example.repository.LocationRepository;

/**
 * Location service - inherits all CRUD operations from BaseService.
 */
@Service
public class LocationService extends BaseService<Location, LocationResponse, LocationRequest> {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository repository, LocationMapper mapper) {
        super(repository, mapper, Location.class);
        this.locationRepository = repository;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name", "code", "description" };
    }

    @Override
    protected void beforeCreate(Location entity, LocationRequest request) {
        validateCodeUnique(request.getCode(), null);
    }

    @Override
    protected void beforeUpdate(Location entity, LocationRequest request) {
        if (!entity.getCode().equals(request.getCode())) {
            validateCodeUnique(request.getCode(), entity.getCode());
        }
    }

    private void validateCodeUnique(String code, String currentCode) {
        if (locationRepository.existsByCodeAndStatusNot(code, EntityStatus.DELETED)) {
            if (currentCode == null || !currentCode.equals(code)) {
                throw new IllegalArgumentException("Location with code '" + code + "' already exists");
            }
        }
    }
}
