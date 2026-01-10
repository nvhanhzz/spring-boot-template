package sti.project.template.business.repository;

import org.springframework.stereotype.Repository;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.business.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    boolean existsByEmailAndStatusNot(String email, EntityStatus status);

    Optional<User> findByEmailAndStatusNot(String email, EntityStatus status);
}
