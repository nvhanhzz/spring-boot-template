package sti.project.template.business.service;

import org.springframework.stereotype.Service;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.service.BaseService;
import sti.project.template.business.dto.request.ExampleRequest;
import sti.project.template.business.dto.response.ExampleResponse;
import sti.project.template.business.entity.Example;
import sti.project.template.business.mapper.ExampleMapper;
import sti.project.template.business.repository.ExampleRepository;

/**
 * Example service.
 */
@Service
public class ExampleService extends BaseService<Example, ExampleResponse, ExampleRequest> {

    private final ExampleRepository exampleRepository;

    public ExampleService(ExampleRepository repository, ExampleMapper mapper) {
        super(repository, mapper, Example.class);
        this.exampleRepository = repository;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name", "code", "description" };
    }

    @Override
    protected void beforeCreate(Example entity, ExampleRequest request) {
        validateCodeUnique(request.getCode(), null);
    }

    @Override
    protected void beforeUpdate(Example entity, ExampleRequest request) {
        if (!entity.getCode().equals(request.getCode())) {
            validateCodeUnique(request.getCode(), entity.getCode());
        }
    }

    private void validateCodeUnique(String code, String currentCode) {
        if (exampleRepository.existsByCodeAndStatusNot(code, EntityStatus.DELETED)) {
            if (currentCode == null || !currentCode.equals(code)) {
                throw new IllegalArgumentException("Example with code '" + code + "' already exists");
            }
        }
    }
}
