package sti.project.template.base.mapper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import sti.project.template.base.dto.AuditUserResponse;
import sti.project.template.business.entity.User;
import sti.project.template.business.repository.UserRepository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuditUserMapper {

    private final UserRepository userRepository;

    private final Cache<UUID, AuditUserResponse> userCache = Caffeine
            .newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public AuditUserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuditUserResponse map(UUID userId) {
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
