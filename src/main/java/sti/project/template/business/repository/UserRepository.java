package sti.project.template.business.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.business.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {

        boolean existsByEmailAndStatusNot(String email, EntityStatus status);

        Optional<User> findByEmailAndStatusNot(String email, EntityStatus status);

        @Query("SELECT u.id FROM User u")
        Page<UUID> findAllIds(Specification<User> spec, Pageable pageable);

        @Query("SELECT DISTINCT u FROM User u " +
                        "LEFT JOIN FETCH u.roles r " +
                        "LEFT JOIN FETCH r.permissions " +
                        "WHERE u.id IN :ids")
        List<User> findByIdsWithRoles(@Param("ids") List<UUID> ids);
}
