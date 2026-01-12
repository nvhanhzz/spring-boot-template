package sti.project.template.base.mapper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import sti.project.template.base.dto.AuditUserResponse;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.entity.BaseEntity;
import sti.project.template.business.entity.User;
import sti.project.template.business.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Base mapper abstract class for entity-DTO conversion.
 * All MapStruct mappers should extend this class with @Mapper(config =
 * BaseMapper.class)
 */
@MapperConfig(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class BaseMapper<E extends BaseEntity, Res extends BaseResponseDTO, Req> {

    @Autowired
    protected UserRepository userRepository;

    /**
     * Convert entity to response DTO
     */
    @Mapping(target = "createdByUser", source = "createdBy", qualifiedByName = "mapAuditUser")
    @Mapping(target = "updatedByUser", source = "updatedBy", qualifiedByName = "mapAuditUser")
    public abstract Res toResponse(E entity);

    /**
     * Convert list of entities to list of response DTOs
     */
    public abstract List<Res> toResponseList(List<E> entities);

    /**
     * Convert request DTO to entity (for create).
     */
    public abstract E toEntity(Req request);

    /**
     * Update entity from request DTO (for update).
     */
    public abstract void updateEntity(Req request, @MappingTarget E entity);

    private final Cache<UUID, AuditUserResponse> userCache = Caffeine
            .newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    @Named("mapAuditUser")
    public AuditUserResponse mapAuditUser(UUID userId) {
        if (userId == null) {
            return null;
        }
        return userCache.get(userId, key -> {
            User user = userRepository.findById(key).orElse(null);
            if (user == null) {
                return null;
            }
            return AuditUserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        });
    }
}