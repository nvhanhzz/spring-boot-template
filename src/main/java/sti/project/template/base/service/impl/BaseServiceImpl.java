package sti.project.template.base.service.impl;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.entity.BaseEntity;
import sti.project.template.base.entity.EntityStatus;
import sti.project.template.base.exception.AppException;
import sti.project.template.base.exception.ErrorCode;
import sti.project.template.base.mapper.BaseMapper;
import sti.project.template.base.repository.BaseRepository;
import sti.project.template.base.service.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base service implementation with common CRUD operations.
 *
 * @param <T>   Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
public abstract class BaseServiceImpl<T extends BaseEntity, Res extends BaseResponseDTO, Req>
        implements BaseService<T, Res, Req> {

    protected final BaseRepository<T> repository;
    protected final BaseMapper<T, Res, Req> mapper;
    protected final Class<T> entityClass;

    protected BaseServiceImpl(BaseRepository<T> repository, BaseMapper<T, Res, Req> mapper, Class<T> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    @Override
    @Transactional(readOnly = true)
    public Res getById(UUID id) {
        T entity = repository.findActiveById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<Res> search(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<T> spec = buildSearchSpecification(keyword);
        Page<T> pageResult = repository.findAll(spec, pageable);

        List<Res> data = mapper.toResponseList(pageResult.getContent());
        return PageDTO.of(data, pageResult.getTotalElements());
    }

    @Override
    @Transactional
    public Res create(Req request) {
        T entity = mapper.toEntity(request);
        beforeCreate(entity, request);
        T saved = repository.save(entity);
        afterCreate(saved, request);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Res update(UUID id, Req request) {
        T entity = repository.findActiveById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        beforeUpdate(entity, request);
        mapper.updateEntity(request, entity);
        T saved = repository.save(entity);
        afterUpdate(saved, request);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        T entity = repository.findActiveById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        beforeDelete(entity);
        entity.setStatus(EntityStatus.DELETED);
        repository.save(entity);
        afterDelete(entity);
    }

    @Override
    @Transactional
    public Res restore(UUID id) {
        T entity = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if (entity.getStatus() != EntityStatus.DELETED) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        entity.setStatus(EntityStatus.ACTIVE);
        T saved = repository.save(entity);
        afterRestore(saved);
        return mapper.toResponse(saved);
    }

    protected Specification<T> buildSearchSpecification(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), EntityStatus.DELETED));

            if (keyword != null && !keyword.trim().isEmpty()) {
                String[] searchFields = getSearchFields();
                if (searchFields.length > 0) {
                    List<Predicate> keywordPredicates = new ArrayList<>();
                    String pattern = "%" + keyword.toLowerCase() + "%";
                    for (String field : searchFields) {
                        keywordPredicates.add(cb.like(cb.lower(root.get(field).as(String.class)), pattern));
                    }
                    predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    protected String[] getSearchFields() {
        return new String[0];
    }

    protected void beforeCreate(T entity, Req request) {
    }

    protected void afterCreate(T entity, Req request) {
    }

    protected void beforeUpdate(T entity, Req request) {
    }

    protected void afterUpdate(T entity, Req request) {
    }

    protected void beforeDelete(T entity) {
    }

    protected void afterDelete(T entity) {
    }

    protected void afterRestore(T entity) {
    }
}
