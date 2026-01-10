package sti.project.template.business.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.business.entity.Role;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    boolean existsByNameAndStatusNot(String name, EntityStatus status);

    @Query("SELECT r.id FROM Role r")
    Page<UUID> findAllIds(Specification<Role> spec, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Role r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE r.id IN :ids")
    List<Role> findByIdsWithPermissions(@Param("ids") List<UUID> ids);
}
