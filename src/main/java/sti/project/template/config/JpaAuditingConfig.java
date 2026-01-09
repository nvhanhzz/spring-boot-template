package sti.project.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA Auditing configuration for automatic createdBy/updatedBy population.
 * Integrates with Spring Security to get current user ID from JWT.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    /**
     * Provides the current auditor (user ID) from SecurityContext.
     * The JWT subject claim should contain the user's UUID.
     */
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            String principal = authentication.getName();
            if (principal == null || "anonymousUser".equals(principal)) {
                return Optional.empty();
            }

            try {
                return Optional.of(UUID.fromString(principal));
            } catch (IllegalArgumentException e) {
                // Principal is not a valid UUID, return empty
                return Optional.empty();
            }
        };
    }
}
