package sti.project.template.business.repository;

import org.springframework.stereotype.Repository;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.business.entity.Role;

@Repository
public interface RoleRepository extends BaseRepository<Role> {
    boolean existsByNameAndStatusNot(String name, EntityStatus status);
}
