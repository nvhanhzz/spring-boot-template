package sti.project.template.business.repository;

import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.business.entity.Example;

import java.util.Optional;

/**
 * Example repository.
 */
public interface ExampleRepository extends BaseRepository<Example> {

    boolean existsByCodeAndStatusNot(String code, EntityStatus status);

    Optional<Example> findByCode(String code);
}
